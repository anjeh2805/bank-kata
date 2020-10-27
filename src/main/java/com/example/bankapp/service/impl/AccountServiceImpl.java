package com.example.bankapp.service.impl;

import com.example.bankapp.dao.AccountRepositoryCustom;
import com.example.bankapp.dto.BusinessException;
import com.example.bankapp.dto.OperationDTO;
import com.example.bankapp.entity.Operation;
import com.example.bankapp.service.AccountService;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    public AccountRepositoryCustom accountRepositoryCustom;

    @Autowired
    public DozerBeanMapper mapper;


    @Override
    @Transactional
    public OperationDTO addAmountToAccount(Double amount, String iban) throws BusinessException{
        Operation operation = this.accountRepositoryCustom.addAmountToAccount(amount, iban);
        OperationDTO operationDTO = null;
        if(operation != null) {
            operationDTO = this.mapper.map( operation, OperationDTO.class);
        }
        return operationDTO;
    }

    @Override
    @Transactional
    public OperationDTO retrieveAmountFromAccount(Double amount, Long accountId) throws BusinessException {
        Operation operation = this.accountRepositoryCustom.retrieveAmountFromAccount(amount, accountId);
        OperationDTO operationDTO = null;
        if(operation != null) {
            operationDTO = this.mapper.map(operation, OperationDTO.class);
        }
        return operationDTO;
    }

    @Override
    @Transactional
    public Set<OperationDTO> getOperationsOfAccount(Date fromDate, Date toDate, Long accountId) throws BusinessException{
        Set<Operation> operations = this.accountRepositoryCustom.getOperationsOfAccount(fromDate, toDate, accountId);
        if(operations != null) {
            Set<OperationDTO> operationsDTO = operations.stream()
                    .map(operation -> this.mapper.map(operation, OperationDTO.class))
                    .collect(Collectors.toSet());

            return operationsDTO;
        }else {
            return null;
        }

    }
}
