package com.hao.demo.shiro.model;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;


/**
 * 通用返回包装类
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -5083023465598029524L;

    /** 返回结果集 */
    private T data;
    /** 状态码 */
    private Integer code;
    /** 返回消息 */
    private String msg;

    public Result() {

    }

    public Result(T data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public Result(HttpServletResponse response, T data, Integer code, String msg) {
        response.setStatus(code);
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    /**
     * 通用返回方法
     */
    public static <T> Result<T> result(T data, Integer code, String msg) {
        return new Result<>(data, code, msg);
    }
}
