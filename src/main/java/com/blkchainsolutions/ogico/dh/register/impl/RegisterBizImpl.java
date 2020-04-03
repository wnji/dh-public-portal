package com.blkchainsolutions.ogico.dh.register.impl;

import com.blkchainsolutions.common.*;

import com.blkchainsolutions.ogico.dh.email.EmailBiz;
import com.blkchainsolutions.ogico.dh.fabric.vo.AuditVo;
import com.blkchainsolutions.ogico.dh.pub.dao.UserDAO;
import com.blkchainsolutions.ogico.dh.pub.dto.EmailDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.UserDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.UserStateEnum;
import com.blkchainsolutions.ogico.dh.register.RegisterBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterBizImpl implements RegisterBiz {
    @Autowired
    private UserDAO userDao;
 //   @Autowired
 //   private JMSSendMessage jmsSendMessage;
    @Autowired
    private EmailBiz emailBiz;

    @Override
    public boolean checkEmailExist(String email) {
        if ( userDao.checkEmailExist(email)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean doRegister(UserDTO userDTO) {
        //check email exist, return -2
        log.info("in do register");
        log.info(userDTO.getEmail());

        String token = PhaseGenerator.generatePhase();
        //UUID.randomUUID().toString();
        userDTO.setToken(token);
        boolean regResult = userDao.registerDB(userDTO);
        log.info("registerDB Status: " + String.valueOf(regResult));

        //audit
        AuditVo auditVo = new AuditVo();
        auditVo.getAudit().setAuditUserID("0");
        auditVo.getAudit().setAuditUsername(userDTO.getEmail());
        auditVo.getAudit().setAuditType(AuditConstant.REGISTRATION);
      //  jmsSendMessage.sendAuditQueue(JSONHelper.toJsonString(auditVo));

        if (regResult) {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setTo(userDTO.getEmail());
            emailDTO.setTitle(EmailContent.titleRegister);
            emailDTO.setContent(EmailContent.contentRegister.replace("#1", FrontEndPath.ActivatePath() + token ));
            emailDTO.setQrCodePath(null);
            emailDTO.setIpdCert(null);
            emailDTO.setBccList(null);
            try {
                if(emailBiz.sendEmail(emailDTO)) {
                    log.info("email sent to owner for registration");
                }
            }catch(Exception e) {
                log.info("error" + e);
            }


            return true;
        }
        return false;
    }

    @Override
    public int doActivate(UserDTO userDTO) {
        return userDao.activateAccountDb(userDTO);
    }

    @Override
    public boolean modifyUserState(String email,int userId, int state,int role) {
        log.info("modifyUserState");

        return userDao.modifyUserState(userId,state,role);
    }
}
