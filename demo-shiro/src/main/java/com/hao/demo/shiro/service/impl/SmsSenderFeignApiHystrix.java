package com.hao.demo.shiro.service.impl;

import com.hao.demo.shiro.service.SmsSenderFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * SmsSenderFeignApiImpl
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/13 14:16
 */
@Slf4j
@Service
public class SmsSenderFeignApiHystrix implements SmsSenderFeignApi {

    @Override
    public void sendValidateCode(String phone, String areaCode, String content) {
        log.warn("【发送短信失败】phone={},areaCode={},content={}", phone, areaCode, content);
    }

    @Override
    public void sendValidateMessage(String phone, String areaCode, String content) {
        log.warn("【发送短信失败】phone={},areaCode={},content={}", phone, areaCode, content);
    }

    @Override
    public void sendEmailMessage(String subject, String email, String content) {
        log.warn("【发送短信失败】subject={},areaCode={},content={}", subject, email, content);
    }
}
