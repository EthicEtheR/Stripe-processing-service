package com.hulkhiretech.payments.Service.Impl;

import com.hulkhiretech.payments.Constant.ErrorCodeEnum;
import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import com.hulkhiretech.payments.Exception.ProccessingException;
import com.hulkhiretech.payments.Service.Interface.PaymentStatusService;
import com.hulkhiretech.payments.Service.Interface.TransactionStatusHandler;
import com.hulkhiretech.payments.Service.PaymentStatusFactory;
import com.hulkhiretech.payments.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

            throw new ProccessingException(ErrorCodeEnum.INVALID_PAYMENT_STATUS.getErrorCode(),
                    ErrorCodeEnum.INVALID_PAYMENT_STATUS.getErrorMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        TransactionStatusHandler handler =statusFactory.getHandler(statusEnum);
        if(handler==null){
            log.error("Invalid handler :{}",handler);

            throw new ProccessingException(ErrorCodeEnum.NO_STATUS_HANDLER_FOUND.getErrorCode(),
                    ErrorCodeEnum.NO_STATUS_HANDLER_FOUND.getErrorMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
       }
        log.info("Got valid handler through factory: {}",handler);
          handler.processStatus(txnDto);

        return txnDto;
    }
}
