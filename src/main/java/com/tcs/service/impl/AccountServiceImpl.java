package com.tcs.service.impl;

import com.tcs.model.Account;
import com.tcs.repository.IAccountRepository;
import com.tcs.repository.IGenericRepository;
import com.tcs.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends CRUDImpl<Account, Long> implements IAccountService {

    private final IAccountRepository repository;

    @Override
    protected IGenericRepository<Account, Long> getRepository() {
        return repository;
    }

    @Override
    public Flux<Account> findByPersonId(Long personId) {
        return repository.findByPersonId(personId);
    }

    @Override
    public Mono<Account> updateAccount(Long id, Account account) {
        return repository.findById(id)
                .flatMap( existingAccount -> {
                    existingAccount.setAccountNumber(account.getAccountNumber());
                    existingAccount.setAccountType(account.getAccountType());
                    existingAccount.setInitialBalance(account.getInitialBalance());
                    existingAccount.setStatus(account.getStatus());
                    existingAccount.setPersonId(account.getPersonId());
                    return repository.save(existingAccount);
                });
    }

    @Override
    public Mono<Boolean> deleteLogic(Long id) {
        return repository.findById(id)
                .flatMap( existingAccount -> {
                    existingAccount.setStatus(false);
                    return repository.save(existingAccount).thenReturn(true);
                }).defaultIfEmpty(false);
    }
}
