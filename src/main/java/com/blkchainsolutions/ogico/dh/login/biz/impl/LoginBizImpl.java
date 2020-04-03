package com.blkchainsolutions.ogico.dh.login.biz.impl;

import com.blkchainsolutions.Configure;
import com.blkchainsolutions.common.AuditConstant;
import com.blkchainsolutions.common.EmailContent;
import com.blkchainsolutions.common.FrontEndPath;
import com.blkchainsolutions.common.PhaseGenerator;
import com.blkchainsolutions.ogico.dh.email.EmailBiz;
import com.blkchainsolutions.ogico.dh.fabric.vo.AuditVo;
import com.blkchainsolutions.ogico.dh.login.biz.LoginBiz;
import com.blkchainsolutions.ogico.dh.pub.dao.UserDAO;
import com.blkchainsolutions.ogico.dh.pub.dto.EmailDTO;
import com.blkchainsolutions.ogico.dh.pub.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class LoginBizImpl implements LoginBiz {

    @Autowired
    public Configure configure=null;

    private static Configure config=null;

    @PostConstruct
    public void init() {
        config = configure;
    }
    @Autowired
    private UserDAO userDao;
    @Override
    public UserDTO doLogin(String login, String pwd) {

        UserDTO userDTO = userDao.checkLogin(login, pwd);
        if (userDTO == null) {
            userDao.updateFailedCount(login);
        }

        if (userDTO != null && userDao.updateLastLoginTime(login)&&userDao.resetFailedCount(login)) {
            //audit
            AuditVo auditVo = new AuditVo();
            auditVo.getAudit().setAuditUserID(String.valueOf(userDTO.getUserID()));
            auditVo.getAudit().setAuditUsername(userDTO.getEmail());
            auditVo.getAudit().setAuditType(AuditConstant.LOGIN);
            //  jmsSendMessage.sendAuditQueue(JSONHelper.toJsonString(auditVo));
            return userDTO;
        } else {
            //audit
            AuditVo auditVo = new AuditVo();
            auditVo.getAudit().setAuditUserID("0");
            auditVo.getAudit().setAuditUsername(login);
            auditVo.getAudit().setAuditType(AuditConstant.FAILED_LOGIN);
            // jmsSendMessage.sendAuditQueue(JSONHelper.toJsonString(auditVo));

        }
        return null;
    }
    @Autowired
    private EmailBiz emailBiz;
    @Override
    public void initiateForgetPassword(String email) {
        String token = PhaseGenerator.generatePhase();

        if(userDao.checkEmailExist(email) == true) {
            log.info("email exist");
            if(userDao.forgetPasswordDb(email, token)) {
                log.info("session key is created");
                //send email to user with link ulr/reset_password/sessionid
                EmailDTO emailDTO = new EmailDTO();
                emailDTO.setTo(email);
                emailDTO.setTitle(EmailContent.titleForgetPassword);
                emailDTO.setContent(EmailContent.contentForgetPassword.replaceAll("#1", FrontEndPath.ResetPasswordPath() + token ).replace("#6", configure.getEmailSignature()));
                emailDTO.setQrCodePath(null);
                emailDTO.setIpdCert(null);
                emailDTO.setBccList(null);
                try {
                    if(emailBiz.sendEmail(emailDTO)) {
                        log.info("email sent to user for forget password");
                    }
                }catch(Exception e) {
                    log.info("error" + e);
                }
            }
        }else {
            log.info("email not exist");
        }
    }

    @Override
    public Boolean resetPassword(String sessionId, String password) {
        if(userDao.resetPasswordDb(password, sessionId)) {
            log.info("successful");
            return true;
        }
        log.info("expired session time");
        return false;
    }
}
