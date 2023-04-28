package com.xu.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.pet.core.BusinessException;
import com.xu.pet.entity.User;
import com.xu.pet.enums.EErrorCode;
import com.xu.pet.mapper.UserMapper;
import com.xu.pet.result.ResultFactory;
import com.xu.pet.service.IMappingService;
import com.xu.pet.service.ISysRoleService;
import com.xu.pet.service.ISysUserRoleService;
import com.xu.pet.service.IUserService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
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
 * 用户管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Lazy
    @Autowired
    private ISysRoleService roleService;

//    @Lazy
//    @Autowired
//    private IUserService userService;

    @Autowired
    private IMappingService mappingService;

    private LambdaQueryWrapper<User> getQuery(UserListRequest req) {
        User condition = mappingService.map(req, User.class);
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>(condition);

        return query;
    }

    /**
     * 新增用户管理
     *
     * @param req 新增用户管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "User::batch", allEntries = true)
            }
    )
    public int create(UserCreateRequest req) {
        User user = mappingService.map(req, User.class);

        if (null == req.getUsername() || req.getUsername().equals("")
                || null == req.getPassword() || req.getPassword().equals("")) {
            return 0;
        }

        //验证用户是否已存在
        boolean exist = exist(UserListRequest.builder()
                .username(req.getUsername())
                .build());
        if (exist) {
            return 2;
        }
        // 默认生成 16 位盐,给密码加密，比较好的做法是前端登录传过来就是密文的密码，后端login的时候再根据手机号拿盐去生成加密密码去比对
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", req.getPassword(), salt, times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userMapper.insert(user);
        if (null == user.getId() || user.getId() <= 0) {
            return 4;
        }
        return 1;

    }

    /**
     * 删除用户管理
     *
     * @param id 主键ID
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "User::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0", value = "User::detail", condition = "#result"),
                    @CacheEvict(key = "#p0", value = "User::info", condition = "#result")
            }
    )
    public boolean remove(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    /**
     * 修改用户管理
     *
     * @param req 修改用户管理入参
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "User::batch", allEntries = true, condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "User::detail", condition = "#result"),
                    @CacheEvict(key = "#p0.id", value = "User::info", condition = "#result")
            }
    )
    public boolean modify(UserModifyRequest req) {
//        if (null == req || null == req.getId()) {
//            return false;
//        }
        UserListResponse record = list(UserListRequest.builder()
                .username(req.getUsername())
                .build()).get(0);
        List<Long> roleIds = record.getRoles().stream().map(SysRoleListResponse::getId).distinct().collect(Collectors.toList());

        List<Long> reqRoleIds = req.getRoles().stream().map(SysRoleListRequest::getId).distinct().collect(Collectors.toList());

        //留存差集合
//        reqRoleIds.removeAll(roleIds);
        //执行删除
        List<Long> b = roleIds.stream().filter(id -> !reqRoleIds.contains(id)).collect(Collectors.toList());

        if (null != b && !b.isEmpty()) {
            List<Long> ids = userRoleService.list(SysUserRoleListRequest.builder()
                            .roleIds(b)
                            .build())
                    .stream()
                    .map(SysUserRoleListResponse::getId)
                    .collect(Collectors.toList());
            //创建用户角色关系
            ids.forEach(x -> {
                userRoleService.remove(x);
            });
        }
        //执行增加
        List<Long> a = reqRoleIds.stream().filter(id -> !roleIds.contains(id)).collect(Collectors.toList());

        if (null != a && !a.isEmpty()) {
            //创建用户角色关系
            a.forEach(x -> {
                userRoleService.create(SysUserRoleCreateRequest.builder()
                        .userId(record.getId())
                        .roleId(x)
                        .build());
            });
        }
        //        //重置密码逻辑
//        if (null != req.getPassword()) {
//            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
//            int times = 2;
//            String encodedPassword = new SimpleHash("md5", req.getPassword(), salt, times).toString();
//            req.setSalt(salt);
//            req.setPassword(encodedPassword);
//        }


        User user = mappingService.map(req, User.class);

        return userMapper.updateById(user) > 0;


    }

    @Override
    public boolean resetPassword(UserListRequest req) {
        if (null == req || null == req.getUsername()) {
            return false;
        }
        Long userId = detail(UserDetailRequest.builder().username(req.getUsername()).build()).getId();
        req.setPassword("123");
        req.setId(userId);
        //重置密码逻辑
        if (null != req.getPassword()) {
            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            int times = 2;
            String encodedPassword = new SimpleHash("md5", req.getPassword(), salt, times).toString();
            req.setSalt(salt);
            req.setPassword(encodedPassword);
        }

        User user = mappingService.map(req, User.class);

        return userMapper.updateById(user) > 0;
    }


    @Override
    public boolean modifyPassword(UserListRequest req) {
        if (null == req || null == req.getUsername()) {
            return false;
        }
        UserDetailResponse userDetailResponse = detail(UserDetailRequest.builder().username(req.getUsername()).build());

        int times = 2;
        //校验原密码
        String oldEncodedPassword = new SimpleHash("md5", req.getOldPassword(), userDetailResponse.getSalt(), times).toString();
        if (!oldEncodedPassword.equals(userDetailResponse.getPassword())) {
            return false;
        }
        //生成新密码
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        String newEncodedPassword = new SimpleHash("md5", req.getNewPassword(), salt, times).toString();
        req.setSalt(salt);
        req.setPassword(newEncodedPassword);
        req.setId(userDetailResponse.getId());
        User user = mappingService.map(req, User.class);

        return userMapper.updateById(user) > 0;
    }

    /**
     * 详情查询用户管理
     *
     * @param req 主键ID
     * @return 用户管理对象
     */
    @Override
