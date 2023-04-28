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
 * 宠物信息列表
 *
 * @author xuqingf
 * @since 2023-03-21
 */
@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(value = "宠物信息", description = "宠物信息列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetListRequest implements Serializable {
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
     * 宠物名称
     */
    @ApiModelProperty(value = "宠物名称")
    private String name;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 宠物名称
     */
    @ApiModelProperty(value = "用户账户")
    private String username;

    /**
     * 救援备注
     */
    @ApiModelProperty(value = "救援备注")
    private String petRemark;

    /**
     * 救援地址
     */
    @ApiModelProperty(value = "救援地址")
    private String address;

    /**
     * 救援反馈
     */
    @ApiModelProperty(value = "救援反馈")
    private String rescueFeedback;

    /**
     * 救助状态:等待救援中 1,正在救援中 2,救援成功 3 ,救援失败4 ,未知 0
     */
    @ApiModelProperty(value = "救助状态:等待救援中 1,正在救援中 2,救援成功 3 ,救援失败4 ,未知 0")
    private Integer status;


    /**
     * 救助状态:等待救援中 1,正在救援中 2,救援成功 3 ,救援失败4 ,未知 0
     */
    @ApiModelProperty(value = "救助状态:等待救援中 1,正在救援中 2,救援成功 3 ,救援失败4 ,未知 0")
    private String statuName;

    /**
     * 宠物图片Url
     */
    @ApiModelProperty(value = "宠物图片Url")
    private String petUrl;

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

    /**
     * 类别id， 1；猫咪 ；2：狗狗；3：小动物 ；4：其他
     */
    @ApiModelProperty(value = "类别id")
    private Integer cid;

    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    private String cName;

    /**
     * 是否已删除,默认0 ,1已删除
     */
    @ApiModelProperty(value = "是否已删除,默认0 ,1已删除")
    private boolean delete_;

}
