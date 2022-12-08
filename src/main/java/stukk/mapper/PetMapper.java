package stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import stukk.entity.Pet;

/**
 * @author wenli
 * @create 2022-12-06 15:23
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet> {
}
