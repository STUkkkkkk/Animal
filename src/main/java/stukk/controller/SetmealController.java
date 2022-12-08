package stukk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import stukk.common.R;
import stukk.dto.SetmealDto;
import stukk.entity.*;
import stukk.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenli
 * @create 2022-09-02 20:07
 * 套餐管理
 */
@Api(tags = "套餐管理（SetmealController）")
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    private SetmealGoodsService setmealGoodsService;

    @Resource
    private PetGoodsService petGoodsService;

    @Resource
    private TypeService typeService;

    @Resource
    private SetmealService setmealService;

    @ApiOperation(value = "新增套餐")
    @PostMapping
    // 清除所有套餐缓存
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> save(
            @ApiParam(value = "套餐信息辅助对象")
            @RequestBody SetmealDto setmealDto) {
        setmealService.saveWithGoods(setmealDto);
        return R.success("新增套餐成功！");
    }

    @ApiOperation(value = "套餐分页查询")
    @GetMapping("/page")
    public R<Page> page(@ApiParam(value = "页数") int page,
                        @ApiParam(value = "页面大小") int pageSize,
                        @ApiParam(value = "套餐名称") String name) {
        // 分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        // 条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper1.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        // 添加排序条件
        queryWrapper1.orderByDesc(Setmeal::getUpdateTime);
        // 执行查询
        setmealService.page(pageInfo, queryWrapper1);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtos = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            // 获取分类id
            Type type = typeService.getById(item.getTypeId());
            if (type != null) {
                // 设置分类名称
                setmealDto.setTypeName(type.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(setmealDtos);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     */
    @ApiOperation(value = "删除套餐")
    @DeleteMapping
    // 清除所有套餐缓存
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(
            @ApiParam(value = "套餐ID数组")
            @RequestParam List<Long> ids) {
        setmealService.removeWithGoods(ids);
        return R.success("成功删除套餐！");
    }

    /**
     * 根据id修改套餐状态
     */
    @ApiOperation(value = "根据id修改套餐状态")
    @PostMapping("/status/{status}")
    public R<String> status(@ApiParam(value = "套餐状态") @PathVariable int status,
                            @ApiParam(value = "套餐ID数组") @RequestParam List<Long> ids) {
        List<Setmeal> list = new ArrayList<>();
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(status);
            list.add(setmeal);
        }
        setmealService.updateBatchById(list);
        return R.success("修改套餐状态成功！");
    }

    /**
     * 根据id获取套餐信息
     */
    @ApiOperation(value = "根据id获取套餐信息")
    @GetMapping("/{id}")
    public R<SetmealDto> get(
            @ApiParam(value = "套餐ID")
            @PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        // 对象拷贝
        BeanUtils.copyProperties(setmeal, setmealDto);
        // 获取分类名称
        Type type = typeService.getById(setmeal.getTypeId());
        setmealDto.setTypeName(type.getName());
        // 获取套餐对应的物品信息
        LambdaQueryWrapper<SetmealGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealGoods::getSetmealId, setmeal.getId());
        List<SetmealGoods> list = setmealGoodsService.list(queryWrapper);

        setmealDto.setSetmealGoods(list);
        return R.success(setmealDto);
    }

    /**
     * 更新套餐
     */
    @ApiOperation(value = "更新套餐")
    @PutMapping
    // 清除所有套餐缓存
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        // 删除套餐原本关联的物品信息
        LambdaQueryWrapper<SetmealGoods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealGoods::getSetmealId, setmealDto.getId());
        setmealGoodsService.remove(lambdaQueryWrapper);

        // 保存更新后套餐的信息
        setmealService.updateWithGoods(setmealDto);
        return R.success("修改套餐成功！");
    }

    /**
     * 根据分类id查询套餐信息
     */
    @ApiOperation(value = "根据分类id查询套餐信息")
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.typeId + '_' + #setmeal.status")
    public R<List<SetmealDto>> list(Setmeal setmeal) {
        // 根据分类id获取套餐信息
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getTypeId, setmeal.getTypeId());
        lambdaQueryWrapper.eq(Setmeal::getStatus, setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
        List<SetmealDto> dtoList = new ArrayList<>();
        for (Setmeal setmeal1 : list) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal1, setmealDto);
            dtoList.add(setmealDto);
        }

        // 根据套餐id获取套餐物品信息
        for (SetmealDto setmealDto : dtoList) {
            LambdaQueryWrapper<SetmealGoods> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealGoods::getSetmealId, setmealDto.getId());
            List<SetmealGoods> setmealDishes = setmealGoodsService.list(queryWrapper);
            setmealDto.setSetmealGoods(setmealDishes);
        }

        return R.success(dtoList);
    }

    /**
     * 根据套餐id获取套餐物品信息
     */
    @ApiOperation(value = "根据套餐id获取套餐物品信息")
    @GetMapping("/goods/{setmealId}")
    public R<List<SetmealGoods>> getDish(@PathVariable Long setmealId) {
        LambdaQueryWrapper<SetmealGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealGoods::getSetmealId, setmealId);
        List<SetmealGoods> goods = setmealGoodsService.list(queryWrapper);
        goods.stream().map((item) -> {
            PetGoods petGoods = petGoodsService.getById(item.getGoodsId());
            item.setImage(petGoods.getImage());
            return item;
        }).collect(Collectors.toList());
        return R.success(goods);
    }
}
