package com.hulkhiretech.payments.service.impl.statushandler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.dao.interfaces.TransactionDAO;
import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.entity.Transaction;
import com.hulkhiretech.payments.service.interfaces.TransactionStatusHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitiatedStatusHandler implements TransactionStatusHandler {

    private final TransactionDAO transactionDAO;
    private final ModelMapper modelMapper;

    @Override
    public TransactionDto handleTransactionStatus(TransactionDto transactionDto) {
        log.info("Data object received in initiated status handler: {}", transactionDto);
        
        // Here you can add any business logic specific to INITIATED status
        // For example: calling external payment provider, setting up payment flow, etc.
        
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        log.info("Mapped Transaction for update: {}", transaction);
        
        String status = transactionDAO.updateTransactionStatus(transaction);
        log.info("Status of transaction update in initiated status handler: {}", status);
        
        log.info("Transaction Status Updated to INITIATED Successfully: {}", transactionDto);
        return transactionDto;
    }
}