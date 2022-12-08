package stukk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import stukk.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wenli
 * @create 2022-09-03 21:48
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
