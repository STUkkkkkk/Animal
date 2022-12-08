package stukk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import stukk.entity.Dog;
import stukk.mapper.DogMapper;
import stukk.service.DogService;

@Service
public class DogServiceImpl extends ServiceImpl<DogMapper, Dog> implements DogService {
}
