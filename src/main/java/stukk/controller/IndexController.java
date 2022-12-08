package stukk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Api(tags = "首页管理（IndexController）")
public class IndexController {
    @ApiOperation(value = "重定向到宠物平台首页")
    @GetMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/front/page/showTime.html");
    }

    @ApiOperation(value = "重定向到后台登录页面")
    @GetMapping("/admin")
    public void admin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/backend/page/login/login.html");
    }
}
