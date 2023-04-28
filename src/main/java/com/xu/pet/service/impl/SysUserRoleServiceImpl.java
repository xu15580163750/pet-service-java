package com.xu.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.entity.SysRole;
import com.xu.pet.entity.SysUserRole;
import com.xu.pet.mapper.SysUserRoleMapper;
import com.xu.pet.service.IMappingService;
import com.xu.pet.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import com.xu.pet.request.*;
import com.xu.pet.response.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.xu.pet.core.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 用户-角色管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

//    @Autowired
//    private ISysUserRoleService userRoleService;

    @Autowired
    private IMappingService mappingService;

    private LambdaQueryWrapper<SysUserRole> getQuery(SysUserRoleListRequest req) {
        SysUserRole condition = mappingService.map(req, SysUserRole.class);
        LambdaQueryWrapper<SysUserRole> query = new LambdaQueryWrapper<>(condition);

        query.in(null != req.getRoleIds() && !req.getRoleIds().isEmpty(), SysUserRole::getRoleId, req.getRoleIds());
        return query;
    }

    /**
     * 新增用户-角色管理
     *
     * @param req 新增用户-角色管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysUserRole::batch", allEntries = true)
            }
    )
    public Long create(SysUserRoleCreateRequest req) {
        SysUserRole sysUserRole = mappingService.map(req, SysUserRole.class);

        sysUserRoleMapper.insert(sysUserRole);

        return sysUserRole.getId();
    }


    /**
     * 新增用户-角色管理(批量)
     *
     * @param userId 新增用户-角色管理入参
     */
    @Override
    public boolean createBatch(Long userId, List<SysRole> roles) {
        removeBatch(userId);
        int count = 0;
        List<Long> roleIds = roles.stream().map(SysRole::getId).collect(Collectors.toList());
        for (Long roleId : roleIds) {
            create(SysUserRoleCreateRequest.builder()
                    .userId(userId)
                    .roleId(roleId)
                    .build());
            count++;
        }
        log.info("批量创建条数:count:{}", count);
        if (count > 0) {
            return true;
        }
        return false;
    }


    /**
     * 删除用户-角色管理
     *
     * @param id 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysUserRole::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysUserRole::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysUserRole::info", condition = "#result")
            }
    )
    public boolean remove(Long id) {
        return sysUserRoleMapper.deleteById(id) > 0;
    }

    @Override
    public boolean removeBatch(Long userId) {
        List<Long> userRoleIds = list(SysUserRoleListRequest.builder()
                .userId(userId)
                .build())
                .stream()
                .map(SysUserRoleListResponse::getId)
                .collect(Collectors.toList());
        int count = 0;
        for (Long id : userRoleIds) {
            remove(id);
            count++;
        }
        log.info("已删除用户角色关系条数:count:{}", count);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改用户-角色管理
     *
     * @param req 修改用户-角色管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysUserRole::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysUserRole::detail", condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysUserRole::info", condition = "#result")
            }
    )
    public boolean modify(SysUserRoleModifyRequest req) {
        if (null == req || null == req.getId()) {
            return false;
        }
        SysUserRole sysUserRole = mappingService.map(req, SysUserRole.class);

        return sysUserRoleMapper.updateById(sysUserRole) > 0;
    }

    /**
     * 详情查询用户-角色管理
     *
     * @param id 主键ID
     * @return 用户-角色管理对象
     */
    @Override
    @Cacheable(value = "SysUserRole::detail", key = "#p0", condition = "#p0 != null")
    public SysUserRoleDetailResponse detail(Long id) {
        if (null == id) {
            return null;
        }
        SysUserRole sysUserRole = sysUserRoleMapper.selectById(id);
        if (null == sysUserRole) {
            return null;
        }

        return mappingService.map(sysUserRole, SysUserRoleDetailResponse.class);
    }

//    /**
//    * 查询用户-角色管理
//    *
//    * @param id 主键ID
//    * @return 用户-角色管理对象
//    */
//    @Override
//    @Cacheable(value = "SysUserRole::info", key = "#p0", condition = "#p0 != null")
//    public SysUserRoleInfoResponse info(Long id) {
//        if (null == id) {
//        return null;
//        }
//        SysUserRole sysUserRole = sysUserRoleMapper.selectById(id);
//        if (null == sysUserRole) {
//        return null;
//        }
//
//        return mappingService.map(sysUserRole, SysUserRoleInfoResponse.class);
//    }

    /**
     * 分页查询用户-角色管理
     *
     * @param req 分页查询用户-角色管理入参
     * @return 分页用户-角色管理对象
     */
    @Override
    @Cacheable(value = "SysUserRole::batch::page", key = "#p0")
    public PageInfo<SysUserRolePageResponse> page(SysUserRolePageRequest req) {
        LambdaQueryWrapper<SysUserRole> query = getQuery(mappingService.map(req, SysUserRoleListRequest.class));

        IPage<SysUserRole> result = sysUserRoleMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), SysUserRolePageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
     * 列表查询用户-角色管理
     *
     * @param req 列表查询用户-角色管理入参
     * @return 列表用户-角色管理对象
     */
    @Override
