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
 * 用户管理
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
@TableName("user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 加盐密钥
     */
    @TableField("salt")
    private String salt;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 使用状态：0停用；1启用
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 用户等级：默认0
     */
    @TableField("level")
    private Integer level;


    /**
     * 用户是否删除：0已删除；1未删除
     */
    @TableField("delete_")
    private Boolean delete_;

    /**
     * 版本号
     */
    @TableField("version")
    private String version;



    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

