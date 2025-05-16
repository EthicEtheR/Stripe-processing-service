package com.hulkhiretech.payments.Service.Interface;

import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.pojo.InitiatePaymentReq;

public interface PaymentServiceInterface {

    public TransactionDTO createPayments(TransactionDTO txn);
    public String initiatePayments(String txnRefs, InitiatePaymentReq initiatePaymentReq);

}
