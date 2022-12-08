package stukk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import stukk.entity.User;
import stukk.mapper.UserMapper;
import stukk.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author wenli
 * @create 2022-09-03 17:40
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
