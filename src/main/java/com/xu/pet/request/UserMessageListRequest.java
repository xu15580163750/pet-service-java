package com.xu.pet.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* 信息管理列表
* @author xuqingf
* @since 2023-03-21
*/
@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(value = "信息管理", description = "信息管理列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMessageListRequest implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 个数
     */
    //@Min(value = 1)
    @ApiModelProperty(value = "个数", required = false)
    private Integer pageSize;

    /**
     * 页码
     */
    //@Min(value = 1)
    @ApiModelProperty(value = "页码", required = false)
    private Integer pageNum;

    /**
     * 唯一id
     */
    @ApiModelProperty(value = "唯一id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 宠物Id
     */
    @ApiModelProperty(value = "宠物Id")
    private Long petId;

    /**
     * 信息
     */
    @ApiModelProperty(value = "信息")
    private String messageText;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private Integer version;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
