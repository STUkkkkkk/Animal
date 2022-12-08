package stukk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stukk.dto.PetDto;
import stukk.entity.Pet;
import stukk.entity.PetPrefer;
import stukk.mapper.PetMapper;
import stukk.service.PetPreferService;
import stukk.service.PetService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenli
 * @create 2022-12-06 15:37
 */
@Service("petService")
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {
    @Resource
    private PetPreferService petPreferService;

    @Override
    @Transactional
    public void saveWithPrefer(PetDto petDto) {
        // 保存宠物基本信息到 tb_pet 表
        this.save(petDto);

        // 获取宠物 id
        Long id = petDto.getId();

        // 获取宠物偏好信息
        List<PetPrefer> prefers = petDto.getPrefers();
        prefers = prefers.stream().map((item) -> {
            item.setPetId(id);
            return item;
        }).collect(Collectors.toList());

        // 保存宠物偏好信息到 tb_pet_prefer 表
        petPreferService.saveBatch(prefers);
    }

    @Override
    public PetDto getByIdWithPrefer(Long petId) {
        // 获取宠物的基本信息
        Pet pet = this.getById(petId);

        // 获取宠物偏好信息
        LambdaQueryWrapper<PetPrefer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PetPrefer::getPetId, petId);
        List<PetPrefer> prefers = petPreferService.list(queryWrapper);

        PetDto petDto = new PetDto();
        BeanUtils.copyProperties(pet, petDto);
        petDto.setPrefers(prefers);

        return petDto;
    }

    @Override
    @Transactional
    public void updateWithPrefer(PetDto petDto) {
        // 更新宠物基本信息到 tb_pet
        this.updateById(petDto);

        // 清理当前 tb_pet_prefer 表中宠物的偏好信息
        LambdaQueryWrapper<PetPrefer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PetPrefer::getPetId, petDto.getId());
        petPreferService.remove(queryWrapper);

        // 更新宠物偏好信息
        List<PetPrefer> prefers = petDto.getPrefers();
        for (PetPrefer prefer : prefers) {
            prefer.setPetId(petDto.getId());
        }
        petPreferService.saveBatch(prefers);
    }
}
