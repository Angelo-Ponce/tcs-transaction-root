package com.tcs.controller;

import com.tcs.dto.MovementDTO;
import com.tcs.dto.MovementReportDTO;
import com.tcs.dto.response.BaseResponse;
import com.tcs.mappers.MovementMapper;
import com.tcs.model.MovementEntity;
import com.tcs.service.IMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/movimientos")
@RequiredArgsConstructor
public class MovementController {

    private final IMovementService service;

    @GetMapping
    public ResponseEntity<BaseResponse> findAll(){
        List<MovementDTO> list = service.findAll().stream()
                .map(MovementMapper.INSTANCE::toMovementDTO).toList();
        return ResponseEntity.ok(BaseResponse.builder().data(list).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getMovementById(@PathVariable("id") Long id) {
        MovementEntity entity = service.findById(id, "Movements");

        return ResponseEntity.ok(BaseResponse.builder()
                .data(MovementMapper.INSTANCE.toMovementDTO(entity))
                .build());
    }

    @GetMapping("/reportes")
    public ResponseEntity<BaseResponse> reportMovementByDateAndClientId(@RequestParam(value = "clientId") String clientId,
                                                                        @RequestParam(value = "startDate") String startDate,
                                                                        @RequestParam(value = "endDate") String endDate) throws Exception {
        List<MovementReportDTO> movementReportVo = service.reportMovementByDateAndClientId(clientId, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
        return ResponseEntity.ok(BaseResponse.builder().data(movementReportVo).build());
    }

    @PostMapping
    public ResponseEntity<BaseResponse> addMovement(@Valid @RequestBody MovementDTO request) throws Exception {
        service.saveMovement(request);
        return ResponseEntity.ok(BaseResponse.builder().data(request).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateMovement(@PathVariable("id") Long id,
                                                       @Valid @RequestBody MovementDTO request){
        request.setMovementId(id);
        MovementEntity movementEntity = MovementMapper.INSTANCE.toMovement(request);
        movementEntity.setLastModifiedDate(new Date());
        movementEntity.setLastModifiedByUser("Angelo");
        MovementEntity entity = service.update(id, movementEntity);
        return ResponseEntity.ok(BaseResponse.builder().data(MovementMapper.INSTANCE.toMovementDTO(entity)).build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteAccount(@PathVariable("id") Long id) {
        // Eliminar registro
        //service.delete(id);
        // Eliminado logico
        service.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }
}
