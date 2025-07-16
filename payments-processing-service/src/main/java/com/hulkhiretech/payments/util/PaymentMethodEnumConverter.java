package com.hulkhiretech.payments.util;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.TransactionStatusEnum;

public class PaymentMethodEnumConverter extends AbstractConverter<String, Integer> {

	@Override
	protected Integer convert(String source) {
		return TransactionStatusEnum.getByName(source).getId();
	}

}
