package com.xu.pet.request;

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
 * 角色-菜单管理列表
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(value = "角色-菜单管理", description = "角色-菜单管理列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleMenuListRequest implements Serializable {
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
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    /**
     * 角色ids
     */
    @ApiModelProperty(value = "角色ids")
    private List<Long> roleIds;

    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private Long menuId;

    /**
     * 菜单ids
     */
    @ApiModelProperty(value = "菜单ids")
    private List<Long> menuIds;

}