//    @Cacheable(value = "User::detail", key = "#p0", condition = "#p0 != null")
    public UserDetailResponse detail(UserDetailRequest req) {
        User condition = mappingService.map(req, User.class);
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>(condition);
        User user = userMapper.selectOne(query);

        if (null == user) {
            return UserDetailResponse.builder().build();
        }

        UserDetailResponse result = mappingService.map(user, UserDetailResponse.class);

        List<Long> roleIds = userRoleService.list(SysUserRoleListRequest.builder().userId(result.getId())
                        .build())
                .stream()
                .map(SysUserRoleListResponse::getRoleId)
                .collect(Collectors.toList());
//        roleService.list(SysRoleListRequest.builder()
//                .ids(roleIds)
//                .build())
//                .stream()
//                .map();
        //设置用户一组角色Ids
        result.setRoleIds(roleIds);
        return result;
    }


    public boolean modifyStatus(UserModifyRequest req) {
        if (null == req || null == req.getUsername()) {
            return false;
        }
        Long userId = detail(UserDetailRequest.builder().username(req.getUsername()).build()).getId();
        req.setId(userId);
        User user = mappingService.map(req, User.class);

        return userMapper.updateById(user) > 0;
    }


    /**
     * 分页查询用户管理
     *
     * @param req 分页查询用户管理入参
     * @return 分页用户管理对象
     */
    @Override
    @Cacheable(value = "User::batch::page", key = "#p0")
    public PageInfo<UserPageResponse> page(UserPageRequest req) {
        LambdaQueryWrapper<User> query = getQuery(mappingService.map(req, UserListRequest.class));

        IPage<User> result = userMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize()), query);

        return new PageInfo<>(mappingService.map(result.getRecords(), UserPageResponse.class), result.getTotal(), req.getPageSize());
    }

    /**
     * 列表查询用户管理
     *
     * @param req 列表查询用户管理入参
     * @return 列表用户管理对象
     */
    @Override
//    @Cacheable(value = "User::batch::list", key = "#p0")
    public List<UserListResponse> list(UserListRequest req) {
        LambdaQueryWrapper<User> query = getQuery(req);
        List<User> result = userMapper.selectList(query);
        List<UserListResponse> userListResponses = mappingService.map(result, UserListResponse.class);
        userListResponses.forEach(x -> {
            List<SysRoleListResponse> sysRoleListResponses = roleService.listByUsername(x.getUsername());
            x.setRoles(sysRoleListResponses);
        });
//        log.info("userListResponses:{}", userListResponses);
        return userListResponses;
    }

    /**
     * 根据一组ID查询用户管理列表
     *
     * @param ids 列表查询用户管理入参
     * @return 列表用户管理对象
     */
    @Override
    @Cacheable(value = "User::batch::listByIds", key = "#p0")
    public List<UserListResponse> listByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<User> result = userMapper.selectBatchIds(ids);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, UserListResponse.class);
    }

    /**
     * 列表用户管理根据条件返回一组ID
     *
     * @param req 列表用户管理条件入参
     * @return 一组用户管理ID
     */
    @Override
    @Cacheable(value = "User::batch::listId", key = "#p0")
    public List<Long> listId(UserListRequest req) {
        LambdaQueryWrapper<User> query = getQuery(req);
        query.select(User::getId);

        List<?> result = userMapper.selectObjs(query);

        return null == result || result.isEmpty() ? Collections.emptyList() : (List<Long>) result;
    }

    /**
     * 计数
     *
     * @param req 列表查询用户管理入参
     * @return 计数
     */
    @Override
    public long count(UserListRequest req) {
        LambdaQueryWrapper<User> query = getQuery(req);

        return userMapper.selectCount(query);
    }

    /**
     * 是否存在{table.comment!}
     *
     * @param req 列表查询{table.comment!}入参
     * @return 是否存在
     */
    @Override
    public boolean exist(UserListRequest req) {
        LambdaQueryWrapper<User> query = getQuery(req)
                .select(User::getId)
                .last("limit 1");

        List<?> result = userMapper.selectObjs(query);

        return null != result && !result.isEmpty();
    }

    /**
     * 根据一组ID返回用户管理名称
     *
     * @param ids 用户管理一组ID
     * @return 用户管理一组名称
     */
    @Override
    public List<Label> nameByIds(List<Long> ids) {
        if (null == ids || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>(new User())
                .in(User::getId, ids)
                .select(User::getId, User::getName);

        List<User> result = userMapper.selectList(query);
        return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
    }

    /**
     * 根据条件返回一组用户管理名称
     *
     * @param req 用户管理条件
     * @return 用户管理名称
     */
    @Override
    public List<Label> names(UserListRequest req) {
        LambdaQueryWrapper<User> query = getQuery(req)
                .select(User::getId, User::getName);

        if (null == req.getPageNum() || null == req.getPageSize()) {
            List<User> result = userMapper.selectList(query);

            return null == result || result.isEmpty() ? Collections.emptyList() : mappingService.map(result, Label.class);
        }

        IPage<User> result = userMapper.selectPage(new Page<>(req.getPageNum(), req.getPageSize(), false), query);
        return null == result || null == result.getRecords() || result.getRecords().isEmpty() ? Collections.emptyList() : mappingService.map(result.getRecords(), Label.class);
    }

    /**
     * 查询名称
     *
     * @param id ID
     * @return 名称
     */
    @Override
    @Cacheable(key = "#p0", value = "User::name", condition = "#p0 != null")
    public String name(Long id) {
        if (null == id || 0L == id) {
            return "";
        }
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>(User.builder().id(id).build())
                .select(User::getName);

        List<?> result = userMapper.selectObjs(query);
        return null == result || result.isEmpty() ? null : (String) result.get(0);
    }

}
