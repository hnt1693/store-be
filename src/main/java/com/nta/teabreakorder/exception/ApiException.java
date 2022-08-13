package com.nta.teabreakorder.exception;

import com.nta.teabreakorder.enums.ErrorCode;

public class ApiException extends Exception{
    public ApiException(String message) {
        super(message);
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getLabel());
    }
}
