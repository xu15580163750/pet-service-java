package com.xu.pet.controller;

import com.xu.pet.core.IdRequest;
import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.request.*;
import com.xu.pet.response.UserDetailResponse;
import com.xu.pet.response.UserListResponse;
import com.xu.pet.response.UserPageResponse;
import com.xu.pet.result.Result;
import com.xu.pet.result.ResultFactory;
import com.xu.pet.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;


import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户管理
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "用户管理", tags = {"用户管理"})
@RequestMapping("/api")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 创建
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "创建")
    @PostMapping(value = "/user/create")
    public Boolean create(@RequestBody @Valid UserCreateRequest req) {

        return null != Integer.valueOf(userService.create(req));
    }

    /**
     * 删除
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "删除")
    @PostMapping("/user/remove")
    public Boolean remove(IdRequest req) {

        return userService.remove(req.getId());
    }

    /**
     * 修改
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping(value = "/user/modify")
    public Boolean modify(@RequestBody @Valid UserModifyRequest req) {

        return userService.modify(req);
    }

    /**
     * 详细
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "详细")
    @PostMapping("/admin/content/info")
    public UserDetailResponse detail(@RequestBody @Valid  UserDetailRequest req) {

        UserDetailResponse detail = userService.detail(req);
        return detail;
    }

    /**
     * 分页
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "分页")
    @PostMapping(value = "/user/page")
    public PageInfo<UserPageResponse> page(@RequestBody @Valid UserPageRequest req) {

        return userService.page(req);
    }

    /**
     * 列表
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "列表")
    @PostMapping(value = "/user/list")
    public List<UserListResponse> list(@RequestBody @Valid UserListRequest req) {

        return userService.list(req);
    }

    /**
     * 名称列表
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "名称列表")
    @PostMapping(value = "/user/list/name")
    public List<Label> listName(UserListRequest req) {
        return userService.names(req);
    }


    @GetMapping("/admin/user")
    public Result listUsers() {
        return ResultFactory.buildSuccessResult(userService.list(UserListRequest.builder().delete_(false).build()));
    }


    @PutMapping("/admin/user/status")
    public Result updateUserStatus(@RequestBody @Valid UserModifyRequest request) {
        userService.modifyStatus(request);
        return ResultFactory.buildSuccessResult("用户状态更新成功");
    }

    @PutMapping("/admin/content/info")
    public Result modifyUserInfo(@RequestBody @Valid UserModifyRequest request) {
        userService.modifyStatus(request);
        return ResultFactory.buildSuccessResult("用户信息更新成功");
    }


    @PutMapping("/admin/user/password")
    public Result resetPassword(@RequestBody @Valid UserListRequest request) {
        userService.resetPassword(request);
        return ResultFactory.buildSuccessResult("重置密码成功");
    }


    @PutMapping("/admin/user")
    public Result editUser(@RequestBody @Valid UserModifyRequest request) {
        userService.modify(request);
        return ResultFactory.buildSuccessResult("修改用户信息成功");
    }


    @PutMapping("/admin/user/remove")
    public Result removeUser(@RequestBody @Valid UserModifyRequest request) {
        request.setDelete_(true);
        userService.modifyStatus(request);
        return ResultFactory.buildSuccessResult("此用户已删除");
    }


    @PutMapping("/admin/content/modify/password")
    public Result modifyPassword(@RequestBody @Valid UserListRequest request) {
        boolean result = userService.modifyPassword(request);
        if (!result) {
            ResultFactory.buildFailResult("原密码不存在,密码修改失败");
        }
        return ResultFactory.buildSuccessResult("密码修改成功");
    }


}
