package com.blkchainsolutions.ogico.dh.email.impl;

import com.blkchainsolutions.Configure;

import com.blkchainsolutions.ogico.dh.email.EmailBiz;
import com.blkchainsolutions.ogico.dh.pub.dto.EmailDTO;
import com.blkchainsolutions.ogico.dh.qrCodeGenerator.QRCodeGenerator;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@Service
public class EmailBizImpl
       implements EmailBiz
{
	
	@Autowired
	public Configure configure;
	
	@Autowired
	private QRCodeGenerator qRCodeGenerator;
	
	
   public boolean sendEmail(EmailDTO emailDTO) throws WriterException, IOException {
      // Recipient's email ID needs to be mentioned.
      String to = emailDTO.getTo();

      // Sender's email ID needs to be mentioned
      String from = configure.getEmailUsername();

      final String username = configure.getEmailUsername();//change accordingly
      final String password = configure.getEmailPW();//change accordingly

      // Assuming you are sending email through relay.jangosmtp.net
     // String host = configure.getEmailHost();

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.host", configure.getEmailHost());
      props.put("mail.smtp.port", configure.getEmailPort());
      
//      props.put("mail.smtp.host", "carmodtest.blkchainsolution.com");
//      props.put("mail.smtp.port", "25");
     //props.put("mail.smtp.ssl.enable", true);
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true"); //TLS

      // Get the Session object.
      Session session = Session.getInstance(props,
         new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
            }
         });

      try {
    	 log.info("emailBiz");
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(to));
         
         if(emailDTO.getBccList() != null) {
        	 message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailDTO.getBccList()));
         }
         // Set Subject: header field
         message.setSubject(emailDTO.getTitle());

         // Create the message part
         BodyPart messageBodyPart = new MimeBodyPart();

         // Now set the actual message
         messageBodyPart.setContent(emailDTO.getContent(),"text/html; charset=utf-8");

         // Create a multipart message
         Multipart multipart = new MimeMultipart();

         // Set text message part
         multipart.addBodyPart(messageBodyPart);
         
         if (emailDTO.getQrCodePath() != null) {
	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         String qrCodePath = emailDTO.getQrCodePath();
	         log.info(qrCodePath);
	         byte[] bytearray = qRCodeGenerator.getQRCodeImage(qrCodePath, 350, 350);
	         
	         log.info(String.valueOf("QR Code byte:" + bytearray));
	         
	         ByteArrayDataSource source = new ByteArrayDataSource(bytearray, "image/png");
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName("trademark.png");
	         //messageBodyPart.setFileName(source.getName());
	         multipart.addBodyPart(messageBodyPart);
         }
         //IPD Cert
       /*  if(emailDTO.getIpdCert() != null) {
        	 messageBodyPart = new MimeBodyPart();
        	 ByteArrayDataSource source2 = new ByteArrayDataSource(emailDTO.getIpdCert().getBytes(), "text/plain");
             messageBodyPart.setDataHandler(new DataHandler(source2));
             messageBodyPart.setFileName("trademarkProof");
             //messageBodyPart.setFileName(source.getName());
             multipart.addBodyPart(messageBodyPart);
             log.info("insert ipd cert txt");
         }*/
         
         
         // Send the complete message parts
         message.setContent(multipart);
         
         // Send message
         Transport.send(message,message.getAllRecipients());
         
         log.info("Sent message successfully....");
         
         return true;
         
      } catch (MessagingException e) {
    	 log.error("sendEmail",e);
         throw new RuntimeException(e);
      }
      
   }

}