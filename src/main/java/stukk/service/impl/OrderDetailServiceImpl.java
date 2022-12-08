package stukk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import stukk.entity.OrderDetail;
import stukk.mapper.OrderDetailMapper;
import stukk.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author wenli
 * @create 2022-09-05 11:14
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
