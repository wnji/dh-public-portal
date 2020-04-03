package com.blkchainsolutions.ogico.dh.fabric.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HkRegistrationIdResponse {
    /*{
        "message": "Success",
            "status": 200,
            "data": {
        "message": "Success",
                "products": [
        {
            "id": "98ff6770-7295-11ea-b352-1fa8b53e26cd",
                "product": {
            "hKRegistrationID": "23071686",
                    "packSize": "0.5 ML",
                    "strength": "",
                    "productName": "FLUARIX TETRA 1'D(NH) TENDER",
                    "brandName": "FLUARIX TETRA NH GT",
                    "productCode": "",
                    "actualQuantity": 1,
                    "manufacturerID": "PFS",
                    "manufacturerName": "PFS",
                    "lastUpdateDate": "2020-03-30T22:49:03.000Z",
                    "publishOfficerID": "1",
                    "publishOfficerUsername": "test",
                    "createdAt": "2020-03-30T22:49:03.000Z",
                    "docType": "product"
        }
        }
        }
        ]
    }
    }*/
    private String message;
    private int status;
    private HkRegistrationIdData data=new HkRegistrationIdData();
    @Data
    public class HkRegistrationIdData{
        private String message;
        private List<ProductsData> products =new ArrayList<>();
    }
    @Data
    public class ProductsData{
        private String id;
        private ProductData product=new ProductData();
    }
    @Data
    public class ProductData{
        private   String hKRegistrationID;
        private   String        packSize;
        private   String         strength;
        private   String        productName;
        private   String         brandName;
        private   String        productCode;
        private   String        actualQuantity;
        private   String        manufacturerID;
        private   String       manufacturerName;
        private   String        lastUpdateDate;
        private   String        publishOfficerID;
        private   String        publishOfficerUsername;
        private   String        createdAt;
        private   String        docType;
    }
}
