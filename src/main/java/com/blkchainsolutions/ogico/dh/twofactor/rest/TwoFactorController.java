package com.blkchainsolutions.ogico.dh.twofactor.rest;



import com.blkchainsolutions.ogico.dh.twofactor.GoogleTwoFactor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class TwoFactorController {
	
	@Autowired
	private GoogleTwoFactor twofactor;

	@RequestMapping(path="/getQRCode/{userId}",method= RequestMethod.GET)
	public ResponseEntity<InputStreamResource> pairUserGenerateQRCode(
	    		@PathVariable(value="userId", required=true) int userId){
		 
		 HttpHeaders headers = new HttpHeaders();
		 headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		 headers.add("Pragma", "no-cache");
		 headers.add("Expires", "0");
		 headers.add("Content-Disposition", "qrcode; filename=\""+userId+".png\"");      
        
		 log.info("QR Code for userID: "+ userId);
		 
		 byte[] buffer=twofactor.pairUser(userId);
		 InputStream sourceStream = new ByteArrayInputStream(buffer);
		 InputStreamResource resource = new InputStreamResource(sourceStream);
        
		 return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("image/png"))
                .body(resource);
		 
	}
	
	@ResponseBody
	@RequestMapping(path="/confirmPairing/{userId}/{code}",method= RequestMethod.GET, produces = "application/json")
	public  Map confirmPairing(
    		@PathVariable(value="userId", required=true)  int userId,
    		@PathVariable(value="code", required=true) String code){
	 
		log.info("confirmPairing for userID: "+ userId);
		
		boolean result= twofactor.confirmPair(userId, code);
		Map<String,String> map = new HashMap<String,String>();
		if(result) {
			map.put("status","1");
		}else {
			map.put("status","0");
		}
		return map;
	}
	
	
	    		
}
