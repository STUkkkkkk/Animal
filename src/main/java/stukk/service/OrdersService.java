package stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import stukk.entity.Orders;

/**
 * @author wenli
 * @create 2022-09-05 11:12
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     *
     * @param orders
     */
    void submit(Orders orders);
}
