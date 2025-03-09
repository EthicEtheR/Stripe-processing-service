package com.hulkhiretech.payments.Service.PaymentService;

import com.hulkhiretech.payments.Service.Interface.PaymentServiceInterface;
import com.hulkhiretech.payments.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentServiceInterface {

    @Override
    public String createPayments(TransactionDTO txn) {
        log.info("Invoked createPayments ");
        // update DB PaymentStatus as CREATED
        //generate and return txnReference Id


        return "";
    }
    @Override
    public String initiatePayments(){
        log.info("InitiatePayments is Invoked");
        // update DB PaymentStatus as INITIATED
        //Call provider service get response
        // if success update DB as PENDING and return necessary object
        // if Failure update DB as FAILURE and retry/return Message

        return"";
    }
}
