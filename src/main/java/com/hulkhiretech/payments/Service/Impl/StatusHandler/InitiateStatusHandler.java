package com.hulkhiretech.payments.Service.Impl.StatusHandler;

import com.hulkhiretech.payments.Service.Interface.TransactionStatusHandler;
import com.hulkhiretech.payments.dao.TransactionDao;
import com.hulkhiretech.payments.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitiateStatusHandler implements TransactionStatusHandler {
    private final TransactionDao transactionDao;
    @Override
    public TransactionDTO processStatus(TransactionDTO txnDto) {
        log.info("Processing processStatus in InitiateStatusHandler");
        transactionDao.initiateTransaction(txnDto);
        return null;
    }
}
