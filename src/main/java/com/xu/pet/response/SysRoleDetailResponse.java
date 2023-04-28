package com.xu.pet.response;

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
* 角色管理详情
* @author xuqingf
* @since 2023-03-21
*/
@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(value = "角色管理", description = "角色管理")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleDetailResponse implements Serializable{
    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 名称中文
     */
    @ApiModelProperty(value = "名称中文")
    private String nameZh;

    /**
     * 角色状态：0停用；1启用
     */
    @ApiModelProperty(value = "角色状态：0停用；1启用")
    private Boolean enabled;

}
