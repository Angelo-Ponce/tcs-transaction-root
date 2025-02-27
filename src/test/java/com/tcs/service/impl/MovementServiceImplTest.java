package com.tcs.service.impl;

import com.tcs.constants.Constants;
import com.tcs.exception.ModelNotFoundException;
import com.tcs.model.Account;
import com.tcs.model.Movement;
import com.tcs.repository.IMovementRepository;
import com.tcs.service.IAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private IMovementRepository mockRepository;

    @Mock
    private IAccountService accountService;

    @InjectMocks
    private MovementServiceImpl movementService;

    @Test
    void givenGetRepository_WhenCalled_ThenReturnCorrectRepositoryInstance() {
        assertEquals(mockRepository, movementService.getRepository());
    }

    @Test
    void givenSaveMove_WhenMoveIsValid_ThenSaveSuccessfully() {
        Movement movement = new Movement();
        movement.setAccountId(1L);
        movement.setMovementValue(BigDecimal.valueOf(500));

        Account account = new Account();
        account.setAccountId(movement.getAccountId());
        account.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(movement.getAccountId())).thenReturn(Mono.just(account));
        when(accountService.save(account)).thenReturn(Mono.just(account));
        when(mockRepository.save(movement)).thenReturn(Mono.just(movement));

        Mono<Movement> result = movementService.saveMovement(movement);
        StepVerifier.create(result)
                .expectNextMatches(savedMovement ->
                        savedMovement.getMovementType().equals(Constants.DEPOSIT) &&
                                savedMovement.getBalance().equals(new BigDecimal("1000")) &&
                                savedMovement.getStatus().equals(Boolean.TRUE))
                .verifyComplete();

        verify(accountService, times(1)).findById(1L);
        verify(accountService, times(1)).save(account);
        verify(mockRepository, times(1)).save(movement);
    }

    @Test
    void givenSaveMove_WhenMoveIsNegative_ThenSaveSuccessfully() {
        Movement movement = new Movement();
        movement.setAccountId(1L);
        movement.setMovementValue(BigDecimal.valueOf(-500));

        Account account = new Account();
        account.setAccountId(movement.getAccountId());
        account.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(movement.getAccountId())).thenReturn(Mono.just(account));
        when(accountService.save(account)).thenReturn(Mono.just(account));
        when(mockRepository.save(movement)).thenReturn(Mono.just(movement));

        Mono<Movement> result = movementService.saveMovement(movement);
        StepVerifier.create(result)
                .expectNextMatches(savedMovement ->
                        savedMovement.getMovementType().equals(Constants.WITHDRAWAL) &&
                                savedMovement.getBalance().equals(new BigDecimal("1000")) &&
                                savedMovement.getStatus().equals(Boolean.TRUE))
                .verifyComplete();

        verify(accountService, times(1)).findById(1L);
        verify(accountService, times(1)).save(account);
        verify(mockRepository, times(1)).save(movement);
    }

    @Test
    void givenSaveMovement_WhenTheAmountExceeds_ThenThrowExceptionForInvalidMovement() {
        Movement movement = new Movement();
        movement.setAccountId(1L);
        movement.setMovementValue(new BigDecimal("-600.00"));

        Account account = new Account();
        account.setAccountId(1L);
        account.setInitialBalance(new BigDecimal("500.00"));

        when(accountService.findById(1L)).thenReturn(Mono.just(account));

        Mono<Movement> result = movementService.saveMovement(movement);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ModelNotFoundException &&
                                throwable.getMessage().equals("400 BAD_REQUEST \"Saldo no disponible\""))
                .verify();

        verify(accountService, times(1)).findById(1L);
        verify(accountService, never()).save(any());
        verify(mockRepository, never()).save(any());
    }

    @Test
    void givenMovementWithZeroValue_whenSaveMovement_thenThrowModelNotFoundException() {
        Movement movement = new Movement();
        movement.setAccountId(1L);
        movement.setMovementValue(BigDecimal.ZERO);

        when(accountService.findById(1L)).thenReturn(Mono.empty());

        Mono<Movement> result = movementService.saveMovement(movement);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof ModelNotFoundException &&
                                throwable.getMessage().contains("Movimiento no"))
                .verify();

        verify(accountService, never()).save(any());
        verify(mockRepository, never()).save(any());
    }

    @Test
    void givenUpdateEntity_WhenEntityExists_ThenReturnUpdatedEntity(){

        Movement mockMovement = Movement.builder()
                .accountId(1L)
                .movementDate(LocalDateTime.now())
                .movementType("DEPOSITO")
                .movementValue(BigDecimal.valueOf(20))
                .balance(BigDecimal.valueOf(20))
                .status(true)
                .build();

        Movement update = Movement.builder()
                .accountId(1L)
                .movementDate(LocalDateTime.now())
                .movementType("DEPOSITO")
                .movementValue(BigDecimal.valueOf(20))
                .balance(BigDecimal.valueOf(40))
                .status(true)
                .build();

        when(mockRepository.findById(mockMovement.getMovementId())).thenReturn(Mono.just(mockMovement));
        when(mockRepository.save(mockMovement)).thenReturn(Mono.just(mockMovement));

        Mono<Movement> result = movementService.updateMovement(mockMovement.getMovementId(), update);
        StepVerifier.create(result)
                .expectNextMatches(savedAccount ->
                        savedAccount.getMovementType().equals(update.getMovementType()) &&
                                savedAccount.getBalance().equals(update.getBalance()))
                .verifyComplete();

        verify(mockRepository, times(1)).findById(mockMovement.getMovementId());
        verify(mockRepository, times(1)).save(mockMovement);
    }

    @Test
    void givenDeleteLogic_WhenAccountExists_ThenMarkAccountAsInactive() {
        Long id = 1L;
        Movement movement = new Movement();
        movement.setMovementId(id);
        movement.setStatus(Boolean.TRUE);
        when(mockRepository.findById(movement.getMovementId())).thenReturn(Mono.just(movement));
        when(mockRepository.save(movement)).thenReturn(Mono.just(movement));

        Mono<Boolean> result = movementService.deleteLogic(movement.getMovementId());

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(mockRepository, times(1)).findById(movement.getMovementId());
        verify(mockRepository, times(1)).save(movement);
    }

    @Test
    void givenNonExistentIdAccount_whenDeleteLogic_thenReturnFalse() {
        Long id = 1L;
        when(mockRepository.findById(id)).thenReturn(Mono.empty());

        Mono<Boolean> result = movementService.deleteLogic(id);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(mockRepository, times(1)).findById(id);
        verify(mockRepository, never()).save(any());
    }
}