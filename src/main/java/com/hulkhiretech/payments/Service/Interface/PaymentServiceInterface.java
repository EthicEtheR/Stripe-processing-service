package com.hulkhiretech.payments.Service.Interface;

import com.hulkhiretech.payments.dto.InitiatePaymentDTO;
import com.hulkhiretech.payments.dto.TransactionDTO;

public interface PaymentServiceInterface {

    public TransactionDTO createPayments(TransactionDTO txn);
    public TransactionDTO initiatePayments(String txnRefs, InitiatePaymentDTO initiatePaymentReq);

}
