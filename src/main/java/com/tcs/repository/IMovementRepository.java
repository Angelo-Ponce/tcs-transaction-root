package com.tcs.repository;

import com.tcs.model.Movement;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface IMovementRepository extends IGenericRepository<Movement, Long>{

    Flux<Movement> findByAccountIdAndMovementDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);

}
