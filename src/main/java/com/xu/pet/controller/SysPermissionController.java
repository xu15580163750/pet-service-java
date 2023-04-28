package com.xu.pet.controller;

import com.xu.pet.core.IdRequest;
import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.request.SysPermissionCreateRequest;
import com.xu.pet.request.SysPermissionListRequest;
import com.xu.pet.request.SysPermissionModifyRequest;
import com.xu.pet.request.SysPermissionPageRequest;
import com.xu.pet.response.SysPermissionDetailResponse;
import com.xu.pet.response.SysPermissionListResponse;
import com.xu.pet.response.SysPermissionPageResponse;
import com.xu.pet.service.ISysPermissionService;
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
 * 权限管理
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "", tags = {""})
@RequestMapping("/api/v1/")
@RestController
@Slf4j
public class SysPermissionController {

    @Autowired
    private ISysPermissionService sysPermissionService;


    public Boolean create(@RequestBody @Valid SysPermissionCreateRequest req) {

        return null != sysPermissionService.create(req);
    }


    public Boolean remove(IdRequest req) {

        return sysPermissionService.remove(req.getId());
    }


    public Boolean modify(@RequestBody @Valid SysPermissionModifyRequest req) {

        return sysPermissionService.modify(req);
    }


    public SysPermissionDetailResponse detail(IdRequest req) {

        return sysPermissionService.detail(req.getId());
    }

    public PageInfo<SysPermissionPageResponse> page(@RequestBody @Valid SysPermissionPageRequest req) {

        return sysPermissionService.page(req);
    }


    public List<SysPermissionListResponse> list(@RequestBody @Valid SysPermissionListRequest req) {

        return sysPermissionService.list(req);
    }


    public List<Label> listName(SysPermissionListRequest req) {
        return sysPermissionService.names(req);
    }

}
