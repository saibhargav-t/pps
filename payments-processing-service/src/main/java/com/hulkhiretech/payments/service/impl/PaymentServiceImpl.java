package com.hulkhiretech.payments.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constants.TransactionStatusEnum;
import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.pojo.CreateTransaction;
import com.hulkhiretech.payments.pojo.CreateTransactionResponse;
import com.hulkhiretech.payments.pojo.InitiateTransactionRequest;
import com.hulkhiretech.payments.service.PaymentStatusService;
import com.hulkhiretech.payments.service.interfaces.PaymentService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ModelMapper modelMapper;
    private final PaymentStatusService paymentStatusService;

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
        // TODO will implement later
        throw new UnsupportedOperationException("Unimplemented method 'initiatePayment'");
    }

}
