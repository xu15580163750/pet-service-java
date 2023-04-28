package com.xu.pet.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.User;
import com.xu.pet.request.*;
import com.xu.pet.response.UserDetailResponse;
import com.xu.pet.response.UserListResponse;
import com.xu.pet.response.UserPageResponse;

import java.util.List;

/**
 * 用户管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
public interface IUserService extends IService<User> {
    /**
     * 新增用户管理
     *
     * @param req 新增用户管理入参
     */
    int create(UserCreateRequest req);

    /**
     * 删除用户管理
     *
     * @param id 主键ID
     */
    boolean remove(Long id);

    /**
     * 修改用户管理
     *
     * @param req 修改用户管理入参
     */
    boolean modify(UserModifyRequest req);

    /**
     * 修改用户管理
     *
     * @param req 修改用户管理入参
     */
    boolean resetPassword(UserListRequest req);

    /**
     * 修改用户管理
     *
     * @param req 修改用户管理入参
     */
    boolean modifyPassword(UserListRequest req);

    /**
     * 详情查询用户管理
     *
     * @param req
     * @return 用户管理详情数据
     */
    UserDetailResponse detail(UserDetailRequest req);


    /**
     * 修改密码
     *
     * @param req
     * @return 用户管理修改密码
     */
    boolean modifyStatus(UserModifyRequest req);


    /**
     * 分页查询用户管理
     *
     * @param req 分页查询用户管理
     * @return 用户管理分页数据
     */
    PageInfo<UserPageResponse> page(UserPageRequest req);

    /**
     * 列表查询用户管理
     *
     * @param req 列表查询用户管理入参
     * @return 用户管理列表数据
     */
    List<UserListResponse> list(UserListRequest req);

    /**
     * 列表查询用户管理根据一组ID
     *
     * @param ids 列表查询用户管理入参
     * @return 列表用户管理对象
     */
    List<UserListResponse> listByIds(List<Long> ids);

    /**
     * 列表用户管理根据条件返回ID
     *
     * @param req 列表用户管理条件入参
     * @return 一组用户管理ID
     */
    List<Long> listId(UserListRequest req);

    /**
     * 计数用户管理
     *
     * @param req 列表查询用户管理入参
     * @return 计数
     */
    long count(UserListRequest req);

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    String name(Long id);

    /**
     * 查询用户管理名称
     *
     * @param ids 一组用户管理ID
     * @return 一组用户管理名称
     */
    List<Label> nameByIds(List<Long> ids);

    /**
     * 根据条件查询用户管理名称
     *
     * @param req 用户管理条件
     * @return 用户管理名称
     */
    List<Label> names(UserListRequest req);

    /**
     * 是否存在用户管理
     *
     * @param req 列表查询用户管理入参
     * @return 是否存在
     */
    boolean exist(UserListRequest req);
}
