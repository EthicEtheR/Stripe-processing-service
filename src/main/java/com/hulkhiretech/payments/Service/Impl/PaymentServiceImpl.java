package com.hulkhiretech.payments.Service.Impl;

import com.google.gson.Gson;
import com.hulkhiretech.payments.Constant.ErrorCodeEnum;
import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import com.hulkhiretech.payments.Exception.ProccessingException;
import com.hulkhiretech.payments.Http.HttpRequest;
import com.hulkhiretech.payments.Http.HttpServiceEngine;
import com.hulkhiretech.payments.Service.Interface.PaymentServiceInterface;
import com.hulkhiretech.payments.Service.Interface.PaymentStatusService;
import com.hulkhiretech.payments.StripeProviderPojo.CreatePaymentDto;
import com.hulkhiretech.payments.StripeProviderPojo.LineItems;
import com.hulkhiretech.payments.dao.TransactionDao;
import com.hulkhiretech.payments.dto.InitiatePaymentDTO;
import com.hulkhiretech.payments.dto.PaymentResDTO;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.pojo.InitiatePaymentReq;
import com.hulkhiretech.payments.pojo.PaymentRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentServiceInterface {
    public static final String STRIPE_URL = "http://localhost:8083/v1/payments";
    private final PaymentStatusService paymentStatusService;
   private final HttpServiceEngine httpServiceEngine;
   private static Gson gson;
   private  final TransactionDao transactionDao;
   private final ModelMapper mapper;
    Gson gsonUtils = new Gson();



    @Override
    public TransactionDTO createPayments(TransactionDTO txn) {
        log.info("Invoked createPayments ");
        txn.setTxnStatus(TransactionStatusEnum.CREATED.getName());
        String TxnRef =generateTxnReference();
        txn.setTxnReference(TxnRef);
         log.info("going to call processStatus with txn:{}",txn);

        TransactionDTO response= paymentStatusService.processStatus(txn);
        log.info("Got response back from paymentStatusService :{}",response);
        // update DB PaymentStatus as CREATED
        //generate and return txnReference Id

       return response;
    }

    private String generateTxnReference() {
        return UUID.randomUUID().toString();
    }

    @Override

    public String initiatePayments(String txnRefs, InitiatePaymentReq paymentReq){
        log.info("InitiatePayments is Invoked");
        log.info("Testing for body which came from postman PaymentReq :{}",paymentReq);

        //make DB call to get txnDTO by using txnRefs
          TransactionDTO txnResDTO=transactionDao.getTransactionByTxnRef(txnRefs);
          log.info("Got txnResDTO from getTransactionByRef :{}",txnResDTO);
        // update DB PaymentStatus as INITIATED

        txnResDTO.setTxnStatus(TransactionStatusEnum.INITIATED.getName());

        paymentStatusService.processStatus(txnResDTO);

        //Call provider service get response means make RestClint request;
        InitiatePaymentDTO paymentDTO=mapper.map(paymentReq,InitiatePaymentDTO.class);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpRequest httpRequest= HttpRequest.builder()
                .method(HttpMethod.POST)
                .url(STRIPE_URL)
                .Headers(httpHeaders)
                .requestBody(paymentDTO)
                .build();


        try {
            ResponseEntity<String> httpResponse= httpServiceEngine.makeHttpCall(httpRequest);
            PaymentResDTO handledResponse= proccessReponse(httpResponse);

            txnResDTO.setTxnStatus(TransactionStatusEnum.PENDING.getName());
            txnResDTO.setProviderReference(handledResponse.getId());
            txnResDTO.setUrl(handledResponse.getUrl());
            paymentStatusService.processStatus(txnResDTO);

        } catch (ProccessingException e) {
            txnResDTO.setTxnStatus(TransactionStatusEnum.FAILED.getName());
            txnResDTO.setErrorCode(e.getErrorCode());
            txnResDTO.setErrorMessage(e.getErrorMessage());
            //TODO SET providerREf
            txnResDTO.setProviderReference(txnResDTO.getProviderReference());
            paymentStatusService.processStatus(txnResDTO);

            throw new RuntimeException(e);
        }



        return"";
    }

    private PaymentResDTO proccessReponse(ResponseEntity<String> httpResponse) {
        if(httpResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED)){
            log.info("Got HttpStatus as CREATED");
            String HttpRes=httpResponse.getBody();
            Gson gson=new Gson();
            PaymentRes paymentRes=gson.fromJson(HttpRes,PaymentRes.class);

            assert paymentRes != null;
            if(paymentRes!=null & paymentRes.getUrl()!=null){
             PaymentResDTO responseDTO=mapper.map(paymentRes,PaymentResDTO.class);

              log.info("Got valid response from StripeProvider");
              return responseDTO;
          }
          log.info("Got correct HttpStatus but not Url ");



        }

        return  null;


    }


}
