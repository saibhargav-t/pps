package com.hulkhiretech.payments.util;

import org.modelmapper.PropertyMap;

import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.entity.Transaction;

public class TransactionDtoToTransactionMap extends PropertyMap<TransactionDto, Transaction>{

	@Override
    protected void configure() {
        using(new PaymentMethodEnumConverter())
                .map(source.getPaymentMethod(), destination.getPaymentMethodId());
//        using(new ProviderEnumConverter())
//                .map(source.getProvider(), destination.getProviderId());
//        using(new PaymentTypeEnumConverter())
//                .map(source.getPaymentType(), destination.getPaymentTypeId());
    }
}
