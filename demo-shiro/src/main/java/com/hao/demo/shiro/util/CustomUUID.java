package com.hao.demo.shiro.util;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * 返回UUID Base64转码后 24位字符
 */
public class CustomUUID {

    public static String getUUIDBase64(){
        String customUUID = Base64.getEncoder().encodeToString(getUUID().getBytes(StandardCharsets.UTF_8));
        return customUUID.substring(0,24);
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static void main(String[] args) {
        String uuid = getUUIDBase64();
        System.out.println(uuid);
    }
}
