package com.xu.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.entity.UserMessage;
import com.xu.pet.mapper.UserMessageMapper;
import com.xu.pet.service.IMappingService;
import com.xu.pet.service.IUserMessageService;
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
 * 信息管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {

    @Resource
    private UserMessageMapper  userMessageMapper;

    @Autowired
    private IMappingService mappingService;

    private LambdaQueryWrapper<UserMessage> getQuery(UserMessageListRequest req) {
        UserMessage condition = mappingService.map(req, UserMessage.class);
        LambdaQueryWrapper<UserMessage> query = new LambdaQueryWrapper<>(condition);

        return query;
    }
    /**
    * 新增信息管理
    *
    * @param req 新增信息管理入参
    */
    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "UserMessage::batch", allEntries = true)
    }
    )
    public Long create(UserMessageCreateRequest req) {
        UserMessage userMessage = mappingService.map(req, UserMessage.class);

        userMessageMapper.insert(userMessage);

        return userMessage.getId();
    }

    /**
    * 删除信息管理
    *
    * @param id 主键ID
    */
    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "UserMessage::batch", allEntries = true, condition = "#result"),
            @CacheEvict(key = "#p0", value = "UserMessage::detail", condition = "#result"),
            @CacheEvict(key = "#p0", value = "UserMessage::info", condition = "#result")
    }
    )
    public boolean remove(Long id) {
        return userMessageMapper.deleteById(id) > 0;
    }

    /**
    * 修改信息管理
    *
    * @param req 修改信息管理入参
    */
    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "UserMessage::batch", allEntries = true, condition = "#result"),
            @CacheEvict(key = "#p0.id", value = "UserMessage::detail", condition = "#result"),
            @CacheEvict(key = "#p0.id", value = "UserMessage::info", condition = "#result")
    }
    )
    public boolean modify(UserMessageModifyRequest req) {
        if (null == req || null == req.getId()) {
        return false;
        }
        UserMessage userMessage = mappingService.map(req, UserMessage.class);

        return userMessageMapper.updateById(userMessage) > 0;
    }

    /**
    * 详情查询信息管理
    *
    * @param id 主键ID
    * @return 信息管理对象
    */
    @Override
    @Cacheable(value = "UserMessage::detail", key = "#p0", condition = "#p0 != null")
    public UserMessageDetailResponse detail(Long id) {
        if (null == id) {
        return null;
        }
        UserMessage userMessage = userMessageMapper.selectById(id);
        if (null == userMessage) {
        return null;
        }

        return mappingService.map(userMessage, UserMessageDetailResponse.class);
    }

