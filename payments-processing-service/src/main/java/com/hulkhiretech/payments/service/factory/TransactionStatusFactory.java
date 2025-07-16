package com.hulkhiretech.payments.service.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.hulkhiretech.payments.constants.TransactionStatusEnum;
import com.hulkhiretech.payments.service.impl.statushandler.CreatedStatusHandler;
import com.hulkhiretech.payments.service.interfaces.TransactionStatusHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionStatusFactory {

    private final ApplicationContext applicationContext;
    public TransactionStatusHandler getTransactionHandler(TransactionStatusEnum transactionStatusEnum){
        log.info("Fetching Transaction handle for status code: {}", transactionStatusEnum);
        switch(transactionStatusEnum){
            case CREATED :
            log.info("Returning CreatedStatusHandler for status code: {}", transactionStatusEnum);
            return applicationContext.getBean(CreatedStatusHandler.class);
            default:
            log.warn("No Transaction handler found for the status code {}", transactionStatusEnum);
            return null;
        }
    }
}
