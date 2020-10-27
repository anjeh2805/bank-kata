package com.example.bankapp.dao;

import com.example.bankapp.dto.BusinessException;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Operation;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public interface AccountRepositoryCustom {
    Optional<Account> getAccountByIban(String iban) throws BusinessException;
    Operation addAmountToAccount(Double operationAmount, String iban) throws BusinessException;
    Operation retrieveAmountFromAccount(Double amount, Long accountId) throws BusinessException;
    Set<Operation> getOperationsOfAccount(Date fromDate, Date toDate, Long accountId) throws BusinessException;
}
