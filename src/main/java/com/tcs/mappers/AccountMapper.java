package com.tcs.mappers;

import com.tcs.dto.AccountDTO;
import com.tcs.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDTO toAccountDTO(Account account);

    Account toAccount(AccountDTO accountDTO);
}
