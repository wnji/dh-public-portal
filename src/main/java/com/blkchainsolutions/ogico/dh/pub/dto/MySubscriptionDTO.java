package com.blkchainsolutions.ogico.dh.pub.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MySubscriptionDTO {
    private int id;
    private String productId;
    private Timestamp createTime;
    private String email;
    private String productName;
}
