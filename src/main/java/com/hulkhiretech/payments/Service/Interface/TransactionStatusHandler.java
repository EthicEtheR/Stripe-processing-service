package com.hulkhiretech.payments.Service.Interface;

import com.hulkhiretech.payments.dto.TransactionDTO;

public interface TransactionStatusHandler {
    public TransactionDTO processStatus(TransactionDTO txnDto);
}