//    /**
//    * 查询信息管理
//    *
//    * @param id 主键ID
//    * @return 信息管理对象
//    */
//    @Override
//    @Cacheable(value = "UserMessage::info", key = "#p0", condition = "#p0 != null")
//    public UserMessageInfoResponse info(Long id) {
//        if (null == id) {
//        return null;
//        }
//        UserMessage userMessage = userMessageMapper.selectById(id);
//        if (null == userMessage) {
//        return null;
//        }
//
//        return mappingService.map(userMessage, UserMessageInfoResponse.class);
//    }

    /**
    * 分页查询信息管理
    *
    * @param req 分页查询信息管理入参
    * @return 分页信息管理对象
    */
    @Override
    @Cacheable(value = "UserMessage::batch::page", key = "#p0")
    public PageInfo<UserMessagePageResponse> page(UserMessagePageRequest req) {
        LambdaQueryWrapper<UserMessage> query = getQuery(mappingService.map(req, UserMessageListRequest.class));

        IPage<UserMessage> result = userMessageMapper.selectPage(new Page<>(req.getPageNum(),req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), UserMessagePageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
    * 列表查询信息管理
    *
    * @param req 列表查询信息管理入参
    * @return 列表信息管理对象
    */
    @Override
    @Cacheable(value = "UserMessage::batch::list", key = "#p0")
    public List<UserMessageListResponse> list(UserMessageListRequest req) {
        LambdaQueryWrapper<UserMessage> query = getQuery(req);

        if (null == req.getPageNum() || null == req.getPageSize()) {
        List<UserMessage> result = userMessageMapper.selectList(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, UserMessageListResponse.class);
        }

        IPage<UserMessage> result = userMessageMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);

        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), UserMessageListResponse.class);
    }

    /**
    * 根据一组ID查询信息管理列表
    *
    * @param ids 列表查询信息管理入参
    * @return 列表信息管理对象
    */
    @Override
    @Cacheable(value = "UserMessage::batch::listByIds", key = "#p0")
    public List <UserMessageListResponse> listByIds(List<Long> ids) {
       if (null == ids || ids.isEmpty()) {
       return Collections.emptyList();
       }
       List<UserMessage> result = userMessageMapper.selectBatchIds(ids);
       return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, UserMessageListResponse.class);
    }

    /**
    * 列表信息管理根据条件返回一组ID
    *
    * @param req 列表信息管理条件入参
    * @return 一组信息管理ID
    */
    @Override
    @Cacheable(value = "UserMessage::batch::listId", key = "#p0")
    public List<Long> listId(UserMessageListRequest req) {
        LambdaQueryWrapper<UserMessage> query = getQuery(req);
        query.select(UserMessage::getId);

        List<?> result = userMessageMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
    * 计数
    *
    * @param req 列表查询信息管理入参
    * @return 计数
    */
    @Override
    public long count(UserMessageListRequest req) {
        LambdaQueryWrapper<UserMessage> query = getQuery(req);

        return userMessageMapper.selectCount(query);
    }

    /**
    * 是否存在{table.comment!}
    *
    * @param req 列表查询{table.comment!}入参
    * @return 是否存在
    */
    @Override
    public boolean exist(UserMessageListRequest req) {
        LambdaQueryWrapper<UserMessage> query = getQuery(req)
        .select(UserMessage::getId)
        .last("limit 1");

        List<?> result =userMessageMapper.selectObjs(query);

        return null != result && !result.isEmpty();
    }

//    /**
//    * 根据一组ID返回信息管理名称
//    *
//    * @param ids 信息管理一组ID
//    * @return 信息管理一组名称
//    */
//    @Override
//    public List<Label> nameByIds(List<Long> ids) {
//        if (null == ids || ids.isEmpty()) {
//        return Collections.emptyList();
//        }
//        LambdaQueryWrapper<UserMessage> query = new LambdaQueryWrapper<>(new UserMessage())
//        .in(UserMessage::getId, ids)
//        .select(UserMessage::getId, UserMessage::getName);
//
//        List<UserMessage> result = userMessageMapper.selectList(query);
//        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
//    }
//
//    /**
//    * 根据条件返回一组信息管理名称
//    *
//    * @param req 信息管理条件
//    * @return 信息管理名称
//    */
//    @Override
//    public List<Label> names(UserMessageListRequest req) {
//        LambdaQueryWrapper<UserMessage> query = getQuery(req)
//        .select(UserMessage::getId, UserMessage::getName);
//
//        if (null == req.getPageNum() || null == req.getPageSize()) {
//        List<UserMessage> result = userMessageMapper.selectList(query);
//
//        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
//        }
//
//        IPage<UserMessage> result = userMessageMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
//        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), Label.class);
//    }
//
//    /**
//    * 查询名称
//    *
//    * @param id ID
//    * @return 名称
//    */
//    @Override
//    @Cacheable(key = "#p0", value = "UserMessage::name", condition = "#p0 != null")
//    public String name(Long id) {
//        if (null == id || 0L == id) {
//        return "";
//        }
//        LambdaQueryWrapper<UserMessage> query = new LambdaQueryWrapper<>(UserMessage.builder().id(id).build())
//        .select(UserMessage::getName);
//
//        List<?> result = userMessageMapper.selectObjs(query);
//        return null == result || result.isEmpty() ? null : (String) result.get(0);
//    }

}
