package stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import stukk.entity.Blog;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
