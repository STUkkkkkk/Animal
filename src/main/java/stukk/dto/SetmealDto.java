package stukk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import stukk.entity.Setmeal;
import stukk.entity.SetmealGoods;

import java.util.List;

@Data
@ApiModel(value = "套餐辅助实体（SetmealDto）")
public class SetmealDto extends Setmeal {

    @ApiModelProperty(value = "套餐物品关系类")
    private List<SetmealGoods> setmealGoods;

    @ApiModelProperty(value = "分类名称")
    private String typeName;
}
