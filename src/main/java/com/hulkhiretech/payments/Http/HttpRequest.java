package com.hulkhiretech.payments.Http;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Data
@Builder
public class HttpRequest {
    private HttpHeaders Headers;
    private HttpMethod method;
    private String url;
    private Object requestBody;

}
