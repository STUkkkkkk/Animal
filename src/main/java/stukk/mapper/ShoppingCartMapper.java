package stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import stukk.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wenli
 * @create 2022-09-04 18:25
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
