package com.hulkhiretech.payments.Service.Interface;

import com.hulkhiretech.payments.dto.TransactionDTO;

public interface PaymentServiceInterface {

    public TransactionDTO createPayments(TransactionDTO txn);
    public String initiatePayments(String txnRefs);

}
