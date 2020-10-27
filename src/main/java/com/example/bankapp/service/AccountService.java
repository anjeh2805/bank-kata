package com.example.bankapp.service;


import com.example.bankapp.dto.BusinessException;
import com.example.bankapp.dto.OperationDTO;
import com.example.bankapp.entity.Operation;

import java.util.Date;
import java.util.Set;

public interface AccountService {

    public OperationDTO addAmountToAccount(Double amount, String iban) throws BusinessException;

    public OperationDTO retrieveAmountFromAccount(Double amount, Long accountId) throws BusinessException;

    public Set<OperationDTO> getOperationsOfAccount(Date fromDate, Date toDate, Long accountId) throws BusinessException;
}
