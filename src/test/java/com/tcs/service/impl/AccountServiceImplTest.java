package com.tcs.service.impl;

import com.tcs.exception.ModelNotFoundException;
import com.tcs.model.AccountEntity;
import com.tcs.repository.IAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private IAccountRepository repository;

    @InjectMocks
    private AccountServiceImpl service;

    @Test
    void givenGetRepository_WhenCalled_ThenReturnCorrectRepositoryInstance() {
        assertEquals(repository, service.getRepository());
    }

    @Test
    void givenDeleteLogic_WhenAccountExists_ThenMarkAccountAsInactive() {
        Long id = 1L;
        AccountEntity account = new AccountEntity();
        account.setAccountId(id);
        account.setStatus(Boolean.TRUE);

        when(repository.findById(id)).thenReturn(Optional.of(account));
        when(repository.save(account)).thenReturn(account);

        service.deleteLogic(id);

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(account);
        assertEquals(Boolean.FALSE, account.getStatus());
        assertNotNull(account.getLastModifiedDate());
    }

    @Test
    void givenDeleteLogic_WhenAccountDoesNotExist_ThenThrowModelNotFoundException() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ModelNotFoundException.class, () -> service.deleteLogic(id));
        verify(repository, times(1)).findById(id);
        verify(repository, times(0)).save(any(AccountEntity.class));
    }
}