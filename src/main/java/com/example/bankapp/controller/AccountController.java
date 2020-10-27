package com.example.bankapp.controller;

import com.example.bankapp.dto.BusinessException;
import com.example.bankapp.dto.OperationDTO;
import com.example.bankapp.dto.OperationParams;
import com.example.bankapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    public AccountService accountService;

    @PostMapping(path = "/deposit/{amount}")
    public ResponseEntity addAmountToAccount(@RequestParam String iban, @PathVariable("amount") Double amount) throws BusinessException {
        OperationDTO operation = this.accountService.addAmountToAccount(amount, iban);
        if(operation != null) {
            return new ResponseEntity(operation, HttpStatus.OK);
        } else {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/retrieve/{accountId}/{amount}")
    public ResponseEntity retrieveAmountFromAccount(@PathVariable("accountId") Long accountId, @PathVariable("amount") Double amount) throws BusinessException {
        OperationDTO operation = this.accountService.retrieveAmountFromAccount(amount, accountId);
        if(operation != null) {
            return new ResponseEntity(operation, HttpStatus.OK);
        } else {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path="/operations")
    public ResponseEntity getAllOperations(@RequestBody OperationParams operationParams) throws BusinessException {
        Set<OperationDTO> operationsDTOS = this.accountService.getOperationsOfAccount(operationParams.getFromDate(), operationParams.getToDate(), operationParams.getAccountId());
         if(operationsDTOS != null) {
            return new ResponseEntity(operationsDTOS, HttpStatus.OK);
         } else {
             return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
}
