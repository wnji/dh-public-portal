package com.blkchainsolutions.ogico.dh.fabric.vo;

import lombok.Data;

import java.util.Map;

@Data
public class ProductVo {
    private Selector selector=new Selector();
    @Data
    public class Selector{
        private Map hKRegistrationID;
        private String packSize;
        private Map strength;
        private Map productName;
        private Map brandName;
        private Map productCode;
        private Integer actualQuantity;
        private Map createdAt;
    }
}
