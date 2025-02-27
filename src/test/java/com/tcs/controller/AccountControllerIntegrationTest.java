package com.tcs.controller;

import com.tcs.dto.AccountDTO;
import com.tcs.model.Account;
import com.tcs.service.IAccountService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IAccountService accountService;

    private AccountDTO accountDTO;
    private Account account;

//    @BeforeEach
//    void setUp() {
//        accountDTO = AccountDTO.builder()
//                .accountId(1L)
//                .accountNumber(12345678)
//                .accountType("SAVINGS")
//                .initialBalance(BigDecimal.valueOf(1000.0))
//                .build();
//
//        accountEntity = AccountEntity.builder()
//                .accountId(1L)
//                .accountNumber(12345678)
//                .accountType("SAVINGS")
//                .initialBalance(BigDecimal.valueOf(1000.0))
//                .createdDate(new Date())
//                .createdByUser("TestUser")
//                .build();
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void findAll_ShouldReturnListOfAccounts_WhenHasAdminRole() throws Exception {
//        when(accountService.findAll()).thenReturn(Arrays.asList(accountEntity));
//
//        mockMvc.perform(get("/api/v1/cuentas")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data[0].accountNumber").value("12345678"));
//
//        verify(accountService, times(1)).findAll();
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void getAccountById_ShouldReturnAccount_WhenIdExists() throws Exception {
//        when(accountService.findById(1L, "Account")).thenReturn(accountEntity);
//
//        mockMvc.perform(get("/api/v1/cuentas/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.accountNumber").value("12345678"));
//
//        verify(accountService, times(1)).findById(1L, "Account");
//    }
}