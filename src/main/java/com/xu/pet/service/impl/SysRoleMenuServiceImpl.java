package com.xu.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.entity.SysMenu;
import com.xu.pet.entity.SysPermission;
import com.xu.pet.entity.SysRoleMenu;
import com.xu.pet.mapper.SysRoleMenuMapper;
import com.xu.pet.service.IMappingService;
import com.xu.pet.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import com.xu.pet.request.*;
import com.xu.pet.response.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 角色-菜单管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private IMappingService mappingService;

    private LambdaQueryWrapper<SysRoleMenu> getQuery(SysRoleMenuListRequest req) {
        SysRoleMenu condition = mappingService.map(req, SysRoleMenu.class);
        LambdaQueryWrapper<SysRoleMenu> query = new LambdaQueryWrapper<>(condition);

        query.in(null != req.getRoleIds() && !req.getRoleIds().isEmpty(), SysRoleMenu::getRoleId, req.getRoleIds());
        query.in(null != req.getMenuIds() && !req.getMenuIds().isEmpty(), SysRoleMenu::getMenuId, req.getMenuIds());
        return query;
    }

    /**
     * 新增角色-菜单管理
     *
     * @param req 新增角色-菜单管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRoleMenu::batch", allEntries = true)
            }
    )
    public Long create(SysRoleMenuCreateRequest req) {
        SysRoleMenu sysRoleMenu = mappingService.map(req, SysRoleMenu.class);

        sysRoleMenuMapper.insert(sysRoleMenu);

        return sysRoleMenu.getId();
    }


    /**
     * 新增角色-菜单管理 批量
     *
     * @param roleId 新增角色-菜单管理入参
     */
    @Override
    public boolean createBatch(Long roleId, List<SysRoleMenuCreateRequest> menus) {
        removeBatch(roleId);
        int count = 0;
        List<Long> menuIds = menus.stream().map(SysRoleMenuCreateRequest::getId).collect(Collectors.toList());
        for (Long menuId : menuIds) {
            create(SysRoleMenuCreateRequest.builder()
                    .menuId(menuId)
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
     * 删除角色-菜单管理
     *
     * @param id 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRoleMenu::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRoleMenu::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRoleMenu::info", condition = "#result")
            }
    )
    public boolean remove(Long id) {
        return sysRoleMenuMapper.deleteById(id) > 0;
    }

    /**
     * 删除角色-菜单管理
     *
     * @param roleId 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRoleMenu::roles::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRoleMenu::roles::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysRoleMenu::roles::info", condition = "#result")
            }
    )
    public boolean removeBatch(Long roleId) {
        List<Long> menuIds = list(SysRoleMenuListRequest.builder()
                .roleId(roleId)
                .build())
                .stream()
                .map(SysRoleMenuListResponse::getId)
                .collect(Collectors.toList());
        int count = 0;
        for (Long id : menuIds) {
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
     * 修改角色-菜单管理
     *
     * @param req 修改角色-菜单管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysRoleMenu::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysRoleMenu::detail", condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysRoleMenu::info", condition = "#result")
            }
    )
    public boolean modify(SysRoleMenuModifyRequest req) {
        if (null == req || null == req.getId()) {
            return false;
        }
        SysRoleMenu sysRoleMenu = mappingService.map(req, SysRoleMenu.class);

        return sysRoleMenuMapper.updateById(sysRoleMenu) > 0;
    }

    /**
     * 详情查询角色-菜单管理
     *
     * @param id 主键ID
     * @return 角色-菜单管理对象
     */
    @Override
    @Cacheable(value = "SysRoleMenu::detail", key = "#p0", condition = "#p0 != null")
    public SysRoleMenuDetailResponse detail(Long id) {
        if (null == id) {
            return null;
        }
        SysRoleMenu sysRoleMenu = sysRoleMenuMapper.selectById(id);
        if (null == sysRoleMenu) {
            return null;
        }

        return mappingService.map(sysRoleMenu, SysRoleMenuDetailResponse.class);
    }
//
//    /**
//    * 查询角色-菜单管理
//    *
//    * @param id 主键ID
//    * @return 角色-菜单管理对象
//    */
//    @Override
//    @Cacheable(value = "SysRoleMenu::info", key = "#p0", condition = "#p0 != null")
//    public SysRoleMenuInfoResponse info(Long id) {
//        if (null == id) {
//        return null;
//        }
//        SysRoleMenu sysRoleMenu = sysRoleMenuMapper.selectById(id);
//        if (null == sysRoleMenu) {
//        return null;
//        }
//
//        return mappingService.map(sysRoleMenu, SysRoleMenuInfoResponse.class);
//    }

    /**
     * 分页查询角色-菜单管理
     *
     * @param req 分页查询角色-菜单管理入参
     * @return 分页角色-菜单管理对象
     */
    @Override
    @Cacheable(value = "SysRoleMenu::batch::page", key = "#p0")
    public PageInfo<SysRoleMenuPageResponse> page(SysRoleMenuPageRequest req) {
        LambdaQueryWrapper<SysRoleMenu> query = getQuery(mappingService.map(req, SysRoleMenuListRequest.class));

        IPage<SysRoleMenu> result = sysRoleMenuMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), SysRoleMenuPageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
     * 列表查询角色-菜单管理
     *
     * @param req 列表查询角色-菜单管理入参
     * @return 列表角色-菜单管理对象
     */
    @Override
//    @Cacheable(value = "SysRoleMenu::batch::list", key = "#p0")
    public List<SysRoleMenuListResponse> list(SysRoleMenuListRequest req) {
        LambdaQueryWrapper<SysRoleMenu> query = getQuery(req);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<SysRoleMenu> result = sysRoleMenuMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysRoleMenuListResponse.class);
        }

        IPage<SysRoleMenu> result = sysRoleMenuMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);

        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), SysRoleMenuListResponse.class);
    }

    /**
     * 根据一组ID查询角色-菜单管理列表
     *
     * @param ids 列表查询角色-菜单管理入参
     * @return 列表角色-菜单管理对象
     */
    @Override
    @Cacheable(value = "SysRoleMenu::batch::listByIds", key = "#p0")
    public List<SysRoleMenuListResponse> listByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysRoleMenu> result = sysRoleMenuMapper.selectBatchIds(ids);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysRoleMenuListResponse.class);
    }

    /**
     * 列表角色-菜单管理根据条件返回一组ID
     *
     * @param req 列表角色-菜单管理条件入参
     * @return 一组角色-菜单管理ID
     */
    @Override
    @Cacheable(value = "SysRoleMenu::batch::listId", key = "#p0")
    public List<Long> listId(SysRoleMenuListRequest req) {
        LambdaQueryWrapper<SysRoleMenu> query = getQuery(req);
        query.select(SysRoleMenu::getId);

        List<?> result = sysRoleMenuMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
     * 计数
     *
     * @param req 列表查询角色-菜单管理入参
     * @return 计数
     */
    @Override
    public long count(SysRoleMenuListRequest req) {
        LambdaQueryWrapper<SysRoleMenu> query = getQuery(req);

        return sysRoleMenuMapper.selectCount(query);
    }

    /**
     * 是否存在{table.comment!}
     *
     * @param req 列表查询{table.comment!}入参
     * @return 是否存在
     */
    @Override
    public boolean exist(SysRoleMenuListRequest req) {
        LambdaQueryWrapper<SysRoleMenu> query = getQuery(req)
                .select(SysRoleMenu::getId)
                .last("limit 1");

        List<?> result = sysRoleMenuMapper.selectObjs(query);

        return null != result && !result.isEmpty();
    }

    @Override
    public void updateRoleMenu(Long roleId, Map<String, List<Long>> menusIds) {
        removeBatch(roleId);
        Long count = 0L;
        List<SysRoleMenuCreateRequest> roleMenuCreateRequests = new ArrayList<>();
        for (Long menuId : menusIds.get("menusIds")) {
            SysRoleMenuCreateRequest rm = SysRoleMenuCreateRequest.builder()
                    .roleId(roleId)
                    .menuId(menuId)
                    .build();
            if (create(rm) > 0) {
                count++;
            }
            log.info("count:{}", count);
        }

    }

//    /**
//    * 根据一组ID返回角色-菜单管理名称
//    *
//    * @param ids 角色-菜单管理一组ID
//    * @return 角色-菜单管理一组名称
//    */
//    @Override
//    public List<Label> nameByIds(List<Long> ids) {
//        if (null == ids || ids.isEmpty()) {
//        return Collections.emptyList();
//        }
//        LambdaQueryWrapper<SysRoleMenu> query = new LambdaQueryWrapper<>(new SysRoleMenu())
//        .in(SysRoleMenu::getId, ids)
//        .select(SysRoleMenu::getId, SysRoleMenu::getName);
//
//        List<SysRoleMenu> result = sysRoleMenuMapper.selectList(query);
//        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
//    }

//    /**
//    * 根据条件返回一组角色-菜单管理名称
//    *
//    * @param req 角色-菜单管理条件
//    * @return 角色-菜单管理名称
//    */
//    @Override
//    public List<Label> names(SysRoleMenuListRequest req) {
//        LambdaQueryWrapper<SysRoleMenu> query = getQuery(req)
//        .select(SysRoleMenu::getId, SysRoleMenu::getName);
//
//        if (null == req.getPageNum() || null == req.getPageSize()) {
//        List<SysRoleMenu> result = sysRoleMenuMapper.selectList(query);
//
//        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
//        }
//
//        IPage<SysRoleMenu> result = sysRoleMenuMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
//        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), Label.class);
//    }

//    /**
//    * 查询名称
//    *
//    * @param id ID
//    * @return 名称
//    */
//    @Override
//    @Cacheable(key = "#p0", value = "SysRoleMenu::name", condition = "#p0 != null")
//    public String name(Long id) {
//        if (null == id || 0L == id) {
//        return "";
//        }
//        LambdaQueryWrapper<SysRoleMenu> query = new LambdaQueryWrapper<>(SysRoleMenu.builder().id(id).build())
//        .select(SysRoleMenu::getName);
//
//        List<?> result = sysRoleMenuMapper.selectObjs(query);
//        return null == result || result.isEmpty() ? null : (String) result.get(0);
//    }

}
