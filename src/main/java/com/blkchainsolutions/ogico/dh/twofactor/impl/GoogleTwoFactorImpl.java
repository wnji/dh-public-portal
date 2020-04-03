package com.blkchainsolutions.ogico.dh.twofactor.impl;

import com.blkchainsolutions.Configure;

import com.blkchainsolutions.ogico.dh.pub.dao.UserDAO;
import com.blkchainsolutions.ogico.dh.pub.dto.UserDTO;
import com.blkchainsolutions.ogico.dh.twofactor.GoogleTwoFactor;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.ICredentialRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;

@Slf4j
@Service
public class GoogleTwoFactorImpl implements GoogleTwoFactor, ICredentialRepository {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    public Configure configure=null;
    @Override
    public byte[] pairUser(int userId) {
        UserDTO user=userDAO.getUserInfoT10(userId);
        String secret= pairing(user.getEmail(),user.getPrivatekey());
        userDAO.updatePreSecretKey(userId,secret);
        return generateQRCoode(user.getFirstName().replace(" ", "%20")+"%20"+user.getLastName().replace(" ", "%20")+"@"+user.getUserID(),secret);
    }
    public String pairing(String login, String privateKey) {
        try {
            String signed= getSignutureKey(login, privateKey);
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            gAuth.setCredentialRepository(this);
            final GoogleAuthenticatorKey key = gAuth.createCredentials(signed);
            return key.getKey();
        }catch(Exception e) {
            log.error("pairing", e);
        }
        return null;
    }
    public String getSignutureKey(String login,String privateKey) {
        try {
            PrivateKey key= getPrivateKey(privateKey);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(key);
            signature.update(String.valueOf(login).getBytes());
            byte[] digitalSignature = signature.sign();
            StringBuffer buffer=new StringBuffer();
            for (byte b : digitalSignature) {
                String st = String.format("%02X", b);
                //	            System.out.print(st);
                buffer.append(st);
            }
            return buffer.toString();
        }catch(Exception e) {
            log.error("getSignutureKey", e);
        }
        return null;
    }
    private static PrivateKey getPrivateKey(String key) {
        try {

            String pkcs8Pem = key;

            pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "");
            pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "");
            pkcs8Pem = pkcs8Pem.replaceAll("\n+","");
            pkcs8Pem = pkcs8Pem.replaceAll("\\s+","");
//	        System.out.println(pkcs8Pem);
            // Base64 decode the result
            //        String encodedPrivateKey = Base64.encode(pkcs8Pem.getBytes());

            byte [] pkcs8EncodedBytes = Base64.decode(pkcs8Pem.getBytes());

            // extract the private key

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory kf = KeyFactory.getInstance("EC");
            PrivateKey privKey = kf.generatePrivate(keySpec);
//	        System.out.println(privKey);
            return privKey;
        }catch(Exception e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
    @Override
    public int validate(int userId, String token) {
        UserDTO user=userDAO.getUserInfoT10(userId);
        if(StringUtils.isEmpty(user.getSecretKey())) {
            return 2;
        }
        try {
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            Integer code=Integer.parseInt(token);
            boolean isCodeValid =gAuth.authorize(user.getSecretKey(), code);

            if(isCodeValid) {
                return 1;
            }
        }catch(Exception e) {
            log.error("validate error", userId+","+token);
            log.error("validate", e);
        }
        return 0;
    }

    @Override
    public boolean confirmPair(int userId, String code) {
        if(this.preValidate(userId, code)) {
            return userDAO.updateSecretKey(userId);
        }
        return false;
    }
    private boolean preValidate(int userId, String token) {
        UserDTO user=userDAO.getUserInfoT10(userId);
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        Integer code=Integer.parseInt(token);
        boolean isCodeValid =gAuth.authorize(user.getPreSecretKey(), code);
        return isCodeValid;
    }

    public byte[] generateQRCoode(String name, String pairCode) {
        try {
            String qrcodeStr="otpauth://totp/"+name+"?secret="+pairCode+"&issuer=IPD%20Pilot%20Blkchain%20Project("+configure.getPortalName()+")";
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrcodeStr, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
            byte[] pngData = out.toByteArray();
            return  pngData;
        }catch(Exception e) {
            log.error("generateQRCoode", e);
        }
        return new byte[0];
    }

    @Override
    public String getSecretKey(String s) {
        return null;
    }

    @Override
    public void saveUserCredentials(String s, String s1, int i, List<Integer> list) {

    }
}
