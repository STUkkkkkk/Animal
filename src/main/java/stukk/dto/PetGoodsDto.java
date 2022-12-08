package stukk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import stukk.entity.PetGoods;
import stukk.entity.PetPrefer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenli
 * @create 2022-12-06 23:21
 */
@Data
@ApiModel(value = "宠物用品辅助实体（PetGoodsDto）")
public class PetGoodsDto extends PetGoods {
    @ApiModelProperty(value = "用品配置")
    private List<PetPrefer> prefers = new ArrayList<>();

    @ApiModelProperty(value = "分类名称")
    private String typeName;

    private Integer copies;
}
