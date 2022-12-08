package stukk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import stukk.entity.Employee;
import stukk.mapper.EmployeeMapper;
import stukk.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author wenli
 * @create 2022-08-29 20:36
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
