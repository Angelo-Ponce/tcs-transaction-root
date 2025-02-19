package com.tcs.service.impl;

import com.tcs.dto.MovementDTO;
import com.tcs.dto.MovementReportDTO;
import com.tcs.exception.ModelNotFoundException;
import com.tcs.model.MovementEntity;
import com.tcs.repository.IGenericRepository;
import com.tcs.repository.IMovementRepository;
import com.tcs.service.IMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl extends CRUDImpl<MovementEntity, Long> implements IMovementService {

    private final IMovementRepository repository;

    @Override
    protected IGenericRepository<MovementEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public void saveMovement(MovementDTO request) {

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
        return List.of();
    }
}
