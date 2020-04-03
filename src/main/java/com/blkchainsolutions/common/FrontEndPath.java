package com.blkchainsolutions.common;

import com.blkchainsolutions.Configure;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Data
@Component
public final class FrontEndPath {
	
	@Autowired
	public Configure configure=null;
	
	private static Configure config=null;
	
	@PostConstruct
    public void init() {
		config = configure;
    }

	
//	private static String baseURL = config.getDomainUrl();

	public static String processRequestPath() {
		
		return config.getDomainUrl() + "transfer_request/";
	}
	
	public static String ConfirmedPath() {
			
			return config.getDomainUrl() + "view_trademark/";
		}
	
	public static String ActivatePath() {
		
		return config.getDomainUrl() + "activate?token=";
	}
	
	public static String ResetPasswordPath() {
		
		return config.getDomainUrl() + "reset_password?token=";
	}


	
	public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
	    
	    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
	    byte[] pngData = pngOutputStream.toByteArray(); 
	    return pngData;
	}
	
}