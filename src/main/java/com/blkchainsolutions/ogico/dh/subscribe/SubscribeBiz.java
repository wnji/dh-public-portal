package com.blkchainsolutions.ogico.dh.subscribe;


import com.blkchainsolutions.ogico.dh.fabric.vo.BatchByNumberResponse;
import com.blkchainsolutions.ogico.dh.fabric.vo.BatchNumberDetailsResponse;
import com.blkchainsolutions.ogico.dh.fabric.vo.ProductResponse;
import com.blkchainsolutions.ogico.dh.fabric.vo.ProductVo;
import com.blkchainsolutions.ogico.dh.pub.dto.MySubscriptionDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.SubscribeProductDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SubscribeBiz {
    public int addSubscribeProduct(String email,String batchNumber ) throws IOException;
    public boolean addSubscribeProduct(SubscribeProductDTO subscribeProductDTO, String productName);
    public boolean cancelSubscribeProduct(SubscribeProductDTO subscribeProductDTO,String productName);
    public Map<String ,Object> productById( String productId) throws IOException;
    public List<MySubscriptionDTO> getMySubscriptionList(String email);
    public ProductResponse searchProductList(ProductVo productVo) throws IOException;
    public BatchByNumberResponse batchByNumber(String number) throws IOException;
    public BatchByNumberResponse batchAll() throws IOException;

    public BatchNumberDetailsResponse batchNumberDetails(String batchNumberId) throws IOException;

    public Boolean saveSubscribeBatch(String batchId, String productName,String email,int alertDays);

    public Boolean cancelSubscribeBatch(String batchId, String productName,@RequestParam("email")String email);
}