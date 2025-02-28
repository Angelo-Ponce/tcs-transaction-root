package com.tcs.service;

import com.tcs.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService extends ICRUDService<Account, Long> {

    Flux<Account> findByPersonId(Long personId);
    Mono<Account> updateAccount(Long id, Account account);
    Mono<Boolean> deleteLogic(Long id);
}
