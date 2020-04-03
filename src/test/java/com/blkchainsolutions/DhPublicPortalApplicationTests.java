package com.blkchainsolutions;

import com.blkchainsolutions.common.EmailContent;
import com.blkchainsolutions.common.FrontEndPath;
import com.blkchainsolutions.ogico.dh.email.EmailBiz;
import com.blkchainsolutions.ogico.dh.pub.dto.EmailDTO;
import com.google.zxing.WriterException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class DhPublicPortalApplicationTests {
@Autowired
private  EmailBiz emailBiz;
    @Test
    void contextLoads() {
    }

    @Test
    void  test() throws IOException, WriterException {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setTo("710246663@qq.com");
        emailDTO.setTitle(EmailContent.titleRegister);
        emailDTO.setContent("test");
        emailDTO.setQrCodePath(null);
        emailDTO.setIpdCert(null);
        emailDTO.setBccList(null);

            emailBiz.sendEmail(emailDTO);
    }
}
