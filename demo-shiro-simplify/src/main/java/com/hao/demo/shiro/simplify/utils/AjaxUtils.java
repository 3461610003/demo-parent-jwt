package com.hao.demo.shiro.simplify.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        try (PrintWriter write = response.getWriter()) {
            write.write(content);
            write.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * 输出json，用jackson转换java对象
	 */
	public static void renderJson(HttpServletResponse response, final Object data, final String... headers) {
		initResponseHeader(response, "application/json", headers);
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cache-Control与Content-Type的设置
	 */
	private static void initResponseHeader(HttpServletResponse response, final String contentType,
                                           final String... headers) {
		String encoding = DEFAULT_ENCODING;
		boolean nocache = DEFAULT_NOCACHE;

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
