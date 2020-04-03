package com.blkchainsolutions.ogico.dh.email.impl;

import com.blkchainsolutions.Configure;

import com.blkchainsolutions.ogico.dh.email.EmailBiz;
import com.blkchainsolutions.ogico.dh.pub.dto.EmailDTO;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
//@Service
public class OgcioEmailBizImpl// implements EmailBiz
{

//	@Autowired
//	private QRCodeGenerator qRCodeGenerator; 
	
	@Autowired
	public Configure configure;
	
	
	
   public boolean sendEmail(EmailDTO emailDTO) throws WriterException, IOException {
     
	  log.info(">>>> OgcioEmailBizImpl "+emailDTO.getTo());
	  log.info(">>>> OgcioEmailBizImpl url "+configure.getOgcioEmailURL());


      try {
    	 OgcioMail testmail = new OgcioMail();
    	 
    	 testmail.setToEmail(new String[] {emailDTO.getTo()});
    	 testmail.setEmailContent(emailDTO.getContent());
    	 testmail.setSubject(emailDTO.getTitle());
    	 log.info(">>>> OgcioEmailBizImpl from "+testmail.getFromEmail());
    	 CloseableHttpClient httpclient = HttpClients.createDefault();
 		HttpPost httpPost = new HttpPost(configure.getOgcioEmailURL());
 		
 		Gson gson=new Gson();
 		
 		StringEntity stringEntity = new StringEntity(gson.toJson(testmail), ContentType.APPLICATION_JSON);
 		httpPost.setEntity(stringEntity);
 		
 		ResponseHandler< String > responseHandler = response -> {
 		    int status = response.getStatusLine().getStatusCode();
 		    
// 		    if (status >= 200 && status < 300) {
 		        HttpEntity entity = response.getEntity();
 		        return entity != null ? EntityUtils.toString(entity) : null;
// 		    } else {
// 		        throw new ClientProtocolException("Unexpected response status: " + status);
// 		    }
 		};
 		String responseBody = httpclient.execute(httpPost, responseHandler);
//		
        log.info("Sent message successfully.... "+responseBody.toString());
         
       
         
      } catch (Exception e) {
    	 log.error("sendEmail",e);
//         throw new RuntimeException(e);
      }
      return true;
   }
   
   @Data
   private  class OgcioMail{
	   private String fromEmailName="IPD Admin";
	   private String fromEmail= configure.getOgcioEmailUsername();
	   private String[] toEmail;
	   private String[]  ccEmail;
	   private String[] bccEmail;
	   private String subject;
	   private String emailContent;
	   private Attachment[]  attachment;
   }
   
   public static void main(String[] args) {
//	   OgcioMail testmail = new OgcioMail();
//	   testmail.setToEmail(new String[]{"a","b","c"});
//	   Attachment[] attachments=new Attachment[1];
//	   Attachment attachment =new Attachment();
//	   attachment.setBase64("123123");
//	   attachment.setFileName("tst.xld");
//	   attachment.setMimeType("sfsf/sffsf");
//	   attachments[0]=attachment;
//	   testmail.setAttachment(attachments);
//	   Gson gson=new Gson();
//	   System.out.println(gson.toJson(testmail));
	   
   }
   
   @Data
   private  class Attachment{
	   private String mimeType;
	   private String  fileName;
	   private String base64;
   }
   /*
    {
 "fromEmailName":"IPD Admin",                                                
 "fromEmail":"ipdadmin@test.cmmp.gov.hk",            
 "toEmail":["leo.chan@blkchainsolutions.com","raingoleo@gmail.com","ryclau@ogcio.gov.hk"], 
 "ccEmail":["raingoleo@hotmail.com","andy.wong@blkchainsolutions.com"],  
 "bccEmail":[],                                       
 "subject":"Test email by leo",                                                  
 "emailContent":"<htm><body>Dear Ray, please call me if you get it.</br>I cannot get from my email</body></html>"     
    "attachment":                                                                               // Optional Field
  [
	{
	  "mimeType": "application/zip",                                          // Mandatory Field
	  "fileName": "URL.zip",                                                      // Mandatory Field
	  "base64": "UEsDBAoAAgAAAP2GI08Q......"                  // Mandatory Field
	},
	{
	  "mimeType":"image/jpeg",
          "fileName":"img02.jpeg",
	  "base64": "/9j/4AAQSkZJRgABAQEB......"
    }
  ]

}

    
    */
}
