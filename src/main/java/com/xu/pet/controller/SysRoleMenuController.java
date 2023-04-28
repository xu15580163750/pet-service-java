package com.xu.pet.controller;

import com.xu.pet.core.IdRequest;
import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.request.SysRoleMenuCreateRequest;
import com.xu.pet.request.SysRoleMenuListRequest;
import com.xu.pet.request.SysRoleMenuModifyRequest;
import com.xu.pet.request.SysRoleMenuPageRequest;
import com.xu.pet.response.SysRoleMenuDetailResponse;
import com.xu.pet.response.SysRoleMenuListResponse;
import com.xu.pet.response.SysRoleMenuPageResponse;
import com.xu.pet.service.ISysRoleMenuService;
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
 * 角色-菜单管理
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "", tags = {""})
@RequestMapping("/api/v1/")
@RestController
@Slf4j
public class SysRoleMenuController {

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;


    public Boolean create(@RequestBody @Valid SysRoleMenuCreateRequest req) {

        return null != sysRoleMenuService.create(req);
    }


    public Boolean remove(IdRequest req) {

        return sysRoleMenuService.remove(req.getId());
    }


    public Boolean modify(@RequestBody @Valid SysRoleMenuModifyRequest req) {

        return sysRoleMenuService.modify(req);
    }


    public SysRoleMenuDetailResponse detail(IdRequest req) {

        return sysRoleMenuService.detail(req.getId());
    }


    public PageInfo<SysRoleMenuPageResponse> page(@RequestBody @Valid SysRoleMenuPageRequest req) {

        return sysRoleMenuService.page(req);
    }


    public List<SysRoleMenuListResponse> list(@RequestBody @Valid SysRoleMenuListRequest req) {

        return sysRoleMenuService.list(req);
    }


//    public List<Label> listName(SysRoleMenuListRequest req) {
//        return sysRoleMenuService.names(req);
//    }

}
