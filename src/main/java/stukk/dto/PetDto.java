package stukk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import stukk.entity.Pet;
import stukk.entity.PetPrefer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenli
 * @create 2022-12-06 23:21
 */
@Data
@ApiModel(value = "宠物信息辅助实体（PetDto）")
public class PetDto extends Pet {
    @ApiModelProperty(value = "宠物配置信息")
    private List<PetPrefer> prefers = new ArrayList<>();

    @ApiModelProperty(value = "分类名称")
    private String typeName;

    private Integer copies;
}
