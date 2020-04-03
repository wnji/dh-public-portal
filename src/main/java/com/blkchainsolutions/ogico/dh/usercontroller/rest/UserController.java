package com.blkchainsolutions.ogico.dh.usercontroller.rest;

import com.blkchainsolutions.common.*;
import com.blkchainsolutions.ogico.dh.fabric.FabricSDK;
import com.blkchainsolutions.ogico.dh.fabric.vo.AuditVo;
import com.blkchainsolutions.ogico.dh.fabric.vo.ProductVo;
import com.blkchainsolutions.ogico.dh.login.biz.LoginBiz;
import com.blkchainsolutions.ogico.dh.pub.dao.UserDAO;
import com.blkchainsolutions.ogico.dh.pub.dto.*;
import com.blkchainsolutions.ogico.dh.register.RegisterBiz;
import com.blkchainsolutions.ogico.dh.subscribe.SubscribeBiz;
import com.blkchainsolutions.ogico.dh.twofactor.GoogleTwoFactor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private LoginBiz loginBiz;
    @Autowired
    private GoogleTwoFactor googleTwoFactor;
    private static boolean need2FA=true;
    static {
        String turnOff2FA=System.getProperty("turnOff2FA");
        log.info("turnOff2FA "+turnOff2FA);
        if(StringUtils.isNotEmpty(turnOff2FA) &&
                String.valueOf("Y").equalsIgnoreCase(turnOff2FA)) {
            need2FA=false;
        }
        log.info("need2FA "+need2FA);
    }


    /**
     * login
     * @param email
     * @param password
     * @param twoFA
     * @return
     */
    @ResponseBody
    @GetMapping({ "/", "/login" })
    public Result login(@RequestParam("email") String email, @RequestParam("password") String password,
                        @RequestParam(value="tfa",required = false) String twoFA) {

        log.info("HCP login "+need2FA);
        log.info(email);
        UserDTO userDTO = new UserDTO();
        userDTO = loginBiz.doLogin(email, password);
        Map<String, Object> m = new LinkedHashMap<String, Object>();
        try {
            if (userDTO != null) {
                if(UserStateEnum.LOGIN_FAILED_COUNT.getState()<userDTO.getFailed_count()) {
                    //输入密码错误3次
                    return ResultUtil.success(3);
                }
                if(UserStateEnum.PENDING_STATE.getState()==userDTO.getState()||UserStateEnum.DISAPPROVED_STATE.getState()==userDTO.getState()){
                    //not active
                    return ResultUtil.success(4);
                }
                if(StringUtils.isEmpty(userDTO.getToken())||!UserTokenEnum.ACTIVATION_TOKEN.getCode().equals(userDTO.getToken())&&!UserTokenEnum.ALREADY_REST_TOKEN.getCode().equals(userDTO.getToken())){
                    //not active
                    return ResultUtil.success(4);
                }
                if(UserStateEnum.DISABLED_STATE.getState()==userDTO.getState()){
                    //DISABLED_STATE

                    return ResultUtil.success(5);
                }

                int twoFARs = googleTwoFactor.validate(userDTO.getUserID(), twoFA); // 0:: 2 factor incorrect, 1::
                // correct, 2:: 2 factor not exist
                if(need2FA) {
                    if (twoFARs == 0) {
                        return ResultUtil.success(2);
                    }
                }
                m.put("userID", userDTO.getUserID());
                m.put("firstName", userDTO.getFirstName());
                m.put("lastName", userDTO.getLastName());
                m.put("lastLoginTime", userDTO.getLastLoginTime());
                m.put("role", userDTO.getRole());
                m.put("state", userDTO.getState());
                m.put("email", email);
                m.put("token",userDTO.getToken());
                if (twoFARs == 1) {
                    m.put("hastfa", 1);

                } else if (twoFARs == 2) {

                    m.put("hastfa", 0);
                }
                log.info("HCP logined");

            } else {
                return ResultUtil.success(6);
            }
        } catch (Exception e) {
            log.error("error in login: ",e);
            return ResultUtil.error(400,"0");
        }
        return ResultUtil.success(m);
    }

    /**
     * 退出登陆
     * @param userID
     * @param email
     * @return
     */
    @ResponseBody
    @GetMapping({ "/", "/logout" })
    public Result logout(@RequestParam("user_id") String userID, @RequestParam("email") String email) {

        log.info("logout");
        log.info("userID: " + userID);
        log.info("email: " + email);
        try {
            // audit
            AuditVo auditVo = new AuditVo();
            auditVo.getAudit().setAuditUserID(String.valueOf(userID));
            auditVo.getAudit().setAuditUsername(email);
            auditVo.getAudit().setAuditType(AuditConstant.LOGOUT);
            //  jmsSendMessage.sendAuditQueue(JSONHelper.toJsonString(auditVo));
        } catch (Exception e) {
            log.info(String.valueOf(e));
            return ResultUtil.error(400,"0");
        }
        return ResultUtil.success(1);
    }

    @ResponseBody
    @GetMapping({ "/", "/forgetPassword" })
    public Result forgetPassword(@RequestParam("email") String email) {
        log.info(email);
        loginBiz.initiateForgetPassword(email);
        return ResultUtil.success(1);
    }

    @ResponseBody
    @GetMapping({ "/", "/resetPassword" })
    public Result resetPassword(@RequestParam("password") String password, @RequestParam("token") String token) {

        log.info(password);
        log.info(token);

        if (loginBiz.resetPassword(token, password)) {
            return ResultUtil.success(1);
        }

        return ResultUtil.success(0);
    }
    @Autowired
    private RegisterBiz registerBiz;
    @ResponseBody
    @GetMapping("modifyUserState")
    public Result modifyUserState(@RequestParam("email")String email,@RequestParam("user_id") int userId,@RequestParam("state") int state,@RequestParam("role")int role){
        Boolean rs= registerBiz.modifyUserState(email,userId,state,role);
        return ResultUtil.success(rs);
    }

    @ResponseBody
    @GetMapping({ "/", "/register" })
    public Result register(@RequestParam("first_name") String firstName,
                           @RequestParam("last_name") String lastName,
                           @RequestParam("email") String email, @RequestParam("password") String password) {
        UserDTO user = new UserDTO();
        log.info(firstName);
        log.info(lastName);
        log.info(email);
        if (registerBiz.checkEmailExist(email)) {
            log.info("duplicate email");
            return ResultUtil.success(2);
        }
        String phrase = PhaseGenerator.generatePhase();
        log.info("Phrase: " + phrase);
        try {
            //  UserResponse response = FabricSDK2.registerUser( email, phrase);

            user.setPassphrase(phrase);
            // user.setPrivatekey(response.getData().getPrivateKey());
            // user.setPublickey(response.getData().getCert());
            user.setPrivatekey("123");
            user.setPublickey("123");
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);

            boolean rs = registerBiz.doRegister(user);
            return ResultUtil.success(rs);
        } catch (Exception e) {

            log.info("fabric error: " + e);
            return ResultUtil.error(400,"5");
        }
    }
    @ResponseBody
    @GetMapping({"/","activate"})
    public Result verify(@RequestParam("token") String token){
        log.info("DH activate account");
        log.info(token);
        UserDTO userDTO=new UserDTO();
        userDTO.setToken(token);
        int rs=registerBiz.doActivate(userDTO);
        log.info("verified userId:" +rs);
        return  ResultUtil.success(rs);
    }
    @Autowired
    private UserDAO userDAO;

    @ResponseBody
    @GetMapping({"/","getUserList"})
    public Result getUserList(){
        List<UserListDTO> list= userDAO.getAllUser();
        return  ResultUtil.success(list);
    }

    @Autowired
    private SubscribeBiz subscribeBiz;
    @ResponseBody
    @GetMapping("subscribeProduct")
    public Result subscribeProduct(@RequestParam("email")String email,@RequestParam("hKRegistrationID")String hKRegistrationID){
        log.info("subscribeProduct");
        try {
            int rs= subscribeBiz.addSubscribeProduct(email,hKRegistrationID);
            return  ResultUtil.success(rs);
        }catch (Exception e){
            log.error("subscribeProduct on error",e);
            return ResultUtil.error();
        }

    }

    @ResponseBody
    @PostMapping({"/","searchProductList"})
    public Result searchProductList(@RequestParam(value = "hKRegistrationID",required = false)String hKRegistrationID,@RequestParam(value = "productName",required = false)String productName,
                                    @RequestParam(value = "brandName",required = false)String brandName,@RequestParam(value = "productCode",required = false)String productCode
            ){
        try {
            ProductVo productVo=new ProductVo();
            if(StringUtils.isNotEmpty(hKRegistrationID)){
                productVo.getSelector().setHKRegistrationID(FabricWildcardMapUtil.getRegex(hKRegistrationID));
            }else {
                productVo.getSelector().setHKRegistrationID(FabricWildcardMapUtil.getRegex(""));
            }
            if(StringUtils.isNotEmpty(productName)){

                productVo.getSelector().setProductName(FabricWildcardMapUtil.getRegex(productName));
            }else {
                productVo.getSelector().setProductName(FabricWildcardMapUtil.getRegex(""));
            }
            if(StringUtils.isNotEmpty(brandName)){

                productVo.getSelector().setBrandName(FabricWildcardMapUtil.getRegex(brandName));
            }else {
                productVo.getSelector().setBrandName(FabricWildcardMapUtil.getRegex(""));
            }
            if(StringUtils.isNotEmpty(productCode)){

                productVo.getSelector().setProductCode(FabricWildcardMapUtil.getRegex(productCode));
            }else {
                productVo.getSelector().setProductCode(FabricWildcardMapUtil.getRegex(""));
            }

            return ResultUtil.success(subscribeBiz.searchProductList(productVo));
        } catch (Exception e) {
            log.error("error on searchProductList",e);
            return ResultUtil.error(400,"Request exception");
        }

    }
    @ResponseBody
    @GetMapping({ "/", "/productById" })
    public Result productById(@RequestParam("productId") String productId){
        try {

            return ResultUtil.success(subscribeBiz.productById(productId)) ;
        } catch (Exception e) {
            log.error("error on productById",e);
            return ResultUtil.error(400,"Request exception");
        }
    }

    @GetMapping("/getMySubscriptionList")
    @ResponseBody
    public Result getMySubscriptionList(@RequestParam("email") String email){
        List<MySubscriptionDTO> list= subscribeBiz.getMySubscriptionList(email);
        return ResultUtil.success(list);
    }

    @ResponseBody
    @GetMapping({"/","saveSubscribeProduct"})
    public Result saveSubscribeProduct(@RequestParam("productId")String productId,
                                       @RequestParam("productName")String productName,@RequestParam("email")String email){

        SubscribeProductDTO subscribeProductDTO=new SubscribeProductDTO();
        subscribeProductDTO.setEmail(email);
        subscribeProductDTO.setProductId(productId);
        Boolean rs= subscribeBiz.addSubscribeProduct(subscribeProductDTO, productName);
        return ResultUtil.success(rs);
    }
    @ResponseBody
    @GetMapping({"/","cancelSubscribeProduct"})
    public Result cancelSubscribeProduct(@RequestParam("productId")String productId,@RequestParam("productName")String productName,@RequestParam("email")String email){
        SubscribeProductDTO subscribeProductDTO=new SubscribeProductDTO();
        subscribeProductDTO.setEmail(email);
        subscribeProductDTO.setProductId(productId);
        Boolean rs= null;
        try {
            rs = subscribeBiz.cancelSubscribeProduct(subscribeProductDTO, productName);
        } catch (Exception e) {
            log.error("error on cancelSubscribeProduct",e);
            return ResultUtil.error(400,"Request exception");
        }
        return ResultUtil.success(rs);
    }

    @GetMapping("batchByNumber")
    @ResponseBody
    public Result batchByNumber(@RequestParam("number")String number){
        try {
            return  ResultUtil.success(subscribeBiz.batchByNumber(number));
        }catch (Exception e){
            log.error("batchByNumber error",e);
            return ResultUtil.error();
        }
    }
    @GetMapping("batchAll")
    @ResponseBody
    public Result batchAll(){
        try {
            return  ResultUtil.success(subscribeBiz.batchAll());
        }catch (Exception e){
            log.error("batchAll error",e);
            return ResultUtil.error();
        }
    }
    @GetMapping("batchNumberDetails")
    @ResponseBody
    public Result batchNumberDetails(@RequestParam("batchNumberId")String batchNumberId){
        try {
            return  ResultUtil.success(subscribeBiz.batchNumberDetails(batchNumberId));
        }catch (Exception e){
            log.error("batchNumberDetails error",e);
            return ResultUtil.error();
        }
    }


    @ResponseBody
    @GetMapping({"/","saveSubscribeBatch"})
    public Result saveSubscribeBatch(@RequestParam("batchId")String batchId,
                                       @RequestParam("productName")String productName,@RequestParam("email")String email,@RequestParam("alertDays")int alertDays){
        Boolean rs=false;
        try {
         rs= subscribeBiz.saveSubscribeBatch(batchId, productName,email,alertDays);
        } catch (Exception e) {
            log.error("error on cancelSubscribeProduct",e);
            return ResultUtil.error(400,"Request exception");
        }
        return ResultUtil.success(rs);
    }
    @ResponseBody
    @GetMapping({"/","cancelSubscribeBatch"})
    public Result cancelSubscribeBatch(@RequestParam("batchId")String batchId,
                                       @RequestParam("productName")String productName,@RequestParam("email")String email){

        Boolean rs= false;
        try {
            rs = subscribeBiz.cancelSubscribeBatch(batchId, productName,email);
        } catch (Exception e) {
            log.error("error on cancelSubscribeProduct",e);
            return ResultUtil.error(400,"Request exception");
        }
        return ResultUtil.success(rs);
    }
}
