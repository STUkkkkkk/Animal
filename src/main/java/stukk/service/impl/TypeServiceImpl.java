package stukk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import stukk.common.CustomException;
import stukk.entity.*;
import stukk.mapper.TypeMapper;
import stukk.service.PetGoodsService;
import stukk.service.PetService;
import stukk.service.SetmealService;
import stukk.service.TypeService;

import javax.annotation.Resource;

/**
 * @author wenli
 * @create 2022-09-01 16:14
 */
@Service("typeService")
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
    @Resource
    private PetService petService;

    @Resource
    private PetGoodsService petGoodsService;

    @Resource
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Pet> petLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id来查询
        petLambdaQueryWrapper.eq(Pet::getTypeId, id);
        // 查询当前分类是否关联了宠物，如果已经关联，抛出一个业务异常
        int count1 = petService.count(petLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了宠物，不能删除该分类！");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id来查询
        setmealLambdaQueryWrapper.eq(Setmeal::getTypeId, id);
        // 查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除该分类！");
        }

        LambdaQueryWrapper<PetGoods> petGoodsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        petGoodsLambdaQueryWrapper.eq(PetGoods::getTypeId, id);
        int count3 = petGoodsService.count(petGoodsLambdaQueryWrapper);
        if (count3 > 0) {
            throw new CustomException("当前分类下关联了宠物用品，不能删除该分类！");
        }

        // 正常删除分类
        super.removeById(id);
    }
}
