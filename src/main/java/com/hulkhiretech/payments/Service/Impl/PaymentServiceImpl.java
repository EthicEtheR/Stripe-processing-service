package com.hulkhiretech.payments.Service.Impl;

import com.google.gson.Gson;
import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import com.hulkhiretech.payments.Http.HttpRequest;
import com.hulkhiretech.payments.Http.HttpServiceEngine;
import com.hulkhiretech.payments.Service.Interface.PaymentServiceInterface;
import com.hulkhiretech.payments.Service.Interface.PaymentStatusService;
import com.hulkhiretech.payments.StripeProviderPojo.CreatePaymentDto;
import com.hulkhiretech.payments.StripeProviderPojo.LineItems;
import com.hulkhiretech.payments.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentServiceInterface {
   private final PaymentStatusService paymentStatusService;
   private final HttpServiceEngine httpServiceEngine;
   private static Gson gson;



    @Override
    public String createPayments(TransactionDTO txn) {
        log.info("Invoked createPayments ");
        txn.setTxnStatus(TransactionStatusEnum.CREATED.getName());
        String TxnRef =generateTxnReference();
        txn.setTxnReference(TxnRef);
         log.info("going to call processStatus :{}",txn);

         paymentStatusService.processStatus(txn);
        // update DB PaymentStatus as CREATED
        //generate and return txnReference Id


        return "Successfully return from createPayment from service";
    }

    private String generateTxnReference() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String initiatePayments(){
        log.info("InitiatePayments is Invoked");
        // update DB PaymentStatus as INITIATED
        //TODO valid object
        TransactionDTO txn=new TransactionDTO();
        txn.setTxnStatus(TransactionStatusEnum.INITIATED.getName());
        paymentStatusService.processStatus(txn);

        //Call provider service get response means make RestClint request;

        HttpRequest httpRequest = getHttpRequest();
        httpServiceEngine.makeHttpCall(httpRequest);


        // TODO if success update DB as PENDING and return necessary object
        txn.setTxnStatus(TransactionStatusEnum.PENDING.getName());
        txn.setProviderReference("From Stripe");
        paymentStatusService.processStatus(txn);

        // TODO if Failure update DB as FAILURE and retry/return Message
        txn.setTxnStatus(TransactionStatusEnum.FAILED.getName());
        txn.setErrorCode("");
        txn.setErrorMessage("Unable to call stripe provider");
        txn.setProviderReference("From Stripe");
        paymentStatusService.processStatus(txn);

        return"";
    }

    private static HttpRequest getHttpRequest() {
        LineItems item1 = new LineItems();
        item1.setQuantity(2);
        item1.setCurrency("USD");
        item1.setProductName("Product A");
        item1.setUnitAmount(100);

        LineItems item2 = new LineItems();
        item2.setQuantity(1);
        item2.setCurrency("USD");
        item2.setProductName("Product B");
        item2.setUnitAmount(200);

        // Add items to list
        List<LineItems> lineItemsList = new ArrayList<>();
        lineItemsList.add(item1);
        lineItemsList.add(item2);

        // Create the main DTO and set values
        CreatePaymentDto paymentDto = new CreatePaymentDto();
        paymentDto.setSuccessUrl("https://example.com/success");
        paymentDto.setCancelUrl("https://example.com/cancel");
        paymentDto.setLineItems(lineItemsList);


        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpRequest httpRequest= HttpRequest.builder()
                .method(HttpMethod.POST)
                .url("http://localhost:8083/v1/payments")
                .Headers(httpHeaders)
                .requestBody(paymentDto)
                .build();

        return httpRequest;
    }
}
