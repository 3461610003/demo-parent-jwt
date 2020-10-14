package com.hao.demo.shiro.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.demo.shiro.model.CommonConstants;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Ajax工具类
 */
public class AjaxUtils {
	private static final String HEADER_ENCODING = "encoding";
	private static final String HEADER_NOCACHE = "no-cache";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final boolean DEFAULT_NOCACHE = true;

	private static ObjectMapper mapper = new ObjectMapper();// JsonUtils

	/**
	 * 直接输出内容
	 */
	public static void render(HttpServletResponse response, final String contentType, final String content,
                              final String... headers) {
		initResponseHeader(response, contentType, headers);
		PrintWriter write = null;
		try {
			write = response.getWriter();
			write.write(content);
			write.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (write != null) {
				write.close();
			}
		}
	}

	/**
	 * 输出text
	 */
	public static void renderText(HttpServletResponse response, final String text, final String... headers) {
		render(response, CommonConstants.Content_Type.TEXT_TYPE, text, headers);
	}

	/**
	 * 输出html
	 */
	public static void renderHtml(HttpServletResponse response, final String html, final String... headers) {
		render(response, CommonConstants.Content_Type.HTML_TYPE, html, headers);
	}

	/**
	 * 输出xml
	 */
	public static void renderXml(HttpServletResponse response, final String xml, final String... headers) {
		render(response, CommonConstants.Content_Type.XML_TYPE, xml, headers);
	}

	/**
	 * 输出json
	 */
	public static void renderJson(HttpServletResponse response, final String jsonString, final String... headers) {
		render(response, CommonConstants.Content_Type.JSON_TYPE, jsonString, headers);
	}

	/**
	 * 输出json，用jackson转换java对象
	 */
	public static void renderJson(HttpServletResponse response, final Object data, final String... headers) {
		initResponseHeader(response, CommonConstants.Content_Type.JSON_TYPE, headers);
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出jsonp，用jackson转换java对象
	 */
	public static void renderJsonp(HttpServletResponse response, final String callbackName, final Object object,
                                   final String... headers) {
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String result = new StringBuilder().append(callbackName).append("(").append(jsonString).append(");").toString();

		// 渲染Content-Type为javascript的返回内容,输出结果为javascript语句
		render(response, CommonConstants.Content_Type.JS_TYPE, result, headers);
	}

	/**
	 * 输入object,输出json 或者 jsonp
	 */
	public static void renderJsonOrJsonp(HttpServletResponse response, final String callbackName, final Object object,
                                         final String... headers) {
		if (null != callbackName && !StringUtils.isEmpty(callbackName)) {
			renderJsonp(response, callbackName, object, headers);
		} else {
			renderJson(response, object, headers);
		}
	}

	/**
	 * 输入jsonString,输出json 或者 jsonp
	 */
	public static void renderJsonOrJsonp(HttpServletResponse response, final String callbackName,
                                         final String jsonString, final String... headers) {
		if (null != callbackName && !StringUtils.isEmpty(callbackName)) {
			renderJsonp(response, callbackName, jsonString, headers);
		} else {
			renderJson(response, jsonString, headers);
		}
	}

	/**
	 * Cache-Control与Content-Type的设置
	 */
	private static void initResponseHeader(HttpServletResponse response, final String contentType,
                                           final String... headers) {
		String encoding = DEFAULT_ENCODING;
		Boolean nocache = DEFAULT_NOCACHE;

		for (String header : headers) {
			String headerName = StringUtils.substringBefore(header, ":");
			String headerValue = StringUtils.substringAfter(header, ":");

			if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
				encoding = headerValue;
			} else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
				nocache = Boolean.parseBoolean(headerValue);
			}
		}

		String fullContentType = contentType + ";charset=" + encoding;
		response.setContentType(fullContentType);
		if (nocache) {
			setDisableCacheHeader(response);
		}
	}

	/**
	 * 禁止缓存
	 */
	public static void setDisableCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		// Http 1.1 header
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}
}
