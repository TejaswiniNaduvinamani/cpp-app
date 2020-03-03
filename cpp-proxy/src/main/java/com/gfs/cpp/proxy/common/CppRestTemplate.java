package com.gfs.cpp.proxy.common;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CppRestTemplate extends RestTemplate {

    @Value("${cpp.external.service.username}")
    private String serviceUserName;

    @Value("${cpp.external.service.password}")
    private String servicePassword;

    @PostConstruct
    public void setAuthenticaiton() {

        BasicAuthorizationInterceptor basicAuthenticationInterceptor = new BasicAuthorizationInterceptor(serviceUserName, servicePassword);
        getInterceptors().add(basicAuthenticationInterceptor);

    }

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(request, headers);
        return super.postForObject(url, httpEntity, responseType, uriVariables);
    }

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)  {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(request, headers);
        return super.postForObject(url, httpEntity, responseType, uriVariables);
    }

}
