package stukk.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stukk.common.R;
import stukk.entity.Blog;
import stukk.service.BlogService;

import java.util.List;

@RestController
@RequestMapping("/blog")
@Api(tags = "博客管理（BlogController）")
public class BlogController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    BlogService blogService;

    @ApiOperation(value = "根据ID获取博客信息")
    @GetMapping("/getById/{id}")
    public R<Blog> getById(@PathVariable Integer id) {
        return R.success(blogService.getById(id));
    }


    @ApiOperation(value = "获取博客列表")
    @GetMapping("/getAll")
    public R<List<Blog>> getAll() {
        String blog = stringRedisTemplate.opsForValue().get("blog");
        if (blog != null) {
            return R.success((List<Blog>) JSON.parse(blog));
        } else {
            List<Blog> list = blogService.query().list();
            stringRedisTemplate.opsForValue().set("blog", JSON.toJSONString(list));
            return R.success(list);
        }
    }

}
