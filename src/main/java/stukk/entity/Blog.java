package stukk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_blog")
@ApiModel(value = "博客实体（Blog）")
public class Blog {

    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    Integer id;

    @ApiModelProperty(value = "标题")
    String name;

    @ApiModelProperty(value = "描述")
    String des;

    @ApiModelProperty(value = "图片")
    String image;

    @ApiModelProperty(value = "观看次数")
    Integer view;

    @ApiModelProperty(value = "内容")
    String content;
}
