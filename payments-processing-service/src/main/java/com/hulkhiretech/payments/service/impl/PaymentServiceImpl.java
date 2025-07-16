package com.hulkhiretech.payments.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constants.TransactionStatusEnum;
import com.hulkhiretech.payments.dao.interfaces.TransactionDAO;
import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.pojo.CreateTransaction;
import com.hulkhiretech.payments.pojo.CreateTransactionResponse;
import com.hulkhiretech.payments.pojo.InitiateTransactionRequest;
import com.hulkhiretech.payments.service.PaymentStatusService;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ModelMapper modelMapper;
    private final PaymentStatusService paymentStatusService;
    private final TransactionDAO transactionDAO;

    @Override
    public CreateTransactionResponse createPayment(CreateTransaction createTransaction) {
        log.info("Transaction received in PaymentServiceImpl:  {}", createTransaction);
        TransactionDto transactionDto = modelMapper.map(createTransaction, TransactionDto.class);
        transactionDto.setTxnStatus(TransactionStatusEnum.CREATED.getName());
        transactionDto.setTxnReference(UUID.randomUUID().toString());
        transactionDto.setRetryCount(0);
        log.info("Transaction Final: {}", transactionDto);
        transactionDto = paymentStatusService.updatePayment(transactionDto);
        CreateTransactionResponse response = new CreateTransactionResponse();
        response.setTxnStatus(transactionDto.getTxnStatus());
        response.setTxnReference(transactionDto.getTxnReference());
        log.info("The created transaction reference is: {}", response);
        return response;
    }

    @Override
    public String initiatePayment(String transactionReference, InitiateTransactionRequest initiateTransactionRequest) {
        log.info("Initiating payment for transaction reference: {}", transactionReference);
        
        // Fetch existing transaction
        TransactionDto existingTransaction = transactionDAO.getTransactionByReference(transactionReference);
        
        if (existingTransaction == null) {
            log.error("Transaction not found for reference: {}", transactionReference);
            throw new RuntimeException("Transaction not found for reference: " + transactionReference);
        }
        
        // Validate current status - should be CREATED to initiate
        if (!TransactionStatusEnum.CREATED.getName().equals(existingTransaction.getTxnStatus())) {
            log.error("Transaction cannot be initiated. Current status: {}", existingTransaction.getTxnStatus());
            throw new RuntimeException("Transaction cannot be initiated. Current status: " + existingTransaction.getTxnStatus());
        }
        
        // Update status to INITIATED
        existingTransaction.setTxnStatus(TransactionStatusEnum.INITIATED.getName());
        
        // You can add any additional logic here based on InitiateTransactionRequest
        // For example: setting provider reference, updating retry count, etc.
        
        // Process the status change
        TransactionDto updatedTransaction = paymentStatusService.updatePayment(existingTransaction);
        
        log.info("Payment initiated successfully for transaction reference: {}", transactionReference);
        return "Payment initiated successfully for transaction reference: " + transactionReference;
    }

}
