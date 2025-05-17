package com.hulkhiretech.payments.Service.Impl;

import com.google.gson.Gson;
import com.hulkhiretech.payments.Constant.ErrorCodeEnum;
import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import com.hulkhiretech.payments.Exception.ProccessingException;
import com.hulkhiretech.payments.Http.HttpRequest;
import com.hulkhiretech.payments.Http.HttpServiceEngine;
import com.hulkhiretech.payments.Service.Interface.PaymentServiceInterface;
import com.hulkhiretech.payments.Service.Interface.PaymentStatusService;
import com.hulkhiretech.payments.dao.TransactionDao;
import com.hulkhiretech.payments.dto.InitiatePaymentDTO;
import com.hulkhiretech.payments.dto.PaymentResDTO;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.pojo.ErrorRes;
import com.hulkhiretech.payments.pojo.PaymentRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

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

    public TransactionDTO initiatePayments(String txnRefs, InitiatePaymentDTO paymentReqDTO){
        log.info("InitiatePayments is Invoked");
        log.info("Testing for body which came from postman PaymentReq :{}",paymentReqDTO);

        //make DB call to get txnDTO by using txnRefs
          TransactionDTO txnResDTO=transactionDao.getTransactionByTxnRef(txnRefs);
          log.info("Got txnResDTO from getTransactionByRef :{}",txnResDTO);

          if(txnResDTO==null){
              throw new ProccessingException(
                      ErrorCodeEnum.INVALID_TXN_REFERENCE.getErrorCode(),
                      ErrorCodeEnum.INVALID_TXN_REFERENCE.getErrorMessage(),
                      HttpStatus.BAD_REQUEST);
          }

        // update DB PaymentStatus as INITIATED

        txnResDTO.setTxnStatus(TransactionStatusEnum.INITIATED.getName());

        paymentStatusService.processStatus(txnResDTO);

        //Call provider service get response means make RestClint request;
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpRequest httpRequest= HttpRequest.builder()
                .method(HttpMethod.POST)
                .url(STRIPE_URL)
                .Headers(httpHeaders)
                .requestBody(paymentReqDTO)
                .build();


        try {
            ResponseEntity<String> httpResponse= httpServiceEngine.makeHttpCall(httpRequest);
            PaymentResDTO handledResponse= processResponse(httpResponse);

            txnResDTO.setTxnStatus(TransactionStatusEnum.PENDING.getName());
            txnResDTO.setProviderReference(handledResponse.getId());
            txnResDTO.setUrl(handledResponse.getUrl());
            paymentStatusService.processStatus(txnResDTO);
            log.info("DtO after calling processStatus as PENDING :{}",txnResDTO);

        } catch (ProccessingException e) {
            txnResDTO.setTxnStatus(TransactionStatusEnum.FAILED.getName());
            txnResDTO.setErrorCode(e.getErrorCode());
            txnResDTO.setErrorMessage(e.getErrorMessage());
            // SET providerREf
            txnResDTO.setProviderReference(txnResDTO.getProviderReference());
            paymentStatusService.processStatus(txnResDTO);

            //TODO clarify this part
            if(e.getErrorCode().equals("30001")){
                log.error("Error at Stripe Side ," +
                        "throwing our custom message and errorCode:{}",e);
                throw new ProccessingException(
                        ErrorCodeEnum.ERROR_AT_STRIPE_PSP.getErrorCode(),
                        ErrorCodeEnum.ERROR_AT_STRIPE_PSP.getErrorMessage(),
                        e.getHttpStatus());
            }

            throw new ProccessingException(
                    ErrorCodeEnum.GENERIC_ERROR.getErrorCode(),
                    ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }



        return txnResDTO;
    }




    private PaymentResDTO processResponse(ResponseEntity<String> httpResponse) {
        if(httpResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED)){
            log.info("Got HttpStatus as CREATED");

            Gson gson=new Gson();
            PaymentRes paymentRes=gson.fromJson(httpResponse.getBody(),PaymentRes.class);

            if(paymentRes!=null &&  paymentRes.getUrl()!=null){
             PaymentResDTO responseDTO=mapper.map(paymentRes,PaymentResDTO.class);

              log.info("Got valid response from StripeProvider");
              return responseDTO;
          }
          log.info("Got correct HttpStatus but not Url ");



        }
        ErrorRes errorRes=gson.fromJson(httpResponse.getBody(),ErrorRes.class);
        log.info("Converted Error response :{}",errorRes);

        if(errorRes!=null && errorRes.getErrorCode()!=null){
            throw new ProccessingException(errorRes.getErrorCode(),
                    errorRes.getErrorMessage(),
                    HttpStatus.valueOf(httpResponse.getStatusCode().value()));
        }

        throw new ProccessingException(
                ErrorCodeEnum.GENERIC_ERROR.getErrorCode(),
                ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);


    }


}
