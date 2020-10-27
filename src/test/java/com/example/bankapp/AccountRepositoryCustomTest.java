package com.example.bankapp;

import com.example.bankapp.controller.AccountController;
import com.example.bankapp.dao.AccountRepository;
import com.example.bankapp.dao.AccountRepositoryCustom;
import com.example.bankapp.dao.impl.AccountRepositoryCustomImpl;
import com.example.bankapp.dto.BusinessException;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Operation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryCustomTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountRepositoryCustomImpl accountRepositoryCustom;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        final AccountRepositoryCustomImpl accountRepositoryCustom = new AccountRepositoryCustomImpl();
        accountRepositoryCustom.entityManager = this.entityManager;
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountRepositoryCustom).build();

    }

    @Test
    public void it_should_return_account_if_iban_exists() throws Exception {
        final String iban = "FR58885555";
        TypedQuery<Account> query = (TypedQuery<Account>) Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT c FROM Account c WHERE c.iban =:iban", Account.class)).thenReturn(query);
        Account account = new Account();
        account.setId(3L);
        account.setIban(iban);
        Mockito.when(query.getSingleResult()).thenReturn(account);
        assertNotNull(this.accountRepositoryCustom.getAccountByIban(iban).get());
    }

    @Test(expected = BusinessException.class)
    public void it_should_return_business_exception_when_iban_does_not_exist() throws BusinessException {
        final String iban = "";
        TypedQuery<Account> query = (TypedQuery<Account>) Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT c FROM Account c WHERE c.iban =:iban", Account.class)).thenReturn(query);
        Mockito.when(query.getSingleResult()).thenThrow(new NoResultException());
        this.accountRepositoryCustom.getAccountByIban(iban);
    }

    @Test
    public void it_should_return_depot_operation_if_iban_exists() throws BusinessException {
        final String iban = "FR58885555";
        final Double amount = 500D;
        Account account = new Account();
        account.setId(3L);
        account.setIban(iban);
        account.setBalance(700D);
        account.setOverdraft(200D);
        account.setOperations(new TreeSet<Operation>(Comparator.comparing(Operation::getDate)));
        AccountRepositoryCustom accountRepositoryCustom = Mockito.spy(this.accountRepositoryCustom);
        Mockito.doReturn(Optional.of(account)).when(accountRepositoryCustom).getAccountByIban(iban);
        Operation operation = accountRepositoryCustom.addAmountToAccount(amount, iban);
        assertEquals(amount, operation.getAmount());
    }

    @Test(expected = BusinessException.class)
    public void it_should_return_business_exception_if_iban_does_not_exists() throws BusinessException {
        final String iban = "";
        final Double amount = 500D;
        AccountRepositoryCustom accountRepositoryCustom = Mockito.spy(this.accountRepositoryCustom);
        Mockito.doThrow(new BusinessException("There is no account with this iban")).when(accountRepositoryCustom).getAccountByIban(iban);
        accountRepositoryCustom.addAmountToAccount(amount, iban);
    }

    @Test
    public void it_should_return_retrieve_operation() throws BusinessException {
        final Long accountId = 1L;
        final Double amount = 700D;
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(700D);
        account.setOverdraft(200D);
        account.setOperations(new TreeSet<Operation>(Comparator.comparing(Operation::getDate)));
        Mockito.when(this.accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        Operation operation = this.accountRepositoryCustom.retrieveAmountFromAccount(amount,  accountId);
        assertEquals(amount, operation.getAmount());
    }

    @Test(expected = BusinessException.class)
    public void it_should_return_Business_exception_when_exceed_Overdraft() throws BusinessException {
        final Long accountId = 1L;
        final Double amount = 1000D;
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(700D);
        account.setOverdraft(200D);
        account.setOperations(new TreeSet<Operation>(Comparator.comparing(Operation::getDate)));
        Mockito.when(this.accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        this.accountRepositoryCustom.retrieveAmountFromAccount(amount,  accountId);
    }

    @Test(expected = BusinessException.class)
    public void it_should_return_Business_exception_when_account_id_does_not_exist() throws BusinessException {
        final Long accountId = 5L;
        final Double amount = 200D;
        Mockito.when(this.accountRepository.findById(accountId)).thenReturn(Optional.empty());
        this.accountRepositoryCustom.retrieveAmountFromAccount(amount,  accountId);
    }

    @Test
    public void it_sould_return_set_of_operations_if_account_id_exists() throws BusinessException {
        final Long accountId = 1L;
        Operation operation = new Operation();
        operation.setDate(new Date());
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(700D);
        account.setOverdraft(200D);
        account.setOperations(new TreeSet<Operation>(Comparator.comparing(Operation::getDate)));
        account.getOperations().add(operation);
        Mockito.when(this.accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        assertEquals(0, this.accountRepositoryCustom.getOperationsOfAccount(new Date(), new Date(), accountId).size());
    }

    @Test(expected = BusinessException.class)
    public void it_should_return_business_exception_if_account_id_exists_does_not_exist() throws BusinessException {
        final Long accountId = 9L;
        Mockito.when(this.accountRepository.findById(accountId)).thenReturn(Optional.empty());
        this.accountRepositoryCustom.getOperationsOfAccount(new Date(), new Date(), accountId);
    }


}
