package com.xu.pet.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.SysPermission;
import com.xu.pet.request.SysPermissionCreateRequest;
import com.xu.pet.request.SysPermissionListRequest;
import com.xu.pet.request.SysPermissionModifyRequest;
import com.xu.pet.request.SysPermissionPageRequest;
import com.xu.pet.response.*;

import java.util.List;
import java.util.Set;

/**
 * 权限管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
public interface ISysPermissionService extends IService<SysPermission> {
    /**
     * 新增权限管理
     *
     * @param req 新增权限管理入参
     */
    Long create(SysPermissionCreateRequest req);

    /**
     * 删除权限管理
     *
     * @param id 主键ID
     */
    boolean remove(Long id);
//
//    /**
//     * 判断权限存在
//     *
//     * @param id 主键ID
//     */
//    boolean remove(Long id);

    /**
     * 修改权限管理
     *
     * @param req 修改权限管理入参
     */
    boolean modify(SysPermissionModifyRequest req);

    /**
     * 详情查询权限管理
     *
     * @param id 主键ID
     * @return 权限管理详情数据
     */
    SysPermissionDetailResponse detail(Long id);

    /**
     * 分页查询权限管理
     *
     * @param req 分页查询权限管理
     * @return 权限管理分页数据
     */
    PageInfo<SysPermissionPageResponse> page(SysPermissionPageRequest req);

    /**
     * 列表查询权限管理
     *
     * @param req 列表查询权限管理入参
     * @return 权限管理列表数据
     */
    List<SysPermissionListResponse> list(SysPermissionListRequest req);

    /**
     * 列表查询权限管理根据一组ID
     *
     * @param ids 列表查询权限管理入参
     * @return 列表权限管理对象
     */
    List<SysPermissionListResponse> listByIds(List<Long> ids);

    /**
     * 根据用户名返回权限信息
     *
     * @param username 用户名
     * @return 列表权限管理对象
     */
    Set<String> listByUsername(String username);

    /**
     * 列表查询权限根据roleId
     *
     * @param roleId 列表查询角色权限入参
     * @return 列表角色权限对象
     */
    List<SysPermissionListResponse> listByRoleId(Long roleId);

    /**
     * 列表权限管理根据条件返回ID
     *
     * @param req 列表权限管理条件入参
     * @return 一组权限管理ID
     */
    List<Long> listId(SysPermissionListRequest req);

    /**
     * 计数权限管理
     *
     * @param req 列表查询权限管理入参
     * @return 计数
     */
    long count(SysPermissionListRequest req);

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    String name(Long id);

    /**
     * 查询权限管理名称
     *
     * @param ids 一组权限管理ID
     * @return 一组权限管理名称
     */
    List<Label> nameByIds(List<Long> ids);

    /**
     * 根据条件查询权限管理名称
     *
     * @param req 权限管理条件
     * @return 权限管理名称
     */
    List<Label> names(SysPermissionListRequest req);

    /**
     * 是否存在权限管理
     *
     * @param req 列表查询权限管理入参
     * @return 是否存在
     */
    boolean exist(SysPermissionListRequest req);
}
