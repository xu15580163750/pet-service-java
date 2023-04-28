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
 * 菜单管理
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
@TableName("sys_menu")
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * url路径
     */
    @TableField("path")
    private String path;

    /**
     * 名称中文
     */
    @TableField("name_zh")
    private String nameZh;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 图标
     */
    @TableField("icon_cls")
    private String iconCls;

    /**
     * 组件
     */
    @TableField("component")
    private String component;

    /**
     * 父菜单id
     */
    @TableField("parent_id")
    private Long parentId;


}

