package stukk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import stukk.common.BaseContext;
import stukk.common.R;
import stukk.entity.ShoppingCart;
import stukk.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wenli
 * @create 2022-09-04 18:27
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
@Api(tags = "购物车管理（ShoppingCartController）")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加到购物车
     */
    @ApiOperation(value = "添加到购物车")
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        // 设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        // 判断宠物、套餐或用品是否在购物车中
        Long petId = shoppingCart.getPetId();
        Long goodId = shoppingCart.getGoodId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (petId != null) {
            // 是宠物
            queryWrapper.eq(ShoppingCart::getPetId, petId);
        } else if (goodId != null) {
            queryWrapper.eq(ShoppingCart::getGoodId, goodId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null) {
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    /**
     * 查询购物车
     */
    @ApiOperation(value = "查询购物车")
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 清空购物车
     */
    @ApiOperation(value = "清空购物车")
    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功！");
    }

    /**
     * 减少购物车中的套餐或宠物
     */
    @ApiOperation(value = "减少购物车")
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = null;
        LambdaQueryWrapper<ShoppingCart> queryWrapper = null;
        Long petId = shoppingCart.getPetId();
        Long goodId = shoppingCart.getGoodId();
        updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        if (petId != null) {
            updateWrapper.eq(ShoppingCart::getPetId, petId);
            queryWrapper.eq(ShoppingCart::getPetId, petId);
        } else if (goodId != null) {
            updateWrapper.eq(ShoppingCart::getGoodId, goodId);
            queryWrapper.eq(ShoppingCart::getGoodId, goodId);
        } else {
            updateWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        if (shoppingCart.getId() != null) {
            ShoppingCart cartServiceById = shoppingCartService.getById(shoppingCart);
            cartServiceById.setNumber(cartServiceById.getNumber() - 1);
            shoppingCartService.updateById(cartServiceById);
            return R.success("成功刷新购物车！");
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        int number = cartServiceOne.getNumber() - 1;

        if (number == 0) {
            shoppingCartService.remove(queryWrapper);
        } else {
            updateWrapper.set(ShoppingCart::getNumber, number);
            shoppingCartService.update(updateWrapper);
        }

        return R.success("成功刷新购物车！");
    }
}
