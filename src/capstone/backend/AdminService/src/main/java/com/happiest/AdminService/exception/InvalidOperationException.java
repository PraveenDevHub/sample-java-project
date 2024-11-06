package com.happiest.AdminService.exception;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.utility.RBundle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOperationException extends RuntimeException {
//    public InvalidOperationException(){
//        super(RBundle.getKey(Constants.ERROR_INVALID_OPERATION));
//    }
    public InvalidOperationException(String message) {
        super(message);
    }
}

