package com.xu.pet.controller;


import com.xu.pet.request.UserCreateRequest;
import com.xu.pet.request.UserDetailRequest;
import com.xu.pet.response.UserDetailResponse;
import com.xu.pet.result.Result;
import com.xu.pet.result.ResultFactory;
import com.xu.pet.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

@Api(value = "登录管理", tags = {"登录管理"})
@RequestMapping("/api")
@RestController
@Slf4j
public class LoginController {

    @Autowired
    IUserService userService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody UserCreateRequest req) {
        String username = req.getUsername();

        username = HtmlUtils.htmlEscape(username);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, req.getPassword());
        try {
            UserDetailResponse userDetailResponse = userService.detail(UserDetailRequest.builder()
                    .delete_(false)
                    .username(username)
                    .build());
            if (null == userDetailResponse || null == userDetailResponse.getId()) {
                return ResultFactory.buildFailResult("该用户不存在");
            }
            if (null != userDetailResponse.getEnabled() && !userDetailResponse.getEnabled()) {
                return ResultFactory.buildFailResult("该用户已被禁用");
            }
            subject.login(usernamePasswordToken);

            log.info("登录操作-成功：username:{}", username);
            return ResultFactory.buildSuccessResult(username);
        } catch (IncorrectCredentialsException e) {
            return ResultFactory.buildFailResult("密码错误");
        } catch (UnknownAccountException e) {
            return ResultFactory.buildFailResult("账号不存在");
        }
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public Result register(@RequestBody UserCreateRequest user) {
        int status = userService.create(user);
        switch (status) {
            case 0:
                return ResultFactory.buildFailResult("用户名和密码不能为空");
            case 1:
                return ResultFactory.buildSuccessResult("注册成功");
            case 2:
                return ResultFactory.buildFailResult("用户已存在");
        }
        return ResultFactory.buildFailResult("未知错误");
    }

    @ApiOperation(value = "登出")
    @GetMapping("/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultFactory.buildSuccessResult("成功登出");
    }

    @ApiOperation(value = "身份认证")
    @GetMapping("/authentication")
    public String authentication() {
        return "身份认证成功";
    }

}
