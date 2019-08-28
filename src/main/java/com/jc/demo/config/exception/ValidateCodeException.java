package com.jc.demo.config.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @author Jin xing jia
 * 2019-08-25 17:48
 **/
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 5022575393500654458L;

    public ValidateCodeException(String message) {
        super(message);

    }
}
