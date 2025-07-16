package com.hulkhiretech.payments.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.entity.Transaction;
import com.hulkhiretech.payments.util.PaymentMethodEnumConverter;
import com.hulkhiretech.payments.util.PaymentTypeEnumConverter;
import com.hulkhiretech.payments.util.ProviderEnumConverter;
import com.hulkhiretech.payments.util.TransactionStatusEnumConverter;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setFieldMatchingEnabled(true)
				.setFieldAccessLevel(AccessLevel.PRIVATE);
		Converter<String, Integer> paymentMethodEnumConverter = new PaymentMethodEnumConverter();
		Converter<String, Integer> paymentTypeEnumConverter = new PaymentTypeEnumConverter();
		Converter<String, Integer> providerEnumConverter = new ProviderEnumConverter();
		Converter<String, Integer> transactionStatusEnumConverter = new TransactionStatusEnumConverter();

		TypeMap<TransactionDto, Transaction> typeMap = modelMapper.createTypeMap(TransactionDto.class, Transaction.class);

		typeMap.addMappings(mapper -> {
		    mapper.using(paymentMethodEnumConverter)
		          .map(TransactionDto::getPaymentMethod, Transaction::setPaymentMethodId);

		    mapper.using(providerEnumConverter)
		          .map(TransactionDto::getProvider, Transaction::setProviderId);

		    mapper.using(paymentTypeEnumConverter)
		          .map(TransactionDto::getPaymentType, Transaction::setPaymentTypeId);

		    mapper.using(transactionStatusEnumConverter)
		          .map(TransactionDto::getTxnStatus, Transaction::setTxnStatusId);
		});

		return modelMapper;
	}
}
