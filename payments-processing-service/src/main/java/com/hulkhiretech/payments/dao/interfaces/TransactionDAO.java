package com.hulkhiretech.payments.dao.interfaces;

import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.entity.Transaction;

public interface TransactionDAO {

	public String saveTransaction(Transaction transaction);

	public String updateTransactionStatus(Transaction transaction);

	public TransactionDto getTransactionByReference(String txnReference);
}
