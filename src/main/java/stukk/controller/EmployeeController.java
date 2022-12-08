package stukk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import stukk.common.R;
import stukk.entity.Employee;
import stukk.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wenli
 * @create 2022-08-29 20:39
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@Api(tags = "员工管理（EmployeeController）")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request  将登录的员工id存入session
     * @param employee 存储根据用户名和密码查询到的员工信息
     */
    @ApiOperation(value = "员工登录")
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 1.将页面提交的密码password进行MD5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3.如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("登录失败，没有这个用户！");
        }

        // 4.密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败，密码错误！");
        }

        // 5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("登录失败，员工账号已被禁用！");
        }

        // 6.登录成功，将员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @ApiOperation(value = "员工退出登录")
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清除session中存储的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }

    /**
     * 新增员工
     *
     * @param employee 存储新增员工信息
     */
    @ApiOperation(value = "新增员工")
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工的信息为：{}", employee.toString());

        // 设置初始密码123456，需要MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeService.save(employee);

        return R.success("新增员工成功！");
    }

    /**
     * 员工信息分页查询
     *
     * @param page     页码
     * @param pageSize 条数/页
     * @param name     检索条件
     */
    @ApiOperation(value = "员工信息分页查询")
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);

        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     */
    @ApiOperation(value = "根据id修改员工信息")
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为：{}", id);

        employeeService.updateById(employee);

        return R.success("员工信息修改成功！");
    }

    /**
     * 根据id查询员工信息
     */
    @ApiOperation(value = "根据id查询员工信息")
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee emp = employeeService.getById(id);
        log.info("根据id获取到的员工信息：{}", emp.toString());
        if (emp != null) {
            return R.success(emp);
        }
        return R.error("没有查询到对应员工信息！");
    }
}
