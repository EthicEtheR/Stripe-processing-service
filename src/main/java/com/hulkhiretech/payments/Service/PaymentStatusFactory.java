package com.hulkhiretech.payments.Service;

import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import com.hulkhiretech.payments.Service.Impl.StatusHandler.*;
import com.hulkhiretech.payments.Service.Interface.TransactionStatusHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentStatusFactory {
    ApplicationContext context;
    public PaymentStatusFactory(ApplicationContext context){
        this.context=context;
    }
    public TransactionStatusHandler getHandler(TransactionStatusEnum statusEnum){
        switch (statusEnum){
            case CREATED :
                return context.getBean(CreateStatusHandler.class);
            case INITIATED:
                return context.getBean(InitiateStatusHandler.class);
            case PENDING:
                return context.getBean(PendingStatusHandler.class);
            case FAILED:
                return context.getBean(FailedStatusHandler.class);
            case SUCCESS:
                return context.getBean(SuccessStatusHandler.class);



        }
        log.info("StatusEnum not found above :{}",statusEnum);

        return null;
    }


}
