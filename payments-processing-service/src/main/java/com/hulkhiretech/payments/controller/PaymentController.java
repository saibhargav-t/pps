package com.hulkhiretech.payments.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.pojo.CreateTransaction;
import com.hulkhiretech.payments.pojo.CreateTransactionResponse;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public CreateTransactionResponse createPayment(@RequestBody CreateTransaction transaction) {
        CreateTransactionResponse response = paymentService.createPayment(transaction);
        log.info("Transaction details: {}", response);
        log.info("Payment Request Received  in Payments Controller");
        return response;
    }

    @PostMapping("/{transactionReference}/initiate")
    public String initiatePayment(@PathVariable String transactionReference) {
        log.info("Transaction Reference: {}", transactionReference);
        return "Payments Initiated Successfully for Transaction Reference: " + transactionReference;
    }
}
