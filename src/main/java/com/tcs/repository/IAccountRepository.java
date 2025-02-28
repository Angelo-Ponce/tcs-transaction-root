package com.tcs.repository;

import com.tcs.model.Account;
import reactor.core.publisher.Flux;

public interface IAccountRepository extends IGenericRepository<Account, Long> {

    Flux<Account> findByPersonId(Long personId);
}
