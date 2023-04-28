package com.xu.pet.controller;

import com.xu.pet.core.IdRequest;
import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.request.SysMenuCreateRequest;
import com.xu.pet.request.SysMenuListRequest;
import com.xu.pet.request.SysMenuModifyRequest;
import com.xu.pet.request.SysMenuPageRequest;
import com.xu.pet.response.SysMenuDetailResponse;
import com.xu.pet.response.SysMenuListResponse;
import com.xu.pet.response.SysMenuPageResponse;
import com.xu.pet.result.Result;
import com.xu.pet.result.ResultFactory;
import com.xu.pet.service.ISysMenuService;
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
 * 菜单管理
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "", tags = {""})
@RequestMapping("/api")
@RestController
@Slf4j
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;


    public Boolean create(@RequestBody @Valid SysMenuCreateRequest req) {

        return null != sysMenuService.create(req);
    }


    public Boolean remove(IdRequest req) {

        return sysMenuService.remove(req.getId());
    }

    public Boolean modify(@RequestBody @Valid SysMenuModifyRequest req) {

        return sysMenuService.modify(req);
    }


    public SysMenuDetailResponse detail(IdRequest req) {

        return sysMenuService.detail(req.getId());
    }


    public PageInfo<SysMenuPageResponse> page(@RequestBody @Valid SysMenuPageRequest req) {

        return sysMenuService.page(req);
    }


    public List<SysMenuListResponse> list(@RequestBody @Valid SysMenuListRequest req) {

        return sysMenuService.list(req);
    }


    public List<Label> listName(SysMenuListRequest req) {
        return sysMenuService.names(req);
    }

    @GetMapping("/menu")
    public Result menu() {
        return ResultFactory.buildSuccessResult(sysMenuService.listByName());
    }

    @GetMapping("/admin/role/menu")
    public Result listAllMenus() {//管理员直接写死1
        return ResultFactory.buildSuccessResult(sysMenuService.listByRoleId(1L));
    }
}
