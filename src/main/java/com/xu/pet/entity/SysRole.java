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
* 角色管理
* @author xuqingf
* @since 2023-03-21
*/

@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("sys_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 名称中文
     */
    @TableField("name_zh")
    private String nameZh;

    /**
     * 角色状态：0停用；1启用
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 用户是否删除：0已删除；1未删除
     */
    @TableField("delete_")
    private Boolean delete_;


}

