package com.blkchainsolutions.ogico.dh.fabric.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductResponse {
 /*   {
        "message": "Success",
            "status": 200,
            "data": {
        "message": "Success",
                "products": [
        {
            "id": "e84389c0-61fa-11ea-92a6-7fad09b32dde",
                "product": {
            "hKRegistrationID": "hKRegistrationID",
                    "packSize": "packSize",
                    "strength": "strength",
                    "productName": "productName",
                    "brandName": "brandName",
                    "productCode": "productCode",
                    "actualQuantity": 2,
                    "createdAt": "2020-03-02T05:56:34.191Z",
                    "docType": "product"
        }
        }
        ]
    }
    }*/

    private String message;
    private int status;
    private ProductData data=new ProductData();
    @Data
    public class ProductData{
        private String message;
        private List<Products> products=new ArrayList<>();

    }
    @Data
    public class  Products{
        private String id;
        private Product product=new Product();
        private int alertDays;
        private int subscribe;
    }


    @Data
    public  class  Product{
        private String hKRegistrationID;
        private String packSize;
        private String strength;
        private String productName;
        private String brandName;
        private String productCode;
        private int actualQuantity;
        private String createdAt;
        private String docType;
    }
}
