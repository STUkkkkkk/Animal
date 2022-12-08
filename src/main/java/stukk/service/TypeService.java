package stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import stukk.entity.Type;

/**
 * @author wenli
 * @create 2022-09-01 16:13
 */
public interface TypeService extends IService<Type> {
    void remove(Long id);
}
