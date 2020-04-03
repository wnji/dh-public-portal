package com.blkchainsolutions.ogico.dh.pub.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SubscribeProductDTO {
    private int id;
    private String productId;
    private Timestamp creationDate;
    private String email;

}
