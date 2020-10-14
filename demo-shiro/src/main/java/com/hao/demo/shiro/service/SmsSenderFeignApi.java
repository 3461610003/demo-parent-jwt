package com.hao.demo.shiro.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 短信发送
 */
//@FeignClient(value = "alicms-provider-sms", fallback = SmsSenderFeignApiHystrix.class)
public interface SmsSenderFeignApi {

    @PostMapping(value = "/sms-sender/code")
    void sendValidateCode(@RequestParam("phone") String phone, @RequestParam("areaCode") String areaCode,
                          @RequestParam("content") String content);

    @PostMapping(value = "/sms-sender/message")
    void sendValidateMessage(@RequestParam("phone") String phone, @RequestParam("areaCode") String areaCode,
                             @RequestParam("content") String content);

    @PostMapping(value = "/sms-sender/email/message")
    void sendEmailMessage(@RequestParam("subject") String subject, @RequestParam("email") String email,
                          @RequestParam("content") String content);
}
