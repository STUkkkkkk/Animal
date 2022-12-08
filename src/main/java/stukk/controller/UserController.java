package stukk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import stukk.common.R;
import stukk.entity.User;
import stukk.service.UserService;
import stukk.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wenli
 * @create 2022-09-03 17:41
 * 用户管理
 */
@Api(tags = "用户登录管理（UserController）")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机短信验证码
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "发送四位验证码-sendMsg")
    @PostMapping("/sendMsg")
    public R<String> sendMsg(
            @ApiParam(value = "用户对象", required = true)
            @RequestBody User user) {
        // 获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            // 生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = {}", code);

            // 调用阿里云提供的短信服务API完成发送短信
            // SMSUtils.sendMessage("", "", phone, code);

            // 保存验证码到session域中
            // session.setAttribute(phone, code);

            // 将生成的验证码缓存到Redis中，并且设置有效期为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.success("发送短信成功！");
        }

        return R.error("发送短信失败！");
    }


    @ApiOperation(value = "用户退出登录-loginout")
    @PostMapping("/loginout")
    public R<String> loginout(
            HttpSession session) {
        session.removeAttribute("user");
        return R.success("退出成功");
    }

    @ApiOperation(value = "用户登录-login")
    @PostMapping("/login")
    public R<User> login(@ApiParam(value = "用户登录数据", required = true)
                         @RequestBody Map<String, String> map,
                         @ApiParam(value = "HttpSession对象")
                         HttpSession session) {
        // 获取手机号
        String phone = map.get("phone");
        // 获取验证码
        String code = map.get("code");
        // 从session中获取保存的验证码
        // String codeInSession = (String) session.getAttribute(phone);

        // 从Redis中获取缓存的验证码
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);

        // 验证码比对，登录
        if (codeInSession != null && codeInSession.equals(code)) {
            // 判断是否为新用户，为新用户注册、登录
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            // 将用户信息存储到session
            session.setAttribute("user", user.getId());

            // 如果用户登录成功，删除Redis中缓存的验证码
            redisTemplate.delete(phone);

            return R.success(user);
        }
        return R.error("登录失败！");
    }
}
