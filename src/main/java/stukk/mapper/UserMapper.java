package stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import stukk.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wenli
 * @create 2022-09-03 17:38
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
