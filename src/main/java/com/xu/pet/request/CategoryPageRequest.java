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
 * 分页
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(value = "", description = "分页")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryPageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 个数
     */
    //@Min(value = 1)
    @ApiModelProperty(value = "个数", required = true)
    private Integer pageSize;

    /**
     * 页码
     */
    //@Min(value = 1)
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;


    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

}
