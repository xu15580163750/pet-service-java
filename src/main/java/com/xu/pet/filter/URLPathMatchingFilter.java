package com.xu.pet.filter;

//import com.gm.wj.service.AdminPermissionService;
//import com.gm.wj.util.SpringContextUtils;

import com.xu.pet.request.SysPermissionListRequest;
import com.xu.pet.service.ISysPermissionService;
import com.xu.pet.service.impl.SysPermissionServiceImpl;
import com.xu.pet.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author xuqingf
 * @date 2023/3
 */
@Slf4j
@Component
public class URLPathMatchingFilter extends PathMatchingFilter {

    @Autowired
    ISysPermissionService permissionService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (HttpMethod.OPTIONS.toString().equals((httpServletRequest).getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }
        if (null == permissionService) {
            permissionService = SpringContextUtils.getContext().getBean(SysPermissionServiceImpl.class);
        }

        String requestAPI = getPathWithinApplication(request);
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未登录用户尝试访问需要登录的接口");
            return false;
        }
        // 判断访问接口是否需要过滤（数据库中是否有对应信息）
        boolean needFilter = permissionService.exist(SysPermissionListRequest.builder()
                .url(requestAPI)
                .build());
        if (!needFilter) {
            return true;
        } else {
            // 判断当前用户是否有相应权限
            boolean hasPermission = false;
            String username = subject.getPrincipal().toString();
            Set<String> permissionAPIs = permissionService.listByUsername(username);
            if (null == permissionAPIs || permissionAPIs.isEmpty()) {
                log.warn("用户：" + username + "无权限：" + requestAPI);
            } else {
                for (String api : permissionAPIs) {
                    // 匹配前缀
                    if (requestAPI.startsWith(api)) {
                        hasPermission = true;
                        break;
                    }
                }
            }
            if (hasPermission) {
                log.trace("用户：" + username + "访问了：" + requestAPI + "接口");
                return true;
            } else {
                log.warn("用户：" + username + "访问了没有权限的接口：" + requestAPI);
                return false;
            }
        }
    }
}
