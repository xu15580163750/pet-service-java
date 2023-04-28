package com.xu.pet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.entity.Category;
import com.xu.pet.request.CategoryCreateRequest;
import com.xu.pet.request.CategoryListRequest;
import com.xu.pet.request.CategoryModifyRequest;
import com.xu.pet.request.CategoryPageRequest;
import com.xu.pet.response.CategoryDetailResponse;
import com.xu.pet.response.CategoryListResponse;
import com.xu.pet.response.CategoryPageResponse;

import java.util.List;

/**
 * @author xuqingf
 * @since 2023-03-21
 */
public interface ICategoryService extends IService<Category> {
    /**
     * 新增
     *
     * @param req 新增入参
     */
    Integer create(CategoryCreateRequest req);

    /**
     * 删除
     *
     * @param id 主键ID
     */
    boolean remove(Long id);

    /**
     * 修改
     *
     * @param req 修改入参
     */
    boolean modify(CategoryModifyRequest req);

    /**
     * 详情查询
     *
     * @param id 主键ID
     * @return 详情数据
     */
    CategoryDetailResponse detail(Long id);

    /**
     * 分页查询
     *
     * @param req 分页查询
     * @return 分页数据
     */
    PageInfo<CategoryPageResponse> page(CategoryPageRequest req);

    /**
     * 列表查询
     *
     * @param req 列表查询入参
     * @return 列表数据
     */
    List<CategoryListResponse> list(CategoryListRequest req);

    /**
     * 列表查询根据一组ID
     *
     * @param ids 列表查询入参
     * @return 列表对象
     */
    List<CategoryListResponse> listByIds(List<Long> ids);

    /**
     * 列表根据条件返回ID
     *
     * @param req 列表条件入参
     * @return 一组ID
     */
    List<Long> listId(CategoryListRequest req);

    /**
     * 计数
     *
     * @param req 列表查询入参
     * @return 计数
     */
    long count(CategoryListRequest req);

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    String name(Integer id);

    /**
     * 查询名称
     *
     * @param ids 一组ID
     * @return 一组名称
     */
    List<Label> nameByIds(List<Long> ids);

    /**
     * 根据条件查询名称
     *
     * @param req 条件
     * @return 名称
     */
    List<Label> names(CategoryListRequest req);

    /**
     * 是否存在
     *
     * @param req 列表查询入参
     * @return 是否存在
     */
    boolean exist(CategoryListRequest req);
}
