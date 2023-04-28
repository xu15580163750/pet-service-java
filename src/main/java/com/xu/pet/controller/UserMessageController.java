package com.xu.pet.controller;

import com.xu.pet.core.IdRequest;
import com.xu.pet.core.PageInfo;
import com.xu.pet.request.UserMessageCreateRequest;
import com.xu.pet.request.UserMessageListRequest;
import com.xu.pet.request.UserMessageModifyRequest;
import com.xu.pet.request.UserMessagePageRequest;
import com.xu.pet.response.UserMessageDetailResponse;
import com.xu.pet.response.UserMessageListResponse;
import com.xu.pet.response.UserMessagePageResponse;
import com.xu.pet.service.IUserMessageService;
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
 * 信息管理
 * </p>
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Api(value = "", tags = {""})
@RequestMapping("/api/v1/")
@RestController
@Slf4j
public class UserMessageController {

    @Autowired
    private IUserMessageService userMessageService;


    public Boolean create(@RequestBody @Valid UserMessageCreateRequest req) {

        return null != userMessageService.create(req);
    }


    public Boolean remove(IdRequest req) {

        return userMessageService.remove(req.getId());
    }


    public Boolean modify(@RequestBody @Valid UserMessageModifyRequest req) {

        return userMessageService.modify(req);
    }

    public UserMessageDetailResponse detail(IdRequest req) {

        return userMessageService.detail(req.getId());
    }

    public PageInfo<UserMessagePageResponse> page(@RequestBody @Valid UserMessagePageRequest req) {

        return userMessageService.page(req);
    }


    public List<UserMessageListResponse> list(@RequestBody @Valid UserMessageListRequest req) {

        return userMessageService.list(req);
    }

//    public List<Label> listName(UserMessageListRequest req) {
//        return userMessageService.names(req);
//    }

}
