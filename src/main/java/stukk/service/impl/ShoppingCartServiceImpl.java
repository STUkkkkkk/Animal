package stukk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import stukk.entity.ShoppingCart;
import stukk.mapper.ShoppingCartMapper;
import stukk.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author wenli
 * @create 2022-09-04 18:26
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
