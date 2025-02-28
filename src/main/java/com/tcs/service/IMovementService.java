package com.tcs.service;

import com.tcs.dto.MovementReportDTO;
import com.tcs.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface IMovementService extends ICRUDService<Movement, Long> {

    Mono<Movement> saveMovement(Movement request);

    Mono<Movement> updateMovement(Long id, Movement request);

    Mono<Boolean> deleteLogic(Long id);

    /**
     * Report movement by date and client id
     * @param clientId client id
     * @param startDate start date
     * @param endDate end date
     */
    Flux<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate, String authToken);
}
