package com.example.bankapp.dto;

public class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }
}
