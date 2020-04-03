package com.blkchainsolutions.ogico.dh.fabric.vo;

import lombok.Data;

@Data
public class ProductByIdResponse {
    /*   {
           "message": "Success",
               "status": 200,
               "data": {
           "message": "Success",
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
       }*/
    private String message;
    private int status;
    private ProductData data=new ProductData();
    @Data
    public class ProductData{
        private String message;
        private Product product=new Product();
    }
    @Data
    public class Product{
        private  String hKRegistrationID;
        private  String          packSize;
        private  String         strength;
        private  String          productName;
        private  String         brandName;
        private  String        productCode;
        private  String         actualQuantity;
        private  String        createdAt;
        private  String         docType;
    }

}
