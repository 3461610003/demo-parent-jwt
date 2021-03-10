# jwt的简单使用
## 流程
    1.首先引用java-jwt依赖
    2.创建token
    3.校验、解析token
    Jwt设置 Header 和 Payload

##  内部介绍
    JWT生成token构成:头部（header), 载荷（payload), 签证（signature) 由三部分生成token<br/>
    如：token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJ1c2VyX2lkIjoiMjEzIiwiaXNzIjoiU2VydmljZSIsImV4cCI6MTU3NzUxNDg1MiwiaWF0IjoxNTc2NjUwODUyfQ.ZbhYK0FVdnWV9Akiq9RX0Ms23gVLOZlAUk-79_Xp2dk 
## 存储内容
    Header
         alg：jwt的算法值 
         cty：内容类型
         typ：jwt类型 
         kid：key的id值
    Payload：
         iss：JWT的签发者，可选 
         sub：JWT所面向的用户，可选 
         exp：过期时间戳（必须大于iat），可选 
         nbf：在此时间之前，该jwt都是不可用的，可选  
         iat：签发时间，可选  
         jti：唯一身份标识，主要用来作为一次性token,从而回避重放攻击，可选  
         aud：接收该JWT的一方，可选  
    @link com.hao.demo.jwt.test.JWTTest

# shiro基本原理
## 原理图：
    shiro架构图.webp
## shiro主要有三大功能模块：
    1. Subject：主体，一般指用户。
    2. SecurityManager：安全管理器，管理所有Subject，可以配合内部安全组件。(类似于SpringMVC中的DispatcherServlet)
    3. Realms：用于进行权限信息的验证，一般需要自己实现。
## 细分功能
    1. Authentication：身份认证/登录(账号密码验证)。
    2. Authorization：授权，即角色或者权限验证。
    3. Session Manager：会话管理，用户登录后的session相关管理。
    4. Cryptography：加密，密码加密等。
    5. Web Support：Web支持，集成Web环境。
    6. Caching：缓存，用户信息、角色、权限等缓存到如redis等缓存中。
    7. Concurrency：多线程并发验证，在一个线程中开启另一个线程，可以把权限自动传播过去。
    8. Testing：测试支持；
    9. Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问。
    10. Remember Me：记住我，登录后，下次再来的话不用登录了。


## 常见的shiro异常：
    1. AuthenticationException 认证异常
    Shiro在登录认证过程中，认证失败需要抛出的异常。 AuthenticationException包含以下子类：
    
    1.1. CredentitalsException 凭证异常
    IncorrectCredentialsException 不正确的凭证
    ExpiredCredentialsException 凭证过期
    
    1.2. AccountException 账号异常
    ConcurrentAccessException: 并发访问异常（多个用户同时登录时抛出）
    UnknownAccountException: 未知的账号
    ExcessiveAttemptsException: 认证次数超过限制
    DisabledAccountException: 禁用的账号
    LockedAccountException: 账号被锁定
    UnsupportedTokenException: 使用了不支持的Token
    
    2. AuthorizationException: 授权异常
    Shiro在登录认证过程中，授权失败需要抛出的异常。 AuthorizationException包含以下子类：
    
    2.1. UnauthorizedException:
    抛出以指示请求的操作或对请求的资源的访问是不允许的。
    
    2.2. UnanthenticatedException:
    当尚未完成成功认证时，尝试执行授权操作时引发异常。


Key         	                            Default Value   Description
shiro.enabled	                            true	        Enables Shiro’s Spring module
shiro.web.enabled	                        true	        Enables Shiro’s Spring web module
shiro.annotations.enabled	                true	        Enables Spring support for Shiro’s annotations
shiro.sessionManager.deleteInvalidSessions	true	        Remove invalid session from session storage
shiro.sessionManager.sessionIdCookieEnabled	true	        Enable session ID to cookie, for session tracking
shiro.sessionManager.sessionIdUrlRewriting  Enabled	        true	Enable session URL rewriting support
shiro.userNativeSessionManager	            false	        If enabled Shiro will manage the HTTP sessions instead of the container
shiro.sessionManager.cookie.name	        JSESSIONID	    Session cookie name
shiro.sessionManager.cookie.maxAge	        -1	            Session cookie max age
shiro.sessionManager.cookie.domain	        null	        Session cookie domain
shiro.sessionManager.cookie.path	        null	        Session cookie path
shiro.sessionManager.cookie.secure	        false	        Session cookie secure flag
shiro.rememberMeManager.cookie.name	        rememberMe	    RememberMe cookie name
shiro.rememberMeManager.cookie.maxAge	    one year	    RememberMe cookie max age
shiro.rememberMeManager.cookie.domain	    null	        RememberMe cookie domain
shiro.rememberMeManager.cookie.path	        null	        RememberMe cookie path
shiro.rememberMeManager.cookie.secure	    false	        RememberMe cookie secure flag
shiro.loginUrl	                            /login.jsp	    Login URL used when unauthenticated users are redirected to login page
shiro.successUrl	                        /	            Default landing page after a user logs in (if alternative cannot be found in the current session)
shiro.unauthorizedUrl	                    null	        Page to redirect user to if they are unauthorized (403 page)




