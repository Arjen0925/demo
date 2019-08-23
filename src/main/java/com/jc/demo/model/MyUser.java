package com.jc.demo.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Jin xing jia
 * 2019-08-22 21:04
 **/
@Data
public class MyUser implements Serializable {
    private static final long serialVersionUID = 3497935890426858541L;

    private String username;

    private String password;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked= true;

    private boolean credentialsNonExpired= true;

    private boolean enabled= true;

}
