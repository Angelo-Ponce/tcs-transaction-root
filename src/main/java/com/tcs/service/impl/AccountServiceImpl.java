package com.tcs.service.impl;

import com.tcs.exception.ModelNotFoundException;
import com.tcs.model.AccountEntity;
import com.tcs.repository.IAccountRepository;
import com.tcs.repository.IGenericRepository;
import com.tcs.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends CRUDImpl<AccountEntity, Long> implements IAccountService {

    private final IAccountRepository repository;

    @Override
    protected IGenericRepository<AccountEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public void deleteLogic(Long id) {
        AccountEntity account = repository.findById(id).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
        account.setStatus(Boolean.FALSE);
        account.setLastModifiedDate(new Date());
        // TODO: IMPLEMENTAR USUARIO
        account.setLastModifiedByUser("Angelo");
        repository.save(account);
    }
}
