package com.hao.demo.shiro.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化工具类
 */
@Slf4j
@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey) {
        return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
    }

    /**
     * 指定某个语言
     */
    public static String get(String msgKey, String language) {
        return messageSource.getMessage(msgKey, null, new Locale(language));
    }

    /**
     * 获取带参国际化翻译值
     */
    public static String getMsg(String msgKey, String ... param) {
        return messageSource.getMessage(msgKey, param, LocaleContextHolder.getLocale());
    }
}
