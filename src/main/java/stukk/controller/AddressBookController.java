package stukk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stukk.common.BaseContext;
import stukk.common.R;
import stukk.entity.AddressBook;
import stukk.service.AddressBookService;

import java.util.List;

/**
 * @author wenli
 * @create 2022-09-03 21:51
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
@Api(tags = "用户收货地址管理（AddressBookController）")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;

    /**
     * 根据手机号获取用户的收货地址列表
     */
    @ApiOperation(value = "根据手机号获取用户的收货地址列表")
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());

        // 条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 根据id获取用户收货地址信息
     */
    @ApiOperation(value = "根据id获取用户收货地址信息")
    @GetMapping("{id}")
    public R<AddressBook> get(@PathVariable String id) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId, id);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);
    }

    /**
     * 保存修改后的收货地址
     */
    @ApiOperation(value = "保存修改后的收货地址")
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success("修改收货地址成功！");
    }

    /**
     * 新增收货地址
     */
    @ApiOperation(value = "新增收货地址")
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id删除收货地址
     */
    @ApiOperation(value = "根据id删除收货地址")
    @DeleteMapping
    public R<String> delete(Long id) {
        addressBookService.removeById(id);
        return R.success("删除地址成功！");
    }

    /**
     * 设置默认地址
     */
    @ApiOperation(value = "设置默认地址")
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        // 获取地址并修改默认状态0
        LambdaUpdateWrapper<AddressBook> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.set(AddressBook::getIsDefault, 0);

        addressBookService.update(lambdaQueryWrapper);

        // 设置新的默认地址
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }

    /**
     * 查找默认地址
     */
    @ApiOperation(value = "查找默认地址")
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (addressBook == null) {
            return R.error("没有找到默认地址！");
        } else {
            return R.success(addressBook);
        }
    }
}
