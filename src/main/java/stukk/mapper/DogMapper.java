package stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import stukk.entity.Dog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DogMapper extends BaseMapper<Dog> {
}
