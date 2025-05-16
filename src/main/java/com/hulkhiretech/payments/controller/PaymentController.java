package com.hulkhiretech.payments.controller;

import com.hulkhiretech.payments.Service.Interface.PaymentServiceInterface;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.pojo.CreatePaymentRequest;
import com.hulkhiretech.payments.pojo.CreatePaymentResponse;
import com.hulkhiretech.payments.pojo.InitiatePaymentReq;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payments")
@Slf4j
public class PaymentController {

    private final ModelMapper mapper;
    private final PaymentServiceInterface paymentService;

    public PaymentController(ModelMapper mapper,PaymentServiceInterface paymentService){
        this.mapper=mapper;
        this.paymentService=paymentService;

    }

    @PostMapping
    public ResponseEntity<CreatePaymentResponse> createPayments(@RequestBody CreatePaymentRequest createPaymentRequest){
        log.info("Invoked createPayments in Controller ||CreatePaymentRequest:" +createPaymentRequest);


        TransactionDTO request=mapper.map(createPaymentRequest,TransactionDTO.class);
        log.info("Converting pojo to dto ||Request: "+request);
        TransactionDTO response=paymentService.createPayments(request);
        // Creating PaymentResponse
        CreatePaymentResponse createPaymentResponse=new CreatePaymentResponse();
        createPaymentResponse.setTxnReference(response.getTxnReference());
        createPaymentResponse.setTxnStatus(response.getTxnStatus());


        return new ResponseEntity<>(createPaymentResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{txnReference}/initiate")
    public ResponseEntity<String> initiatePayments(@PathVariable String txnReference,
                                                   @RequestBody InitiatePaymentReq initiatePaymentReq){
        log.info("Invoked initiatePayment in controller ||txnReference:" +txnReference);

        paymentService.initiatePayments(txnReference,initiatePaymentReq);


        return new ResponseEntity<>("Returning from Initiate",HttpStatus.OK);
    }

}
