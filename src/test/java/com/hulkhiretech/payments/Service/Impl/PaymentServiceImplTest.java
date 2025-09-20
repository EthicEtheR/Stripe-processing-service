package com.hulkhiretech.payments.Service.Impl;

import com.google.gson.Gson;
import com.hulkhiretech.payments.Constant.ErrorCodeEnum;
import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import com.hulkhiretech.payments.Exception.ProccessingException;
import com.hulkhiretech.payments.Http.HttpServiceEngine;
import com.hulkhiretech.payments.Service.Interface.PaymentStatusService;
import com.hulkhiretech.payments.dao.TransactionDao;
import com.hulkhiretech.payments.dto.InitiatePaymentDTO;
import com.hulkhiretech.payments.dto.PaymentResDTO;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.pojo.PaymentRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @Mock
    private PaymentStatusService paymentStatusService;
    @Mock
    private   TransactionDao transactionDao;
    @Mock
    private  HttpServiceEngine httpServiceEngine;
    @Mock
    private ModelMapper mapper;
    @Mock
    private Gson gson;



    @InjectMocks
    private PaymentServiceImpl paymentService;

   @Test
    public void testcreatePayment(){
        log.info("Test case for createPayment method :{}",paymentService);

        //Arrange
        TransactionDTO txnDTO =new TransactionDTO();

        //Act
        paymentService.createPayments(txnDTO);

        //Assert
        assertEquals(TransactionStatusEnum.CREATED.getName(),txnDTO.getTxnStatus());
        assertNotNull(txnDTO.getTxnReference());
    }

    @Test
    public void testInitiatePaymentNullObj(){
        log.info("Test cast for initiatePayment when object is Null");
        //Arrange
        InitiatePaymentDTO paymentDTO=new InitiatePaymentDTO();
        String txnRef="";

        //Act
        ProccessingException proccessingException=assertThrows(
                ProccessingException.class,()->
                        paymentService.initiatePayments(txnRef,paymentDTO)
        );

        //Assert
        assertNotNull(proccessingException);
        assertEquals(ErrorCodeEnum.INVALID_TXN_REFERENCE.getErrorCode(),
                proccessingException.getErrorCode());
        assertEquals(ErrorCodeEnum.INVALID_TXN_REFERENCE.getErrorMessage(),
                proccessingException.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST,proccessingException.getHttpStatus());



    }


    @Test
    public void testInitiatePaymentNotNullObj(){
        log.info("Test cast for initiatePayment when object is Not Null");
        //Arrange
        InitiatePaymentDTO paymentDTO=new InitiatePaymentDTO();
        String txnRef="tx123";
        TransactionDTO transactionDTO=new TransactionDTO();
        ResponseEntity<String> httpResponse= new ResponseEntity<>("", HttpStatus.CREATED);



        when(transactionDao.getTransactionByTxnRef(txnRef)).thenReturn(transactionDTO);
        when(httpServiceEngine.makeHttpCall(any())).thenReturn(httpResponse);
        PaymentRes paymentRes=new PaymentRes();
        paymentRes.setUrl("https//Test.com");
        when(gson.fromJson(anyString(), eq(PaymentRes.class))).thenReturn(paymentRes);
        PaymentResDTO responseDTO=new PaymentResDTO();
        responseDTO.setId("983");
        responseDTO.setUrl("Http//www.test.com");
        when(mapper.map(paymentRes,PaymentResDTO.class)).thenReturn(responseDTO);



        //Act
        TransactionDTO txnDTORES =paymentService.initiatePayments(txnRef,paymentDTO);

        //Assert
        assertNotNull(txnDTORES);
        assertEquals(TransactionStatusEnum.PENDING.getName(),
                txnDTORES.getTxnStatus());
        assertEquals("983",txnDTORES.getProviderReference());
        assertEquals("Http//www.test.com",txnDTORES.getUrl());





    }

}
