package com.blkchainsolutions.ogico.dh.pub.dao;

import com.blkchainsolutions.ogico.dh.pub.dto.UserDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.UserListDTO;

import java.util.List;

public interface UserDAO {
    public UserDTO checkLogin(String email, String pwd);
    public boolean updateFailedCount(String login);
    public boolean updateLastLoginTime(String login);
    public UserDTO getUserInfoT10(int userId);
    public boolean updatePreSecretKey(int id, String secret);
    public boolean updateSecretKey(int id);
    public boolean checkEmailExist(String email);
    public boolean forgetPasswordDb(String email, String token);
    public boolean resetPasswordDb(String password, String token);
    public boolean modifyUserState( int userId, int state,int role);
    public int activateAccountDb(UserDTO userDTO);
    public boolean registerDB(UserDTO user);
    public List<UserListDTO> getAllUser();
    public UserDTO checkEmail(String email);
    public boolean resetFailedCount(String login);
}
