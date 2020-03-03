package com.gfs.cpp.proxy.common;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;

import com.gfs.cpp.proxy.common.CppRestTemplate;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class CppRestTemplateTest {

    @Spy
    @InjectMocks
    private CppRestTemplate target;

    @Test
    public void shouldAddAuthenticationInterceptor() throws Exception {

        int existingInterceptorSize = target.getInterceptors().size();

        String userName = "user1234";
        ReflectionTestUtils.setField(target, "serviceUserName", userName);
        String password = "pass1234";
        ReflectionTestUtils.setField(target, "servicePassword", password);

        target.setAuthenticaiton();

        assertThat(target.getInterceptors().size(), equalTo(existingInterceptorSize + 1));

        BasicAuthorizationInterceptor actualInterceptor = (BasicAuthorizationInterceptor) target.getInterceptors().get(0);

        String actualUserName = (String) ReflectionTestUtils.getField(actualInterceptor, "username");
        String actualPassword = (String) ReflectionTestUtils.getField(actualInterceptor, "password");

        assertThat(actualUserName, equalTo(userName));
        assertThat(actualPassword, equalTo(password));
    }

    @Test
    public void shouldImplementPostForObject() {

        String url = "URL";
        Object request = new Object();
        Class<String> responseType = String.class;
        Object uriVariable = new Object();
        Object[] uriVariables = new Object[] { uriVariable };

        String response = "X";
        doReturn(response).when(target).execute(eq(url), eq(HttpMethod.POST), any(RequestCallback.class), any(ResponseExtractor.class),
                eq(uriVariable));

        String result = target.postForObject(url, request, responseType, uriVariables);
        assertThat(result, is(response));
        verify(target).execute(eq(url), eq(HttpMethod.POST), any(RequestCallback.class), any(ResponseExtractor.class), eq(uriVariable));
    }

    @Test
    public void shouldImplementPostForObjectWithMap() {

        String url = "URL";
        Object request = new Object();
        Class<String> responseType = String.class;
        Map<String, ?> uriVariables = new HashMap<>();

        String response = "X";
        doReturn(response).when(target).execute(eq(url), eq(HttpMethod.POST), any(RequestCallback.class), any(ResponseExtractor.class),
                eq(uriVariables));

        String result = target.postForObject(url, request, responseType, uriVariables);
        assertThat(result, is(response));

        verify(target).execute(eq(url), eq(HttpMethod.POST), any(RequestCallback.class), any(ResponseExtractor.class), eq(uriVariables));
    }

}
