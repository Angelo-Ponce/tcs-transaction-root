package com.tcs.repository;

import com.tcs.model.Account;
import reactor.core.publisher.Mono;

public interface IAccountRepository extends IGenericRepository<Account, Long> {

    Mono<Account> findByAccountNumber(String accountNumber);
}
