package com.tcs.service.impl;

import com.tcs.model.Account;
import com.tcs.repository.IAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private IAccountRepository mockRepository;

    @InjectMocks
    private AccountServiceImpl service;

    @Test
    void givenGetRepository_WhenCalled_ThenReturnCorrectRepositoryInstance() {
        assertEquals(mockRepository, service.getRepository());
    }

    @Test
    void givenUpdateEntity_WhenEntityExists_ThenReturnUpdatedEntity(){

        Account mockAccount = Account.builder()
                .accountId(1L)
                .accountNumber("654321")
                .accountType("AHORRO")
                .initialBalance(BigDecimal.valueOf(20))
                .status(true)
                .personId(12L)
                .build();

        Account update = Account.builder()
                .accountId(1L)
                .accountNumber("12345")
                .accountType("AHORRO")
                .initialBalance(BigDecimal.valueOf(20))
                .status(true)
                .personId(12L)
                .build();

        when(mockRepository.findById(mockAccount.getAccountId())).thenReturn(Mono.just(mockAccount));
        when(mockRepository.save(mockAccount)).thenReturn(Mono.just(mockAccount));

        Mono<Account> result = service.updateAccount(mockAccount.getAccountId(), update);
        StepVerifier.create(result)
                .expectNextMatches(savedAccount ->
                        savedAccount.getAccountNumber().equals(update.getAccountNumber()) &&
                                savedAccount.getInitialBalance().equals(update.getInitialBalance()))
                .verifyComplete();

        verify(mockRepository, times(1)).findById(mockAccount.getAccountId());
        verify(mockRepository, times(1)).save(mockAccount);
    }

    @Test
    void givenDeleteLogic_WhenAccountExists_ThenMarkAccountAsInactive() {
        Long id = 1L;
        Account account = new Account();
        account.setAccountId(id);
        account.setStatus(Boolean.TRUE);
        when(mockRepository.findById(account.getAccountId())).thenReturn(Mono.just(account));
        when(mockRepository.save(account)).thenReturn(Mono.just(account));

        Mono<Boolean> result = service.deleteLogic(account.getAccountId());

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(mockRepository, times(1)).findById(account.getAccountId());
        verify(mockRepository, times(1)).save(account);
    }

    @Test
    void givenNonExistentIdAccount_whenDeleteLogic_thenReturnFalse() {
        Long id = 1L;
        when(mockRepository.findById(id)).thenReturn(Mono.empty());

        Mono<Boolean> result = service.deleteLogic(id);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(mockRepository, times(1)).findById(id);
        verify(mockRepository, never()).save(any());
    }
}