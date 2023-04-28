package com.xu.pet.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.SysMenu;
import com.xu.pet.request.SysMenuCreateRequest;
import com.xu.pet.request.SysMenuListRequest;
import com.xu.pet.request.SysMenuModifyRequest;
import com.xu.pet.request.SysMenuPageRequest;
import com.xu.pet.response.SysMenuDetailResponse;
import com.xu.pet.response.SysMenuListResponse;
import com.xu.pet.response.SysMenuPageResponse;

import java.util.List;

/**
 * 菜单管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 新增菜单管理
     *
     * @param req 新增菜单管理入参
     */
    Long create(SysMenuCreateRequest req);

    /**
     * 删除菜单管理
     *
     * @param id 主键ID
     */
    boolean remove(Long id);

    /**
     * 修改菜单管理
     *
     * @param req 修改菜单管理入参
     */
    boolean modify(SysMenuModifyRequest req);

    /**
     * 详情查询菜单管理
     *
     * @param id 主键ID
     * @return 菜单管理详情数据
     */
    SysMenuDetailResponse detail(Long id);

    /**
     * 分页查询菜单管理
     *
     * @param req 分页查询菜单管理
     * @return 菜单管理分页数据
     */
    PageInfo<SysMenuPageResponse> page(SysMenuPageRequest req);

    /**
     * 列表查询菜单管理
     *
     * @param req 列表查询菜单管理入参
     * @return 菜单管理列表数据
     */
    List<SysMenuListResponse> list(SysMenuListRequest req);

    /**
     * 列表查询菜单根据roleId
     *
     * @param roleId 列表查询菜单管理入参
     * @return 菜单管理列表数据
     */
    List<SysMenuListResponse> listByRoleId(Long roleId);

    /**
     * 列表查询菜单根据用户名
     *
     * @return 菜单管理列表数据
     */
    List<SysMenuListResponse> listByName();

    /**
     * 列表查询菜单管理根据一组ID
     *
     * @param ids 列表查询菜单管理入参
     * @return 列表菜单管理对象
     */
    List<SysMenuListResponse> listByIds(List<Long> ids);

    /**
     * 列表菜单管理根据条件返回ID
     *
     * @param req 列表菜单管理条件入参
     * @return 一组菜单管理ID
     */
    List<Long> listId(SysMenuListRequest req);

    /**
     * 计数菜单管理
     *
     * @param req 列表查询菜单管理入参
     * @return 计数
     */
    long count(SysMenuListRequest req);

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    String name(Long id);

    /**
     * 查询菜单管理名称
     *
     * @param ids 一组菜单管理ID
     * @return 一组菜单管理名称
     */
    List<Label> nameByIds(List<Long> ids);

    /**
     * 根据条件查询菜单管理名称
     *
     * @param req 菜单管理条件
     * @return 菜单管理名称
     */
    List<Label> names(SysMenuListRequest req);

    /**
     * 是否存在菜单管理
     *
     * @param req 列表查询菜单管理入参
     * @return 是否存在
     */
    boolean exist(SysMenuListRequest req);

}
