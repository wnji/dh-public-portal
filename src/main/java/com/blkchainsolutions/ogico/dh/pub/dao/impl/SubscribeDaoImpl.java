package com.blkchainsolutions.ogico.dh.pub.dao.impl;

import com.blkchainsolutions.common.TimeSetup;

import com.blkchainsolutions.ogico.dh.pub.dao.SubscribeDao;
import com.blkchainsolutions.ogico.dh.pub.dto.MySubscriptionDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.SubscribeProductDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.TaskSubscribeProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SubscribeDaoImpl implements SubscribeDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<MySubscriptionDTO> getMySubscriptionList(String email) {
        log.info("getMySubscriptionList email:"+email);
        String query="SELECT id,email,product_id,create_time FROM public_subscribe_product WHERE email =?";
        List<MySubscriptionDTO> list=new ArrayList<>();
        return jdbcTemplate.execute(query, new PreparedStatementCallback<List<MySubscriptionDTO>>() {
            @Override
            public List<MySubscriptionDTO> doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1,email);
                ResultSet rs=  preparedStatement.executeQuery();
                Boolean anyResults=false;
                while (rs.next()){
                    anyResults=true;
                    MySubscriptionDTO mySubscriptionDTO=new MySubscriptionDTO();
                    mySubscriptionDTO.setId(rs.getInt("id"));
                    mySubscriptionDTO.setEmail(rs.getString("email"));
                    mySubscriptionDTO.setCreateTime(rs.getTimestamp("create_time"));
                    mySubscriptionDTO.setProductId(rs.getString("product_id"));
                    list.add(mySubscriptionDTO);
                }
                if(!anyResults){
                    log.info("no to do list for this search");
                }
                return list;
            }
        });
    }

    @Override
    @Transactional
    public Boolean addSubscribeProduct(SubscribeProductDTO subscribeProductDTO) {
        log.info("addSubscribeProduct");
        try {
            String query2="DELETE FROM public_subscribe_product WHERE  email=? and product_id=?";
            jdbcTemplate.execute(query2, new PreparedStatementCallback<Object>() {
                @Override
                public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1,subscribeProductDTO.getEmail());
                    ps.setString(2,subscribeProductDTO.getProductId());
                    ps.executeUpdate();
                    return ps.executeUpdate();
                }
            });
            String query="INSERT INTO public_subscribe_product(email,product_id,create_time)VALUES(?,?,?)";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1,subscribeProductDTO.getEmail());
                    ps.setString(2,subscribeProductDTO.getProductId());
                    ps.setTimestamp(3, TimeSetup.getTimestamp(), TimeSetup.getTimezone());
                    if(ps.executeUpdate()>0){
                        return true;
                    }
                    return false;
                }
            });

        }catch (Exception e){
            log.error("error on addSubscribeProduct",e);
        }
        return false;
    }

    @Override
    public Boolean cancelSubscribeProduct(SubscribeProductDTO subscribeProductDTO) {

        log.info("cancelSubscribeProduct");
        try {
            String query="DELETE FROM public_subscribe_product WHERE product_id=? and email=?";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1,subscribeProductDTO.getProductId());
                    ps.setString(2,subscribeProductDTO.getEmail());
                    if(ps.executeUpdate()>0){
                        return true;
                    }
                    return false;
                }
            });

        }catch (Exception e){
            log.error("error on cancelSubscribeProduct",e);
        }
        return false;
    }

    @Override
    public Boolean saveSubscribeBatch(String batchId, String email, int alertDays) {

        log.info("saveSubscribeBatch");
        try {
            String query2="DELETE FROM public_suberibe_batch WHERE  email=? and batch_id=?";
            jdbcTemplate.execute(query2, new PreparedStatementCallback<Object>() {
                @Override
                public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1,email);
                    ps.setString(2,batchId);
                    ps.executeUpdate();
                    return ps.executeUpdate();
                }
            });
            String query="INSERT INTO public_suberibe_batch(email,create_time,batch_id,alert_days)VALUES(?,?,?,?)";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1,email);
                    ps.setTimestamp(2, TimeSetup.getTimestamp(), TimeSetup.getTimezone());
                    ps.setString(3,batchId);
                    ps.setInt(4,alertDays);

                    if(ps.executeUpdate()>0){
                        return true;
                    }
                    return false;
                }
            });

        }catch (Exception e){
            log.error("error on saveSubscribeBatch",e);
        }
        return false;
    }

    @Override
    public Boolean cancelSubscribeBatch(String batchId,  String email) {
        log.info("cancelSubscribeBatch");
        try {
            String query="DELETE FROM public_suberibe_batch WHERE batch_id=? and email=?";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1,batchId);
                    ps.setString(2,email);
                    if(ps.executeUpdate()>0){
                        return true;
                    }
                    return false;
                }
            });

        }catch (Exception e){
            log.error("error on cancelSubscribeBatch",e);
        }
        return false;
    }


}
