package com.xu.pet.config;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;


public class SwaggerConfigBuilder {
    private String title;
    private String description;
    private String version;
    private String basePackage;
    private String groupName;


    public static SwaggerConfigBuilder create() {
        return new SwaggerConfigBuilder();
    }

    public SwaggerConfigBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public SwaggerConfigBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public SwaggerConfigBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public SwaggerConfigBuilder setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

    public SwaggerConfigBuilder setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public Docket build() {
        Contact contact = new Contact("xuqingf", "", "");
        ApiInfo apiInfo = new ApiInfo(title, description, version, "", contact, "", "", new ArrayList<>());

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(apiInfo)
                .groupName(groupName)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .build();
    }
}
