package com.xu.pet.controller;

import com.xu.pet.core.IdRequest;
import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.request.CategoryCreateRequest;
import com.xu.pet.request.CategoryListRequest;
import com.xu.pet.request.CategoryModifyRequest;
import com.xu.pet.request.CategoryPageRequest;
import com.xu.pet.response.CategoryDetailResponse;
import com.xu.pet.response.CategoryListResponse;
import com.xu.pet.response.CategoryPageResponse;
import com.xu.pet.service.ICategoryService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;


import java.util.List;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>
 * 
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "", tags = {""})
@RequestMapping("/api/v1/")
@RestController
@Slf4j
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;


    public Boolean create(@RequestBody @Valid CategoryCreateRequest req) {

        return null != categoryService.create(req);
    }


    public Boolean remove(IdRequest req) {

        return categoryService.remove(req.getId());
    }


    public Boolean modify(@RequestBody @Valid CategoryModifyRequest req) {

        return categoryService.modify(req);
    }


    public CategoryDetailResponse detail(IdRequest req) {

        return categoryService.detail(req.getId());
    }


    public PageInfo<CategoryPageResponse> page(@RequestBody @Valid CategoryPageRequest req) {

        return categoryService.page(req);
    }


    public List<CategoryListResponse> list(@RequestBody @Valid CategoryListRequest req) {

        return categoryService.list(req);
    }


    public List<Label> listName(CategoryListRequest req) {
        return categoryService.names(req);
    }

}
