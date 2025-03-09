package com.hulkhiretech.payments.Service.Interface;

import com.hulkhiretech.payments.dto.TransactionDTO;

public interface PaymentServiceInterface {

    public String createPayments(TransactionDTO txn);
    public String initiatePayments();

}
