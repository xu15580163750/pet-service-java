package com.xu.pet.entity;

import java.util.Date;
import java.io.Serializable;

import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.*;

/**
 * 分类管理
 *
 * @author xuqingf
 * @since 2023-03-21
 */

@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("category")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId("id")
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;


}

