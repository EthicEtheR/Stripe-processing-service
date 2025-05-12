package com.hulkhiretech.payments.Service.Interface;

import com.hulkhiretech.payments.dto.TransactionDTO;

public interface PaymentStatusService {
    TransactionDTO processStatus(TransactionDTO txnDto);
}
