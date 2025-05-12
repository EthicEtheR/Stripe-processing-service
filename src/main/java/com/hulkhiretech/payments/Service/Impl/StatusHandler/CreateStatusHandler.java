package com.hulkhiretech.payments.Service.Impl.StatusHandler;

import com.hulkhiretech.payments.Service.Interface.TransactionStatusHandler;
import com.hulkhiretech.payments.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateStatusHandler implements TransactionStatusHandler {
    @Override
    public TransactionDTO processStatus(TransactionDTO txnDto) {
        log.info("Processing createStatusHandler");

        //TODO logic of create status
        //invoked DAO layer for DB save
        return null;
    }
}
