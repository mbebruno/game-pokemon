package com.game.conf;

import com.game.enums.ApplicationError;
import com.game.exception.ApplicationException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RestTemplateErrorInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        final ClientHttpResponse response = execution.execute(request, body);
        if (!response.getStatusCode().is2xxSuccessful()){
            throw new ApplicationException(ApplicationError.DEFAULT_REST_TEMPLATE_ERROR);
        }
        return response;
    }
}
