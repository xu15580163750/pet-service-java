package com.xu.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.entity.SysPermission;
import com.xu.pet.entity.SysRole;
import com.xu.pet.entity.SysRolePermission;
import com.xu.pet.mapper.SysRolePermissionMapper;
import com.xu.pet.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.swing.text.html.Option;

import com.xu.pet.request.*;
import com.xu.pet.response.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import com.xu.pet.core.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 角色权限
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private IUserService userService;

    @Lazy
    @Autowired
    private ISysRolePermissionService rolePermissionService;

    @Lazy
    @Autowired
    private ISysPermissionService permissionService;


    @Autowired
    private IMappingService mappingService;

    private LambdaQueryWrapper<SysRolePermission> getQuery(SysRolePermissionListRequest req) {
        SysRolePermission condition = mappingService.map(req, SysRolePermission.class);
        LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<>(condition);
        query.in(null != req.getRoleIds() && !req.getRoleIds().isEmpty(), SysRolePermission::getRoleId, req.getRoleIds());

        return query;
    }

    /**
     * 新增角色权限
     *
     * @param req 新增角色权限入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRolePermission::batch", allEntries = true)
            }
    )
    public Long create(SysRolePermissionCreateRequest req) {
        SysRolePermission sysRolePermission = mappingService.map(req, SysRolePermission.class);

        sysRolePermissionMapper.insert(sysRolePermission);

        return sysRolePermission.getId();
    }

    @Override
    public boolean createBatch(Long roleId, List<SysPermissionCreateRequest> permissions) {
        removeBatch(roleId);
        int count = 0;
        List<Long> permissionIds = permissions.stream().map(SysPermissionCreateRequest::getId).collect(Collectors.toList());
        for (Long permissionId : permissionIds) {
            if (create(SysRolePermissionCreateRequest.builder()
                    .permissionId(permissionId)
                    .roleId(roleId)
                    .build()) > 0L) {
                count++;
            }
        }
        log.info("批量创建条数:count:{}", count);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除角色权限
     *
     * @param id 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRolePermission::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRolePermission::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRolePermission::info", condition = "#result")
            }
    )
    public boolean remove(Long id) {
        return sysRolePermissionMapper.deleteById(id) > 0;
    }


    /**
     * 删除角色权限 批量
     *
     * @param roleId 主键ID
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRolePermission::roles::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRolePermission::roles::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRolePermission::roles::info", condition = "#result")
            }
    )
    @Override
    public boolean removeBatch(Long roleId) {
        List<Long> permissionRoleIds = list(SysRolePermissionListRequest.builder()
                .roleId(roleId)
                .build())
                .stream()
                .map(SysRolePermissionListResponse::getId)
                .collect(Collectors.toList());
        int count = 0;
        for (Long id : permissionRoleIds) {
            remove(id);
            count++;
        }
        log.info("已删除角色权限关系条数:count:{}", count);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改角色权限
     *
     * @param req 修改角色权限入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRolePermission::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysRolePermission::detail", condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysRolePermission::info", condition = "#result")
            }
    )
    public boolean modify(SysRolePermissionModifyRequest req) {
        if (null == req || null == req.getId()) {
            return false;
        }
        SysRolePermission sysRolePermission = mappingService.map(req, SysRolePermission.class);

        return sysRolePermissionMapper.updateById(sysRolePermission) > 0;
    }

    /**
     * 详情查询角色权限
     *
     * @param id 主键ID
     * @return 角色权限对象
     */
    @Override
    @Cacheable(value = "SysRolePermission::detail", key = "#p0", condition = "#p0 != null")
    public SysRolePermissionDetailResponse detail(Long id) {
        if (null == id) {
            return null;
        }
        SysRolePermission sysRolePermission = sysRolePermissionMapper.selectById(id);
        if (null == sysRolePermission) {
            return null;
        }

        return mappingService.map(sysRolePermission, SysRolePermissionDetailResponse.class);
    }
//
//    /**
//    * 查询角色权限
//    *
//    * @param id 主键ID
//    * @return 角色权限对象
//    */
//    @Override
//    @Cacheable(value = "SysRolePermission::info", key = "#p0", condition = "#p0 != null")
//    public SysRolePermissionInfoResponse info(Long id) {
//        if (null == id) {
//        return null;
//        }
//        SysRolePermission sysRolePermission = sysRolePermissionMapper.selectById(id);
//        if (null == sysRolePermission) {
//        return null;
//        }
//
//        return mappingService.map(sysRolePermission, SysRolePermissionInfoResponse.class);
//    }

    /**
     * 分页查询角色权限
     *
     * @param req 分页查询角色权限入参
     * @return 分页角色权限对象
     */
    @Override
    @Cacheable(value = "SysRolePermission::batch::page", key = "#p0")
    public PageInfo<SysRolePermissionPageResponse> page(SysRolePermissionPageRequest req) {
        LambdaQueryWrapper<SysRolePermission> query = getQuery(mappingService.map(req, SysRolePermissionListRequest.class));

        IPage<SysRolePermission> result = sysRolePermissionMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), SysRolePermissionPageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
     * 列表查询角色权限
     *
     * @param req 列表查询角色权限入参
     * @return 列表角色权限对象
     */
    @Override
