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
import stukk.entity.Cat;
import stukk.entity.Dog;
import stukk.service.CatService;
import stukk.service.DogService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/animal")
@Api(tags = "猫狗管理（AnimalController）")
public class AnimalController {

    @Autowired
    DogService dogService;

    @Autowired
    CatService catService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "根据ID获取狗狗的信息")
    @GetMapping("/getDogById/{id}")
    public R<Dog> getDogById(@PathVariable("id") Integer id) {
        Dog dog = dogService.query().eq("id", id).one();
        return R.success(dog);
    }

    @ApiOperation(value = "根据ID获取猫猫的信息")
    @GetMapping("/getCatById/{id}")
    public R<Cat> getCatById(@PathVariable("id") Integer id) {
        Cat cat = catService.query().eq("id", id).one();
        return R.success(cat);
    }

    @ApiOperation(value = "获取全部猫猫的信息")
    @GetMapping("/getAllCat")
    public R<List<Map<String, Object>>> getAllCat() {
        String cat1 = stringRedisTemplate.opsForValue().get("cat");
        if (cat1 != null) {
            return R.success((List<Map<String, Object>>) JSON.parse(cat1));
        }

        List<Cat> cats = catService.query().list();
        List<Map<String, Object>> list = new LinkedList<>();
        for (Cat cat : cats) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cat.getId());
            map.put("name", cat.getName());
            map.put("url", cat.getUrl());
            list.add(map);
        }
        stringRedisTemplate.opsForValue().set("cat", JSON.toJSONString(list));
        return R.success(list);
    }


    @ApiOperation(value = "获取全部狗狗的信息")
    @GetMapping("/getAllDog")
    public R<List<Map<String, Object>>> getAllDog() {

        String dog1 = stringRedisTemplate.opsForValue().get("dog");
        if (dog1 != null) {
            return R.success((List<Map<String, Object>>) JSON.parse(dog1));
        }

        List<Dog> dogs = dogService.query().list();
        List<Map<String, Object>> list = new LinkedList<>();
        for (Dog dog : dogs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", dog.getId());
            map.put("name", dog.getName());
            map.put("url", dog.getUrl());
            list.add(map);
        }
        stringRedisTemplate.opsForValue().set("dog", JSON.toJSONString(list));
        return R.success(list);
    }

}
