package com.hulkhiretech.payments.dao.impl;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hulkhiretech.payments.dao.interfaces.TransactionDAO;
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
            return "Your Transaction is saved with Ref No: " + transaction.getTxnReference() + ". Please keep this for future reference.";
        } else {
            log.error("Failed to save transaction");
            return "Transaction save failed";
        }
    }

}
