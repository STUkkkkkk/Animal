package stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import stukk.dto.PetDto;
import stukk.entity.Pet;

/**
 * @author wenli
 * @create 2022-12-06 15:31
 */
public interface PetService extends IService<Pet> {
    // 新增宠物，同时插入宠物对应的偏好数据，需要操作两张表：tb_pet、tb_pet_prefer
    void saveWithPrefer(PetDto petDto);

    // 根据id来查询宠物信息和偏好信息
    PetDto getByIdWithPrefer(Long petId);

    // 根据id来修改宠物基本信息和偏好信息
    void updateWithPrefer(PetDto petDto);
}
