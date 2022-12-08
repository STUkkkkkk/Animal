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
@TableName("tb_dog")
@ApiModel(value = "狗实体（Dog）")
public class Dog {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "路径")
    private String url;

    @ApiModelProperty(value = "体重")
    private String weight;

    @ApiModelProperty(value = "全名")
    private String allName;

    @ApiModelProperty(value = "能力")
    private String ability;

    @ApiModelProperty(value = "定位")
    private String position;

    @ApiModelProperty(value = "生活习性")
    private String life;

    @ApiModelProperty(value = "毛发")
    private String wool;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "英文名")
    private String englishName;

    @ApiModelProperty(value = "高")
    private String height;

    @ApiModelProperty(value = "腹肌")
    private String abs;

    @ApiModelProperty(value = "图片")
    private String image;
}
