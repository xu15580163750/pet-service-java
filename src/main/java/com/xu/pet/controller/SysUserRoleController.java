package com.xu.pet.controller;

import com.xu.pet.core.IdRequest;
import com.xu.pet.core.PageInfo;
import com.xu.pet.request.SysUserRoleCreateRequest;
import com.xu.pet.request.SysUserRoleListRequest;
import com.xu.pet.request.SysUserRoleModifyRequest;
import com.xu.pet.request.SysUserRolePageRequest;
import com.xu.pet.response.SysUserRoleDetailResponse;
import com.xu.pet.response.SysUserRoleListResponse;
import com.xu.pet.response.SysUserRolePageResponse;
import com.xu.pet.service.ISysUserRoleService;
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
 * 用户-角色管理
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "", tags = {""})
@RequestMapping("/api/v1/")
@RestController
@Slf4j
public class SysUserRoleController{

    @Autowired
    private ISysUserRoleService sysUserRoleService;


    public Boolean create(@RequestBody @Valid SysUserRoleCreateRequest req) {

        return null != sysUserRoleService.create(req);
    }


    public Boolean remove(IdRequest req) {

        return sysUserRoleService.remove(req.getId());
    }


    public Boolean modify(@RequestBody @Valid SysUserRoleModifyRequest req) {

        return sysUserRoleService.modify(req);
    }


    public SysUserRoleDetailResponse detail(IdRequest req) {

        return sysUserRoleService.detail(req.getId());
    }


    public PageInfo<SysUserRolePageResponse> page(@RequestBody @Valid SysUserRolePageRequest req) {

        return sysUserRoleService.page(req);
    }


    public List<SysUserRoleListResponse> list(@RequestBody @Valid SysUserRoleListRequest req) {

        return sysUserRoleService.list(req);
    }

//    @Override
//    public List<Label> listName(SysUserRoleListRequest req) {
//        return sysUserRoleService.names(req);
//    }
//
//    private class SysUserRolePageRequest {
//    }
}
