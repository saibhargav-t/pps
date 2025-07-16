package com.hulkhiretech.payments.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hulkhiretech.payments.dao.interfaces.TransactionDAO;
import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.entity.Transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TransactionDAOImpl implements TransactionDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public String saveTransaction(Transaction transaction) {
		log.info("Saving transaction: {}", transaction);
		String sql = """
				INSERT INTO payments.Transaction (userId,paymentMethodId,providerId,paymentTypeId,txnStatusId,amount,currency,
				merchantTransactionReference,txnReference,providerReference, retryCount) VALUES
				(:userId,:paymentMethodId,:providerId,:paymentTypeId,:txnStatusId,:amount,:currency,:merchantTransactionReference,
				:txnReference,:providerReference,:retryCount)""";

		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(transaction);

		int rowsInserted = jdbcTemplate.update(sql, params);

		if (rowsInserted > 0) {
			log.info("Transaction saved successfully with reference: {}", transaction.getTxnReference());
			return "Your Transaction is saved with Ref No: " + transaction.getTxnReference()
					+ ". Please keep this for future reference.";
		} else {
			log.error("Failed to save transaction");
			return "Transaction save failed";
		}
	}

	@Override
	public String updateTransactionStatus(Transaction transaction) {
		log.info("Updating transaction status: {}", transaction);
		String sql = """
				UPDATE payments.Transaction
				SET txnStatusId = :txnStatusId,
				    retryCount = :retryCount,
				    providerReference = :providerReference
				WHERE txnReference = :txnReference""";

		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(transaction);
		int rowsUpdated = jdbcTemplate.update(sql, params);

		if (rowsUpdated > 0) {
			log.info("Transaction status updated successfully for reference: {}", transaction.getTxnReference());
			return "Transaction status updated successfully for reference: " + transaction.getTxnReference();
		} else {
			log.error("Failed to update transaction status for reference: {}", transaction.getTxnReference());
			return "Transaction status update failed";
		}
	}

	@Override
	public TransactionDto getTransactionByReference(String txnReference) {
		log.info("Fetching transaction by reference: {}", txnReference);
		String sql = """
				SELECT t.id, t.userId, t.paymentMethodId, t.providerId, t.paymentTypeId, t.txnStatusId,
				       t.amount, t.currency, t.merchantTransactionReference, t.txnReference,
				       t.providerReference, t.retryCount,
				       pm.name as paymentMethod, pr.name as provider, pt.name as paymentType, ts.name as txnStatus
				FROM payments.Transaction t
				LEFT JOIN payments.PaymentMethod pm ON t.paymentMethodId = pm.id
				LEFT JOIN payments.Provider pr ON t.providerId = pr.id
				LEFT JOIN payments.PaymentType pt ON t.paymentTypeId = pt.id
				LEFT JOIN payments.TransactionStatus ts ON t.txnStatusId = ts.id
				WHERE t.txnReference = :txnReference""";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("txnReference", txnReference);

		try {
			return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
				TransactionDto dto = new TransactionDto();
				dto.setId(rs.getInt("id"));
				dto.setUserId(rs.getInt("userId"));
				dto.setPaymentMethod(rs.getString("paymentMethod"));
				dto.setProvider(rs.getString("provider"));
				dto.setPaymentType(rs.getString("paymentType"));
				dto.setTxnStatus(rs.getString("txnStatus"));
				dto.setAmount(rs.getBigDecimal("amount"));
				dto.setCurrency(rs.getString("currency"));
				dto.setMerchantTransactionReference(rs.getString("merchantTransactionReference"));
				dto.setTxnReference(rs.getString("txnReference"));
				dto.setProviderReference(rs.getString("providerReference"));
				dto.setRetryCount(rs.getInt("retryCount"));
				return dto;
			});
		} catch (EmptyResultDataAccessException e) {
			log.error("Transaction not found for reference: {}", txnReference);
			return null;
		}
	}

}
