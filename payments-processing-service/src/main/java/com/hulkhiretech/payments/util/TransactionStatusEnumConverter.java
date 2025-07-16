package com.hulkhiretech.payments.util;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.PaymentMethodEnum;

public class TransactionStatusEnumConverter extends AbstractConverter<String, Integer> {

	@Override
	protected Integer convert(String source) {
		return PaymentMethodEnum.getByName(source).getId();
	}

}
