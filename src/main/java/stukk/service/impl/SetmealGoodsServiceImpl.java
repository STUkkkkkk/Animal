package stukk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import stukk.entity.SetmealGoods;
import stukk.mapper.SetmealGoodsMapper;
import stukk.service.SetmealGoodsService;

/**
 * @author wenli
 * @create 2022-12-06 15:38
 */
@Service("setmealGoodsService")
public class SetmealGoodsServiceImpl extends ServiceImpl<SetmealGoodsMapper, SetmealGoods> implements SetmealGoodsService {
}
