package com.xu.pet.realm;

//import com.gm.wj.entity.User;
//import com.gm.wj.service.AdminPermissionService;
//import com.gm.wj.service.AdminRoleService;
//import com.gm.wj.service.UserService;

import com.xu.pet.core.BusinessException;
import com.xu.pet.entity.User;
import com.xu.pet.enums.EErrorCode;
import com.xu.pet.request.UserDetailRequest;
import com.xu.pet.response.UserDetailResponse;
import com.xu.pet.service.IMappingService;
import com.xu.pet.service.ISysPermissionService;
import com.xu.pet.service.ISysRolePermissionService;
import com.xu.pet.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author xuqingf
 * @date 2023/03
 */
@Slf4j
@Component
public class PetRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    @Autowired
    private ISysRolePermissionService rolePermissionService;


    @Autowired
    private IMappingService mappingService;

    // 重写获取授权信息方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取当前用户的所有权限
        String username = principalCollection.getPrimaryPrincipal().toString();
        Set<String> permissions = rolePermissionService.listByName(username);

        // 将权限放入授权信息中
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    // 获取认证信息，即根据 token 中的用户名从数据库中获取密码、盐等并返回
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = token.getPrincipal().toString();
        User user = mappingService.map(Optional.ofNullable(userService.detail(UserDetailRequest.builder()
                        .username(userName)
                        .build()))
                .orElse(UserDetailResponse.builder()
                        .build()), User.class);
        if (ObjectUtils.isEmpty(user)) {
            throw new UnknownAccountException();
        }
        String passwordInDB = user.getPassword();
        String salt = user.getSalt();
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, passwordInDB,
                ByteSource.Util.bytes(salt), getName());
        if (null == authenticationInfo) {
            throw new BusinessException(EErrorCode.BIZ_DEFAULT.getCode(), "信息为空---获取失败");
        }
        return authenticationInfo;
    }
}

//    这段代码是一个 Shiro 框架的自定义 Realm 类，具有权限控制和认证功能。实现了 AuthorizingRealm 抽象类
//        ，重写了 doGetAuthorizationInfo 和 doGetAuthenticationInfo 方法。
//
//        doGetAuthorizationInfo 方法：获取当前用户的所有权限并返回授权信息。
//        首先从 PrincipalCollection 中获取用户名，再从 adminPermissionService 中获取该用户的所有权限，
//        将权限放入 SimpleAuthorizationInfo 对象中并返回。
//
//        doGetAuthenticationInfo 方法：获取认证信息，
//        即根据 token 中的用户名从数据库中获取密码、盐等并返回。首先从 token 中获取用户名，
//        再从 userService 中获取该用户名对应的用户信息，判断用户是否存在。
//        如果不存在，抛出 UnknownAccountException 异常。如果存在，则从用户信息中获取密码和盐，生成 SimpleAuthenticationInfo 对象并返回。
//
//        需要注意的是，整个认证流程由 Shiro 框架完成，开发人员只需实现
//        doGetAuthorizationInfo 和 doGetAuthenticationInfo 方法即可。该 Realm 类需要在 Shiro 配置文件中进行配置，
//        以便在 Shiro 框架中使用。
