package com.xu.pet.controller;

import com.xu.pet.core.IdRequest;
import com.xu.pet.core.PageInfo;
import com.xu.pet.request.SysRolePermissionCreateRequest;
import com.xu.pet.request.SysRolePermissionListRequest;
import com.xu.pet.request.SysRolePermissionModifyRequest;
import com.xu.pet.request.SysRolePermissionPageRequest;
import com.xu.pet.response.SysRolePermissionDetailResponse;
import com.xu.pet.response.SysRolePermissionListResponse;
import com.xu.pet.response.SysRolePermissionPageResponse;
import com.xu.pet.service.ISysRolePermissionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;


import java.util.List;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>
 * 角色权限
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "", tags = {""})
@RequestMapping("/api/v1/")
@RestController
@Slf4j
public class SysRolePermissionController{

    @Autowired
    private ISysRolePermissionService sysRolePermissionService;


    public Boolean create(@RequestBody @Valid SysRolePermissionCreateRequest req) {

        return null != sysRolePermissionService.create(req);
    }


    public Boolean remove(IdRequest req) {

        return sysRolePermissionService.remove(req.getId());
    }


    public Boolean modify(@RequestBody @Valid SysRolePermissionModifyRequest req) {

        return sysRolePermissionService.modify(req);
    }


    public SysRolePermissionDetailResponse detail(IdRequest req) {

        return sysRolePermissionService.detail(req.getId());
    }


    public PageInfo<SysRolePermissionPageResponse> page(@RequestBody @Valid SysRolePermissionPageRequest req) {

        return sysRolePermissionService.page(req);
    }


    public List<SysRolePermissionListResponse> list(@RequestBody @Valid SysRolePermissionListRequest req) {

        return sysRolePermissionService.list(req);
    }

//    @Override
//    public List<Label> listName(SysRolePermissionListRequest req) {
//        return sysRolePermissionService.names(req);
//    }

}
