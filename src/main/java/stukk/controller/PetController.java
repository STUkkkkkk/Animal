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
import stukk.dto.PetDto;
import stukk.entity.Pet;
import stukk.entity.PetPrefer;
import stukk.entity.Type;
import stukk.service.PetPreferService;
import stukk.service.PetService;
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
@RequestMapping("/pet")
@Api(tags = "宠物管理器（PetController）")
public class PetController {
    @Value("${reggie.path}")
    private String basePath;

    @Resource
    private PetService petService;

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
    @ApiOperation(value = "宠物信息分页查询")
    public R<Page<PetDto>> page(@ApiParam(value = "页数") int page,
                                @ApiParam(value = "页面大小") int pageSize,
                                @ApiParam(value = "宠物名称") String name) {
        // 分页构造器
        Page<Pet> pageInfo = new Page<>(page, pageSize);
        Page<PetDto> petDtoPage = new Page<>();
        // 条件构造器
        LambdaQueryWrapper<Pet> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Pet::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Pet::getUpdateTime).orderByAsc(Pet::getSort);
        // 执行查询
        petService.page(pageInfo, queryWrapper);
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, petDtoPage, "records");

        List<Pet> records = pageInfo.getRecords();

        List<PetDto> list = records.stream().map((item) -> {
            PetDto petDto = new PetDto();

            BeanUtils.copyProperties(item, petDto);

            // 获取分类id
            Long typeId = item.getTypeId();
            // 获取分类名称
            Type type = typeService.getById(typeId);
            if (type != null) {
                petDto.setTypeName(type.getName());
            }

            return petDto;
        }).collect(Collectors.toList());

        petDtoPage.setRecords(list);

        return R.success(petDtoPage);
    }

    /**
     * 新增宠物
     */
    @PostMapping
    @ApiOperation(value = "新增宠物")
    public R<String> save(
            @ApiParam(value = "宠物实体辅助对象")
            @RequestBody PetDto petDto) {
        petService.saveWithPrefer(petDto);

        // 清理某个分类下面的宠物缓存数据
        String key = "pet_" + petDto.getTypeId() + "_1";
        redisTemplate.delete(key);

        return R.success("新增宠物成功！");
    }

    /**
     * 根据id获取宠物信息和对应的偏好信息
     */
    @ApiOperation(value = "根据id获取宠物信息和对应的偏好信息")
    @GetMapping("/{id}")
    public R<PetDto> get(
            @ApiParam(value = "宠物ID")
            @PathVariable Long id) {
        PetDto petDto = petService.getByIdWithPrefer(id);
        return R.success(petDto);
    }

    /**
     * 根据id修改宠物基本信息和对应的偏好信息
     */
    @ApiOperation(value = "根据id修改宠物基本信息和对应的偏好信息")
    @PutMapping
    public R<String> update(
            @ApiParam(value = "宠物信息辅助实体")
            @RequestBody PetDto petDto) {
        petService.updateWithPrefer(petDto);

        // 清理某个分类下面的宠物缓存数据
        String key = "pet_" + petDto.getTypeId() + "_1";
        redisTemplate.delete(key);

        return R.success("修改宠物成功！");
    }

    /**
     * 根据id修改宠物状态
     */
    @ApiOperation(value = "根据id修改宠物状态")
    @PostMapping("/status/{status}")
    public R<String> status(
            @ApiParam(value = "状态信息")
            @PathVariable int status,
            @ApiParam(value = "宠物ID数组")
            Long[] ids) {
        List<Pet> petList = new ArrayList<>();
        for (Long id : ids) {
            Pet pet = new Pet();
            pet.setId(id);
            pet.setStatus(status);
            petList.add(pet);
        }
        petService.updateBatchById(petList);
        return R.success("修改宠物状态成功！");
    }

    /**
     * 根据id删除宠物
     */
    @ApiOperation(value = "根据id删除宠物")
    @DeleteMapping
    public R<String> delete(@ApiParam(value = "宠物ID数组") Long[] ids) {
        List<Long> idList = Arrays.asList(ids);

        // 获取宠物对应图片的路径
        LambdaQueryWrapper<Pet> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(Pet::getId, idList);
        List<Pet> petList = petService.list(queryWrapper1);
        // 判断宠物是否在起售
        queryWrapper1.eq(Pet::getStatus, 1);
        int count = petService.count(queryWrapper1);
        if (count > 0) {
            throw new CustomException("宠物正在出售，不可删除！");
        }

        List<String> imgList = new ArrayList<>();
        for (Pet pet : petList) {
            imgList.add(pet.getImage());
        }

        // 删除图片
        for (String s : imgList) {
            File file = new File(basePath + s);
            file.delete();
        }

        // 删除宠物表对应的宠物信息
        petService.removeByIds(idList);

        // 删除宠物对应的偏好信息
        LambdaQueryWrapper<PetPrefer> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(PetPrefer::getPetId, idList);
        petPreferService.remove(queryWrapper2);

        return R.success("删除宠物成功！");
    }

    @ApiOperation(value = "根据条件查询宠物数据")
    @GetMapping("/list")
    public R<List<PetDto>> list(@ApiParam(value = "宠物对象") Pet pet) {
        List<PetDto> dtoList = null;

        String key = "pet_" + pet.getTypeId() + "_" + pet.getStatus();

        // 先从Redis中获取数据
        dtoList = (List<PetDto>) redisTemplate.opsForValue().get(key);

        if (dtoList != null) {
            // 如果存在，直接返回，无需查询数据库
            return R.success(dtoList);
        }

        // 如果不存在，需要查询数据库
        // 条件构造器
        LambdaQueryWrapper<Pet> queryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        queryWrapper.eq(pet.getTypeId() != null, Pet::getTypeId, pet.getTypeId());
        // 限制只查询起售状态的宠物
        queryWrapper.eq(Pet::getStatus, 1);
        // 添加排序条件
        queryWrapper.orderByAsc(Pet::getSort).orderByDesc(Pet::getUpdateTime);

        // 执行查询
        List<Pet> list = petService.list(queryWrapper);

        dtoList = list.stream().map((item) -> {
            PetDto petDto = new PetDto();

            BeanUtils.copyProperties(item, petDto);

            // 获取分类id
            Long typeId = item.getTypeId();
            // 获取分类名称
            Type type = typeService.getById(typeId);
            if (type != null) {
                petDto.setTypeName(type.getName());
            }

            // 获取宠物偏好信息
            LambdaQueryWrapper<PetPrefer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PetPrefer::getPetId, petDto.getId());
            List<PetPrefer> prefers = petPreferService.list(lambdaQueryWrapper);

            petDto.setPrefers(prefers);

            return petDto;
        }).collect(Collectors.toList());

        // 并将查询到的宠物数据缓存到Redis
        redisTemplate.opsForValue().set(key, dtoList, 60, TimeUnit.MINUTES);

        return R.success(dtoList);
    }
}
