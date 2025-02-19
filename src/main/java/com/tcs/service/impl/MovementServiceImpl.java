package com.tcs.service.impl;

import com.tcs.dto.MovementDTO;
import com.tcs.dto.MovementReportDTO;
import com.tcs.exception.ModelNotFoundException;
import com.tcs.mappers.MovementMapper;
import com.tcs.model.AccountEntity;
import com.tcs.model.MovementEntity;
import com.tcs.repository.IGenericRepository;
import com.tcs.repository.IMovementRepository;
import com.tcs.service.IAccountService;
import com.tcs.service.IMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl extends CRUDImpl<MovementEntity, Long> implements IMovementService {

    private final IMovementRepository repository;

    private final IAccountService accountService;

    @Override
    protected IGenericRepository<MovementEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public void saveMovement(MovementDTO request) {
        // Obtener cuenta
        AccountEntity accountEntity = this.accountService.findById(request.getAccountId(), "Account");
        BigDecimal balance = accountEntity.getInitialBalance().add(request.getMovementValue());

        validMovement(request, balance);

        request.setAccountId(accountEntity.getAccountId());
        request.setMovementDate(new Date());
        request.setBalance(accountEntity.getInitialBalance());
        request.setStatus(Boolean.TRUE);
        save(request);

        // Actualizar el saldo inicial
        accountEntity.setInitialBalance(balance);
        accountEntity.setLastModifiedDate(new Date());
        accountEntity.setLastModifiedByUser("Angelo");
        this.accountService.save(accountEntity);
    }

    @Override
    public void deleteLogic(Long id) {
        MovementEntity entity = repository.findById(id).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
        entity.setStatus(Boolean.FALSE);
        entity.setLastModifiedDate(new Date());
        // TODO: IMPLEMENTAR USUARIO
        entity.setLastModifiedByUser("Angelo");
        repository.save(entity);
    }

    @Override
    public List<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        List<MovementEntity> movementVo = this.repository.reportMovement(clientId, getDateTransform(startDate), getDateTransform(endDate));
        List<MovementReportDTO> reportVos = new ArrayList<>();
        if (!movementVo.isEmpty()) {
            reportVos.addAll(movementVo.stream().map( data -> MovementReportDTO.builder()
                    .movementDate(data.getMovementDate())
                    .name(data.getAccount().getClient().getName())
                    .accountNumber(data.getAccount().getAccountNumber())
                    .accountType(data.getAccount().getAccountType())
                    .initialBalance(data.getBalance())
                    .movementStatus(data.getStatus())
                    .movementValue(data.getMovementValue())
                    .balance(data.getAccount().getInitialBalance())
                    .build()
            ).toList()) ;

//            movementVo.forEach( data -> {
//                MovementReportDTO movement = MovementReportDTO.builder()
//                        .movementDate(data.getMovementDate())
//                        .name(data.getAccount().getClient().getName())
//                        .accountNumber(data.getAccount().getAccountNumber())
//                        .accountType(data.getAccount().getAccountType())
//                        .initialBalance(data.getAccount().getInitialBalance())
//                        .movementStatus(data.getStatus())
//                        .movementValue(data.getMovementValue())
//                        .balance(data.getBalance())
//                        .build();
//                reportVos.add(movement);
//            });
        }
        return reportVos;
    }

    private void validMovement(MovementDTO request, BigDecimal balance){

        if(balance.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new ModelNotFoundException("Saldo no disponible");
        }

        if (request.getMovementValue().compareTo(BigDecimal.ZERO) > 0){
            request.setMovementType("DEPOSITO");
        } else if(request.getMovementValue().compareTo(BigDecimal.ZERO) < 0){
            request.setMovementType("RETIRO");
        } else {
            throw new ModelNotFoundException("Movimiento no valido");
        }
    }

    private void save(MovementDTO movementDTO) {
        MovementEntity movementEntity = MovementMapper.INSTANCE.toMovement(movementDTO);
        movementEntity.setCreatedDate(new Date());
        movementEntity.setCreatedByUser("Angelo");
        repository.save(movementEntity);
        movementDTO.setMovementId(movementEntity.getMovementId());
    }

    private Date getDateTransform ( LocalDateTime localDateTime ){
        // Convertir a ZonedDateTime
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        // Convertir a Instant
        Instant instant = zonedDateTime.toInstant();
        // Crear Date
        return Date.from(instant);
    }
}
