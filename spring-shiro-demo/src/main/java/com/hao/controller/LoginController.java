package com.hao.controller;

import com.hao.bean.User;
import com.hao.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
@Slf4j
public class LoginController {

    @Resource
    private LoginService loginService;

    @GetMapping("/login")
    public ModelAndView login(User user) {
        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            return new ModelAndView("login", "msg", "请输入用户名和密码！");
        }
        String msg;
        //用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
//            subject.checkRole("admin");
//            subject.checkPermissions("query", "add");
            return new ModelAndView("index");
        } catch (UnknownAccountException e) {
            log.error("用户名不存在！", e);
            msg =  "用户名不存在！";
        } catch (AuthenticationException e) {
            log.error("账号或密码错误！", e);
            msg =  "账号或密码错误！";
        } catch (AuthorizationException e) {
            log.error("没有权限！", e);
            msg =  "没有权限";
        }
        return new ModelAndView("login", "msg", msg);
    }

    @RequiresRoles("admin")
    @GetMapping("/admin")
    public String admin() {
        return "admin success!";
    }

    @RequiresPermissions("query")
    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequiresPermissions("add")
    @GetMapping("/add")
    public String add() {
        return "add success!";
    }

    @GetMapping("/unauth")
    public String error() {
        loginService.error();
        return null;
    }
}
