package com.blkchainsolutions.ogico.dh.login.biz;

import com.blkchainsolutions.ogico.dh.pub.dto.UserDTO;

public interface LoginBiz {
    public UserDTO doLogin(String login, String pwd);
    public void initiateForgetPassword(String email);
    public Boolean resetPassword(String sessionId, String password);
}
