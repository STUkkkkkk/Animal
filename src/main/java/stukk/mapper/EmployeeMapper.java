package stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import stukk.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wenli
 * @create 2022-08-29 20:34
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
