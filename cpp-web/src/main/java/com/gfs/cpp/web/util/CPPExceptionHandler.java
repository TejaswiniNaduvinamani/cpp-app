package com.gfs.cpp.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.StatusDTO;

@ControllerAdvice
public class CPPExceptionHandler extends ResponseEntityExceptionHandler {

    public static final Logger log = LoggerFactory.getLogger(CPPExceptionHandler.class);

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex, WebRequest request) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCode(HttpStatus.FORBIDDEN.value());
        statusDTO.setErrorMessage(HttpStatus.FORBIDDEN.getReasonPhrase());
        log.error("AccessDeniedException caught : ", ex);
        return handleExceptionInternal(ex, statusDTO, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> exceptionHandler(Exception ex, WebRequest request) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        statusDTO.setErrorMessage("Unexpected exception occurred.");
        log.error("Exception caught  : ", ex);
        return handleExceptionInternal(ex, statusDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Object> exceptionHandler(RuntimeException runtimeException, WebRequest request) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        statusDTO.setErrorMessage("Unexpected exception occurred.");
        log.error(" RuntimeException caught: ", runtimeException);
        return handleExceptionInternal(runtimeException, statusDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = CPPRuntimeException.class)
    protected ResponseEntity<Object> exceptionHandler(CPPRuntimeException cppRuntimeException, WebRequest request) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCode(cppRuntimeException.getErrorCode());
        statusDTO.setErrorMessage(cppRuntimeException.getMessage());
        statusDTO.setErrorType(cppRuntimeException.getType().toString());
        log.error("CPPRuntimeException  caught: ", cppRuntimeException);
        return handleExceptionInternal(cppRuntimeException, statusDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
