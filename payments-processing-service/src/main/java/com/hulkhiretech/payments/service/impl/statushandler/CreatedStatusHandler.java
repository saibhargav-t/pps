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
public class CreatedStatusHandler implements TransactionStatusHandler {

    private final TransactionDAO transactionDAO;
    private final ModelMapper modelMapper;

    @Override
    public TransactionDto handleTransactionStatus(TransactionDto transactionDto) {
        log.info("Data object received in created status handler is{},transactionDto");
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        log.info("Mapped Final Transaction is: {}", transaction);
        String status = transactionDAO.saveTransaction(transaction);
        log.info("Status of transaction in created status handler is {}", status);
        log.info("Transaction Created Successfully : {}", transactionDto);
        return transactionDto;
    }

}
