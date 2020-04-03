package com.blkchainsolutions.ogico.dh.pub.dao;

import com.blkchainsolutions.ogico.dh.pub.dto.MySubscriptionDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.SubscribeProductDTO;


import java.util.List;

public interface SubscribeDao {

    public List<MySubscriptionDTO> getMySubscriptionList(String email);
    public Boolean addSubscribeProduct(SubscribeProductDTO subscribeProductDTO);
    public Boolean cancelSubscribeProduct(SubscribeProductDTO subscribeProductDTO);
    public Boolean saveSubscribeBatch(String batchId, String email,int alertDays);

    public Boolean cancelSubscribeBatch(String batchId, String email);
}
