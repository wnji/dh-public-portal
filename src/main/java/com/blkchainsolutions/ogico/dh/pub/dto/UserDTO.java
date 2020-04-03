package com.blkchainsolutions.ogico.dh.pub.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDTO {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int role;
    private int state;
    private Timestamp lastLoginTime;
    private String privatekey;
    private String publickey;
    private String passphrase;
    private String token;
    private String secretKey;
    private String preSecretKey;
    private int failed_count;
}
