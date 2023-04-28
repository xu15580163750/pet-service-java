package com.xu.pet.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.Pet;
import com.xu.pet.request.PetCreateRequest;
import com.xu.pet.request.PetListRequest;
import com.xu.pet.request.PetModifyRequest;
import com.xu.pet.request.PetPageRequest;
import com.xu.pet.response.PetDetailResponse;
import com.xu.pet.response.PetListResponse;
import com.xu.pet.response.PetPageResponse;

import java.util.List;

/**
 * 宠物信息
 *
 * @author xuqingf
 * @since 2023-03-21
 */
public interface IPetService extends IService<Pet>{
    /**
     * 新增宠物信息
     *
     * @param req 新增宠物信息入参
     */
    Long create(PetCreateRequest req);

    /**
     * 删除宠物信息
     *
     * @param id 主键ID
     */
    boolean remove(Long id);

    /**
     * 修改宠物信息
     *
     * @param req 修改宠物信息入参
     */
    boolean modify(PetModifyRequest req);

    /**
     * 详情查询宠物信息
     *
     * @param id 主键ID
     * @return 宠物信息详情数据
     */
    PetDetailResponse detail(Long id);

    /**
     * 分页查询宠物信息
     *
     * @param req 分页查询宠物信息
     * @return 宠物信息分页数据
     */
    PageInfo<PetPageResponse> page(PetPageRequest req);

    /**
     * 列表查询宠物信息
     *
     * @param req 列表查询宠物信息入参
     * @return 宠物信息列表数据
     */
    List<PetListResponse> list(PetListRequest req);

    /**
     * 列表查询宠物信息根据一组ID
     *
     * @param ids 列表查询宠物信息入参
     * @return 列表宠物信息对象
     */
    List<PetListResponse> listByIds(List<Long> ids);

    /**
     * 列表宠物信息根据条件返回ID
     *
     * @param req 列表宠物信息条件入参
     * @return 一组宠物信息ID
     */
    List<Long> listId(PetListRequest req);

    /**
     * 计数宠物信息
     *
     * @param req 列表查询宠物信息入参
     * @return 计数
     */
    long count(PetListRequest req);

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    String name(Long id) ;

    /**
     * 查询宠物信息名称
     *
     * @param ids 一组宠物信息ID
     * @return 一组宠物信息名称
     */
    List<Label> nameByIds(List<Long> ids);

    /**
     * 根据条件查询宠物信息名称
     *
     * @param req 宠物信息条件
     * @return 宠物信息名称
     */
    List<Label> names(PetListRequest req);

    /**
     * 是否存在宠物信息
     *
     * @param req 列表查询宠物信息入参
     * @return 是否存在
     */
    boolean exist(PetListRequest req);

}
