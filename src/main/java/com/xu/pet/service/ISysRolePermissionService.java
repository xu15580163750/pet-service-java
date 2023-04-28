package com.xu.pet.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.SysPermission;
import com.xu.pet.entity.SysRole;
import com.xu.pet.entity.SysRolePermission;
import com.xu.pet.request.*;
import com.xu.pet.response.SysRolePermissionDetailResponse;
import com.xu.pet.response.SysRolePermissionListResponse;
import com.xu.pet.response.SysRolePermissionPageResponse;

import java.util.List;
import java.util.Set;

/**
 * 角色权限
 *
 * @author xuqingf
 * @since 2023-03-21
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {
    /**
     * 新增角色权限
     *
     * @param req 新增角色权限入参
     */
    Long create(SysRolePermissionCreateRequest req);

    /**
     * 新增角色权限-批量
     *
     * @param roleId 新增角色权限入参
     */
    boolean createBatch(Long roleId, List<SysPermissionCreateRequest> permissions);

    /**
     * 删除角色权限
     *
     * @param id 主键ID
     */
    boolean remove(Long id);

    /**
     * 删除角色权限-批量
     *
     * @param roleId 主键ID
     */
    boolean removeBatch(Long roleId);

    /**
     * 修改角色权限
     *
     * @param req 修改角色权限入参
     */
    boolean modify(SysRolePermissionModifyRequest req);

    /**
     * 详情查询角色权限
     *
     * @param id 主键ID
     * @return 角色权限详情数据
     */
    SysRolePermissionDetailResponse detail(Long id);

    /**
     * 分页查询角色权限
     *
     * @param req 分页查询角色权限
     * @return 角色权限分页数据
     */
    PageInfo<SysRolePermissionPageResponse> page(SysRolePermissionPageRequest req);

    /**
     * 列表查询角色权限
     *
     * @param req 列表查询角色权限入参
     * @return 角色权限列表数据
     */
    List<SysRolePermissionListResponse> list(SysRolePermissionListRequest req);

    /**
     * 列表查询角色权限根据一组ID
     *
     * @param ids 列表查询角色权限入参
     * @return 列表角色权限对象
     */
    List<SysRolePermissionListResponse> listByIds(List<Long> ids);

    /**
     * 列表查询角色权限根据用户名
     *
     * @param username 列表查询角色权限入参
     * @return 列表角色权限对象
     */
    Set<String> listByName(String username);


    /**
     * 列表角色权限根据条件返回ID
     *
     * @param req 列表角色权限条件入参
     * @return 一组角色权限ID
     */
    List<Long> listId(SysRolePermissionListRequest req);

    /**
     * 计数角色权限
     *
     * @param req 列表查询角色权限入参
     * @return 计数
     */
    long count(SysRolePermissionListRequest req);

//    /**
//     * 查询名称
//     *
//     * @param id ID
//     * @return 名称
//     */
//    String name(Long id) ;
//
//    /**
//     * 查询角色权限名称
//     *
//     * @param ids 一组角色权限ID
//     * @return 一组角色权限名称
//     */
//    List<Label> nameByIds(List<Long> ids);
//
//    /**
//     * 根据条件查询角色权限名称
//     *
//     * @param req 角色权限条件
//     * @return 角色权限名称
//     */
//    List<Label> names(SysRolePermissionListRequest req);

    /**
     * 是否存在角色权限
     *
     * @param req 列表查询角色权限入参
     * @return 是否存在
     */
    boolean exist(SysRolePermissionListRequest req);
}
