package stukk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import stukk.common.R;
import stukk.entity.Type;
import stukk.service.TypeService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wenli
 * @create 2022-12-06 22:50
 */
@RestController
@RequestMapping("/type")
@Api(tags = "分类管理器（TypeController）")
public class TypeController {
    @Resource
    private TypeService typeService;

    /**
     * 新增分类
     */
    @PostMapping
    @ApiOperation(value = "新增分类")
    public R<String> save(
            @ApiParam(value = "分类信息对象")
            @RequestBody Type type) {
        typeService.save(type);
        return R.success("新增分类成功！");
    }

    /**
     * 修改分类
     */
    @PutMapping
    @ApiOperation(value = "修改分类")
    public R<String> update(
            @ApiParam(value = "分类信息对象")
            @RequestBody Type type) {
        typeService.updateById(type);
        return R.success("分类修改成功！");
    }

    /**
     * 根据id删除分类
     */
    @DeleteMapping
    @ApiOperation(value = "根据id删除分类")
    public R<String> delete(
            @ApiParam(value = "分类ID")
            Long id) {
        typeService.remove(id);
        return R.success("删除成功！");
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public R<Page> page(@ApiParam(value = "页数") int page,
                        @ApiParam(value = "页面大小") int pageSize) {
        // 构造分页构造器
        Page<Type> pageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Type> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Type::getSort);
        // 执行查询
        typeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据条件查询分类数据
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据条件查询分类数据")
    public R<List<Type>> list(@ApiParam(value = "分类信息对象") Type type) {
        // 条件构造器
        LambdaQueryWrapper<Type> queryWrapper = new LambdaQueryWrapper<>();
        // 添加检索条件
        queryWrapper.eq(type.getType() != null, Type::getType, type.getType());
        // 添加排序条件
        queryWrapper.orderByAsc(Type::getSort).orderByDesc(Type::getUpdateTime);
        // 执行查询
        List<Type> list = typeService.list(queryWrapper);
        return R.success(list);
    }
}
