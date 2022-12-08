package stukk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import stukk.common.CustomException;
import stukk.common.R;
import stukk.dto.PetGoodsDto;
import stukk.entity.PetGoods;
import stukk.entity.PetPrefer;
import stukk.entity.Type;
import stukk.service.PetGoodsService;
import stukk.service.PetPreferService;
import stukk.service.TypeService;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wenli
 * @create 2022-12-06 23:13
 */
@RestController
@RequestMapping("/good")
@Api(tags = "宠物物品管理器（PetGoodsController）")
public class PetGoodsController {
    @Value("${reggie.path}")
    private String basePath;

    @Resource
    private PetGoodsService petGoodsService;

    @Resource
    private PetPreferService petPreferService;

    @Resource
    private TypeService typeService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 宠物信息分页查询
     */
    @GetMapping("/page")
    @ApiOperation(value = "物品信息分页查询")
    public R<Page<PetGoodsDto>> page(@ApiParam(value = "页数") int page,
                                     @ApiParam(value = "页面大小") int pageSize,
                                     @ApiParam(value = "物品名称") String name) {
        // 分页构造器
        Page<PetGoods> pageInfo = new Page<>(page, pageSize);
        Page<PetGoodsDto> petGoodsDtoPage = new Page<>();
        // 条件构造器
        LambdaQueryWrapper<PetGoods> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), PetGoods::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(PetGoods::getUpdateTime).orderByAsc(PetGoods::getSort);
        // 执行查询
        petGoodsService.page(pageInfo, queryWrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, petGoodsDtoPage, "records");

        List<PetGoods> records = pageInfo.getRecords();

        List<PetGoodsDto> list = records.stream().map((item) -> {
            PetGoodsDto petGoodsDto = new PetGoodsDto();

            BeanUtils.copyProperties(item, petGoodsDto);

            // 获取分类id
            Long typeId = item.getTypeId();
            // 获取分类名称
            Type type = typeService.getById(typeId);
            if (type != null) {
                petGoodsDto.setTypeName(type.getName());
            }

            return petGoodsDto;
        }).collect(Collectors.toList());

        petGoodsDtoPage.setRecords(list);

        return R.success(petGoodsDtoPage);
    }

    /**
     * 新增物品
     */
    @PostMapping
    @ApiOperation(value = "新增物品")
    public R<String> save(
            @ApiParam(value = "物品实体辅助对象")
            @RequestBody PetGoodsDto petGoodsDto) {
        petGoodsService.saveWithPrefer(petGoodsDto);

        // 清理某个分类下面的物品缓存数据
        String key = "petGoods_" + petGoodsDto.getTypeId() + "_1";
        redisTemplate.delete(key);

        return R.success("新增物品成功！");
    }

    /**
     * 根据id获取物品信息和对应的偏好信息
     */
    @ApiOperation(value = "根据id获取物品信息和对应的偏好信息")
    @GetMapping("/{id}")
    public R<PetGoodsDto> get(
            @ApiParam(value = "物品ID")
            @PathVariable Long id) {
        PetGoodsDto petGoodsDto = petGoodsService.getByIdWithPrefer(id);
        return R.success(petGoodsDto);
    }

    /**
     * 根据id修改物品基本信息和对应的偏好信息
     */
    @ApiOperation(value = "根据id修改物品基本信息和对应的偏好信息")
    @PutMapping
    public R<String> update(
            @ApiParam(value = "物品信息辅助实体")
            @RequestBody PetGoodsDto petGoodsDto) {
        petGoodsService.updateWithPrefer(petGoodsDto);

        // 清理某个分类下面的物品缓存数据
        String key = "petGoods_" + petGoodsDto.getTypeId() + "_1";
        redisTemplate.delete(key);

        return R.success("修改物品成功！");
    }

    /**
     * 根据id修改物品状态
     */
    @ApiOperation(value = "根据id修改物品状态")
    @PostMapping("/status/{status}")
    public R<String> status(
            @ApiParam(value = "状态信息")
            @PathVariable int status,
            @ApiParam(value = "物品ID数组")
            Long[] ids) {
        List<PetGoods> petGoodsList = new ArrayList<>();
        for (Long id : ids) {
            PetGoods petGoods = new PetGoods();
            petGoods.setId(id);
            petGoods.setStatus(status);
            petGoodsList.add(petGoods);
        }
        petGoodsService.updateBatchById(petGoodsList);
        return R.success("修改物品状态成功！");
    }

    /**
     * 根据id删除物品
     */
    @ApiOperation(value = "根据id删除物品")
    @DeleteMapping
    public R<String> delete(@ApiParam(value = "物品ID数组") Long[] ids) {
        List<Long> idList = Arrays.asList(ids);

        // 获取物品对应图片的路径
        LambdaQueryWrapper<PetGoods> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(PetGoods::getId, idList);
        List<PetGoods> petGoodsList = petGoodsService.list(queryWrapper1);
        // 判断物品是否在起售
        queryWrapper1.eq(PetGoods::getStatus, 1);
        int count = petGoodsService.count(queryWrapper1);
        if (count > 0) {
            throw new CustomException("物品正在出售，不可删除！");
        }

        List<String> imgList = new ArrayList<>();
        for (PetGoods petGoods : petGoodsList) {
            imgList.add(petGoods.getImage());
        }

        // 删除图片
        for (String s : imgList) {
            File file = new File(basePath + s);
            file.delete();
        }

        // 删除物品表对应的物品信息
        petGoodsService.removeByIds(idList);

        // 删除物品对应的偏好信息
        LambdaQueryWrapper<PetPrefer> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(PetPrefer::getPetId, idList);
        petPreferService.remove(queryWrapper2);

        return R.success("删除物品成功！");
    }

    @ApiOperation(value = "根据条件查询物品数据")
    @GetMapping("/list")
    public R<List<PetGoodsDto>> list(@ApiParam(value = "物品类型id") Long typeId) {
        List<PetGoodsDto> dtoList = null;

        String key = "petGoods_" + typeId + "_1";

        // 先从Redis中获取数据
        dtoList = (List<PetGoodsDto>) redisTemplate.opsForValue().get(key);

        if (dtoList != null) {
            // 如果存在，直接返回，无需查询数据库
            return R.success(dtoList);
        }

        // 如果不存在，需要查询数据库
        // 条件构造器
        LambdaQueryWrapper<PetGoods> queryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        queryWrapper.eq(typeId != null, PetGoods::getTypeId, typeId);
        // 限制只查询起售状态的物品
        queryWrapper.eq(PetGoods::getStatus, 1);
        // 添加排序条件
        queryWrapper.orderByDesc(PetGoods::getUpdateTime);

        // 执行查询
        List<PetGoods> list = petGoodsService.list(queryWrapper);

        dtoList = list.stream().map((item) -> {
            PetGoodsDto petGoodsDto = new PetGoodsDto();

            BeanUtils.copyProperties(item, petGoodsDto);

            // 获取分类名称
            Type type = typeService.getById(typeId);
            if (type != null) {
                petGoodsDto.setTypeName(type.getName());
            }

            // 获取物品偏好信息
            LambdaQueryWrapper<PetPrefer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PetPrefer::getPetId, petGoodsDto.getId());
            List<PetPrefer> prefers = petPreferService.list(lambdaQueryWrapper);

            petGoodsDto.setPrefers(prefers);

            return petGoodsDto;
        }).collect(Collectors.toList());

        // 并将查询到的物品数据缓存到Redis
        redisTemplate.opsForValue().set(key, dtoList, 60, TimeUnit.MINUTES);

        return R.success(dtoList);
    }
}
