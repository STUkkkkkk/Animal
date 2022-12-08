package stukk.mapper;

import com.alibaba.druid.filter.AutoLoad;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import stukk.entity.Cat;

@Mapper
public interface CatMapper extends BaseMapper<Cat> {

}
