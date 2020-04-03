package com.blkchainsolutions.ogico.dh.fabric.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BatchByProductIdResponse {
/*    {
        "message": "Success",
            "status": 200,
            "data": {
        "batches": [
        {
            "id": "59ac21e0-6998-11ea-ac31-5ddb0ae81c78",
                "batch": {
            "batchNumber": "42i73423",
                    "productID": "38dfda40-6823-11ea-bff4-e5add5638e8c",
                    "creationDate": "2020-06-03T05:10:52Z",
                    "expiryDate": "2020-03-03T05:10:52Z",
                    "docType": "batch"
        }
        }
        ],
        "message": "Success"
    }
    }*/

    private String message;
    private int status;
    private BatchesData data=new BatchesData();
    @Data
    public class BatchesData{
        private   String message;
        private List<Batches> batches=new ArrayList<>();
    }

    @Data
    public class Batches{
        private   String id;
        private Batch batch=new Batch();
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
