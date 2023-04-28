package com.xu.pet.controller;

import com.xu.pet.core.BusinessException;
import com.xu.pet.core.IdRequest;
import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.enums.EErrorCode;
import com.xu.pet.request.*;
import com.xu.pet.response.SysRoleDetailResponse;
import com.xu.pet.response.SysRoleListResponse;
import com.xu.pet.response.SysRolePageResponse;
import com.xu.pet.result.Result;
import com.xu.pet.result.ResultFactory;
import com.xu.pet.service.ISysPermissionService;
import com.xu.pet.service.ISysRoleMenuService;
import com.xu.pet.service.ISysRolePermissionService;
import com.xu.pet.service.ISysRoleService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;


import java.util.List;
import java.util.Map;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 角色管理
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "", tags = {""})
@RequestMapping("/api")
@RestController
@Slf4j
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysRolePermissionService rolePermissionService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysRoleMenuService roleMenuService;


    public Boolean create(@RequestBody @Valid SysRoleCreateRequest req) {

        return null != sysRoleService.create(req);
    }


    public Boolean remove(IdRequest req) {

        return sysRoleService.remove(req.getId());
    }


    public Boolean modify(@RequestBody @Valid SysRoleModifyRequest req) {

        return sysRoleService.modify(req);
    }


    public SysRoleDetailResponse detail(IdRequest req) {

        return sysRoleService.detail(req.getId());
    }


    public PageInfo<SysRolePageResponse> page(@RequestBody @Valid SysRolePageRequest req) {

        return sysRoleService.page(req);
    }


    public List<SysRoleListResponse> list(@RequestBody @Valid SysRoleListRequest req) {

        return sysRoleService.list(req);
    }


    public List<Label> listName(SysRoleListRequest req) {
        return sysRoleService.names(req);
    }


    @GetMapping("/admin/role")
    public Result listRoles() {
        return ResultFactory.buildSuccessResult(sysRoleService.listPermissionMeue(SysRoleListRequest.builder().delete_(false).build()));
    }

    @PutMapping("/admin/role/status")
    public Result updateRoleStatus(@RequestBody @Valid SysRoleModifyRequest request) {
        if (null == request.getId()) {
            throw new BusinessException(EErrorCode.BIZ_DEFAULT.getCode(), "角色id不能为空");
        }
        if (null == request.getEnabled()) {
            throw new BusinessException(EErrorCode.BIZ_DEFAULT.getCode(), "角色状态不能为空");
        }
        boolean status = sysRoleService.modify(request);
        SysRoleDetailResponse adminRole = sysRoleService.detail(request.getId());

        String message = "用户" + adminRole.getNameZh() + "状态更新成功";
        if (!status) {
            throw new BusinessException(EErrorCode.BIZ_DEFAULT.getCode(), "状态更新异常");
        }
        return ResultFactory.buildSuccessResult(message);
    }

    @PutMapping("/admin/role")
    public Result editRole(@RequestBody SysRoleCreateRequest request) {
        rolePermissionService.createBatch(request.getId(), request.getPerms());
        String message = "修改角色信息成功";
        return ResultFactory.buildSuccessResult(message);
    }


    @PostMapping("/admin/role")
    public Result addRole(@RequestBody SysRoleCreateRequest request) {
        Long roleId = sysRoleService.create(request);
//        rolePermissionService.createBatch(roleId, request.getPermissions());
        return ResultFactory.buildSuccessResult("角色增加成功");
    }

    @GetMapping("/admin/role/perm")
    public Result listPerms() {
        return ResultFactory.buildSuccessResult(permissionService.list(SysPermissionListRequest.builder().build()));
    }

    @PutMapping("/admin/role/menu")
    public Result updateRoleMenu(@RequestParam Long rid, @RequestBody Map<String, List<Long>> menusIds) {
        roleMenuService.updateRoleMenu(rid, menusIds);
        return ResultFactory.buildSuccessResult("更新成功");
    }

    @PutMapping("/admin/role/remove")
    public Result removeUser(@RequestBody @Valid SysRoleModifyRequest request) {
        request.setDelete_(true);
        sysRoleService.modify(request);
        return ResultFactory.buildSuccessResult("此角色已删除");
    }

}
