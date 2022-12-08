package stukk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import stukk.common.BaseContext;
import stukk.common.R;
import stukk.dto.OrdersDto;
import stukk.entity.OrderDetail;
import stukk.entity.Orders;
import stukk.service.OrderDetailService;
import stukk.service.OrdersService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenli
 * @create 2022-09-05 11:15
 */
@Api(tags = "订单管理（OrdersController）")
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderDetailService orderDetailService;

    @ApiOperation(value = "修改订单状态")
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders) {
        if (orders.getStatus() == 2) {
            orders.setStatus(3);
        }
        ordersService.updateById(orders);
        return R.success("修改订单状态成功！");
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public R<Page<Orders>> page(int page, int pageSize, Integer number, LocalDateTime beginTime, LocalDateTime endTime) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(number), Orders::getNumber, number);
        if (beginTime != null && endTime != null) {
            queryWrapper.between(Orders::getOrderTime, beginTime, endTime);
        }
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        ordersService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 用户下单
     */
    @ApiOperation(value = "用户下单")
    @PostMapping("/submit")
    public R<String> submit(@ApiParam(value = "订单对象") @RequestBody Orders orders) {
        orders.setStatus(2);
        ordersService.submit(orders);
        return R.success("下单成功！");
    }

    /**
     * 查看订单
     */
    @ApiOperation(value = "查看订单")
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(@ApiParam(value = "页数") int page,
                                       @ApiParam(value = "页面大小") int pageSize) {
        // 分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>();
        // 条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        Page<Orders> ordersPage = ordersService.page(pageInfo, queryWrapper);

        List<Orders> records = ordersPage.getRecords();

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        // 构造dtoList
        List<OrdersDto> dtoList = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(lambdaQueryWrapper);
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(dtoList);

        return R.success(dtoPage);
    }
}
