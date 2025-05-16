package com.hulkhiretech.payments.Http;

import com.hulkhiretech.payments.Constant.ErrorCodeEnum;
import com.hulkhiretech.payments.Exception.ProccessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class HttpServiceEngine {

    private final RestClient restClient;



    public HttpServiceEngine(RestClient.Builder restClient){
        this.restClient=restClient.build();

    }


    public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest){
        log.info("Into HttpServiceEngine");

        //make a API call by help of restClint
        try {
            ResponseEntity<String> response =restClient.method(httpRequest.getMethod())
                    .uri(httpRequest.getUrl())
                    .headers(headers -> headers.addAll(httpRequest.getHeaders()))
                    .body(httpRequest.getRequestBody()).retrieve().toEntity(String.class);

            log.info("Got Successful Response :{}",response);
            return response;


        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HttpErrorException occurred");

            if (e.getStatusCode().equals(HttpStatus.GATEWAY_TIMEOUT) ||
                    e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {

                log.error("Received error from 5xx statusCode :{}", e.getStatusCode());

                throw  new ProccessingException(
                        ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorCode(),
                        ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorMessage(),
                        HttpStatus.valueOf(e.getStatusCode().value())
                );
            }

            log.info("Returning getResponseBodyAsString :{}", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());


        } catch (Exception e) {
            log.error("Generic Exception Happened :{}",e);
            throw new ProccessingException(
                    ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorCode(),
                    ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

        }



    }
}
