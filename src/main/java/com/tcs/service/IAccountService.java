package com.tcs.service;

import com.tcs.model.Account;
import reactor.core.publisher.Mono;

public interface IAccountService extends ICRUDService<Account, Long> {

    Mono<Account> updateAccount(Long id, Account account);
    Mono<Boolean> deleteLogic(Long id);
}
