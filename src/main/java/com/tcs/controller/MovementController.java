package com.tcs.controller;

import com.tcs.dto.MovementDTO;
import com.tcs.dto.MovementReportDTO;
import com.tcs.mappers.MovementMapper;
import com.tcs.service.IMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/movimientos")
@RequiredArgsConstructor
public class MovementController {

    private final IMovementService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<MovementDTO>>> findAll() {
        Flux<MovementDTO> movementFx = service.findAll()
                .map(MovementMapper.INSTANCE::toMovementDTO);

        return Mono.just(
                        ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(movementFx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovementDTO>> findById(@PathVariable("id") Long id) {
        return service.findById(id)
                .map(MovementMapper.INSTANCE::toMovementDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<MovementDTO>> save(@RequestBody @Valid MovementDTO response, ServerHttpRequest req) {
        return service.saveMovement(MovementMapper.INSTANCE.toMovement(response))
                .map(e -> {
                    URI location = URI.create(req.getURI().toString().concat("/").concat(e.getAccountId().toString()));
                    return ResponseEntity.created(location)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(MovementMapper.INSTANCE.toMovementDTO(e));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/reportes")
    public Mono<ResponseEntity<Flux<MovementReportDTO>>> reportMovementByDateAndClientId(@RequestParam(value = "clientId") String clientId,
                                                                                         @RequestParam(value = "startDate") LocalDateTime startDate,
                                                                                         @RequestParam(value = "endDate") LocalDateTime endDate,
                                                                                         ServerHttpRequest request) {
        String authToken = request.getHeaders().getFirst("Authorization");
        Flux<MovementReportDTO> movementReportVo = service.reportMovementByDateAndClientId(clientId, startDate, endDate, authToken);
        return Mono.just(
                        ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(movementReportVo))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MovementDTO>> update (@PathVariable("id") Long id, @Valid @RequestBody MovementDTO response) {
        return service.updateMovement(id, MovementMapper.INSTANCE.toMovement(response))
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(MovementMapper.INSTANCE.toMovementDTO(e))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") Long id) {
        return service.deleteById(id)
                .flatMap( result -> {
                    if (Boolean.TRUE.equals(result)){
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @DeleteMapping("/deletelogic/{id}")
    public Mono<ResponseEntity<Void>> deleteLogic(@PathVariable("id") Long id) {
        return service.deleteLogic(id)
                .flatMap( result -> {
                    if(Boolean.TRUE.equals(result)) {
                        return Mono.just(ResponseEntity.noContent().build());
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }
}
