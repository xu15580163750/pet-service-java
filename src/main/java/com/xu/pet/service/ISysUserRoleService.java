package com.xu.pet.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.SysRole;
import com.xu.pet.entity.SysUserRole;
import com.xu.pet.request.SysUserRoleCreateRequest;
import com.xu.pet.request.SysUserRoleListRequest;
import com.xu.pet.request.SysUserRoleModifyRequest;
import com.xu.pet.request.SysUserRolePageRequest;
import com.xu.pet.response.SysUserRoleDetailResponse;
import com.xu.pet.response.SysUserRoleListResponse;
import com.xu.pet.response.SysUserRolePageResponse;

import java.util.List;

/**
 * 用户-角色管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
    /**
     * 新增用户-角色管理
     *
     * @param req 新增用户-角色管理入参
     */
    Long create(SysUserRoleCreateRequest req);

    /**
     * 新增用户-角色管理(批量)
     *
     * @param userId 新增用户-角色管理入参
     */
    boolean createBatch(Long userId, List<SysRole> roles);

    /**
     * 删除用户-角色管理
     *
     * @param id 主键ID
     */
    boolean remove(Long id);

    /**
     * 删除用户-角色管理(批量)
     *
     * @param userId 主键ID
     */
    boolean removeBatch(Long userId);



    /**
     * 修改用户-角色管理
     *
     * @param req 修改用户-角色管理入参
     */
    boolean modify(SysUserRoleModifyRequest req);

    /**
     * 详情查询用户-角色管理
     *
     * @param id 主键ID
     * @return 用户-角色管理详情数据
     */
    SysUserRoleDetailResponse detail(Long id);

    /**
     * 分页查询用户-角色管理
     *
     * @param req 分页查询用户-角色管理
     * @return 用户-角色管理分页数据
     */
    PageInfo<SysUserRolePageResponse> page(SysUserRolePageRequest req);

    /**
     * 列表查询用户-角色管理
     *
     * @param req 列表查询用户-角色管理入参
     * @return 用户-角色管理列表数据
     */
    List<SysUserRoleListResponse> list(SysUserRoleListRequest req);

    /**
     * 列表查询用户-角色管理根据一组ID
     *
     * @param ids 列表查询用户-角色管理入参
     * @return 列表用户-角色管理对象
     */
    List<SysUserRoleListResponse> listByIds(List<Long> ids);

    /**
     * 列表用户-角色管理根据条件返回ID
     *
     * @param req 列表用户-角色管理条件入参
     * @return 一组用户-角色管理ID
     */
    List<Long> listId(SysUserRoleListRequest req);

    /**
     * 计数用户-角色管理
     *
     * @param req 列表查询用户-角色管理入参
     * @return 计数
     */
    long count(SysUserRoleListRequest req);

//    /**
//     * 查询名称
//     *
//     * @param id ID
//     * @return 名称
//     */
//    String name(Long id) ;
//
//    /**
//     * 查询用户-角色管理名称
//     *
//     * @param ids 一组用户-角色管理ID
//     * @return 一组用户-角色管理名称
//     */
//    List<Label> nameByIds(List<Long> ids);
//
//    /**
//     * 根据条件查询用户-角色管理名称
//     *
//     * @param req 用户-角色管理条件
//     * @return 用户-角色管理名称
//     */
//    List<Label> names(SysUserRoleListRequest req);

    /**
     * 是否存在用户-角色管理
     *
     * @param req 列表查询用户-角色管理入参
     * @return 是否存在
     */
    boolean exist(SysUserRoleListRequest req);

}
