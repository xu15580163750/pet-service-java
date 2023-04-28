package com.xu.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.entity.SysMenu;
import com.xu.pet.mapper.SysMenuMapper;
import com.xu.pet.service.*;
import org.apache.shiro.SecurityUtils;
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
 * 菜单管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private IMappingService mappingService;

    @Autowired
    private ISysRoleMenuService roleMenuService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private IUserService userService;


    private LambdaQueryWrapper<SysMenu> getQuery(SysMenuListRequest req) {
        SysMenu condition = mappingService.map(req, SysMenu.class);
        LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>(condition);
        query.in(null != req.getIds() && !req.getIds().isEmpty(), SysMenu::getId, req.getIds());

        return query;
    }

    /**
     * 新增菜单管理
     *
     * @param req 新增菜单管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysMenu::batch", allEntries = true)
            }
    )
    public Long create(SysMenuCreateRequest req) {
        SysMenu sysMenu = mappingService.map(req, SysMenu.class);

        sysMenuMapper.insert(sysMenu);

        return sysMenu.getId();
    }

    /**
     * 删除菜单管理
     *
     * @param id 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysMenu::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysMenu::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "SysMenu::info", condition = "#result")
            }
    )
    public boolean remove(Long id) {
        return sysMenuMapper.deleteById(id) > 0;
    }

    /**
     * 修改菜单管理
     *
     * @param req 修改菜单管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "SysMenu::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysMenu::detail", condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "SysMenu::info", condition = "#result")
            }
    )
    public boolean modify(SysMenuModifyRequest req) {
        if (null == req || null == req.getId()) {
            return false;
        }
        SysMenu sysMenu = mappingService.map(req, SysMenu.class);

        return sysMenuMapper.updateById(sysMenu) > 0;
    }

    /**
     * 详情查询菜单管理
     *
     * @param id 主键ID
     * @return 菜单管理对象
     */
    @Override
    @Cacheable(value = "SysMenu::detail", key = "#p0", condition = "#p0 != null")
    public SysMenuDetailResponse detail(Long id) {
        if (null == id) {
            return null;
        }
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        if (null == sysMenu) {
            return null;
        }

        return mappingService.map(sysMenu, SysMenuDetailResponse.class);
    }
//
//    /**
//    * 查询菜单管理
//    *
//    * @param id 主键ID
//    * @return 菜单管理对象
//    */
//    @Override
//    @Cacheable(value = "SysMenu::info", key = "#p0", condition = "#p0 != null")
//    public SysMenuInfoResponse info(Long id) {
//        if (null == id) {
//        return null;
//        }
//        SysMenu sysMenu = sysMenuMapper.selectById(id);
//        if (null == sysMenu) {
//        return null;
//        }
//
//        return mappingService.map(sysMenu, SysMenuInfoResponse.class);
//    }

    /**
     * 分页查询菜单管理
     *
     * @param req 分页查询菜单管理入参
     * @return 分页菜单管理对象
     */
    @Override
    @Cacheable(value = "SysMenu::batch::page", key = "#p0")
    public PageInfo<SysMenuPageResponse> page(SysMenuPageRequest req) {
        LambdaQueryWrapper<SysMenu> query = getQuery(mappingService.map(req, SysMenuListRequest.class));

        IPage<SysMenu> result = sysMenuMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), SysMenuPageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
     * 列表查询菜单管理
     *
     * @param req 列表查询菜单管理入参
     * @return 列表菜单管理对象
     */
    @Override
