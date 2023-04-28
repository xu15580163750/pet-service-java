package com.xu.pet.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.entity.Category;
import com.xu.pet.mapper.CategoryMapper;
import com.xu.pet.service.ICategoryService;
import com.xu.pet.service.IMappingService;
import com.xu.pet.service.IPetService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import com.xu.pet.request.*;
import com.xu.pet.response.*;

import java.util.Collections;
import java.util.List;

import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Resource
    private CategoryMapper categoryMapper;


    @Autowired
    private IMappingService mappingService;

    private LambdaQueryWrapper<Category> getQuery(CategoryListRequest req) {
        Category condition = mappingService.map(req, Category.class);
        LambdaQueryWrapper<Category> query = new LambdaQueryWrapper<>(condition);

        return query;
    }

    /**
     * 新增
     *
     * @param req 新增入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "Category::batch", allEntries = true)
            }
    )
    public Integer create(CategoryCreateRequest req) {
        Category category = mappingService.map(req, Category.class);

        categoryMapper.insert(category);

        return category.getId();
    }

    /**
     * 删除
     *
     * @param id 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "Category::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "Category::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "Category::info", condition = "#result")
            }
    )
    public boolean remove(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }

    /**
     * 修改
     *
     * @param req 修改入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "Category::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "Category::detail", condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "Category::info", condition = "#result")
            }
    )
    public boolean modify(CategoryModifyRequest req) {
        if (null == req || null == req.getId()) {
            return false;
        }
        Category category = mappingService.map(req, Category.class);

        return categoryMapper.updateById(category) > 0;
    }

    /**
     * 详情查询
     *
     * @param id 主键ID
     * @return 对象
     */
    @Override
    @Cacheable(value = "Category::detail", key = "#p0", condition = "#p0 != null")
    public CategoryDetailResponse detail(Long id) {
        if (null == id) {
            return null;
        }
        Category category = categoryMapper.selectById(id);
        if (null == category) {
            return null;
        }

        return mappingService.map(category, CategoryDetailResponse.class);
    }

//    /**
//    * 查询
//    *
//    * @param id 主键ID
//    * @return 对象
//    */
//    @Override
//    @Cacheable(value = "Category::info", key = "#p0", condition = "#p0 != null")
//    public CategoryInfoResponse info(Long id) {
//        if (null == id) {
//        return null;
//        }
//        Category category = categoryMapper.selectById(id);
//        if (null == category) {
//        return null;
//        }
//
//        return mappingService.map(category, CategoryInfoResponse.class);
//    }

    /**
     * 分页查询
     *
     * @param req 分页查询入参
     * @return 分页对象
     */
    @Override
    @Cacheable(value = "Category::batch::page", key = "#p0")
    public PageInfo<CategoryPageResponse> page(CategoryPageRequest req) {
        LambdaQueryWrapper<Category> query = getQuery(mappingService.map(req, CategoryListRequest.class));

        IPage<Category> result = categoryMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), CategoryPageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
     * 列表查询
     *
     * @param req 列表查询入参
     * @return 列表对象
     */
    @Override
    @Cacheable(value = "Category::batch::list", key = "#p0")
    public List<CategoryListResponse> list(CategoryListRequest req) {
        LambdaQueryWrapper<Category> query = getQuery(req);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<Category> result = categoryMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, CategoryListResponse.class);
        }

        IPage<Category> result = categoryMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);

        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), CategoryListResponse.class);
    }

    /**
     * 根据一组ID查询列表
     *
     * @param ids 列表查询入参
     * @return 列表对象
     */
    @Override
    @Cacheable(value = "Category::batch::listByIds", key = "#p0")
    public List<CategoryListResponse> listByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Category> result = categoryMapper.selectBatchIds(ids);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, CategoryListResponse.class);
    }

    /**
     * 列表根据条件返回一组ID
     *
     * @param req 列表条件入参
     * @return 一组ID
     */
    @Override
    @Cacheable(value = "Category::batch::listId", key = "#p0")
    public List<Long> listId(CategoryListRequest req) {
        LambdaQueryWrapper<Category> query = getQuery(req);
        query.select(Category::getId);

        List<?> result = categoryMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
     * 计数
     *
     * @param req 列表查询入参
     * @return 计数
     */
    @Override
    public long count(CategoryListRequest req) {
        LambdaQueryWrapper<Category> query = getQuery(req);

        return categoryMapper.selectCount(query);
    }

    /**
     * 是否存在{table.comment!}
     *
     * @param req 列表查询{table.comment!}入参
     * @return 是否存在
     */
    @Override
    public boolean exist(CategoryListRequest req) {
        LambdaQueryWrapper<Category> query = getQuery(req)
                .select(Category::getId)
                .last("limit 1");

        List<?> result = categoryMapper.selectObjs(query);

        return null != result && !result.isEmpty();
    }

    /**
     * 根据一组ID返回名称
     *
     * @param ids 一组ID
     * @return 一组名称
     */
    @Override
    public List<Label> nameByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Category> query = new LambdaQueryWrapper<>(new Category())
                .in(Category::getId, ids)
                .select(Category::getId, Category::getName);

        List<Category> result = categoryMapper.selectList(query);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
    }

    /**
     * 根据条件返回一组名称
     *
     * @param req 条件
     * @return 名称
     */
    @Override
    public List<Label> names(CategoryListRequest req) {
        LambdaQueryWrapper<Category> query = getQuery(req)
                .select(Category::getId, Category::getName);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<Category> result = categoryMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
        }

        IPage<Category> result = categoryMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), Label.class);
    }

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    @Override
//    @Cacheable(key = "#p0", value = "Category::name", condition = "#p0 != null")
    public String name(Integer id) {
        if (null == id || 0L == id) {
            return "";
        }
        LambdaQueryWrapper<Category> query = new LambdaQueryWrapper<>(Category.builder().id(id).build())
                .select(Category::getName);

        List<?> result = categoryMapper.selectObjs(query);
        return null == result || result.isEmpty() ? null : (String) result.get(0);
    }

}
