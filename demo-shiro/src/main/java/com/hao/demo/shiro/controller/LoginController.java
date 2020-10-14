package com.hao.demo.shiro.controller;


import com.hao.demo.shiro.model.*;
import com.hao.demo.shiro.service.AdminService;
import com.hao.demo.shiro.service.SmsSenderFeignApi;
import com.hao.demo.shiro.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;

/**
 * 管理端用户登陆Controller
 */
@Slf4j
@RestController
@RequestMapping
public class LoginController extends BaseAdminController {

    @Resource
    private AdminService adminService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SmsSenderFeignApi smsSenderFeignApi;

    /**
     * 管理端用户登陆
     */
    @PostMapping(value = "/login")
    public Result<AdminEntity> login(HttpServletRequest request, HttpServletResponse response, String userName,
                                     String password, String smsCode) {
        log.info("===管理端用户登录========");
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(smsCode)) {
            return Result.result(null, StatusCode.LACK_VALUE, MessageUtils.get("login.lack.info"));
        }
//        if (!validateMsgCode(userName, redisUtil, smsCode)) {
//            return Result.result(null, StatusCode.NO_RETURN_DATA, MessageUtils.get("common.code.sms"));
//        }
        return shiroLogin(request, response, userName, password, UserType.ADMIN.getValue());
    }

    /**
     * 登陆
     */
    private Result<AdminEntity> shiroLogin(HttpServletRequest request, HttpServletResponse response, String userName,
                                           String password, String type) {
        String jwt = request.getHeader("Token");
        if (!StringUtils.isEmpty(jwt)) {
            redisUtil.del(JWTUtil.getSessionId(jwt), JWTUtil.getTokenKey(jwt));
        }
        AdminEntity admin = adminService.findByUserName(userName);
        if (null == admin) {
            return Result.result(null, StatusCode.NO_RETURN_DATA, MessageUtils.get("login.error.exist"));
        }
//        if (!encryptPassword(password, admin.getSalt()).equals(admin.getPassword())) {
        if (!admin.getPassword().equals(password)) {
            return Result.result(null, StatusCode.NO_RETURN_DATA, MessageUtils.get("bot.error.password"));
        }
        if (admin.getLoginLimits() != 0) {
            if (admin.getLoginLimits() <= admin.getLoginTimes()) {
                return Result.result(null, StatusCode.FIND_ERROR, MessageUtils.get("login.error.limit"));
            }
            admin.setLoginTimes(admin.getLoginTimes() + 1);
        }
        admin.setLoginTime(new Date());
        adminService.modifySelective(admin);
        //当前访问ip
        String currentIp = IpAddressUtils.getIpAdrress(request);

        String salt = CustomUUID.getUUIDBase64() + "_" + currentIp;
        String token = JWTUtil.sign(userName, type, salt, admin.getId());
        //设置jwt标记
        String tokenKey = "jwt:" + type + ":" + admin.getId() + ":" + currentIp;

//        if (null != redisUtil.get(tokenKey)){
//            if (!currentIp.equals(getTokenIp((String) redisUtil.get(tokenKey)))) {
//                redisUtil.set(tokenKey+":repeat",1,1800);
//            }
//        }
        redisUtil.set(tokenKey, salt, 14400);
        response.setHeader("Token", token);
        response.setHeader("Access-Control-Expose-Headers", "Token");
        return Result.result(null, StatusCode.FIND_SUCCESS, MessageUtils.get("login.success.login"));
    }

    /**
     * 客户端用户登出
     */
    @GetMapping(value = "/logout")
    public Result logout() {
        log.info("===用户退出===");
        Subject subject = SecurityUtils.getSubject();

        if (subject.getPrincipals() != null) {
            String token = (String) subject.getPrincipal();
            String salt = JWTUtil.getSalt(token);
            assert salt != null;
            String tokenKey = JWTUtil.getTokenKey(token) + ":" + salt.substring(salt.lastIndexOf("_") + 1, salt.length());
            //清除redis用户登陆信息
            redisUtil.del(JWTUtil.getSessionId(token), tokenKey);
        }
        subject.logout();
        return Result.result(null, StatusCode.OPERATION_SUCCESS, MessageUtils.get("login.logout"));
    }

    @GetMapping(value = "/test")
    public Result test() {
        log.info("===测试接口===");
        return Result.result(null, StatusCode.FIND_SUCCESS, "测试接口联通。。。。");
    }

    @GetMapping(value = "/hello")
    public Result hello() {
        log.info("===hello ===");
        return Result.result(null, StatusCode.FIND_SUCCESS, "hello hello hello ...");
    }

    @GetMapping(value = "/admins/current")
    public Result<AdminEntity> currentAdmin(HttpServletRequest request) {
        log.info("===获取当前用户信息===");
        Long id = UserUtils.getCurrentUserId(request);
        return Result.result(DataUtils.selectAdminById(id), StatusCode.FIND_SUCCESS, ResultMessage.FIND_SUCCESS);
    }

    @GetMapping(value = "/admins/menus")
    public Result<Set<String>> menus(HttpServletRequest request) {
        log.info("===获取当前用户信息===");
        Long id = UserUtils.getCurrentUserId(request);
        return Result.result(DataUtils.selectMenuById(id), StatusCode.FIND_SUCCESS, ResultMessage.FIND_SUCCESS);
    }

    /**
     * 默认无权操作
     */
    @GetMapping(value = "/unAuth")
    public Result unAuth() {
        return Result.result(null, StatusCode.NOT_FOUND, MessageUtils.get("login.error.permit"));
    }

    /**
     * 图形验证码
     */
    @GetMapping(value = "/img-code")
    public void getYzm(HttpServletResponse response) throws Exception {

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 定义图片长度和宽度
        int width = 65, height = 30;
        // 创建内存图像
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new SecureRandom();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.setColor(new Color(255, 255, 255));
        g.drawRect(0, 0, width - 1, height - 1);
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width), y = random.nextInt(height), xl = random.nextInt(12), yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // 随机获取四位字母或数字型字符
        String str = "ABCDEFGHJKMNPQRSTUVWXYZ123456789";
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            char ch = str.charAt(new SecureRandom().nextInt(str.length()));
            sb.append(ch);
        }
        String code = sb.toString().toLowerCase();
        log.info("验证码：" + code);
        g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
        g.drawString(code, 5, 20);

        //验证码id储存在cookie中

        String codeId = CustomUUID.getUUID();
        response.setHeader(CustomConstants.Cookie_Name.IMG_CODE, codeId);
        // 验证码储存在redis
        redisUtil.set("imgCode:" + codeId, code, 60);

        //HttpSession session = request.getSession();
        //session.setAttribute("code", code);
        g.dispose();
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    /**
     * 短信验证码
     */
    @GetMapping(value = "/sms-code")
    public Result getMsgCode(HttpServletRequest request, String userName, String imgCode) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(imgCode)) {
            return Result.result(null, StatusCode.LACK_VALUE, MessageUtils.get(ResultMessage.LACK_VALUE));
        }
        //校验图形验证码
        if (validateImgCode(request, redisUtil, imgCode)) {
            return Result.result(null, StatusCode.NO_RETURN_DATA, MessageUtils.get("common.code.img"));
        }
        AdminEntity admin = adminService.findByUserName(userName);
        if (null == admin) {
            return Result.result(null, StatusCode.OPERATION_ERROR, MessageUtils.get("login.error.exist"));
        }

        Integer code = getRandomCode();//获取随机验证码
        redisUtil.set("validateCode:admin:" + userName, code, 300);//redis有效时间5分钟
        String content = MessageFormat.format(MessageUtils.get("message.template.code"), code + "");
//        SmsCodeUtil.getSmsCode(admin.getPhone(), null, content);
        smsSenderFeignApi.sendValidateCode(admin.getPhone(), null, content);
        return Result.result(null, StatusCode.FIND_SUCCESS, MessageUtils.get("login.send"));

    }

    private Color getRandColor(int fc, int bc) {
        SecureRandom random = new SecureRandom();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private Integer getRandomCode() {
        SecureRandom random = new SecureRandom();
        return random.nextInt(900000) + 100000;
    }
}
