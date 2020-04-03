package com.blkchainsolutions.ogico.dh.pub.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserListDTO {
    private int userId;
    private  String firstName;
    private String lastName;
    private String email;
    private int state;
    private int role;
    private Timestamp lastLoginTime;
    private String token;

}
