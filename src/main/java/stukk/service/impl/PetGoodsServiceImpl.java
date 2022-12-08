package stukk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stukk.dto.PetGoodsDto;
import stukk.entity.PetGoods;
import stukk.entity.PetPrefer;
import stukk.mapper.PetGoodsMapper;
import stukk.service.PetGoodsService;
import stukk.service.PetPreferService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenli
 * @create 2022-12-06 15:34
 */
@Service("petGoodsService")
public class PetGoodsServiceImpl extends ServiceImpl<PetGoodsMapper, PetGoods> implements PetGoodsService {
    @Resource
    private PetPreferService petPreferService;

    @Override
    @Transactional
    public void saveWithPrefer(PetGoodsDto petGoodsDto) {
        // 保存物品的基本信息到 tb_pet_goods
        this.save(petGoodsDto);

        List<PetPrefer> prefers = petGoodsDto.getPrefers();
        prefers = prefers.stream().map((item) -> {
            item.setPetId(petGoodsDto.getId());
            return item;
        }).collect(Collectors.toList());

        petPreferService.saveBatch(prefers);
    }

    @Override
    public PetGoodsDto getByIdWithPrefer(Long petGoodsId) {
        PetGoods petGoods = this.getById(petGoodsId);
        PetGoodsDto petGoodsDto = new PetGoodsDto();
        BeanUtils.copyProperties(petGoods, petGoodsDto);

        LambdaQueryWrapper<PetPrefer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PetPrefer::getPetId, petGoodsId);
        List<PetPrefer> petGoodsPrefers = petPreferService.list(queryWrapper);

        petGoodsDto.setPrefers(petGoodsPrefers);
        return petGoodsDto;
    }

    @Override
    @Transactional
    public void updateWithPrefer(PetGoodsDto petGoodsDto) {
        this.updateById(petGoodsDto);

        LambdaQueryWrapper<PetPrefer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PetPrefer::getPetId, petGoodsDto.getId());
        petPreferService.remove(queryWrapper);

        List<PetPrefer> prefers = petGoodsDto.getPrefers();
        for (PetPrefer prefer : prefers) {
            prefer.setPetId(petGoodsDto.getId());
        }
        petPreferService.saveBatch(prefers);
    }
}
