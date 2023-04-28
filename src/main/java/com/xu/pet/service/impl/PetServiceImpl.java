package com.xu.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.entity.Pet;
import com.xu.pet.enums.EHelpStatus;
import com.xu.pet.mapper.PetMapper;
import com.xu.pet.service.ICategoryService;
import com.xu.pet.service.IMappingService;
import com.xu.pet.service.IPetService;
import com.xu.pet.service.IUserService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import com.xu.pet.request.*;
import com.xu.pet.response.*;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 宠物信息
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements IPetService {

    @Resource
    private PetMapper petMapper;

    @Autowired
    private IMappingService mappingService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserService userService;

    private LambdaQueryWrapper<Pet> getQuery(PetListRequest req) {
        Pet condition = mappingService.map(req, Pet.class);
        condition.setName(null);
        condition.setDelete_(false);
        LambdaQueryWrapper<Pet> query = new LambdaQueryWrapper<>(condition);
        query.like(null != req.getName(), Pet::getName, req.getName());
        query.orderByDesc(Pet::getCreateTime);

        return query;
    }

    /**
     * 新增宠物信息
     *
     * @param req 新增宠物信息入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "Pet::batch", allEntries = true)
            }
    )
    public Long create(PetCreateRequest req) {
        if (null != req.getUsername()) {
            Long userId = userService.detail(UserDetailRequest.builder().username(req.getUsername()).build()).getId();
            req.setUserId(userId);
        }

        Pet pet = mappingService.map(req, Pet.class);

        petMapper.insert(pet);

        return pet.getId();
    }

    /**
     * 删除宠物信息
     *
     * @param id 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "Pet::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "Pet::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "Pet::info", condition = "#result")
            }
    )
    public boolean remove(Long id) {
        return petMapper.deleteById(id) > 0;
    }

    /**
     * 修改宠物信息
     *
     * @param req 修改宠物信息入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "Pet::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "Pet::detail", condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "Pet::info", condition = "#result")
            }
    )
    public boolean modify(PetModifyRequest req) {
//        if (null == req || null == req.getId()) {
//            return false;
//        }
        if (null != req.getUsername()) {
            Long userId = userService.detail(UserDetailRequest.builder().username(req.getUsername()).build()).getId();
            req.setUserId(userId);
        }
        Pet pet = mappingService.map(req, Pet.class);

        return petMapper.updateById(pet) > 0;
    }

    /**
     * 详情查询宠物信息
     *
     * @param id 主键ID
     * @return 宠物信息对象
     */
    @Override
//    @Cacheable(value = "Pet::detail", key = "#p0", condition = "#p0 != null")
    public PetDetailResponse detail(Long id) {
        if (null == id) {
            return null;
        }
        Pet pet = petMapper.selectById(id);
        if (null == pet) {
            return null;
        }

        return mappingService.map(pet, PetDetailResponse.class);
    }

//    /**
//    * 查询宠物信息
//    *
//    * @param id 主键ID
//    * @return 宠物信息对象
//    */
//    @Override
//    @Cacheable(value = "Pet::info", key = "#p0", condition = "#p0 != null")
//    public PetInfoResponse info(Long id) {
//        if (null == id) {
//        return null;
//        }
//        Pet pet = petMapper.selectById(id);
//        if (null == pet) {
//        return null;
//        }
//
//        return mappingService.map(pet, PetInfoResponse.class);
//    }

    /**
     * 分页查询宠物信息
     *
     * @param req 分页查询宠物信息入参
     * @return 分页宠物信息对象
     */
    @Override
    @Cacheable(value = "Pet::batch::page", key = "#p0")
    public PageInfo<PetPageResponse> page(PetPageRequest req) {
        LambdaQueryWrapper<Pet> query = getQuery(mappingService.map(req, PetListRequest.class));

        IPage<Pet> result = petMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), PetPageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
     * 列表查询宠物信息
     *
     * @param req 列表查询宠物信息入参
     * @return 列表宠物信息对象
     */
    @Override
//    @Cacheable(value = "Pet::batch::list", key = "#p0")
    public List<PetListResponse> list(PetListRequest req) {
        LambdaQueryWrapper<Pet> query = getQuery(req);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<Pet> result = petMapper.selectList(query);

            if (null == result || result.isEmpty()) {
                return Collections.emptyList();
            }

            List<PetListResponse> petListResponses = mappingService.map(result, PetListResponse.class);
            petListResponses.forEach(x -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = x.getCreateTime().format(formatter);
                x.setData(formattedDateTime);
                x.setCreateTime(null);
                x.setCName(Optional.ofNullable(categoryService.name(x.getCid())).orElse(""));
                if (x.getStatus().equals(EHelpStatus.HELPWAIT.getCode())) {
                    x.setStatuName(EHelpStatus.HELPWAIT.getValue());
                }
                if (x.getStatus().equals(EHelpStatus.HELPING.getCode())) {
                    x.setStatuName(EHelpStatus.HELPING.getValue());
                }
                if (x.getStatus().equals(EHelpStatus.HELPSUCCESS.getCode())) {
                    x.setStatuName(EHelpStatus.HELPSUCCESS.getValue());
                }
                if (x.getStatus().equals(EHelpStatus.HELPFAIL.getCode())) {
                    x.setStatuName(EHelpStatus.HELPFAIL.getValue());
                }
            });
            return petListResponses;
        }

        IPage<Pet> result = petMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);

        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), PetListResponse.class);
    }

    /**
     * 根据一组ID查询宠物信息列表
     *
     * @param ids 列表查询宠物信息入参
     * @return 列表宠物信息对象
     */
    @Override
    @Cacheable(value = "Pet::batch::listByIds", key = "#p0")
    public List<PetListResponse> listByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Pet> result = petMapper.selectBatchIds(ids);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, PetListResponse.class);
    }

    /**
     * 列表宠物信息根据条件返回一组ID
     *
     * @param req 列表宠物信息条件入参
     * @return 一组宠物信息ID
     */
    @Override
    @Cacheable(value = "Pet::batch::listId", key = "#p0")
    public List<Long> listId(PetListRequest req) {
        LambdaQueryWrapper<Pet> query = getQuery(req);
        query.select(Pet::getId);

        List<?> result = petMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
     * 计数
     *
     * @param req 列表查询宠物信息入参
     * @return 计数
     */
    @Override
    public long count(PetListRequest req) {
        LambdaQueryWrapper<Pet> query = getQuery(req);

        return petMapper.selectCount(query);
    }

    /**
     * 是否存在{table.comment!}
     *
     * @param req 列表查询{table.comment!}入参
     * @return 是否存在
     */
    @Override
    public boolean exist(PetListRequest req) {
        LambdaQueryWrapper<Pet> query = getQuery(req)
                .select(Pet::getId)
                .last("limit 1");

        List<?> result = petMapper.selectObjs(query);

        return null != result && !result.isEmpty();
    }

    /**
     * 根据一组ID返回宠物信息名称
     *
     * @param ids 宠物信息一组ID
     * @return 宠物信息一组名称
     */
    @Override
    public List<Label> nameByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Pet> query = new LambdaQueryWrapper<>(new Pet())
                .in(Pet::getId, ids)
                .select(Pet::getId, Pet::getName);

        List<Pet> result = petMapper.selectList(query);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
    }

    /**
     * 根据条件返回一组宠物信息名称
     *
     * @param req 宠物信息条件
     * @return 宠物信息名称
     */
    @Override
    public List<Label> names(PetListRequest req) {
        LambdaQueryWrapper<Pet> query = getQuery(req)
                .select(Pet::getId, Pet::getName);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<Pet> result = petMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
        }

        IPage<Pet> result = petMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), Label.class);
    }

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    @Override
    @Cacheable(key = "#p0", value = "Pet::name", condition = "#p0 != null")
    public String name(Long id) {
        if (null == id || 0L == id) {
            return "";
        }
        LambdaQueryWrapper<Pet> query = new LambdaQueryWrapper<>(Pet.builder().id(id).build())
                .select(Pet::getName);

        List<?> result = petMapper.selectObjs(query);
        return null == result || result.isEmpty() ? null : (String) result.get(0);
    }

}
