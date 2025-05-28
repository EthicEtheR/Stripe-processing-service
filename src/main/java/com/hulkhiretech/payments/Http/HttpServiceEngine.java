package com.hulkhiretech.payments.Http;

import com.hulkhiretech.payments.Constant.ErrorCodeEnum;
import com.hulkhiretech.payments.Exception.ProccessingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class HttpServiceEngine {

    private final RestClient restClient;



    public HttpServiceEngine(RestClient.Builder restClient){
        this.restClient=restClient.build();

    }
    @CircuitBreaker(name = "payment-processing-service",
            fallbackMethod = "fallbackProcessPayment")
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
            log.error("HttpErrorException occurred :"+e);

            if (e.getStatusCode().equals(HttpStatus.GATEWAY_TIMEOUT) ||
                    e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {

                log.error("Received error from 5xx statusCode :{}", e.getStatusCode());

                throw  new ProccessingException(
                        ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PS.getErrorCode(),
                        ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PS.getErrorMessage(),
                        HttpStatus.valueOf(e.getStatusCode().value())
                );
            }

            log.info("Returning getResponseBodyAsString :{}", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());


        } catch (ResourceAccessException e) {
            log.error("Connection error: unable to reach server. {}", e.getMessage());
            throw new ProccessingException(
                    ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PS.getErrorCode(),
                    ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PS.getErrorMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE
            );



        } catch (Exception e) {
            log.error("Generic Exception Happened :"+e);
            throw new ProccessingException(
                    ErrorCodeEnum.GENERIC_ERROR.getErrorCode(),
                    ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

        }



    }
    public ResponseEntity<String> fallbackProcessPayment(HttpRequest httpRequest, Throwable t) {
        // Handle fallback logic here
        log.error("Fallback method invoked due to exception:" + t.getMessage());
        throw new ProccessingException(
                ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PS.getErrorCode(),
                ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PS.getErrorMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
