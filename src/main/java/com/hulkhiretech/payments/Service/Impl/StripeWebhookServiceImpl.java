package com.hulkhiretech.payments.Service.Impl;

import com.google.gson.Gson;
import com.hulkhiretech.payments.Constant.ErrorCodeEnum;
import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import com.hulkhiretech.payments.Service.Interface.PaymentStatusService;
import com.hulkhiretech.payments.Service.Interface.StripeWebhookService;
import com.hulkhiretech.payments.dao.TransactionDao;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.dto.stripe.CheckoutSessionData;
import com.hulkhiretech.payments.dto.stripe.StripeEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripeWebhookServiceImpl  implements StripeWebhookService {
    public static final String PAID = "paid";
    public static final String COMPLETE = "complete";
    private final Gson gson;
    private final PaymentStatusService paymentStatusService;
    private final TransactionDao transactionDao;

    public static final String CHECKOUT_SESSION_COMPLETED = "checkout.session.completed";
    public static final String CHECKOUT_SESSION_ASYNC_PAYMENT_SUCCEEDED = "checkout.session.async_payment_succeeded";
    public static final String CHECKOUT_SESSION_ASYNC_PAYMENT_FAILED = "checkout.session.async_payment_failed";


    public void processEvent(StripeEventDTO eventDTO){
        log.info("Invoked processEvent In Stripe service");

        if(CHECKOUT_SESSION_COMPLETED.equals(eventDTO.getType())){
            log.info("type is checkout session completed");

           CheckoutSessionData objData= gson.fromJson(eventDTO.getData().getObject(),
                   CheckoutSessionData.class);
           log.info("objData :{}",objData);


            if(COMPLETE.equals(objData.getStatus()) &&
                  PAID.equals(objData.getPaymentStatus())){
               log.info("Payment Success");
               //code for success processing

                TransactionDTO txnResDTO=transactionDao.getTransactionByProviderRef(objData.getId());
                if(txnResDTO==null){
                    log.error("Got txnResDTO null in StripeWebhookServiceImpl");
                    return;
                }

                txnResDTO.setTxnStatus(TransactionStatusEnum.SUCCESS.getName());
                paymentStatusService.processStatus(txnResDTO);

           }
           return ;
        }
        if(CHECKOUT_SESSION_ASYNC_PAYMENT_SUCCEEDED.equals(eventDTO.getType())){
            log.info("type is checkout session async payment succeeded");

            CheckoutSessionData objData= gson.fromJson(eventDTO.getData().getObject(),
                    CheckoutSessionData.class);
            log.info("objData :{}",objData);

            if(COMPLETE.equals(objData.getStatus()) &&
                    PAID.equals(objData.getPaymentStatus())){
                log.info("Payment Success");
                //code for success processing

                TransactionDTO txnResDTO=transactionDao.getTransactionByProviderRef(objData.getId());
                if(txnResDTO==null){
                    log.error("Got txnResDTO null in StripeWebhookServiceImpl");
                    return;
                }

                txnResDTO.setTxnStatus(TransactionStatusEnum.SUCCESS.getName());
                paymentStatusService.processStatus(txnResDTO);

            }



            return;
        }
        if(CHECKOUT_SESSION_ASYNC_PAYMENT_FAILED.equals(eventDTO.getType())){
            log.info("type is checkout session async payment failed");

            CheckoutSessionData objData= gson.fromJson(eventDTO.getData().getObject(),
                    CheckoutSessionData.class);
            log.info("objData :{}",objData);


           TransactionDTO txnResDTO= transactionDao.getTransactionByProviderRef(objData.getId());
            if(txnResDTO==null){
                log.error(" We got txnResDTO null in StripeWebhookServiceImpl");
                return;
            }

            txnResDTO.setTxnStatus(TransactionStatusEnum.FAILED.getName());
            txnResDTO.setErrorCode(ErrorCodeEnum.CHECKOUT_SESSION_PAYMENT_FAILED.getErrorCode());
            txnResDTO.setErrorMessage(ErrorCodeEnum.CHECKOUT_SESSION_PAYMENT_FAILED.getErrorMessage());

            paymentStatusService.processStatus(txnResDTO);


            return;
        }

     log.info("Event type not  configured ");
        return;

    }
}
