package stukk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import stukk.entity.AddressBook;
import stukk.mapper.AddressBookMapper;
import stukk.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author wenli
 * @create 2022-09-03 21:50
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