//    @Cacheable(value = "SysRolePermission::batch::list", key = "#p0")
    public List<SysRolePermissionListResponse> list(SysRolePermissionListRequest req) {
        LambdaQueryWrapper<SysRolePermission> query = getQuery(req);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<SysRolePermission> result = sysRolePermissionMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysRolePermissionListResponse.class);
        }

        IPage<SysRolePermission> result = sysRolePermissionMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);

        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), SysRolePermissionListResponse.class);
    }

    /**
     * 根据一组ID查询角色权限列表
     *
     * @param ids 列表查询角色权限入参
     * @return 列表角色权限对象
     */
    @Override
    @Cacheable(value = "SysRolePermission::batch::listByIds", key = "#p0")
    public List<SysRolePermissionListResponse> listByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysRolePermission> result = sysRolePermissionMapper.selectBatchIds(ids);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysRolePermissionListResponse.class);
    }


    /**
     * 列表查询角色权限根据用户名
     *
     * @param username 列表查询角色权限入参
     * @return 列表角色权限对象
     */
    @Override
    public Set<String> listByName(String username) {
        //获取用户信息
        Long userId = Optional.ofNullable(userService.detail(UserDetailRequest.builder()
                                .username(username)
                                .build())
                        .getId())
                .orElse(0L);

        //获取一组角色Ids
        List<Long> roleIds = userRoleService.list(SysUserRoleListRequest.builder()
                        .userId(userId)
                        .build())
                .stream()
                .map(SysUserRoleListResponse::getRoleId)
                .distinct()
                .collect(Collectors.toList());

        if (null == roleIds||roleIds.isEmpty()) {
            return Collections.emptySet();
        }
        //获取一组权限ids
        List<Long> permissionIds = rolePermissionService.list(SysRolePermissionListRequest.builder()
                        .roleIds(roleIds)
                        .build())
                .stream()
                .map(SysRolePermissionListResponse::getPermissionId)
                .distinct()
                .collect(Collectors.toList());

        if (null == permissionIds||permissionIds.isEmpty()) {
            return Collections.emptySet();
        }
        //获取一组url
        Set<String> urls = permissionService.listByIds(permissionIds)
                .stream()
                .map(SysPermissionListResponse::getUrl)
                .collect(Collectors.toSet());

        log.info("user-当前权限:urls:{}", urls);
        return null != urls && !urls.isEmpty() ? urls : Collections.emptySet();

    }

    /**
     * 列表角色权限根据条件返回一组ID
     *
     * @param req 列表角色权限条件入参
     * @return 一组角色权限ID
     */
    @Override
    @Cacheable(value = "SysRolePermission::batch::listId", key = "#p0")
    public List<Long> listId(SysRolePermissionListRequest req) {
        LambdaQueryWrapper<SysRolePermission> query = getQuery(req);
        query.select(SysRolePermission::getId);

        List<?> result = sysRolePermissionMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
     * 计数
     *
     * @param req 列表查询角色权限入参
     * @return 计数
     */
    @Override
    public long count(SysRolePermissionListRequest req) {
        LambdaQueryWrapper<SysRolePermission> query = getQuery(req);

        return sysRolePermissionMapper.selectCount(query);
    }

    /**
     * 是否存在{table.comment!}
     *
     * @param req 列表查询{table.comment!}入参
     * @return 是否存在
     */
    @Override
    public boolean exist(SysRolePermissionListRequest req) {
        LambdaQueryWrapper<SysRolePermission> query = getQuery(req)
                .select(SysRolePermission::getId)
                .last("limit 1");

        List<?> result = sysRolePermissionMapper.selectObjs(query);

        return null != result && !result.isEmpty();
    }

//    /**
//    * 根据一组ID返回角色权限名称
//    *
//    * @param ids 角色权限一组ID
//    * @return 角色权限一组名称
//    */
//    @Override
//    public List<Label> nameByIds(List<Long> ids) {
//        if (null == ids || ids.isEmpty()) {
//        return Collections.emptyList();
//        }
//        LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<>(new SysRolePermission())
//        .in(SysRolePermission::getId, ids)
//        .select(SysRolePermission::getId, SysRolePermission::getName);
//
//        List<SysRolePermission> result = sysRolePermissionMapper.selectList(query);
//        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
//    }

//    /**
//    * 根据条件返回一组角色权限名称
//    *
//    * @param req 角色权限条件
//    * @return 角色权限名称
//    */
//    @Override
//    public List<Label> names(SysRolePermissionListRequest req) {
//        LambdaQueryWrapper<SysRolePermission> query = getQuery(req)
//        .select(SysRolePermission::getId, SysRolePermission::getName);
//
//        if (null == req.getPageNum() || null == req.getPageSize()) {
//        List<SysRolePermission> result = sysRolePermissionMapper.selectList(query);
//
//        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
//        }
//
//        IPage<SysRolePermission> result = sysRolePermissionMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
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
//    @Cacheable(key = "#p0", value = "SysRolePermission::name", condition = "#p0 != null")
//    public String name(Long id) {
//        if (null == id || 0L == id) {
//        return "";
//        }
//        LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<>(SysRolePermission.builder().id(id).build())
//        .select(SysRolePermission::getName);
//
//        List<?> result = sysRolePermissionMapper.selectObjs(query);
//        return null == result || result.isEmpty() ? null : (String) result.get(0);
//    }
//
//    private class SysRolePermissionInfoResponse {
//    }
}
