package com.tcs.controller;

import com.tcs.dto.AccountDTO;
import com.tcs.model.Account;
import com.tcs.service.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = "eureka.client.enabled=false")
@WithMockUser(roles = {"ADMIN"})
class AccountControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private IAccountService service;

    @MockitoBean
    private WebProperties.Resources resources;

    private AccountDTO accountDTO;
    private Account account;

    @BeforeEach
    void setUp() {
        accountDTO = AccountDTO.builder()
                .accountId(1L)
                .accountNumber("12345678")
                .accountType("SAVINGS")
                .initialBalance(BigDecimal.valueOf(1000.0))
                .build();

        account = Account.builder()
                .accountId(1L)
                .accountNumber("12345678")
                .accountType("SAVINGS")
                .initialBalance(BigDecimal.valueOf(1000.0))
                .build();
    }

    @Test
    //@WithMockUser(roles = "ADMIN")
    void findAll_ShouldReturnListOfAccounts_WhenHasAdminRole(){
        when(service.findAll()).thenReturn(Flux.just(account));

        webTestClient.get()
                .uri("/api/v1/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(AccountDTO.class)
                .hasSize(1)
                .contains(accountDTO);
    }

    @Test
    void givenAccountId_whenFindById_thenReturnAccount() {
        when(service.findById(1L)).thenReturn(Mono.just(account));

        webTestClient.get()
                .uri("/api/v1/cuentas/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AccountDTO.class)
                .isEqualTo(accountDTO);
    }

    @Test
    void givenAccountId_whenFindById_thenReturnNotFound() {
        when(service.findById(1L)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/cuentas/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}