package stukk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import stukk.entity.Cat;
import stukk.mapper.CatMapper;
import stukk.service.CatService;

@Service
public class CatServiceImpl extends ServiceImpl<CatMapper, Cat> implements CatService {
}
