package com.xu.pet.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@ControllerAdvice
public class SwaggerConfig {

    @Bean(value = "data")
    public Docket docket(Environment environment) {
        return SwaggerConfigBuilder.create()
                .setTitle("宠物服务")
                .setDescription("pet-service")
                .setVersion("1.0")
                .setBasePackage("com.xu.pet")
                .setGroupName("data")
                .build();
    }
}
