package com.xu.pet.controller;

import com.xu.pet.core.BusinessException;
import com.xu.pet.core.IdRequest;
import com.xu.pet.core.Label;
import com.xu.pet.core.PageInfo;
import com.xu.pet.enums.EErrorCode;
import com.xu.pet.request.*;
import com.xu.pet.response.PetDetailResponse;
import com.xu.pet.response.PetListResponse;
import com.xu.pet.response.PetPageResponse;
import com.xu.pet.result.Result;
import com.xu.pet.result.ResultFactory;
import com.xu.pet.service.ICategoryService;
import com.xu.pet.service.IMappingService;
import com.xu.pet.service.IPetService;
import com.xu.pet.service.IUserService;
import com.xu.pet.util.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;


import java.io.File;
import java.io.IOException;
import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 宠物信息
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "", tags = {""})
@RequestMapping("/api")
@RestController
@Slf4j
public class PetController {

    @Autowired
    private IPetService petService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserService userService;

    @Autowired
    IMappingService mappingService;

    public Boolean create(@RequestBody @Valid PetCreateRequest req) {

        return null != petService.create(req);
    }


    public Boolean remove(IdRequest req) {

        return petService.remove(req.getId());
    }


    public Boolean modify(@RequestBody @Valid PetModifyRequest req) {

        return petService.modify(req);
    }


    public PetDetailResponse detail(IdRequest req) {

        return petService.detail(req.getId());
    }

    public PageInfo<PetPageResponse> page(@RequestBody @Valid PetPageRequest req) {

        return petService.page(req);
    }


    public List<PetListResponse> list(@RequestBody @Valid PetListRequest req) {

        return petService.list(req);
    }


    public List<Label> listName(PetListRequest req) {
        return petService.names(req);
    }

    @PutMapping("/pets")
    public Result listPets() {
        List<PetListResponse> list = petService.list(PetListRequest.builder().delete_(false).build());
        return ResultFactory.buildSuccessResult(list);
    }

    @PutMapping("/admin/pets")
    public Result adminlistPets(@RequestBody @Valid PetListRequest request) {
        Long userId = userService.detail(UserDetailRequest.builder().username(request.getUsername()).build()).getId();
       if (!userId.equals(1L)) {
            List<PetListResponse> list = petService.list(PetListRequest.builder().userId(userId).build());
            return ResultFactory.buildSuccessResult(list);
        }
        List<PetListResponse> adminlist = petService.list(PetListRequest.builder().build());
        return ResultFactory.buildSuccessResult(adminlist);
    }

    @PostMapping("/admin/content/pet/create")
    public Result addOrUpdateBooks(@RequestBody @Valid PetCreateRequest request) {
        if (null == request.getId()) {
            if (petService.create(request) > 0) {
                return ResultFactory.buildSuccessResult("增加救助宠物信息成功");
            }
            return ResultFactory.buildFailResult("增加失败,请稍后再试");
        }
        PetModifyRequest modifyRequest = mappingService.map(request, PetModifyRequest.class);
        if (petService.modify(modifyRequest)) {
            return ResultFactory.buildSuccessResult("修改救助宠物信息成功");
        }
        return ResultFactory.buildFailResult("修改失败,请稍后再试");
    }

    @PostMapping("/admin/content/pets/delete")
    public Result deleteBook(@RequestBody @Valid PetModifyRequest request) {
        request.setDelete_(true);
        if (petService.modify(request)) {
            return ResultFactory.buildSuccessResult("删除成功");
        }
        return ResultFactory.buildFailResult("删除失败,请稍后再试");
    }

    @GetMapping("/search")
    public Result searchResult(@RequestParam("keywords") String keywords) {
        if ("".equals(keywords)) {
            return ResultFactory.buildSuccessResult(petService.list(PetListRequest.builder().build()));
        } else {
            return ResultFactory.buildSuccessResult(petService.list(PetListRequest.builder().name(keywords).build()));
        }
    }

    @GetMapping("/categories/{cid}/pets")
    public Result listByCategory(@PathVariable("cid") Integer cid) {
        if (0 != cid) {
            return ResultFactory.buildSuccessResult(petService.list(PetListRequest.builder().cid(cid).build()));
        } else {
            return ResultFactory.buildSuccessResult(petService.list(PetListRequest.builder().build()));
        }
    }

    @PostMapping("/admin/content/pet/covers")
    public String coversUpload(MultipartFile file) {
        String folder = "D:/workspace/img";
        File imageFolder = new File(folder);
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        File f = new File(imageFolder, StringUtils.getRandomString(6) + suffix);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            String imgURL = "http://localhost:7007/api/file/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


}
