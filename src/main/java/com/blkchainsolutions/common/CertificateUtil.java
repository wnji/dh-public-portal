package com.blkchainsolutions.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.openssl.PEMReader;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;

@Slf4j
public class CertificateUtil {

	public static String cleanUpPK(String cert) {
		String pkcs8Pem = cert.toString();
		        
		pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "");
		pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "");
		pkcs8Pem = pkcs8Pem.replaceAll("\n+","");
		pkcs8Pem = pkcs8Pem.replaceAll("\\s+",""); 
		        
		return pkcs8Pem;
	}
	
	public static PrivateKey getPrivateKey(String cert) {
		
		try {
	        
	        byte [] pkcs8EncodedBytes =Base64.decode(cert.getBytes());
	    	
	        // extract the private key
	
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
	        KeyFactory kf = KeyFactory.getInstance("EC");
	        PrivateKey privKey = kf.generatePrivate(keySpec);
	        
	        return privKey;
		}catch(Exception e) {
			log.error("getPrivateKey",  e);
		}
		return null;
	}
	
	public static PrivateKey getPrivateKeyWithClenaUp(String cert) {
		
		try {
	        
	        byte [] pkcs8EncodedBytes =Base64.decode(cleanUpPK(cert).getBytes());
	    	
	        // extract the private key
	
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
	        KeyFactory kf = KeyFactory.getInstance("EC");
	        PrivateKey privKey = kf.generatePrivate(keySpec);
	        
	        return privKey;
		}catch(Exception e) {
			log.error("getPrivateKey",  e);
		}
		return null;
	}
	
	
	
	 public static PublicKey getPublicKey(String cert) {
		 StringReader reader = null;
	    	try { 
	    		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//		    	File file = new File("/Users/leo/Downloads/cert.crt");
//				FileReader reader = new FileReader(file);
	    		reader =new StringReader(cert);
				PEMReader pemReader = new PEMReader(reader);
				X509CertificateObject keyPair  = (X509CertificateObject) pemReader.readObject();
//				System.out.println(keyPair.getPublicKey());

		        return keyPair.getPublicKey();
	    	}catch(Exception e) {
	    		e.printStackTrace(System.err);
	    	}finally {
	    		reader.close();
	    	}
	    	return null;
	    }

	
}
