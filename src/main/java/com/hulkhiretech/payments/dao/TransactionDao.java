package com.hulkhiretech.payments.dao;

import com.hulkhiretech.payments.dto.TransactionDTO;

public interface TransactionDao {
    public TransactionDTO createTransaction(TransactionDTO txnDto);
    public TransactionDTO getTransactionByTxnRef(String txnRefs);
    public TransactionDTO initiateTransaction(TransactionDTO txnDTO);
}
