package com.xu.pet.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.SysRole;
import com.xu.pet.request.SysRoleCreateRequest;
import com.xu.pet.request.SysRoleListRequest;
import com.xu.pet.request.SysRoleModifyRequest;
import com.xu.pet.request.SysRolePageRequest;
import com.xu.pet.response.SysRoleDetailResponse;
import com.xu.pet.response.SysRoleListResponse;
import com.xu.pet.response.SysRolePageResponse;

import java.util.List;

/**
 * 角色管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
public interface ISysRoleService extends IService<SysRole>{
    /**
     * 新增角色管理
     *
     * @param req 新增角色管理入参
     */
    Long create(SysRoleCreateRequest req);

    /**
     * 删除角色管理
     *
     * @param id 主键ID
     */
    boolean remove(Long id);

    /**
     * 修改角色管理
     *
     * @param req 修改角色管理入参
     */
    boolean modify(SysRoleModifyRequest req);

    /**
     * 详情查询角色管理
     *
     * @param id 主键ID
     * @return 角色管理详情数据
     */
    SysRoleDetailResponse detail(Long id);

    /**
     * 根据用户名返回角色信息
     *
     * @param username 用户名
     * @return 角色管理详情数据
     */
    List<SysRoleListResponse> listByUsername(String username);

    /**
     * 分页查询角色管理
     *
     * @param req 分页查询角色管理
     * @return 角色管理分页数据
     */
    PageInfo<SysRolePageResponse> page(SysRolePageRequest req);

    /**
     * 列表查询角色管理
     *
     * @param req 列表查询角色管理入参
     * @return 角色管理列表数据
     */
    List<SysRoleListResponse> list(SysRoleListRequest req);

    /**
     * 列表查询角色菜单，权限
     *
     * @param req 列表查询角色管理入参
     * @return 角色管理列表数据
     */
    List<SysRoleListResponse> listPermissionMeue(SysRoleListRequest req);

    /**
     * 列表查询角色管理根据一组ID
     *
     * @param ids 列表查询角色管理入参
     * @return 列表角色管理对象
     */
    List<SysRoleListResponse> listByIds(List<Long> ids);

    /**
     * 列表角色管理根据条件返回ID
     *
     * @param req 列表角色管理条件入参
     * @return 一组角色管理ID
     */
    List<Long> listId(SysRoleListRequest req);

    /**
     * 计数角色管理
     *
     * @param req 列表查询角色管理入参
     * @return 计数
     */
    long count(SysRoleListRequest req);

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    String name(Long id) ;

    /**
     * 查询角色管理名称
     *
     * @param ids 一组角色管理ID
     * @return 一组角色管理名称
     */
    List<Label> nameByIds(List<Long> ids);

    /**
     * 根据条件查询角色管理名称
     *
     * @param req 角色管理条件
     * @return 角色管理名称
     */
    List<Label> names(SysRoleListRequest req);

    /**
     * 是否存在角色管理
     *
     * @param req 列表查询角色管理入参
     * @return 是否存在
     */
    boolean exist(SysRoleListRequest req);
}
