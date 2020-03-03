package com.gfs.cpp.common.exception;

public class CPPRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CPPExceptionType type;

    public CPPRuntimeException(final CPPExceptionType type) {
        this.type = type;
    }

    public CPPRuntimeException(final CPPExceptionType type, final Throwable cause) {
        super(cause);
        this.type = type;
    }

    public CPPRuntimeException(final CPPExceptionType type, final String message) {
        super(message);
        this.type = type;
    }

    public CPPRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CPPExceptionType getType() {
        return type;
    }

    public Integer getErrorCode() {
        return type.getErrorCode();
    }
}
