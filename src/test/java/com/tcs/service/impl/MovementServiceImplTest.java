package com.tcs.service.impl;

import com.tcs.dto.MovementDTO;
import com.tcs.exception.ModelNotFoundException;
import com.tcs.model.AccountEntity;
import com.tcs.model.MovementEntity;
import com.tcs.repository.IMovementRepository;
import com.tcs.service.IAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void givenSaveMove_WhenMoveIsValid_ThenSaveSuccessfully() {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setAccountId(1L);
        movementDTO.setMovementValue(BigDecimal.valueOf(500));

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1L);
        accountEntity.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(1L, "Account")).thenReturn(accountEntity);
        when(accountService.save(any(AccountEntity.class))).thenReturn(accountEntity);
        when(movementRepository.save(any(MovementEntity.class))).thenReturn(new MovementEntity());

        movementService.saveMovement(movementDTO);

        assertEquals(accountEntity.getInitialBalance(), BigDecimal.valueOf(1500));
        verify(accountService, times(1)).findById(1L, "Account");
        verify(accountService, times(1)).save(accountEntity);
        verify(movementRepository, times(1)).save(any(MovementEntity.class));
    }

    @Test
    void givenSaveMovement_WhenTheAmountExceeds_ThenThrowExceptionForInvalidMovement() {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setAccountId(1L);
        movementDTO.setMovementValue(BigDecimal.valueOf(-2000)); // Exceeds balance

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1L);
        accountEntity.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(1L, "Account")).thenReturn(accountEntity);

        assertThrows(ModelNotFoundException.class, () -> movementService.saveMovement(movementDTO));

        verify(accountService, times(1)).findById(1L, "Account");
        verify(accountService, times(0)).save(any(AccountEntity.class));
        verify(movementRepository, times(0)).save(any(MovementEntity.class));
    }

//    @Test
//    void testSaveMovement_ShouldSetMovementTypeAsDeposit() {
//        MovementDTO movementDTO = new MovementDTO();
//        movementDTO.setAccountId(1L);
//        movementDTO.setMovementValue(BigDecimal.valueOf(500)); // Positive movement
//
//        AccountEntity accountEntity = new AccountEntity();
//        accountEntity.setAccountId(1L);
//        accountEntity.setInitialBalance(BigDecimal.valueOf(1000));
//
//        when(accountService.findById(1L, "Account")).thenReturn(accountEntity);
//        when(accountService.save(any(AccountEntity.class))).thenReturn(accountEntity);
//        when(movementRepository.save(any(MovementEntity.class))).thenReturn(new MovementEntity());
//
//        movementService.saveMovement(movementDTO);
//
//        verify(accountService, times(1)).findById(1L, "Account");
//        verify(accountService, times(1)).save(any(AccountEntity.class));
//        verify(movementRepository, times(1)).save(any(MovementEntity.class));
//    }

    @Test
    void givenSaveMove_WhenMoveIsNegative_ThenSaveSuccessfully() {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setAccountId(1L);
        movementDTO.setMovementValue(BigDecimal.valueOf(-500)); // Negative movement

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1L);
        accountEntity.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.findById(1L, "Account")).thenReturn(accountEntity);
        when(accountService.save(any(AccountEntity.class))).thenReturn(accountEntity);
        when(movementRepository.save(any(MovementEntity.class))).thenReturn(new MovementEntity());

        movementService.saveMovement(movementDTO);

        verify(accountService, times(1)).findById(1L, "Account");
        verify(accountService, times(1)).save(any(AccountEntity.class));
        verify(movementRepository, times(1)).save(any(MovementEntity.class));
    }

    @Test
    void givenDeleteLogic_WhenMovementExists_ThenMarkMovementAsInactive() {
        Long id = 1L;
        MovementEntity movement = new MovementEntity();
        movement.setMovementId(id);
        movement.setStatus(Boolean.TRUE);

        when(movementRepository.findById(id)).thenReturn(Optional.of(movement));
        when(movementRepository.save(movement)).thenReturn(movement);

        movementService.deleteLogic(id);

        verify(movementRepository, times(1)).findById(id);
        verify(movementRepository, times(1)).save(movement);
        assertEquals(Boolean.FALSE, movement.getStatus());
        assertNotNull(movement.getLastModifiedDate());
    }

    @Test
    void givenDeleteLogic_WhenMovementDoesNotExist_ThenThrowModelNotFoundException() {
        Long id = 1L;

        when(movementRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ModelNotFoundException.class, () -> movementService.deleteLogic(id));
        verify(movementRepository, times(1)).findById(id);
        verify(movementRepository, times(0)).save(any(MovementEntity.class));
    }
}