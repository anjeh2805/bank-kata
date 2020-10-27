package com.example.bankapp;

import com.example.bankapp.controller.AccountController;
import com.example.bankapp.dao.AccountRepositoryCustom;
import com.example.bankapp.dto.BusinessException;
import com.example.bankapp.dto.OperationDTO;
import com.example.bankapp.dto.OperationType;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Operation;
import com.example.bankapp.service.impl.AccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepositoryCustom accountRepositoryCustom;

    @Spy
    private DozerBeanMapper mapper = new DozerBeanMapper();

    @InjectMocks
    private AccountServiceImpl accountService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        final AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.accountRepositoryCustom = this.accountRepositoryCustom;
        accountService.mapper = this.mapper;
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountService).build();

    }

    @Test
    public void it_should_return_deposit_operation_if_iban_exists() throws Exception {
        final String iban = "FR58885555";
        final double amount = 500;
        Operation operation = new Operation();
        operation.setId(4L);
        operation.setAmount(500D);
        operation.setAccount(new Account());
        operation.setDate(new Date());
        operation.setType("DEPOSIT");
        Mockito.when(this.accountRepositoryCustom.addAmountToAccount(amount, iban)).thenReturn(operation);
        OperationDTO operationDTO = this.accountService.addAmountToAccount(amount, iban);
        assertEquals( 500D, operationDTO.getAmount() );
    }

    @Test (expected = BusinessException.class)
    public void it_should_return_business_exception_when_iban_does_not_exit() throws BusinessException {
        final String iban = "";
        final double amount = 500;
        Mockito.doThrow(new BusinessException("There is no account with this iban")).
                when(this.accountRepositoryCustom).addAmountToAccount(amount, iban);
        this.accountService.addAmountToAccount(amount, iban);
    }

    @Test
    public void it_should_return_retrieve_operation_if_account_id_exists() throws Exception {
        final Long accountId = 1L;
        final double amount = 200;
        Operation operation = new Operation();
        operation.setId(4L);
        operation.setAmount(200D);
        operation.setAccount(new Account());
        operation.setDate(new Date());
        operation.setType("WITHDRAWAL");
        Mockito.when(this.accountRepositoryCustom.retrieveAmountFromAccount(amount, accountId)).thenReturn(operation);
        OperationDTO operationDTO = this.accountService.retrieveAmountFromAccount(amount, accountId);
        assertEquals( 200D, operationDTO.getAmount() );
    }
    @Test(expected = BusinessException.class)
    public void it_should_return_business_exception_when_exceed_Overdraft() throws BusinessException {
        final Long accountId = 1L;
        final double amount = 5000;
        Mockito.doThrow(new BusinessException("Retrieve not authorized, we have depassed the threshold")).
                when(this.accountRepositoryCustom).retrieveAmountFromAccount(amount, accountId);
        this.accountService.retrieveAmountFromAccount(amount, accountId);

    }

    @Test(expected = BusinessException.class)
    public void it_should_return_business_exception_when_accound_id_does_not_exist() throws BusinessException {
        final Long accountId = 1L;
        final double amount = 5000;
        Mockito.doThrow(new BusinessException("There are not account with this id")).
                when(this.accountRepositoryCustom).retrieveAmountFromAccount(amount, accountId);
        this.accountService.retrieveAmountFromAccount(amount, accountId);

    }

    @Test
    public void it_should_return_all_operations_of_account() throws BusinessException {
        final Long accountId = 1L;
        Mockito.when(this.accountRepositoryCustom.getOperationsOfAccount(new Date(), new Date(), accountId)).
                thenReturn(new HashSet<>());
        assertEquals(0, this.accountService.getOperationsOfAccount(new Date(), new Date(), accountId).size());

    }




}
