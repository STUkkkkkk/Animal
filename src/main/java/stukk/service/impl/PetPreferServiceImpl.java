package stukk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import stukk.entity.PetPrefer;
import stukk.mapper.PetPreferMapper;
import stukk.service.PetPreferService;

/**
 * @author wenli
 * @create 2022-12-06 15:36
 */
@Service("petPreferService")
public class PetPreferServiceImpl extends ServiceImpl<PetPreferMapper, PetPrefer> implements PetPreferService {
}