//    @Cacheable(value = "SysUserRole::batch::list", key = "#p0")
    public List<SysUserRoleListResponse> list(SysUserRoleListRequest req) {
        LambdaQueryWrapper<SysUserRole> query = getQuery(req);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<SysUserRole> result = sysUserRoleMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysUserRoleListResponse.class);
        }

        IPage<SysUserRole> result = sysUserRoleMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);

        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), SysUserRoleListResponse.class);
    }

    /**
     * 根据一组ID查询用户-角色管理列表
     *
     * @param ids 列表查询用户-角色管理入参
     * @return 列表用户-角色管理对象
     */
    @Override
    @Cacheable(value = "SysUserRole::batch::listByIds", key = "#p0")
    public List<SysUserRoleListResponse> listByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysUserRole> result = sysUserRoleMapper.selectBatchIds(ids);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysUserRoleListResponse.class);
    }

    /**
     * 列表用户-角色管理根据条件返回一组ID
     *
     * @param req 列表用户-角色管理条件入参
     * @return 一组用户-角色管理ID
     */
    @Override
    @Cacheable(value = "SysUserRole::batch::listId", key = "#p0")
    public List<Long> listId(SysUserRoleListRequest req) {
        LambdaQueryWrapper<SysUserRole> query = getQuery(req);
        query.select(SysUserRole::getId);

        List<?> result = sysUserRoleMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
     * 计数
     *
     * @param req 列表查询用户-角色管理入参
     * @return 计数
     */
    @Override
    public long count(SysUserRoleListRequest req) {
        LambdaQueryWrapper<SysUserRole> query = getQuery(req);

        return sysUserRoleMapper.selectCount(query);
    }

    /**
     * 是否存在{table.comment!}
     *
     * @param req 列表查询{table.comment!}入参
     * @return 是否存在
     */
    @Override
    public boolean exist(SysUserRoleListRequest req) {
        LambdaQueryWrapper<SysUserRole> query = getQuery(req)
                .select(SysUserRole::getId)
                .last("limit 1");

        List<?> result = sysUserRoleMapper.selectObjs(query);

        return null != result && !result.isEmpty();
    }

//    /**
//    * 根据一组ID返回用户-角色管理名称
//    *
//    * @param ids 用户-角色管理一组ID
//    * @return 用户-角色管理一组名称
//    */
//    @Override
//    public List<Label> nameByIds(List<Long> ids) {
//        if (null == ids || ids.isEmpty()) {
//        return Collections.emptyList();
//        }
//        LambdaQueryWrapper<SysUserRole> query = new LambdaQueryWrapper<>(new SysUserRole())
//        .in(SysUserRole::getId, ids)
//        .select(SysUserRole::getId, SysUserRole::getName);
//
//        List<SysUserRole> result = sysUserRoleMapper.selectList(query);
//        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
//    }
//
//    /**
//    * 根据条件返回一组用户-角色管理名称
//    *
//    * @param req 用户-角色管理条件
//    * @return 用户-角色管理名称
//    */
//    @Override
//    public List<Label> names(SysUserRoleListRequest req) {
//        LambdaQueryWrapper<SysUserRole> query = getQuery(req)
//        .select(SysUserRole::getId, SysUserRole::getName);
//
//        if (null == req.getPageNum() || null == req.getPageSize()) {
//        List<SysUserRole> result = sysUserRoleMapper.selectList(query);
//
//        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
//        }
//
//        IPage<SysUserRole> result = sysUserRoleMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
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
//    @Cacheable(key = "#p0", value = "SysUserRole::name", condition = "#p0 != null")
//    public String name(Long id) {
//        if (null == id || 0L == id) {
//        return "";
//        }
//        LambdaQueryWrapper<SysUserRole> query = new LambdaQueryWrapper<>(SysUserRole.builder().id(id).build())
//        .select(SysUserRole::getName);
//
//        List<?> result = sysUserRoleMapper.selectObjs(query);
//        return null == result || result.isEmpty() ? null : (String) result.get(0);
//    }

}
