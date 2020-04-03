package com.blkchainsolutions.ogico.dh.twofactor;

public interface GoogleTwoFactor {
    public byte[] pairUser(int userId) ;
    public int validate(int userId, String token) ;
    public boolean confirmPair(int userId, String code) ;
}
