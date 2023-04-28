package com.xu.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.core.BusinessException;
import com.xu.pet.entity.SysMenu;
import com.xu.pet.entity.SysRole;
import com.xu.pet.enums.EErrorCode;
import com.xu.pet.mapper.SysRoleMapper;
import com.xu.pet.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import com.xu.pet.request.*;
import com.xu.pet.response.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 角色管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private IMappingService mappingService;

    @Lazy
    @Autowired
    private IUserService userService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private ISysRolePermissionService rolePermissionService;

    @Autowired
    private ISysRoleMenuService roleMenuService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysPermissionService permissionService;

    private LambdaQueryWrapper<SysRole> getQuery(SysRoleListRequest req) {
        SysRole condition = mappingService.map(req, SysRole.class);
        LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<>(condition);

        query.in(null != req.getIds() && !req.getIds().isEmpty(), SysRole::getId, req.getIds());

        return query;
    }

    /**
     * 新增角色管理
     *
     * @param req 新增角色管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRole::batch", allEntries = true)
            }
    )
    public Long create(SysRoleCreateRequest req) {
        SysRole sysRole = mappingService.map(req, SysRole.class);

        sysRoleMapper.insert(sysRole);

        return sysRole.getId();
    }

    /**
     * 删除角色管理
     *
     * @param id 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRole::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRole::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRole::info", condition = "#result")
            }
    )
    public boolean remove(Long id) {
        return sysRoleMapper.deleteById(id) > 0;
    }

    /**
     * 修改角色管理
     *
     * @param req 修改角色管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRole::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysRole::detail", condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysRole::info", condition = "#result")
            }
    )
    public boolean modify(SysRoleModifyRequest req) {
        if (null == req || null == req.getId()) {
            return false;
        }
        SysRole sysRole = mappingService.map(req, SysRole.class);

        return sysRoleMapper.updateById(sysRole) > 0;
    }

    /**
     * 详情查询角色管理
     *
     * @param id 主键ID
     * @return 角色管理对象
     */
    @Override
    @Cacheable(value = "SysRole::detail", key = "#p0", condition = "#p0 != null")
    public SysRoleDetailResponse detail(Long id) {
        if (null == id) {
            return null;
        }
        SysRole sysRole = sysRoleMapper.selectById(id);
        if (null == sysRole) {
            return null;
        }

        return mappingService.map(sysRole, SysRoleDetailResponse.class);
    }


    /**
     * 根据用户名返回角色信息
     *
     * @param username 用户名
     * @return 角色管理详情数据
     */
    @Override
    public List<SysRoleListResponse> listByUsername(String username) {
        UserDetailResponse user = userService.detail(UserDetailRequest.builder()
                .username(username)
                .build());

        if (null == user || null == user.getId()) {
            throw new BusinessException(EErrorCode.BIZ_DEFAULT.getCode(), "用户不存在");
        }

        List<Long> roleIds = userRoleService.list(SysUserRoleListRequest.builder()
                        .userId(user.getId())
                        .build())
                .stream()
                .map(SysUserRoleListResponse::getRoleId)
                .collect(Collectors.toList());

        if (null != roleIds && roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysRoleListResponse> roles = listPermissionMeue(SysRoleListRequest.builder()
                .ids(roleIds)
                .build());

        return null != roles && !roles.isEmpty() ? roles : Collections.emptyList();

    }

//    /**
//    * 查询角色管理
//    *
//    * @param id 主键ID
//    * @return 角色管理对象
//    */
//    @Override
//    @Cacheable(value = "SysRole::info", key = "#p0", condition = "#p0 != null")
//    public SysRoleInfoResponse info(Long id) {
//        if (null == id) {
//        return null;
//        }
//        SysRole sysRole = sysRoleMapper.selectById(id);
//        if (null == sysRole) {
//        return null;
//        }
//
//        return mappingService.map(sysRole, SysRoleInfoResponse.class);
//    }

    /**
     * 分页查询角色管理
     *
     * @param req 分页查询角色管理入参
     * @return 分页角色管理对象
     */
    @Override
    @Cacheable(value = "SysRole::batch::page", key = "#p0")
    public PageInfo<SysRolePageResponse> page(SysRolePageRequest req) {
        LambdaQueryWrapper<SysRole> query = getQuery(mappingService.map(req, SysRoleListRequest.class));

        IPage<SysRole> result = sysRoleMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), SysRolePageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
     * 列表查询角色管理
     *
     * @param req 列表查询角色管理入参
     * @return 列表角色管理对象
     */
    @Override
    @Cacheable(value = "SysRole::batch::list", key = "#p0")
    public List<SysRoleListResponse> list(SysRoleListRequest req) {
        LambdaQueryWrapper<SysRole> query = getQuery(req);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<SysRole> result = sysRoleMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysRoleListResponse.class);
        }

        IPage<SysRole> result = sysRoleMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);

        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), SysRoleListResponse.class);
    }


    /**
     * 列表查询角色菜单，权限
     *
     * @param req 列表查询角色管理入参
     * @return 角色管理列表数据
     */
    @Override
    public List<SysRoleListResponse> listPermissionMeue(SysRoleListRequest req) {
        List<SysRoleListResponse> result = list(req);

        result.forEach(x -> {
            //查询角色菜单
            List<SysMenuListResponse> menuList = menuService.listByRoleId(x.getId());

            //查询角色权限
            List<SysPermissionListResponse> permssionsList = permissionService.listByRoleId(x.getId());

            x.setMenus(menuList);
            x.setPermissions(permssionsList);
        });
//        log.info("result:{}", result);
        return null != result && !result.isEmpty() ? result : Collections.emptyList();
    }

    /**
     * 根据一组ID查询角色管理列表
     *
     * @param ids 列表查询角色管理入参
     * @return 列表角色管理对象
     */
    @Override
    @Cacheable(value = "SysRole::batch::listByIds", key = "#p0")
    public List<SysRoleListResponse> listByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysRole> result = sysRoleMapper.selectBatchIds(ids);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysRoleListResponse.class);
    }

    /**
     * 列表角色管理根据条件返回一组ID
     *
     * @param req 列表角色管理条件入参
     * @return 一组角色管理ID
     */
    @Override
    @Cacheable(value = "SysRole::batch::listId", key = "#p0")
    public List<Long> listId(SysRoleListRequest req) {
        LambdaQueryWrapper<SysRole> query = getQuery(req);
        query.select(SysRole::getId);

        List<?> result = sysRoleMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
     * 计数
     *
     * @param req 列表查询角色管理入参
     * @return 计数
     */
    @Override
    public long count(SysRoleListRequest req) {
        LambdaQueryWrapper<SysRole> query = getQuery(req);

        return sysRoleMapper.selectCount(query);
    }

    /**
     * 是否存在{table.comment!}
     *
     * @param req 列表查询{table.comment!}入参
     * @return 是否存在
     */
    @Override
    public boolean exist(SysRoleListRequest req) {
        LambdaQueryWrapper<SysRole> query = getQuery(req)
                .select(SysRole::getId)
                .last("limit 1");

        List<?> result = sysRoleMapper.selectObjs(query);

        return null != result && !result.isEmpty();
    }

    /**
     * 根据一组ID返回角色管理名称
     *
     * @param ids 角色管理一组ID
     * @return 角色管理一组名称
     */
    @Override
    public List<Label> nameByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<>(new SysRole())
                .in(SysRole::getId, ids)
                .select(SysRole::getId, SysRole::getName);

        List<SysRole> result = sysRoleMapper.selectList(query);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
    }

    /**
     * 根据条件返回一组角色管理名称
     *
     * @param req 角色管理条件
     * @return 角色管理名称
     */
    @Override
    public List<Label> names(SysRoleListRequest req) {
        LambdaQueryWrapper<SysRole> query = getQuery(req)
                .select(SysRole::getId, SysRole::getName);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<SysRole> result = sysRoleMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
        }

        IPage<SysRole> result = sysRoleMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), Label.class);
    }

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    @Override
    @Cacheable(key = "#p0", value = "SysRole::name", condition = "#p0 != null")
    public String name(Long id) {
        if (null == id || 0L == id) {
            return "";
        }
        LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<>(SysRole.builder().id(id).build())
                .select(SysRole::getName);

        List<?> result = sysRoleMapper.selectObjs(query);
        return null == result || result.isEmpty() ? null : (String) result.get(0);
    }

}
