package com.blkchainsolutions.ogico.dh.fabric;

import com.blkchainsolutions.Configure;
import com.blkchainsolutions.common.FabricHttpUtil;
import com.blkchainsolutions.common.JSONHelper;

import com.blkchainsolutions.ogico.dh.fabric.vo.*;
import com.blkchainsolutions.ogico.dh.pub.dao.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Component
public class FabricSDK {

    private static UserDAO userDAO;

    @Autowired
    public UserDAO tuserDAO;

    @Autowired
    public Configure configure=null;

    private static Configure config=null;


    @PostConstruct
    public void init() {
        config = configure;
        FabricSDK.userDAO = tuserDAO;
    }

    public static BatchByNumberResponse batchByNumber(String number) throws IOException {
        String responseBody= FabricHttpUtil.httpGet(config.getChainUrl()+"/batch/by-number?number="+number);
        BatchByNumberResponse response= (BatchByNumberResponse) JSONHelper.toObject(responseBody,BatchByNumberResponse.class);
        return response;
    }
    public static ProductByIdResponse productById(String id) throws IOException {
        String responseBody= FabricHttpUtil.httpGet(config.getChainUrl()+"/product/by-id?id="+id);
        ProductByIdResponse response= (ProductByIdResponse) JSONHelper.toObject(responseBody,ProductByIdResponse.class);
        return response;
    }
    public static ProductResponse productDynamicSearch(ProductVo productVo) throws IOException {
        String requestJson=JSONHelper.toJsonString(productVo);
        String responseBody= FabricHttpUtil.httpPOST(requestJson,config.getChainUrl()+"/product/dynamic-search");
        ProductResponse response= (ProductResponse) JSONHelper.toObject(responseBody,ProductResponse.class);
        return response;
    }
    public static BatchByProductIdResponse batchByProductId(String id) throws IOException {
        String responseBody= FabricHttpUtil.httpGet(config.getChainUrl()+"/batch/by-product-id?sort_type=asc&id="+id);
        BatchByProductIdResponse response= (BatchByProductIdResponse) JSONHelper.toObject(responseBody,BatchByProductIdResponse.class);
        return response;
    }
    public static BatchByNumberResponse batchAll() throws IOException {
        String responseBody= FabricHttpUtil.httpGet(config.getChainUrl()+"/batch/all?sortType=asc");
        BatchByNumberResponse response= (BatchByNumberResponse) JSONHelper.toObject(responseBody,BatchByNumberResponse.class);
        return response;
    }

    public static BatchNumberDetailsResponse batchById(String id) throws IOException {
        String responseBody= FabricHttpUtil.httpGet(config.getChainUrl()+"/batch/by-id?id="+id);
        BatchNumberDetailsResponse response= (BatchNumberDetailsResponse) JSONHelper.toObject(responseBody,BatchNumberDetailsResponse.class);
        return response;
    }
    public static  HkRegistrationIdResponse productByHkRegistrationId(String id) throws IOException {
        String responseBody= FabricHttpUtil.httpGet(config.getChainUrl()+"/product/by-hk-registration-id?id="+id);
        HkRegistrationIdResponse response= (HkRegistrationIdResponse) JSONHelper.toObject(responseBody,HkRegistrationIdResponse.class);
        return response;
    }
}
