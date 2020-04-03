package com.blkchainsolutions.ogico.dh.register;


import com.blkchainsolutions.ogico.dh.pub.dto.UserDTO;

public interface RegisterBiz {
    public boolean checkEmailExist(String email);
    public boolean doRegister(UserDTO userDTO);
    public int doActivate(UserDTO userDTO);
   public boolean modifyUserState(String email, int userId, int state,int role);
}
