package com.blkchainsolutions.ogico.dh.pub.dao.impl;

import com.blkchainsolutions.common.TimeSetup;
import com.blkchainsolutions.ogico.dh.pub.dao.UserDAO;
import com.blkchainsolutions.ogico.dh.pub.dto.UserDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.UserListDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.UserTokenEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDAOImpl implements UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public UserDTO checkLogin(String email, String pwd) {
        UserDTO userDTO = new UserDTO();

        log.info("checkLogin "+email);

        try {
            String query = "SELECT id,first_name,last_name,last_login_time,role,state,token,passphrase,failed_count FROM user WHERE email = ? and password = ?  and source=1";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<UserDTO>() {
                @Override
                public UserDTO doInPreparedStatement(PreparedStatement ps)
                        throws SQLException, DataAccessException {

                    ps.setString(1,email);
                    ps.setString(2,pwd);

                    ResultSet rs= ps.executeQuery();
                    if ( rs.next()) {
                        userDTO.setUserID(rs.getInt("id"));
                        userDTO.setFirstName(rs.getString("first_name"));
                        userDTO.setLastName(rs.getString("last_name"));
                        userDTO.setLastLoginTime(rs.getTimestamp("last_login_time"));
                        userDTO.setRole(rs.getInt("role"));
                        userDTO.setState(rs.getInt("state"));
                        userDTO.setEmail(email);
                        userDTO.setToken(rs.getString("token"));
                        userDTO.setPassphrase(rs.getString("passphrase"));
                        userDTO.setFailed_count(rs.getInt("failed_count"));
                        return userDTO;
                    }
                    return null;
                }
            });



        }catch(Exception e ) {
            log.error("error checkLogin: ", e);
        }

        return null;
    }

    @Override
    public boolean updateFailedCount(String login) {
        log.info("added fail count +1");

        try {
            String query = "UPDATE user SET failed_count = failed_count+1 WHERE email = ? and source=1";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {


                    ps.setString(1, login);

                    if (ps.executeUpdate() == 1) {
                        return true;
                    } else {
                        return false;
                    }

                }
            });

        } catch (Exception e) {
            log.error("error: ", e);
        }

        return false;
    }

    @Override
    public boolean updateLastLoginTime(String email) {
        try {
            String query = "UPDATE user SET last_login_time = ? WHERE email = ? and source=1";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps)
                        throws SQLException, DataAccessException {

                    ps.setTimestamp(1, TimeSetup.getTimestamp(), TimeSetup.getTimezone());
                    ps.setString(2, email);

                    if (ps.executeUpdate() == 1 ) {
                        return true;
                    }else {
                        return false;
                    }

                }
            });


        }catch(Exception e) {
            log.error("error updateLastLoginTime: ", e);
        }


        return false;
    }

    @Override
    public UserDTO getUserInfoT10(int userId) {
        UserDTO userDTO = new UserDTO();

        try {


            String query = "SELECT id,first_name,last_name,last_login_time, email, privatekey, publickey, secretKey,token, preSecretKey FROM user WHERE id = ? and source=1";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<UserDTO>() {
                @Override
                public UserDTO doInPreparedStatement(PreparedStatement ps)
                        throws SQLException, DataAccessException {

                    ps.setInt(1,userId);

                    ResultSet rs= ps.executeQuery();
                    if ( rs.next()) {

                        userDTO.setUserID(rs.getInt("id"));
                        userDTO.setFirstName(rs.getString("first_name"));
                        userDTO.setLastName(rs.getString("last_name"));
                        userDTO.setLastLoginTime(rs.getTimestamp("last_login_time"));
                        userDTO.setEmail(rs.getString("email"));
                        userDTO.setPrivatekey(rs.getString("privatekey"));
                        userDTO.setPublickey(rs.getString("publickey"));
                        userDTO.setSecretKey(rs.getString("secretKey"));
                        userDTO.setPreSecretKey(rs.getString("preSecretKey"));
                        userDTO.setToken(rs.getString("token"));
                        return userDTO;
                    }
                    return null;
                }
            });
        }catch(Exception e ) {
            log.info(String.valueOf(e));
        }


        return null;
    }

    @Override
    public boolean updatePreSecretKey(int id, String preSecretKey) {
        log.info("updatePreSecretKey");
        try {
            String query = "UPDATE user SET preSecretKey=?  WHERE id = ? and source=1";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps)
                        throws SQLException, DataAccessException {

                    ps.setString(1, preSecretKey);
                    ps.setInt(2, id);

                    if (ps.executeUpdate() == 1 ) {
                        log.info("updatePreSecretKey " + id);
                        return true;
                    }else {
                        return false;
                    }

                }
            });


        }catch(Exception e) {
            log.error("updatePreSecretKey", e);
        }

        return false;
    }

    @Override
    public boolean updateSecretKey(int id) {
        try {
            String query = "UPDATE user SET secretKey=preSecretKey WHERE id = ? and source=1";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

                    ps.setInt(1, id);

                    if (ps.executeUpdate() == 1) {
                        log.info("updateSecretKey " + id);
                        return true;
                    } else {
                        return false;
                    }

                }
            });

        } catch (Exception e) {
            log.error("updateSecretKey", e);
        }

        return false;
    }

    @Override
    public boolean checkEmailExist(String email) {
        log.info("check email exist");
        String query = "SELECT count(*) FROM user WHERE email = ? and source=1";
        log.info(query);
        int rs = jdbcTemplate.execute(query, new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

                ps.setString(1, email);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int i = rs.getInt(1);

                    return i;

                }
                return 0;
            }
        });

        if (rs >= 1) {
            log.info("email exist");
            return true;
        }

        return false;
    }

    @Override
    public boolean forgetPasswordDb(String email, String token) {
        try {
            String query = "UPDATE user SET token = ? ,token_time = ? WHERE email = ? and source=1";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

                    ps.setString(1, token);
                    ps.setTimestamp(2, TimeSetup.getTimestamp(), TimeSetup.getTimezone());
                    ps.setString(3, email);

                    if (ps.executeUpdate() == 1) {
                        log.info("initiated session id for forget password");
                        return true;
                    } else {
                        return false;
                    }

                }
            });

        } catch (Exception e) {
            log.error("error: ", e);
        }

        return false;
    }

    @Override
    public boolean resetPasswordDb(String password, String token) {

        try {
            String query = "UPDATE user SET password = ?,token=?,failed_count=0 Where token = ? AND token_time > ? and source=1";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps)
                        throws SQLException, DataAccessException {

                    ps.setString(1, password);
                    ps.setString(2, UserTokenEnum.ALREADY_REST_TOKEN.getCode());
                    ps.setString(3, token);
                    ps.setTimestamp(4, TimeSetup.getTimestampMinus10Mins(), TimeSetup.getTimezone());

                    if (ps.executeUpdate() == 1 ) {
                        return true;
                    }else {
                        return false;
                    }

                }
            });


        }catch(Exception e) {
            log.info(String.valueOf(e));
        }

        log.info("initiated session id for forget password");

        return false;
    }

    @Override
    public boolean modifyUserState(int userId, int state,int role) {
        log.info("modifyUserState");
        log.info("userId:"+userId);
        log.info("state:"+state);
        try{
            String query="UPDATE user SET state=?, role=? WHERE id=? and source=1";

            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setInt(1,state);
                    ps.setInt(2,role);
                    ps.setInt(3,userId);
                    if(ps.executeUpdate()==1){
                        return true;
                    }else {
                        return false;
                    }
                }
            });
        }catch (Exception e){
            log.error(" error on modifyUserState",e);
        }

        return false;
    }

    @Override
    public int activateAccountDb(UserDTO userDTO) {
        log.info("activateAccountDb");
        try {
            String query="SELECT id FROM user WHERE token =? and source=1";
            int id=jdbcTemplate.execute(query, new PreparedStatementCallback<Integer>() {
                @Override
                public Integer doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                    preparedStatement.setString(1,userDTO.getToken());
                    ResultSet rs=  preparedStatement.executeQuery();
                    if(rs.next()){
                        return rs.getInt(1);
                    }
                    return 0;
                }
            });
            log.info("UserId:",id);
            String query2="UPDATE user SET token=? WHERE token=? and source=1";
            boolean rs=jdbcTemplate.execute(query2,new PreparedStatementCallback<Boolean>(){
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1,UserTokenEnum.ACTIVATION_TOKEN.getCode());
                    ps.setString(2,userDTO.getToken());
                    if(ps.executeUpdate()==1){
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });
            if(rs){
                return 1;
            }
        }catch (Exception e){
            log.error("error:",e);
        }
        return 0;
    }

    @Override
    public boolean registerDB(UserDTO userDTO) {

        log.info("fill in data");

        String query = "INSERT INTO user (email, create_time, last_update_time, last_login_time, privatekey, publickey, passphrase, token, state, role,source)" + " values (?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {

                ps.setString(1,userDTO.getEmail());
                ps.setTimestamp(2, TimeSetup.getTimestamp(), TimeSetup.getTimezone());
                ps.setTimestamp(3, TimeSetup.getTimestamp(), TimeSetup.getTimezone());
                ps.setTimestamp(4, TimeSetup.getTimestamp(), TimeSetup.getTimezone());
                ps.setString(5,userDTO.getPrivatekey());
                ps.setString(6,userDTO.getPublickey());
                ps.setString(7,userDTO.getPassphrase());
                ps.setString(8,userDTO.getToken());
                ps.setInt(9, 0);
                ps.setInt(10, 1);
                ps.setInt(11,1);
                if (ps.executeUpdate() == 1 ) {
                    return true;
                }else {
                    return false;
                }
            }
        });
    }

    @Override
    public List<UserListDTO> getAllUser() {
        log.info("getUserList");
        List<UserListDTO> list = new ArrayList<>();
        try {
            String query = "SELECT id,email, last_name, first_name, last_login_time, privatekey, publickey, passphrase, token, state,role FROM user  WHERE source=1";

            return jdbcTemplate.execute(query, new PreparedStatementCallback<List<UserListDTO>>() {
                @Override
                public List<UserListDTO> doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {

                    ResultSet rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        UserListDTO userListDTO = new UserListDTO();
                        userListDTO.setEmail(rs.getString("email"));
                        userListDTO.setUserId(rs.getInt("id"));
                        userListDTO.setFirstName(rs.getString("first_name"));
                        userListDTO.setLastName(rs.getString("last_name"));
                        userListDTO.setRole(rs.getInt("role"));
                        userListDTO.setState(rs.getInt("state"));
                        userListDTO.setLastLoginTime(rs.getTimestamp("last_login_time"));
                        userListDTO.setToken(rs.getString("token"));
                        list.add(userListDTO);
                    }


                    return list;
                }
            });
        }catch (Exception e){
            log.error("error getUserList" ,e);
        }
        return list;
    }

    @Override
    public UserDTO checkEmail(String email) {
        log.info("checkEmail");
        String query="SELECT id,email FROM user WHERE email=? and source=1";
        UserDTO userDTO=new UserDTO();
        return jdbcTemplate.execute(query, new PreparedStatementCallback<UserDTO>() {
            @Override
            public UserDTO doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1,email);
                ResultSet rs=ps.executeQuery();
                if(rs.next()){
                    userDTO.setUserID(rs.getInt("id"));
                    userDTO.setEmail(rs.getString("email"));
                }
                return userDTO;
            }
        });
    }
    @Override
    public boolean resetFailedCount(String login) {
        try {
            String query = "UPDATE user SET failed_count=0 WHERE email = ? and  source=0";
            return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

                    ps.setString(1, login);

                    if (ps.executeUpdate() == 1) {
                        log.info("resetFailedCount");

                        return true;
                    } else {
                        return false;
                    }

                }
            });

        } catch (Exception e) {
            log.error("error resetFailedCount: ", e);
        }


        return false;
    }
}
