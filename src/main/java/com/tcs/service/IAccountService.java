package com.tcs.service;

import com.tcs.model.AccountEntity;

public interface IAccountService extends ICRUD<AccountEntity, Long> {
    void deleteLogic(Long id);
}
