package stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import stukk.dto.PetGoodsDto;
import stukk.entity.PetGoods;

/**
 * @author wenli
 * @create 2022-12-06 15:30
 */
public interface PetGoodsService extends IService<PetGoods> {
    // 新增物品，同时插入物品对应的偏好数据，需要操作两张表：tb_pet_goods、tb_pet_prefer
    void saveWithPrefer(PetGoodsDto petGoodsDto);

    // 根据id来查询物品信息和偏好信息
    PetGoodsDto getByIdWithPrefer(Long petGoodsId);

    // 根据id来修改物品基本信息和偏好信息
    void updateWithPrefer(PetGoodsDto petGoodsDto);
}
