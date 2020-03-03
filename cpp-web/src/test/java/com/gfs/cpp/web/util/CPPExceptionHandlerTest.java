package com.gfs.cpp.web.util;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.request.WebRequest;

import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.web.util.CPPExceptionHandler;

public class CPPExceptionHandlerTest {

    @Mock
    WebRequest request;

    @InjectMocks
    CPPExceptionHandler cppExceptionHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExceptionHandlerExceptionWebRequest() {
        ResponseEntity<Object> responseEntity = cppExceptionHandler.exceptionHandler(new Exception(), request);
        assertTrue(responseEntity.getStatusCodeValue() == 500);
    }

    @Test
    public void testExceptionHandlerRuntimeExceptionWebRequest() {
        ResponseEntity<Object> responseEntity = cppExceptionHandler.exceptionHandler(new RuntimeException(), request);
        assertTrue(responseEntity.getStatusCodeValue() == 500);
    }

    @Test
    public void testHandleAccessDeniedException() {
        ResponseEntity<Object> responseEntity = cppExceptionHandler.handleAccessDeniedException(new AccessDeniedException("test exception"), request);
        assertTrue(responseEntity.getStatusCodeValue() == 403);
    }

    @Test
    public void testHandleCPPRuntimeException() {
        ResponseEntity<Object> responseEntity = cppExceptionHandler
                .exceptionHandler(new CPPRuntimeException(CPPExceptionType.NOT_FOUND, "test exception"), request);
        assertTrue(responseEntity.getStatusCodeValue() == 500);
    }

    @Test
    public void testHandleCPPRuntimeExceptionForNotValid() {
        ResponseEntity<Object> responseEntity = cppExceptionHandler
                .exceptionHandler(new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS, "test exception"), request);
        assertTrue(responseEntity.getStatusCodeValue() == 500);
    }

}
