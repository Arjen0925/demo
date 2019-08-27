package com.jc.demo.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Jin xing jia
 * 2019-08-27 21:20
 **/
@Data
public class SmsCode {

    private String code;

    private LocalDateTime expireTime;

    public SmsCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public SmsCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
