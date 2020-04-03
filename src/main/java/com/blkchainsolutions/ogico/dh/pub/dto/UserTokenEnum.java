package com.blkchainsolutions.ogico.dh.pub.dto;


public enum UserTokenEnum {

   //  activated==00sfHDbvsj43sj3JChbf  already=reset==00sfHDbvsj43sj3JChbf
    ACTIVATION_TOKEN("activated==00sfHDbvsj43sj3JChbf"),ALREADY_REST_TOKEN("already=reset==00sfHDbvsj43sj3JChbf");
    private String code;

    UserTokenEnum(String s) {
        this.code=s;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

