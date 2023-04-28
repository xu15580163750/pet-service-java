package com.xu.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.core.BusinessException;
import com.xu.pet.entity.SysPermission;
import com.xu.pet.enums.EErrorCode;
import com.xu.pet.mapper.SysPermissionMapper;
import com.xu.pet.service.*;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import com.xu.pet.request.*;
import com.xu.pet.response.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 权限管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private IMappingService mappingService;

//    @Autowired
//    private IUserService userService;

    @Autowired
    private ISysRolePermissionService rolePermissionService;

//    @Autowired
//    private ISysRoleService roleService;

    private LambdaQueryWrapper<SysPermission> getQuery(SysPermissionListRequest req) {
        SysPermission condition = mappingService.map(req, SysPermission.class);
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>(condition);

        query.in(null != req.getIds() && !req.getIds().isEmpty(), SysPermission::getId, req.getIds());
        return query;
    }

    /**
     * 新增权限管理
     *
     * @param req 新增权限管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysPermission::batch", allEntries = true)
            }
    )
    public Long create(SysPermissionCreateRequest req) {
        SysPermission sysPermission = mappingService.map(req, SysPermission.class);

        sysPermissionMapper.insert(sysPermission);

        return sysPermission.getId();
    }

    /**
     * 删除权限管理
     *
     * @param id 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysPermission::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysPermission::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysPermission::info", condition = "#result")
            }
    )
    public boolean remove(Long id) {
        return sysPermissionMapper.deleteById(id) > 0;
    }

    /**
     * 修改权限管理
     *
     * @param req 修改权限管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysPermission::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysPermission::detail", condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysPermission::info", condition = "#result")
            }
    )
    public boolean modify(SysPermissionModifyRequest req) {
        if (null == req || null == req.getId()) {
            return false;
        }
        SysPermission sysPermission = mappingService.map(req, SysPermission.class);

        return sysPermissionMapper.updateById(sysPermission) > 0;
    }

    /**
     * 详情查询权限管理
     *
     * @param id 主键ID
     * @return 权限管理对象
     */
    @Override
    @Cacheable(value = "SysPermission::detail", key = "#p0", condition = "#p0 != null")
    public SysPermissionDetailResponse detail(Long id) {
        if (null == id) {
            return null;
        }
        SysPermission sysPermission = sysPermissionMapper.selectById(id);
        if (null == sysPermission) {
            return null;
        }

        return mappingService.map(sysPermission, SysPermissionDetailResponse.class);
    }

//    /**
//    * 查询权限管理
//    *
//    * @param id 主键ID
//    * @return 权限管理对象
//    */
//    @Override
//    @Cacheable(value = "SysPermission::info", key = "#p0", condition = "#p0 != null")
//    public SysPermissionInfoResponse info(Long id) {
//        if (null == id) {
//        return null;
//        }
//        SysPermission sysPermission = sysPermissionMapper.selectById(id);
//        if (null == sysPermission) {
//        return null;
//        }
//
//        return mappingService.map(sysPermission, SysPermissionInfoResponse.class);
//    }

    /**
     * 分页查询权限管理
     *
     * @param req 分页查询权限管理入参
     * @return 分页权限管理对象
     */
    @Override
    @Cacheable(value = "SysPermission::batch::page", key = "#p0")
    public PageInfo<SysPermissionPageResponse> page(SysPermissionPageRequest req) {
        LambdaQueryWrapper<SysPermission> query = getQuery(mappingService.map(req, SysPermissionListRequest.class));

        IPage<SysPermission> result = sysPermissionMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), SysPermissionPageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
     * 列表查询权限管理
     *
     * @param req 列表查询权限管理入参
     * @return 列表权限管理对象
     */
    @Override
