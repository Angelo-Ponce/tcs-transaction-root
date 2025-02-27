package com.tcs.service.impl;

import com.tcs.repository.IMovementRepository;
import com.tcs.service.IAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private IMovementRepository movementRepository;

    @Mock
    private IAccountService accountService;

    @InjectMocks
    private MovementServiceImpl movementService;

    @Test
    void givenGetRepository_WhenCalled_ThenReturnCorrectRepositoryInstance() {
        assertEquals(movementRepository, movementService.getRepository());
    }

//    @Test
//    void givenSaveMove_WhenMoveIsValid_ThenSaveSuccessfully() {
//        MovementDTO movementDTO = new MovementDTO();
//        movementDTO.setAccountId(1L);
//        movementDTO.setMovementValue(BigDecimal.valueOf(500));
//
//        Account account = new Account();
//        account.setAccountId(1L);
//        account.setInitialBalance(BigDecimal.valueOf(1000));
//
//        when(accountService.findById(1L, "Account")).thenReturn(account);
//        when(accountService.save(any(Account.class))).thenReturn(account);
//        when(movementRepository.save(any(Movement.class))).thenReturn(new Movement());
//
//        movementService.saveMovement(movementDTO);
//
//        assertEquals(account.getInitialBalance(), BigDecimal.valueOf(1500));
//        verify(accountService, times(1)).findById(1L, "Account");
//        verify(accountService, times(1)).save(account);
//        verify(movementRepository, times(1)).save(any(Movement.class));
//    }
//
//    @Test
//    void givenSaveMovement_WhenTheAmountExceeds_ThenThrowExceptionForInvalidMovement() {
//        MovementDTO movementDTO = new MovementDTO();
//        movementDTO.setAccountId(1L);
//        movementDTO.setMovementValue(BigDecimal.valueOf(-2000)); // Exceeds balance
//
//        Account account = new Account();
//        account.setAccountId(1L);
//        account.setInitialBalance(BigDecimal.valueOf(1000));
//
//        when(accountService.findById(1L, "Account")).thenReturn(account);
//
//        assertThrows(ModelNotFoundException.class, () -> movementService.saveMovement(movementDTO));
//
//        verify(accountService, times(1)).findById(1L, "Account");
//        verify(accountService, times(0)).save(any(Account.class));
//        verify(movementRepository, times(0)).save(any(Movement.class));
//    }
//
//
//    @Test
//    void givenSaveMove_WhenMoveIsNegative_ThenSaveSuccessfully() {
//        MovementDTO movementDTO = new MovementDTO();
//        movementDTO.setAccountId(1L);
//        movementDTO.setMovementValue(BigDecimal.valueOf(-500)); // Negative movement
//
//        Account account = new Account();
//        account.setAccountId(1L);
//        account.setInitialBalance(BigDecimal.valueOf(1000));
//
//        when(accountService.findById(1L, "Account")).thenReturn(account);
//        when(accountService.save(any(Account.class))).thenReturn(account);
//        when(movementRepository.save(any(Movement.class))).thenReturn(new Movement());
//
//        movementService.saveMovement(movementDTO);
//
//        verify(accountService, times(1)).findById(1L, "Account");
//        verify(accountService, times(1)).save(any(Account.class));
//        verify(movementRepository, times(1)).save(any(Movement.class));
//    }
//
//    @Test
//    void givenDeleteLogic_WhenMovementExists_ThenMarkMovementAsInactive() {
//        Long id = 1L;
//        Movement movement = new Movement();
//        movement.setMovementId(id);
//        movement.setStatus(Boolean.TRUE);
//
//        when(movementRepository.findById(id)).thenReturn(Optional.of(movement));
//        when(movementRepository.save(movement)).thenReturn(movement);
//
//        movementService.deleteLogic(id);
//
//        verify(movementRepository, times(1)).findById(id);
//        verify(movementRepository, times(1)).save(movement);
//        assertEquals(Boolean.FALSE, movement.getStatus());
//        assertNotNull(movement.getLastModifiedDate());
//    }
//
//    @Test
//    void givenDeleteLogic_WhenMovementDoesNotExist_ThenThrowModelNotFoundException() {
//        Long id = 1L;
//
//        when(movementRepository.findById(id)).thenReturn(Optional.empty());
//
//        assertThrows(ModelNotFoundException.class, () -> movementService.deleteLogic(id));
//        verify(movementRepository, times(1)).findById(id);
//        verify(movementRepository, times(0)).save(any(Movement.class));
//    }
}