package com.hao.demo.shiro.simplify.config.shiro;

import com.hao.demo.shiro.simplify.model.JWTToken;
import com.hao.demo.shiro.simplify.model.Result;
import com.hao.demo.shiro.simplify.utils.AjaxUtils;
import com.hao.demo.shiro.simplify.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt 过滤器
 */
@Component
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {
//    @Override
//    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
//        Object tokenObj = token.getPrincipal();
//        if (tokenObj != null) {
//            String tokenStr = tokenObj.toString();
//            HttpServletResponse httpResponse = WebUtils.toHttp(response);
//            Date expiresAt = JWTUtil.getExpiresAt(tokenStr);
//            if (expiresAt != null && new Date().after(expiresAt)) {
//                String newToken = JWTUtil.reSign(tokenStr);
//                if (newToken != null) {
//                    log.warn("密码重置。。。");
//                    //生成新的TOKEN
//                    httpResponse.setHeader("Token", newToken);
//                    httpResponse.setHeader("Access-Control-Expose-Headers","Token");
//                }
//            }
//        }
//        return true;
//    }

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含token字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        log.info("===2===检测header里面是否包含token字段");
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("token");
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        log.info("===2===执行登录操作，校验token正确性");
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Token");
        String message = "登陆过期";
        if (!StringUtils.isEmpty(token)) {
            //日志记录
            String api = getPathWithinApplication(request);
            String url = req.getMethod() + "=" + api;
            Map<String, String[]> params = new HashMap<>(req.getParameterMap());
            log.info("===2===访问URL:{}, 用户ID:{}, 参数:{}", url, JWTUtil.getUid(token), getReqParams(params));

            JWTToken jwtToken = new JWTToken(token);
            //获取登陆端类型
            String loginType = JWTUtil.getLoginType(token);
            jwtToken.setLoginType(loginType);
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
            try {
                getSubject(request, response).login(jwtToken);
                return true;
            } catch (Exception e) {
                if (e.getMessage().equals("异地登陆")) {
                    message = "你的账号已在异地登陆，如非本人操作，请及时修改密码";
                } else {
                    message = "登录过期";
                }
            }
        }
        AjaxUtils.renderJson((HttpServletResponse) response,
                Result.result(null, 403, message));
        return false;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("===2=== onAccessDenied，执行登录操");
        return executeLogin(request, response);
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("===2=== 跨域支支持设置 。。。");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
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

    private Map<String, String[]> getReqParams(Map<String, String[]> params) {
        params.remove("oldPw");
        params.remove("newPw");
        params.remove("tpw");
        params.remove("password");
        return params;
    }
}