//    @Cacheable(value = "SysMenu::batch::list", key = "#p0")
    public List<SysMenuListResponse> list(SysMenuListRequest req) {
        LambdaQueryWrapper<SysMenu> query = getQuery(req);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<SysMenu> result = sysMenuMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysMenuListResponse.class);
        }

        IPage<SysMenu> result = sysMenuMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);

        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), SysMenuListResponse.class);
    }

    /**
     * 列表查询菜单根据roleId
     *
     * @param roleId 列表查询菜单管理入参
     * @return 菜单管理列表数据
     */
    @Override
    public List<SysMenuListResponse> listByRoleId(Long roleId) {
        //获取角色对应的菜单Id
        List<Long> menuIds = roleMenuService.list(SysRoleMenuListRequest.builder()
                        .roleId(roleId)
                        .build())
                .stream()
                .map(SysRoleMenuListResponse::getMenuId)
                .collect(Collectors.toList());
        //获取菜单集合
        if (null == menuIds || menuIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysMenuListResponse> result = list(SysMenuListRequest.builder()
                .ids(menuIds)
                .build());
        //构建菜单
        handleMenus(result);

        return result;
    }


    /**
     * 列表查询菜单根据用户名
     *
     * @return 菜单管理列表数据
     */
    @Override
    public List<SysMenuListResponse> listByName() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        //获取一组角色Ids
        List<Long> roleIds = userService.detail(UserDetailRequest.builder()
                        .username(username)
                        .build())
                .getRoleIds();
        if (null == roleIds || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        //获取一组菜单Ids
        List<Long> menuIds = roleMenuService.list(SysRoleMenuListRequest.builder()
                        .roleIds(roleIds)
                        .build())
                .stream()
                .map(SysRoleMenuListResponse::getMenuId)
                .distinct()
                .collect(Collectors.toList());
        if (null == menuIds || menuIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysMenuListResponse> result = list(SysMenuListRequest.builder()
                .ids(menuIds)
                .build());

        if (null == result || result.isEmpty()) {
            return Collections.emptyList();
        }
        //获取树形菜单
        handleMenus(result);
        return null != result && !result.isEmpty() ? result : Collections.emptyList();
    }


    /*
     *
     *获取菜单树形
     * */
    public void handleMenus(List<SysMenuListResponse> menus) {
        menus.forEach(m -> {
            List<SysMenuListResponse> children = list(SysMenuListRequest.builder()
                    .parentId(m.getId())
                    .build());
            m.setChildren(children);
        });
        menus.removeIf(m -> m.getParentId() != 0);
    }

    /**
     * 根据一组ID查询菜单管理列表
     *
     * @param ids 列表查询菜单管理入参
     * @return 列表菜单管理对象
     */
    @Override
    @Cacheable(value = "SysMenu::batch::listByIds", key = "#p0")
    public List<SysMenuListResponse> listByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysMenu> result = sysMenuMapper.selectBatchIds(ids);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, SysMenuListResponse.class);
    }

    /**
     * 列表菜单管理根据条件返回一组ID
     *
     * @param req 列表菜单管理条件入参
     * @return 一组菜单管理ID
     */
    @Override
    @Cacheable(value = "SysMenu::batch::listId", key = "#p0")
    public List<Long> listId(SysMenuListRequest req) {
        LambdaQueryWrapper<SysMenu> query = getQuery(req);
        query.select(SysMenu::getId);

        List<?> result = sysMenuMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
     * 计数
     *
     * @param req 列表查询菜单管理入参
     * @return 计数
     */
    @Override
    public long count(SysMenuListRequest req) {
        LambdaQueryWrapper<SysMenu> query = getQuery(req);

        return sysMenuMapper.selectCount(query);
    }

    /**
     * 是否存在{table.comment!}
     *
     * @param req 列表查询{table.comment!}入参
     * @return 是否存在
     */
    @Override
    public boolean exist(SysMenuListRequest req) {
        LambdaQueryWrapper<SysMenu> query = getQuery(req)
                .select(SysMenu::getId)
                .last("limit 1");

        List<?> result = sysMenuMapper.selectObjs(query);

        return null != result && !result.isEmpty();
    }

    /**
     * 根据一组ID返回菜单管理名称
     *
     * @param ids 菜单管理一组ID
     * @return 菜单管理一组名称
     */
    @Override
    public List<Label> nameByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>(new SysMenu())
                .in(SysMenu::getId, ids)
                .select(SysMenu::getId, SysMenu::getName);

        List<SysMenu> result = sysMenuMapper.selectList(query);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
    }

    /**
     * 根据条件返回一组菜单管理名称
     *
     * @param req 菜单管理条件
     * @return 菜单管理名称
     */
    @Override
    public List<Label> names(SysMenuListRequest req) {
        LambdaQueryWrapper<SysMenu> query = getQuery(req)
                .select(SysMenu::getId, SysMenu::getName);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<SysMenu> result = sysMenuMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
        }

        IPage<SysMenu> result = sysMenuMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), Label.class);
    }

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    @Override
    @Cacheable(key = "#p0", value = "SysMenu::name", condition = "#p0 != null")
    public String name(Long id) {
        if (null == id || 0L == id) {
            return "";
        }
        LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>(SysMenu.builder().id(id).build())
                .select(SysMenu::getName);

        List<?> result = sysMenuMapper.selectObjs(query);
        return null == result || result.isEmpty() ? null : (String) result.get(0);
    }

}
