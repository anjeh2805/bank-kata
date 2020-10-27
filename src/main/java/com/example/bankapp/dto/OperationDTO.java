package com.example.bankapp.dto;

import java.util.Date;

public class OperationDTO {
    private OperationType type;
    private Date date;
    private Double amount;
    private Double balance;

    public OperationDTO() {}

    public OperationDTO(OperationType type, Date date, Double amount, Double balance){
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.balance= balance;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
