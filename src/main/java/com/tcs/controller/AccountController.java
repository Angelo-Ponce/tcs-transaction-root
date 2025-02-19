package com.tcs.controller;

import com.tcs.dto.AccountDTO;
import com.tcs.dto.response.BaseResponse;
import com.tcs.mappers.AccountMapper;
import com.tcs.model.AccountEntity;
import com.tcs.service.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService service;

    @GetMapping
    public ResponseEntity<BaseResponse> findAll(){
        List<AccountDTO> list = service.findAll().stream()
                .map(AccountMapper.INSTANCE::toAccountDTO).toList();

        return ResponseEntity.ok(BaseResponse.builder().data(list).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getAccountById(@PathVariable("id") Long id) {
        AccountEntity entity = service.findById(id, "Account");

        return ResponseEntity.ok(BaseResponse.builder()
                .data(AccountMapper.INSTANCE.toAccountDTO(entity))
                .build());
    }

    @PostMapping
    public ResponseEntity<BaseResponse> addAccount(@Valid @RequestBody AccountDTO request) {
        AccountEntity accountEntity = AccountMapper.INSTANCE.toAccountEntity(request);
        accountEntity.setCreatedDate(new Date());
        accountEntity.setCreatedByUser("Angelo");
        AccountEntity account = service.save(accountEntity);

        return ResponseEntity.ok(BaseResponse.builder().data(AccountMapper.INSTANCE.toAccountDTO(account)).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateAccount(@PathVariable("id") Long id,
                                                      @Valid @RequestBody AccountDTO dto) {
        dto.setAccountId(id);
        AccountEntity accountEntity = AccountMapper.INSTANCE.toAccountEntity(dto);
        accountEntity.setLastModifiedDate(new Date());
        accountEntity.setLastModifiedByUser("Angelo");
        AccountEntity entity = service.update(id, accountEntity);
        return ResponseEntity.ok(BaseResponse.builder().data(AccountMapper.INSTANCE.toAccountDTO(entity)).build());
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
