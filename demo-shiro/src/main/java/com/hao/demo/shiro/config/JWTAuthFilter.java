package com.hao.demo.shiro.config;

import com.hao.demo.shiro.model.JWTToken;
import com.hao.demo.shiro.model.Result;
import com.hao.demo.shiro.model.StatusCode;
import com.hao.demo.shiro.util.AjaxUtils;
import com.hao.demo.shiro.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 自定义 jwt 过滤器2
 */
@Component
@Slf4j
public class JWTAuthFilter extends BasicHttpAuthenticationFilter {

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含token字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        log.info("22222过滤器 isLoginAttempt 判断用户是否想要登入 ...");
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("token");
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        log.info("22222过滤器 executeLogin 执行登录 ...");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Token");
        JWTToken jwtToken = new JWTToken(token);
        //获取登陆端类型
        String loginType = JWTUtil.getLoginType(token);
        jwtToken.setLoginType(loginType);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        return true;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("22222过滤器 isAccessAllowed ...");
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("22222过滤器 校验token有效性 ...");
        String token = ((HttpServletRequest) request).getHeader("Token");
        //判断请求的请求头是否带上 "Token"
        if (token != null) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                // 认证
                executeLogin(request, response);

                HttpServletRequest req = (HttpServletRequest) request;
                String api = getPathWithinApplication(request);
                String url = req.getMethod() + "=" + convertApi(api);
                log.info("555 当前用户正在访问的 url => {}", url);
//                if (Objects.equals(JWTUtil.getUid(token), 1L)) {
//                    return true;
//                }
                // 提交给realm进行授权
                if (getSubject(request, response).isPermitted(url)) {
                    return true;
                }
                AjaxUtils.renderJson((HttpServletResponse) response,
                        Result.result(null, StatusCode.NOT_FOUND, "无权限操作！"));
                return false;
            } catch (Exception e) {
                AjaxUtils.renderJson((HttpServletResponse) response,
                        Result.result(null, StatusCode.NO_PERMISSION, "登陆过期"));
                return false;
            }
        }
        //如果请求头不存在 Token，直接返回 false
        AjaxUtils.renderJson((HttpServletResponse) response,
                Result.result(null, StatusCode.NO_PERMISSION, "登陆过期"));
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("22222过滤器 对跨域提供支持");
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

    private String convertApi(String url) {
        return url.replaceAll("[\\d,]+", "*");
    }
}
