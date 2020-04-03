package com.blkchainsolutions.ogico.dh.subscribe.impl;

import com.blkchainsolutions.common.EmailContent;


import com.blkchainsolutions.ogico.dh.email.EmailBiz;
import com.blkchainsolutions.ogico.dh.fabric.FabricSDK;

import com.blkchainsolutions.ogico.dh.fabric.vo.*;
import com.blkchainsolutions.ogico.dh.pub.dao.SubscribeDao;
import com.blkchainsolutions.ogico.dh.pub.dao.UserDAO;
import com.blkchainsolutions.ogico.dh.pub.dto.EmailDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.MySubscriptionDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.SubscribeProductDTO;

import com.blkchainsolutions.ogico.dh.subscribe.SubscribeBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SubscribeBizImp implements SubscribeBiz {
    @Autowired
    private SubscribeDao subscribeDao;
    @Autowired
    private EmailBiz emailBiz;



    @Override
    public int addSubscribeProduct(String email, String HkRegistrationId) throws IOException {
        HkRegistrationIdResponse response=  FabricSDK.productByHkRegistrationId(HkRegistrationId);
        if (response.getData().getProducts().size()<=0){
            return 2;
        }
        boolean rs=false;
        for(int i=0;i<response.getData().getProducts().size();i++){
            SubscribeProductDTO subscribeProductDTO=new SubscribeProductDTO();
            subscribeProductDTO.setProductId(response.getData().getProducts().get(i).getId());
            subscribeProductDTO.setEmail(email);
             rs= subscribeDao.addSubscribeProduct(subscribeProductDTO);
        }
        if(rs){
            this.endEmail( email,EmailContent.titleSubscribe ,EmailContent.contentSubscribe.replace("#1",HkRegistrationId));
            return 1;
        }
        return 0;
    }


    @Override
    public  Map<String ,Object>  productById(String productId) throws IOException {
        Map<String ,Object> map =new HashMap<>();
        ProductByIdResponse productByIdResponse = FabricSDK.productById(productId);
       // BatchByProductIdResponse batchByProductIdResponse=FabricSDK.batchByProductId(productId);
        map.put("product",productByIdResponse.getData().getProduct());
       // map.put("batches",batchByProductIdResponse.getData().getBatches());
        return  map;

    }

    private void   endEmail(String email,String  title ,String content){
        log.info("endEmail:" +email);
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setTo(email);
        emailDTO.setTitle(title);
        emailDTO.setContent(content);
        emailDTO.setQrCodePath(null);
        emailDTO.setIpdCert(null);
        emailDTO.setBccList(null);
        try {
            if(emailBiz.sendEmail(emailDTO)) {
                log.info("Email to owner to activate or disapprove addSubscribeProduct");
            }
        }catch(Exception e) {
            log.info("error" + e);

        }
    }
    @Override
    public List<MySubscriptionDTO> getMySubscriptionList(String email) {
        List<MySubscriptionDTO> list= subscribeDao.getMySubscriptionList(email);
        List<MySubscriptionDTO> mySubscriptionDTOList=new ArrayList<>();
        if(list.size()>0){
            try {
                for(int i=0;i<list.size();i++) {
                    MySubscriptionDTO mySubscriptionDTO = new MySubscriptionDTO();
                    mySubscriptionDTO.setId(list.get(i).getId());
                    mySubscriptionDTO.setProductId(list.get(i).getProductId());
                    mySubscriptionDTO.setEmail(list.get(i).getEmail());
                    mySubscriptionDTO.setCreateTime(list.get(i).getCreateTime());
                    ProductByIdResponse productByIdResponse = FabricSDK.productById(list.get(i).getProductId());
                    mySubscriptionDTO.setProductName(productByIdResponse.getData().getProduct().getProductName());
                    mySubscriptionDTOList.add(mySubscriptionDTO);
                }
            } catch (Exception e) {
                log.error("getMySubscriptionList FabricSDK error",e);
            }

        }
        return mySubscriptionDTOList;
    }

    @Override
    public ProductResponse searchProductList(ProductVo productVo) throws IOException {
        ProductResponse productResponse =  FabricSDK.productDynamicSearch(productVo);
        return productResponse;
    }

    @Override
    public boolean addSubscribeProduct(SubscribeProductDTO subscribeProductDTO,String productName) {
        boolean rs= subscribeDao.addSubscribeProduct(subscribeProductDTO);
        if(rs){
            this.endEmail(subscribeProductDTO.getEmail(),EmailContent.titleSubscribe,EmailContent.contentSubscribe.replace("#1", productName ));
        }
        return rs;
    }
    @Override
    public boolean cancelSubscribeProduct(SubscribeProductDTO subscribeProductDTO,String productName) {
        boolean rs= subscribeDao.cancelSubscribeProduct(subscribeProductDTO);
        if(rs){
            this.endEmail(subscribeProductDTO.getEmail(),EmailContent.titleSubscribe,EmailContent.contentNotSubscribe.replace("#1", productName ));
        }
        return rs;
    }

    @Override
    public BatchByNumberResponse batchByNumber(String number) throws IOException {
        BatchByNumberResponse response = FabricSDK.batchByNumber(number);
        for (int i=0;i<response.getData().getBatches().size();i++){
            ProductByIdResponse rs=  FabricSDK.productById(response.getData().getBatches().get(i).getBatch().getProductID());
            if(rs.getData().getProduct().getProductName()!=null){
                response.getData().getBatches().get(i).setProduct(rs.getData().getProduct());
            }else {
                ProductByIdResponse product=new ProductByIdResponse();
                response.getData().getBatches().get(i).setProduct(product.getData().getProduct());
            }

        }
        return response;
    }

    @Override
    public BatchByNumberResponse batchAll() throws IOException {
        BatchByNumberResponse response = FabricSDK.batchAll();
        for (int i=0;i<response.getData().getBatches().size();i++){
            ProductByIdResponse rs=  FabricSDK.productById(response.getData().getBatches().get(i).getBatch().getProductID());
            if(rs.getData().getProduct().getProductName()!=null){
                response.getData().getBatches().get(i).setProduct(rs.getData().getProduct());
            }else {
                ProductByIdResponse product=new ProductByIdResponse();
                response.getData().getBatches().get(i).setProduct(product.getData().getProduct());
            }
        }
        return response;
    }

    @Override
    public BatchNumberDetailsResponse batchNumberDetails(String batchNumberId) throws IOException {
        BatchNumberDetailsResponse response = FabricSDK.batchById(batchNumberId);
        response.getData().setId(batchNumberId);
        if(response.getData().getBatch().getProductID()!=null){
            ProductByIdResponse rs=FabricSDK.productById(response.getData().getBatch().getProductID());
            if(rs.getStatus()==200){
                response.getData().setProduct(rs.getData().getProduct());
            }else {
                response.getData().setProduct(new ProductByIdResponse().getData().getProduct());
            }
        }
        return response;
    }

    @Override
    public Boolean saveSubscribeBatch(String batchId, String productName, String email, int alertDays) {
       Boolean rs = subscribeDao.saveSubscribeBatch(batchId,email,alertDays);
       if(rs){
          String content= EmailContent.contentExpiryAlert.replace("#1",productName);
               content=content.replace("#2",String.valueOf(alertDays));
            this.endEmail(email,EmailContent.titleSubscribe,content);
       }

        return rs;
    }

    @Override
    public Boolean cancelSubscribeBatch(String batchId, String productName, String email) {
        Boolean rs = subscribeDao.cancelSubscribeBatch(batchId,email);
        if(rs){
            String content= EmailContent.contentSubscribeBatch.replace("#1",productName);
            this.endEmail(email,EmailContent.titleSubscribe,content);
        }
        return rs;
    }
}
