package com.hulkhiretech.payments.Service.Impl;

import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import com.hulkhiretech.payments.Service.Interface.PaymentStatusService;
import com.hulkhiretech.payments.Service.Interface.TransactionStatusHandler;
import com.hulkhiretech.payments.Service.PaymentStatusFactory;
import com.hulkhiretech.payments.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusServiceImpl implements PaymentStatusService {
    private final PaymentStatusFactory statusFactory;
    @Override
    public TransactionDTO processStatus(TransactionDTO txnDto) {
        log.info("Received txnDto in processStatus :{}",txnDto);

        TransactionStatusEnum statusEnum=TransactionStatusEnum.getByName(txnDto.getTxnStatus());
        if(statusEnum==null){
            log.error("Got invalid statusEnum :{}",statusEnum);
            //TODO Custom exception handling
        }
        TransactionStatusHandler handler =statusFactory.getHandler(statusEnum);
        if(handler==null){
            log.error("Invaild handler :{}",handler);
            //TODO custom exception handing
        }
        log.info("Got valid handler through factory: {}",handler);
        handler.processStatus(txnDto);

        return txnDto;
    }
}
