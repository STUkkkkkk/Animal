package stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import stukk.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wenli
 * @create 2022-09-05 11:11
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