//    @Cacheable(value = "SysPermission::batch::list", key = "#p0")
    public List<SysPermissionListResponse> list(SysPermissionListRequest req) {
        LambdaQueryWrapper<SysPermission> query = getQuery(req);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<SysPermission> result = sysPermissionMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysPermissionListResponse.class);
        }

        IPage<SysPermission> result = sysPermissionMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);

        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), SysPermissionListResponse.class);
    }

    /**
     * 根据一组ID查询权限管理列表
     *
     * @param ids 列表查询权限管理入参
     * @return 列表权限管理对象
     */
    @Override
    @Cacheable(value = "SysPermission::batch::listByIds", key = "#p0")
    public List<SysPermissionListResponse> listByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysPermission> result = sysPermissionMapper.selectBatchIds(ids);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysPermissionListResponse.class);
    }

    @Override
    public Set<String> listByUsername(String username) {
//        //一组角色信息
//        List<SysRoleListResponse> records = roleService.listByUsername(username);
//
//        //一组角色Ids
//        List<Long> roleIds = records.stream().map(SysRoleListResponse::getId).collect(Collectors.toList());
//
//        rolePermissionService.listByName()
        return rolePermissionService.listByName(username);
    }

    /**
     * 列表查询权限根据roleId
     *
     * @param roleId 列表查询角色权限入参
     * @return 列表角色权限对象
     */
    @Override
    public List<SysPermissionListResponse> listByRoleId(Long roleId) {
        //获取一组权限ids
        List<Long> permissionIds = rolePermissionService.list(SysRolePermissionListRequest.builder()
                        .roleId(roleId)
                        .build())
                .stream()
                .map(SysRolePermissionListResponse::getPermissionId)
                .collect(Collectors.toList());
        //根据一组权限Ids获取权限
        if (null == permissionIds || permissionIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysPermissionListResponse> result = list(SysPermissionListRequest.builder()
                .ids(permissionIds)
                .build());
        return null != result && !result.isEmpty() ? result : Collections.emptyList();

    }

    /**
     * 列表权限管理根据条件返回一组ID
     *
     * @param req 列表权限管理条件入参
     * @return 一组权限管理ID
     */
    @Override
    @Cacheable(value = "SysPermission::batch::listId", key = "#p0")
    public List<Long> listId(SysPermissionListRequest req) {
        LambdaQueryWrapper<SysPermission> query = getQuery(req);
        query.select(SysPermission::getId);

        List<?> result = sysPermissionMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
     * 计数
     *
     * @param req 列表查询权限管理入参
     * @return 计数
     */
    @Override
    public long count(SysPermissionListRequest req) {
        LambdaQueryWrapper<SysPermission> query = getQuery(req);

        return sysPermissionMapper.selectCount(query);
    }

    /**
     * 是否存在权限
     *
     * @param req 列表查询{table.comment!}入参
     * @return 是否存在
     */
    @Override
    public boolean exist(SysPermissionListRequest req) {
        List<SysPermissionListResponse> permissionList = list(SysPermissionListRequest.builder().build());

        for (SysPermissionListResponse r : permissionList) {
            // match prefix
            if (req.getUrl().startsWith(r.getUrl())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据一组ID返回权限管理名称
     *
     * @param ids 权限管理一组ID
     * @return 权限管理一组名称
     */
    @Override
    public List<Label> nameByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>(new SysPermission())
                .in(SysPermission::getId, ids)
                .select(SysPermission::getId, SysPermission::getName);

        List<SysPermission> result = sysPermissionMapper.selectList(query);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
    }

    /**
     * 根据条件返回一组权限管理名称
     *
     * @param req 权限管理条件
     * @return 权限管理名称
     */
    @Override
    public List<Label> names(SysPermissionListRequest req) {
        LambdaQueryWrapper<SysPermission> query = getQuery(req)
                .select(SysPermission::getId, SysPermission::getName);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<SysPermission> result = sysPermissionMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
        }

        IPage<SysPermission> result = sysPermissionMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), Label.class);
    }

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    @Override
    @Cacheable(key = "#p0", value = "SysPermission::name", condition = "#p0 != null")
    public String name(Long id) {
        if (null == id || 0L == id) {
            return "";
        }
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>(SysPermission.builder().id(id).build())
                .select(SysPermission::getName);

        List<?> result = sysPermissionMapper.selectObjs(query);
        return null == result || result.isEmpty() ? null : (String) result.get(0);
    }

}
