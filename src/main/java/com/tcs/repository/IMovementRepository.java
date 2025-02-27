package com.tcs.repository;

import com.tcs.model.Movement;
import reactor.core.publisher.Flux;

public interface IMovementRepository extends IGenericRepository<Movement, Long>{

    /*@Query("select m FROM movement m where m.account.client.clientId = :clientId and m.movementDate between :startDate and :endDate")
    List<Movement> reportMovement(@Param("clientId") String clientId,
                                  @Param("startDate") Date startDate,
                                  @Param("endDate") Date endDate);*/
    Flux<Movement> findByAccountId(Long accountId);
}
