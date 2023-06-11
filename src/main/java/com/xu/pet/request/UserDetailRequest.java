package com.xu.pet.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户管理创建
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(value = "用户管理", description = "用户管理创建")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailRequest implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 加盐密钥
     */
    @ApiModelProperty(value = "加盐密钥")
    private String salt;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 使用状态：0停用；1启用
     */
    @ApiModelProperty(value = "使用状态：0停用；1启用")
    private Boolean enabled;


    /**
     * 用户是否删除：0已删除；1未删除
     */
    @ApiModelProperty(value = "用户是否删除：0已删除；1未删除")
    private Boolean delete_;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;

//    /**
//     * 创建时间
//     */
//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime;
//
//    /**
//     * 更新时间
//     */
//    @ApiModelProperty(value = "更新时间")
//    private LocalDateTime updateTime;
}
