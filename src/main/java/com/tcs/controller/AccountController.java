package com.tcs.controller;

import com.tcs.dto.AccountDTO;
import com.tcs.mappers.AccountMapper;
import com.tcs.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("api/v1/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<AccountDTO>>> findAll(){
        Flux<AccountDTO> accountDTOFlux = service.findAll()
                .map(AccountMapper.INSTANCE::toAccountDTO);

        return Mono.just(
                        ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(accountDTOFlux))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountDTO>> findById(@PathVariable("id") Long id) {
        return service.findById(id)
                .map(AccountMapper.INSTANCE::toAccountDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<AccountDTO>> save(@RequestBody AccountDTO response, ServerHttpRequest req) {
        return service.save(AccountMapper.INSTANCE.toAccount(response))
                .map(e -> {
                    URI location = URI.create(req.getURI().toString().concat("/").concat(e.getAccountId().toString()));
                    return ResponseEntity.created(location)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(AccountMapper.INSTANCE.toAccountDTO(e));
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AccountDTO>> update(@PathVariable("id") Long id, @RequestBody AccountDTO response) {

        return service.updateAccount(id, AccountMapper.INSTANCE.toAccount(response))
                .map(AccountMapper.INSTANCE::toAccountDTO)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
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
