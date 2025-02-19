package com.tcs.service;

import com.tcs.dto.MovementDTO;
import com.tcs.dto.MovementReportDTO;
import com.tcs.model.MovementEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface IMovementService extends ICRUD<MovementEntity, Long> {

    void saveMovement(MovementDTO request);

    void deleteLogic(Long id);

    /**
     * Report movement by date and client id
     * @param clientId client id
     * @param startDate start date
     * @param endDate end date
     * @return
     * @throws Exception
     */
    List<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate) throws Exception;
}
