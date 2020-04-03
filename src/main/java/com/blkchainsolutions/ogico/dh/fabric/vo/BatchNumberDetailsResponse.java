package com.blkchainsolutions.ogico.dh.fabric.vo;

import lombok.Data;

@Data
public class BatchNumberDetailsResponse {
/*    {
        "message": "Success",
            "status": 200,
            "data": {
        "batch": {
            "batchNumber": "batchNumber",
                    "productID": "pro",
                    "creationDate": "",
                    "expiryDate": "2020-03-03T05:10:52Z",
                    "docType": "batch"
        },
        "message": "Success"
    }
    }*/

    private String message;
    private int status;
    private BatchesData data=new BatchesData();
    @Data
    public class BatchesData{
        private   String message;
        private  Batch batch=new Batch();
        private ProductByIdResponse.Product product;
        private String id;
    }
    @Data
    public class Batch{
        private String  batchNumber;
        private String  productID;
        private String  creationDate;
        private String  expiryDate;
        private String  docType;
    }

}
