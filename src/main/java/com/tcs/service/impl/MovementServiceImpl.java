package com.tcs.service.impl;

import com.tcs.constants.Constants;
import com.tcs.dto.MovementReportDTO;
import com.tcs.exception.ModelNotFoundException;
import com.tcs.model.Account;
import com.tcs.model.Movement;
import com.tcs.repository.IGenericRepository;
import com.tcs.repository.IMovementRepository;
import com.tcs.service.IAccountService;
import com.tcs.service.IMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl extends CRUDImpl<Movement, Long> implements IMovementService {

    private final IMovementRepository repository;

    private final IAccountService accountService;

    @Override
    protected IGenericRepository<Movement, Long> getRepository() {
        return repository;
    }

    @Override
    public Mono<Movement> saveMovement(Movement movement) {

        return validateMovementValue(movement.getMovementValue())
                .then(validateAccount(movement.getAccountId()))
                .flatMap(account -> {
                    BigDecimal balance = account.getInitialBalance().add(movement.getMovementValue());
                    // Validar si hay saldo disponible
                    if (balance.compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new ModelNotFoundException("Saldo no disponible"));
                    }
                    // Actualizar detalles del movimiento y la cuenta
                    updateMovementDetails(movement, account, balance);
                    // Actualizar el saldo de la cuenta
                    return accountService.save(account)
                            .then(repository.save(movement)); // Guardar el movimiento
                });
    }

    @Override
    public Mono<Movement> updateMovement(Long id, Movement request) {
        return repository.findById(id)
                .flatMap( movement -> {
                    movement.setAccountId(request.getAccountId());
                    movement.setMovementDate(request.getMovementDate());
                    movement.setMovementType(request.getMovementType());
                    movement.setMovementValue(request.getMovementValue());
                    movement.setBalance(request.getBalance());
                    movement.setStatus(request.getStatus());
                    return repository.save(movement);
                });
    }

    @Override
    public Mono<Boolean> deleteLogic(Long id) {
        return repository.findById(id)
                .flatMap( movement -> {
                   movement.setStatus(false);
                   return repository.save(movement).thenReturn(true);
                }).defaultIfEmpty(false);
    }

    @Override
    public Flux<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        return Flux.empty();
//        List<Movement> movementVo = this.repository.reportMovement(clientId, getDateTransform(startDate), getDateTransform(endDate));
//        List<MovementReportDTO> reportVos = new ArrayList<>();
//        if (!movementVo.isEmpty()) {
//            reportVos.addAll(movementVo.stream().map( data -> MovementReportDTO.builder()
//                    .movementDate(data.getMovementDate())
//                    .name(data.getAccount().getClient().getName())
//                    .accountNumber(data.getAccount().getAccountNumber())
//                    .accountType(data.getAccount().getAccountType())
//                    .initialBalance(data.getBalance())
//                    .movementStatus(data.getStatus())
//                    .movementValue(data.getMovementValue())
//                    .balance(data.getAccount().getInitialBalance())
//                    .build()
//            ).toList());
//        }
//        return reportVos;
    }
//
//    private Date getDateTransform ( LocalDateTime localDateTime ){
//        // Convertir a ZonedDateTime
//        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
//        // Convertir a Instant
//        Instant instant = zonedDateTime.toInstant();
//        // Crear Date
//        return Date.from(instant);
//    }

    private Mono<Account> validateAccount(Long accountId) {
        return accountService.findById(accountId)
                .switchIfEmpty(Mono.error(new ModelNotFoundException("Cuenta no encontrada")));
    }

    private Mono<Void> validateMovementValue(BigDecimal movementValue) {
        if (movementValue.compareTo(BigDecimal.ZERO) == 0) {
            return Mono.error(new ModelNotFoundException("Movimiento no válido"));
        }
        return Mono.empty();
    }

    private String determineMovementType(BigDecimal movementValue) {
        return movementValue.compareTo(BigDecimal.ZERO) > 0
                ? Constants.DEPOSIT
                : Constants.WITHDRAWAL;
    }

    private void updateMovementDetails(Movement movement, Account account, BigDecimal newBalance) {
        movement.setMovementType(determineMovementType(movement.getMovementValue()));
        movement.setAccountId(account.getAccountId());
        movement.setMovementDate(LocalDateTime.now());
        movement.setBalance(account.getInitialBalance());
        movement.setStatus(Boolean.TRUE);
        account.setInitialBalance(newBalance);
    }
}
