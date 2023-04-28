package com.xu.pet.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.SysMenu;
import com.xu.pet.entity.SysPermission;
import com.xu.pet.entity.SysRoleMenu;
import com.xu.pet.request.SysRoleMenuCreateRequest;
import com.xu.pet.request.SysRoleMenuListRequest;
import com.xu.pet.request.SysRoleMenuModifyRequest;
import com.xu.pet.request.SysRoleMenuPageRequest;
import com.xu.pet.response.SysRoleMenuDetailResponse;
import com.xu.pet.response.SysRoleMenuListResponse;
import com.xu.pet.response.SysRoleMenuPageResponse;

import java.util.List;
import java.util.Map;

/**
 * 角色-菜单管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {
    /**
     * 新增角色-菜单管理
     *
     * @param req 新增角色-菜单管理入参
     */
    Long create(SysRoleMenuCreateRequest req);


    /**
     * 新增角色-菜单管理 批量
     *
     * @param roleId 新增角色-菜单管理入参
     */
    boolean createBatch(Long roleId, List<SysRoleMenuCreateRequest> menus);

    /**
     * 删除角色-菜单管理
     *
     * @param id 主键ID
     */
    boolean remove(Long id);

    /**
     * 删除角色-菜单管理-批量
     *
     * @param roleId 主键ID
     */
    boolean removeBatch(Long roleId);

    /**
     * 修改角色-菜单管理
     *
     * @param req 修改角色-菜单管理入参
     */
    boolean modify(SysRoleMenuModifyRequest req);

    /**
     * 详情查询角色-菜单管理
     *
     * @param id 主键ID
     * @return 角色-菜单管理详情数据
     */
    SysRoleMenuDetailResponse detail(Long id);

    /**
     * 分页查询角色-菜单管理
     *
     * @param req 分页查询角色-菜单管理
     * @return 角色-菜单管理分页数据
     */
    PageInfo<SysRoleMenuPageResponse> page(SysRoleMenuPageRequest req);

    /**
     * 列表查询角色-菜单管理
     *
     * @param req 列表查询角色-菜单管理入参
     * @return 角色-菜单管理列表数据
     */
    List<SysRoleMenuListResponse> list(SysRoleMenuListRequest req);

    /**
     * 列表查询角色-菜单管理根据一组ID
     *
     * @param ids 列表查询角色-菜单管理入参
     * @return 列表角色-菜单管理对象
     */
    List<SysRoleMenuListResponse> listByIds(List<Long> ids);

    /**
     * 列表角色-菜单管理根据条件返回ID
     *
     * @param req 列表角色-菜单管理条件入参
     * @return 一组角色-菜单管理ID
     */
    List<Long> listId(SysRoleMenuListRequest req);

    /**
     * 计数角色-菜单管理
     *
     * @param req 列表查询角色-菜单管理入参
     * @return 计数
     */
    long count(SysRoleMenuListRequest req);

//    /**
//     * 查询名称
//     *
//     * @param id ID
//     * @return 名称
//     */
//    String name(Long id) ;
//
//    /**
//     * 查询角色-菜单管理名称
//     *
//     * @param ids 一组角色-菜单管理ID
//     * @return 一组角色-菜单管理名称
//     */
//    List<Label> nameByIds(List<Long> ids);
//
//    /**
//     * 根据条件查询角色-菜单管理名称
//     *
//     * @param req 角色-菜单管理条件
//     * @return 角色-菜单管理名称
//     */
//    List<Label> names(SysRoleMenuListRequest req);

    /**
     * 是否存在角色-菜单管理
     *
     * @param req 列表查询角色-菜单管理入参
     * @return 是否存在
     */
    boolean exist(SysRoleMenuListRequest req);

    /**
     * 更新角色菜单-批量-菜单管理
     *
     * @param roleId 更新角色菜单-批量-菜单管理入参
     * @return 更新角色菜单-批量
     */
    void updateRoleMenu(Long roleId, Map<String, List<Long>> menusIds);
}
