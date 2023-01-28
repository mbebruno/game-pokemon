package com.game.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()));
        restTemplate.getInterceptors().add(new RestTemplateErrorInterceptor());


        return restTemplate;
    }

    public ObjectMapper objectMapper() {

        return new ObjectMapper();
    }

}
