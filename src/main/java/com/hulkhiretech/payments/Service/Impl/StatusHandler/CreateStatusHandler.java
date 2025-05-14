package com.hulkhiretech.payments.Service.Impl.StatusHandler;

import com.hulkhiretech.payments.Service.Interface.TransactionStatusHandler;
import com.hulkhiretech.payments.dao.TransactionDao;
import com.hulkhiretech.payments.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateStatusHandler implements TransactionStatusHandler {
    public TransactionDao transactionDao;
   public  CreateStatusHandler(TransactionDao transactionDao){
        this.transactionDao=transactionDao;
    }
    @Override
    public TransactionDTO processStatus(TransactionDTO txnDto) {
        log.info("Processing createStatusHandler");

        // logic of create status to dao layer
        TransactionDTO txnRes=transactionDao.createTransaction(txnDto);
        log.info("Response came from Dao layer in createStatusHandler :{}",txnRes);

        return txnRes;
    }
}
