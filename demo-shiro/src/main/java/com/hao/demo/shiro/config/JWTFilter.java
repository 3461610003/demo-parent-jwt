package com.hao.demo.shiro.config;

import com.alibaba.fastjson.JSON;
import com.hao.demo.shiro.model.JWTToken;
import com.hao.demo.shiro.model.Result;
import com.hao.demo.shiro.model.StatusCode;
import com.hao.demo.shiro.util.AjaxUtils;
import com.hao.demo.shiro.util.JWTUtil;
import com.hao.demo.shiro.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义 jwt 过滤器1
 */
@Component
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter{
//    /**
//     * 判断用户是否想要登入。
//     * 检测header里面是否包含Authorization字段即可
//     */
//    @Override
//    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
//        HttpServletRequest req = (HttpServletRequest) request;
//        String authorization = req.getHeader("Authorization");
//        return authorization != null;
//    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        log.info("22222过滤器 user executeLogin 执行登录 ...");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Token");
        String message = "登陆过期";
        if (!StringUtils.isEmpty(token)) {
            //日志记录
            String api = getPathWithinApplication(request);
            String url = req.getMethod()+"="+api;
            Map<String,String[]> params = new HashMap<>(req.getParameterMap());
            log.info("访问URL:{}, 用户ID:{}, 参数:{}", url, JWTUtil.getUid(token), getReqParams(params));

            JWTToken jwtToken = new JWTToken(token);
            //获取登陆端类型
            String loginType = JWTUtil.getLoginType(token);
            jwtToken.setLoginType(loginType);
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
            try {
                getSubject(request, response).login(jwtToken);
                return true;
            } catch (Exception e) {
                if (e.getMessage().equals("异地登陆")){
                    message = MessageUtils.get("user.location");
                }else {
                    message = MessageUtils.get("user.expire");
                }
            }
        }
        AjaxUtils.renderJson((HttpServletResponse)response,
                Result.result(null, StatusCode.NO_PERMISSION, message));
        return false;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.info("22222过滤器 user isAccessAllowed ...");
        return executeLogin(request, response);
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("22222过滤器 user 对跨域提供支持");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp= (HttpServletResponse) response;
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            resp.setHeader("Access-control-Allow-Origin", req.getHeader("Origin"));
            resp.setHeader("Access-Control-Allow-Methods", req.getMethod());
            resp.setHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
            resp.setStatus(HttpStatus.OK.value());
            return false;
        }
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            resp.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.info("22222过滤器 user onAccessDenied");
        return super.onAccessDenied(request, response);
    }


    private Map<String,String[]> getReqParams(Map<String,String[]> params){
        params.remove("oldPw");
        params.remove("newPw");
        params.remove("tpw");
        params.remove("password");
        return params;
    }

    private String getRequestPayload(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = req.getReader()) {
            char[]buff = new char[1024];
            int len;
            while((len = reader.read(buff)) != -1) {
                sb.append(buff,0, len);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(JSON.parseObject(sb.toString()));
    }
}
