package stukk.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐物品关系
 */
@Data
@TableName("tb_setmeal_goods")
@Api(value = "套餐物品关系实体（SetmealGoods）")
public class SetmealGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "套餐ID")
    private Long setmealId;

    @ApiModelProperty(value = "物品图片路径")
    private String image;

    @ApiModelProperty(value = "物品ID")
    private Long goodsId;

    @ApiModelProperty(value = "物品名称 （冗余字段）")
    private String name;

    @ApiModelProperty(value = "物品原价（冗余字段）")
    private BigDecimal price;

    @ApiModelProperty(value = "物品份数")
    private Integer copies;

    @ApiModelProperty(value = "排序权值")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    // 是否删除
    private Integer isDeleted;
}
