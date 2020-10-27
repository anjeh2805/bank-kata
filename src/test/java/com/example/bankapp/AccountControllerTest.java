package com.example.bankapp;

import com.example.bankapp.controller.AccountController;
import com.example.bankapp.dto.OperationDTO;
import com.example.bankapp.dto.OperationParams;
import com.example.bankapp.dto.OperationType;
import com.example.bankapp.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc
public class AccountControllerTest {

    @MockBean
    public AccountService accountService;

    @Autowired
    private WebApplicationContext context;


    private MockMvc mockMvc;


    @InjectMocks
    private AccountController accountController;

    @Before
    public void setup() {
        final AccountController accountController = new AccountController();
        accountController.accountService = accountService;
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

    }

    @Test
    public void when_iban_exists_then_return_status_ok() throws Exception {
        final String iban = "FR58885555";
        final double amount = 500;
        final OperationDTO operation = new OperationDTO();
        operation.setAmount(500D);
        operation.setDate(new Date());
        operation.setType(OperationType.DEPOSIT);
        Mockito.when(this.accountService.addAmountToAccount(amount, iban)).thenReturn(operation);
        this.mockMvc.perform(post("/account/deposit/{amount}", amount).param("iban", iban)).andExpect(status().isOk());
    }

  @Test
    public void should_do_nothing_when_account_id_exist() throws Exception {
        final Long accountId= 1L;
      final double amount = 500;
      final OperationDTO operation = new OperationDTO();
      operation.setAmount(500D);
      operation.setDate(new Date());
      operation.setType(OperationType.WITHDRAWAL);
       Mockito.when(this.accountService.retrieveAmountFromAccount(amount, accountId)).thenReturn(operation);
       this.mockMvc.perform(post("/account/retrieve/{accountId}/{amount}", accountId, amount)).andExpect(status().isOk());
  }

  @Test
    public  void should_return_internal_error_when_amount_exceed_the_threshold() throws Exception {
      final Long accountId= 1L;
      final double amount = 500;
      Mockito.when(this.accountService.retrieveAmountFromAccount(amount, accountId)).thenReturn(null);
      this.mockMvc.perform(post("/account/retrieve/{accountId}/{amount}", accountId, amount)).andExpect(status().isInternalServerError());

  }
  @Test
    public void return_list_operations() throws Exception {
      ObjectMapper mapper = new ObjectMapper();
      OperationParams params = new OperationParams();
      params.setAccountId(1L);
      params.setFromDate(new Date());
      params.setToDate(new Date());
      Set<OperationDTO> operations = new HashSet<OperationDTO>();
      OperationDTO operationDTO = new OperationDTO(OperationType.DEPOSIT,new Date(), 500D, 1555D );
      operations.add(operationDTO);
      Mockito.when(this.accountService.getOperationsOfAccount
              (params.getFromDate(), params.getToDate(), params.getAccountId())).thenReturn(operations);
      this.mockMvc.perform(post("/account/operations").
              contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(params))).
              andExpect(status().isOk());
  }
  @Test
    public void _it_should_return_internal_error_when_accound_id_does_not_exist() throws Exception {
      ObjectMapper mapper = new ObjectMapper();
      OperationParams params = new OperationParams();
      params.setAccountId(1L);
      params.setFromDate(new Date());
      params.setToDate(new Date());
      Mockito.when(this.accountService.getOperationsOfAccount(params.getFromDate(),params.getToDate(), params.getAccountId())).thenReturn(null);
      this.mockMvc.perform(post("/account/operations").
              contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(params))).
              andExpect(status().isInternalServerError());
  }
}
