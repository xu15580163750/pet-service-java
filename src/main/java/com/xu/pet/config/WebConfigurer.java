package com.xu.pet.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xuqingf
 * @date 2023/3
 */
@SpringBootConfiguration
public class WebConfigurer implements WebMvcConfigurer, HandlerInterceptor {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //所有请求都允许跨域，使用这种配置方法就不能在 interceptor 中再配置 header 了
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8080","http://localhost:8081")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/file/**").addResourceLocations("file:" + "d:/workspace/img/");
    }

//    这段代码是 Spring Boot Web 应用程序中的一个配置类，用于注册资源处理器。
//    具体来说，它实现了 WebMvcConfigurer 接口，并覆盖了其中的 addResourceHandlers 方法。
//    下面是代码注释和解释：

    // 覆盖 WebMvcConfigurer 接口中的方法，用于注册资源处理器
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 注册访问 /api/file/** 路径时的资源处理器
//        registry.addResourceHandler("/api/file/**")
//                // 指定资源所在的本地物理路径，这里是 d:/workspace/img/
//                .addResourceLocations("file:" + "d:/workspace/img/");
//    }

//    这段代码的作用是将 "/api/file/**" 路径映射到本地物理路径 "d:/workspace/img/"，
//    从而可以通过访问 "/api/file/xxx" 的 URL 来直接获取本地存储在 "d:/workspace/img/" 文件夹中的文件。具体来讲：
//
//    addResourceHandler 方法指定了匹配的请求路径，这里是 "/api/file/**"，表示匹配所有以 "/api/file/" 开头的请求路径；
//    addResourceLocations 方法指定了资源所在的本地物理路径，这里是 "file:d:/workspace/img/"，
//    表示映射到窗口系统的 d:/workspace/img/ 文件夹。"file:" 表示协议类型，即指定了使用文件协议的资源路径。
//    因此，当我们在前端页面中需要访问 "/api/file/xxx" 的 URL 时，
//    Spring Boot 就会将这个请求路径映射到本地的 "d:/workspace/img/" 文件夹中，然后直接返回对应的文件。
//    在实际应用中，可以将这种方式用于实现图片、音视频等多媒体资源的访问和播放。

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(getLoginIntercepter())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/index.html")
//                .excludePathPatterns("/api/login")
//                .excludePathPatterns("/api/logout");
//    }

//    这段代码设置了一个拦截器，通过调用registry.addInterceptor()方法来添加拦截器并配置其拦截路径和排除路径：
//    getLoginIntercepter()方法返回一个实现了HandlerInterceptor接口的拦截器对象。
//    addPathPatterns()方法用于添加拦截路径，这里使用了"/**"表示拦截所有路径。
//    excludePathPatterns()方法用于排除拦截路径，这里排除了"/index.html"、"/api/login"和"/api/logout"三个路径。
//    代码注释如下：
//
//        // 添加拦截器，并配置拦截路径和排除路径
//        // registry.addInterceptor(getLoginIntercepter()) .addPathPatterns("/**")
//        // 拦截所有路径 .excludePathPatterns("/index.html")
//        // 排除/index.html路径 .excludePathPatterns("/api/login")
//        // 排除/api/login路径 .excludePathPatterns("/api/logout"); // 排除/api/logout路径 }


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        // 放行 options 请求，否则无法让前端带上自定义的 header 信息，导致 sessionID 改变，shiro 验证失败
        if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }

        Subject subject = SecurityUtils.getSubject();
        // 使用 shiro 验证
        if (!subject.isAuthenticated()) {
            return false;
        }
        return true;
    }

}
