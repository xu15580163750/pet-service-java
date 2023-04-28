package com.xu.pet.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.UserMessage;
import com.xu.pet.request.UserMessageCreateRequest;
import com.xu.pet.request.UserMessageListRequest;
import com.xu.pet.request.UserMessageModifyRequest;
import com.xu.pet.request.UserMessagePageRequest;
import com.xu.pet.response.UserMessageDetailResponse;
import com.xu.pet.response.UserMessageListResponse;
import com.xu.pet.response.UserMessagePageResponse;

import java.util.List;


/**
 * 信息管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
public interface IUserMessageService extends IService<UserMessage>{
    /**
     * 新增信息管理
     *
     * @param req 新增信息管理入参
     */
    Long create(UserMessageCreateRequest req);

    /**
     * 删除信息管理
     *
     * @param id 主键ID
     */
    boolean remove(Long id);

    /**
     * 修改信息管理
     *
     * @param req 修改信息管理入参
     */
    boolean modify(UserMessageModifyRequest req);

    /**
     * 详情查询信息管理
     *
     * @param id 主键ID
     * @return 信息管理详情数据
     */
    UserMessageDetailResponse detail(Long id);

    /**
     * 分页查询信息管理
     *
     * @param req 分页查询信息管理
     * @return 信息管理分页数据
     */
    PageInfo<UserMessagePageResponse> page(UserMessagePageRequest req);

    /**
     * 列表查询信息管理
     *
     * @param req 列表查询信息管理入参
     * @return 信息管理列表数据
     */
    List<UserMessageListResponse> list(UserMessageListRequest req);

    /**
     * 列表查询信息管理根据一组ID
     *
     * @param ids 列表查询信息管理入参
     * @return 列表信息管理对象
     */
    List<UserMessageListResponse> listByIds(List<Long> ids);

    /**
     * 列表信息管理根据条件返回ID
     *
     * @param req 列表信息管理条件入参
     * @return 一组信息管理ID
     */
    List<Long> listId(UserMessageListRequest req);

    /**
     * 计数信息管理
     *
     * @param req 列表查询信息管理入参
     * @return 计数
     */
    long count(UserMessageListRequest req);

//    /**
//     * 查询名称
//     *
//     * @param id ID
//     * @return 名称
//     */
//    String name(Long id) ;
//
//    /**
//     * 查询信息管理名称
//     *
//     * @param ids 一组信息管理ID
//     * @return 一组信息管理名称
//     */
//    List<Label> nameByIds(List<Long> ids);
//
//    /**
//     * 根据条件查询信息管理名称
//     *
//     * @param req 信息管理条件
//     * @return 信息管理名称
//     */
//    List<Label> names(UserMessageListRequest req);

    /**
     * 是否存在信息管理
     *
     * @param req 列表查询信息管理入参
     * @return 是否存在
     */
    boolean exist(UserMessageListRequest req);

}
