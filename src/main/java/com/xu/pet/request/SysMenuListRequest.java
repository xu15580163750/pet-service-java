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
 * 菜单管理列表
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(value = "菜单管理", description = "菜单管理列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuListRequest implements Serializable {
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
     * ids
     */
    @ApiModelProperty(value = "ids")
    private List<Long> ids;

    /**
     * url路径
     */
    @ApiModelProperty(value = "url路径")
    private String path;

    /**
     * 名称中文
     */
    @ApiModelProperty(value = "名称中文")
    private String nameZh;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String iconCls;

    /**
     * 组件
     */
    @ApiModelProperty(value = "组件")
    private String component;

    /**
     * 父菜单id
     */
    @ApiModelProperty(value = "父菜单id")
    private Long parentId;

}
