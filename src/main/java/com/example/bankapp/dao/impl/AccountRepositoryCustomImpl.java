package com.example.bankapp.dao.impl;

import com.example.bankapp.dao.AccountRepository;
import com.example.bankapp.dao.AccountRepositoryCustom;
import com.example.bankapp.dto.BusinessException;
import com.example.bankapp.dto.OperationType;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class AccountRepositoryCustomImpl implements AccountRepositoryCustom {

    @Autowired
    public EntityManager entityManager;

    @Autowired
    public AccountRepository accountRepository;


    @Override
    public Optional<Account> getAccountByIban(String iban) throws BusinessException {
        try {
            String sqlQuery = "SELECT c FROM Account c WHERE c.iban =:iban";
            TypedQuery<Account> query = this.entityManager.createQuery(sqlQuery, Account.class);
            query.setParameter("iban", iban);
            return Optional.of(query.getSingleResult());
        }catch (NoResultException e ) {
            throw new BusinessException("There is no account with this iban");
        }
    }

    @Override
    public Operation addAmountToAccount(Double operationAmount, String iban) throws BusinessException {
        Operation operation= null;
        Optional<Account> account = this.getAccountByIban(iban);
        if(account.isPresent()) {
            Double finalBalance = account.get().getBalance() + operationAmount;
            account.get().setBalance(finalBalance);
            operation = new Operation();
            operation.setAccount(account.get());
            operation.setDate(new Date());
            operation.setAmount(operationAmount);
            operation.setBalance(finalBalance);
            operation.setType(OperationType.DEPOSIT.toString());
            operation.setDescription("DEPOSIT");
            account.get().getOperations().add(operation);
            this.accountRepository.save(account.get());
        }
        return operation;
    }
    @Override
    public Operation retrieveAmountFromAccount(Double amount, Long accountId) throws BusinessException {
        Operation operation = null;
        Optional<Account> account = this.accountRepository.findById(accountId);
        if(account.isPresent()) {
            Double finalBalance = account.get().getBalance() - amount;
            // Test if the final balance is in the zone authorized by the bank overdraft.
            if(finalBalance + account.get().getOverdraft() >=0) {
                operation = new Operation();
                account.get().setBalance(finalBalance);
                operation.setDate(new Date());
                operation.setAccount(account.get());
                operation.setAmount(amount);
                operation.setBalance(finalBalance);
                operation.setType(OperationType.WITHDRAWAL.toString());
                operation.setDescription("WITHDRAWAL");
                account.get().getOperations().add(operation);
                this.accountRepository.save(account.get());
            } else {
                throw new BusinessException("Retrieve not authorized, we have depassed the threshold");
            }
        }else {
            throw new BusinessException("There are not account with this id");
        }
        return operation;
    }

    @Override
    public Set<Operation> getOperationsOfAccount(Date fromDate, Date toDate, Long accountId) throws BusinessException{
        Optional<Account> account = this.accountRepository.findById(accountId);
        if(account.isPresent()) {
            Set<Operation> operations = new TreeSet<Operation>(Comparator.comparing(Operation::getDate));
                operations =  account.get().getOperations().stream().filter(operation ->
                    operation.getDate().after(fromDate) && operation.getDate().before(toDate)).collect(Collectors.toSet());
            return operations;
        } else {
            throw new BusinessException("There are not account with this id");
        }
    }
}
