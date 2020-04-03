package com.blkchainsolutions.ogico.dh.fabric.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BatchByNumberResponse {
/*
    {
        "message": "Success",
            "status": 200,
            "data": {
        "batches": [
        {
            "id": "b92753e0-68f5-11ea-bff4-e5add5638e8c",
                "batch": {
            "batchNumber": "batchNumber",
                    "productID": "",
                    "creationDate": "",
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
private BatchByData data=new BatchByData();
@Data
public class BatchByData{
    private String message;
    private List<Batches> batches=new ArrayList<>();
}
@Data
public class Batches{
    private String id;
    private Batch  batch=new Batch();
    private ProductByIdResponse.Product product;
}
@Data
public class Batch{
   private  String batchNumber;
    private  String         productID;
    private  String        creationDate;
    private  String       expiryDate;
    private  String       docType;
}
}
