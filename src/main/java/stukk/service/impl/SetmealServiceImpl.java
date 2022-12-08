package stukk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stukk.common.CustomException;
import stukk.dto.SetmealDto;
import stukk.entity.Setmeal;
import stukk.entity.SetmealGoods;
import stukk.mapper.SetmealMapper;
import stukk.service.SetmealGoodsService;
import stukk.service.SetmealService;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenli
 * @create 2022-09-01 18:10
 */
@Service("setmealService")
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Value("${reggie.path}")
    private String basePath;

    @Resource
    private SetmealGoodsService setmealGoodsService;

    /**
     * 新增套餐，同时保存套餐和物品的关联关系
     */
    @Override
    @Transactional
    public void saveWithGoods(SetmealDto setmealDto) {
        // 保存套餐的基本信息，操作 tb_setmeal 表
        this.save(setmealDto);

        List<SetmealGoods> setmealGoods = setmealDto.getSetmealGoods();
        setmealGoods.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 保存套餐和物品的关联关系，操作 tb_setmeal_goods 表
        setmealGoodsService.saveBatch(setmealGoods);
    }

    /**
     * 删除套餐，同时删除套餐和物品的关联数据
     */
    @Override
    @Transactional
    public void removeWithGoods(List<Long> ids) {
        // 查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        List<Setmeal> list = this.list(queryWrapper);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        // 如果不能删除，抛出一个业务异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除！");
        }
        // 如果可以删除，先删除套餐表中的数据
        this.removeByIds(ids);
        // 删除套餐图片数据
        for (Setmeal setmeal : list) {
            File file = new File(basePath + setmeal.getImage());
            if (file != null) {
                file.delete();
            }
        }
        // 删除关系表中的数据
        LambdaQueryWrapper<SetmealGoods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealGoods::getSetmealId, ids);
        setmealGoodsService.remove(lambdaQueryWrapper);
    }

    /**
     * 修改套餐，同时修改套餐和物品的关联数据
     */
    @Override
    @Transactional
    public void updateWithGoods(SetmealDto setmealDto) {
        // 更新套餐的基本信息
        this.updateById(setmealDto);

        // 更新套餐与物品的关联数据
        List<SetmealGoods> setmealGoods = setmealDto.getSetmealGoods();
        setmealGoods.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealGoodsService.saveBatch(setmealGoods);
    }
}
