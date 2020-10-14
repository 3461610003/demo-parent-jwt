package com.hao.demo.shiro.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
  * 加密工具类
*/
public class EncryptUtil {
	
	public static String md5(String str){
		if(str==null){
			return null;
		}
		return DigestUtils.md5Hex(str);
	}
}
