package com.jc.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jinxingjia 8/22/19 3:07 PM
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "test success!!!!";
    }

    @GetMapping("session/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String sessionInvalid(){
        return "session已失效，请重新认证";
    }

}
