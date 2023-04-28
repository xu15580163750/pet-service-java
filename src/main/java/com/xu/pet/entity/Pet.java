package com.xu.pet.entity;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 宠物管理
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
@TableName("pet")
public class Pet implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 宠物名称
     */
    @TableField("name")
    private String name;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 救援备注
     */
    @TableField("pet_remark")
    private String petRemark;

    /**
     * 救援地址
     */
    @TableField("address")
    private String address;

    /**
     * 救援反馈
     */
    @TableField("rescue_feedback")
    private String rescueFeedback;

    /**
     * 救助状态:等待救援中 1,正在救援中 2,救援成功 3 ,救援失败4 ,未知 0
     */
    @TableField("status")
    private Integer status;

    /**
     * 宠物图片Url
     */
    @TableField("pet_url")
    private String petUrl;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;


    /**
     * 类别id
     */
    @TableField("cid")
    private Integer cid;

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

    /**
     * 是否已删除,默认0 ,1已删除
     */
    @TableField("delete_")
    private boolean delete_;


}

