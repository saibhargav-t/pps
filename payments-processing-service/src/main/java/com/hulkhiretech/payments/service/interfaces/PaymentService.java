package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.pojo.CreateTransaction;
import com.hulkhiretech.payments.pojo.CreateTransactionResponse;
import com.hulkhiretech.payments.pojo.InitiateTransactionRequest;

public interface PaymentService {

    public CreateTransactionResponse createPayment(CreateTransaction createTransaction);
    public String initiatePayment(String transactionReference, InitiateTransactionRequest initiateTransactionRequest);
}
