package com.hao.demo.shiro.simplify.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;


/**
 * 通用返回包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -5083023465598029524L;

    /** 返回结果集 */
    private T data;
    /** 状态码 */
    private Integer code;
    /** 返回消息 */
    private String msg;

    /**
     * 通用返回方法
     */
    public static <T> Result<T> result(T data, Integer code, String msg) {
        return new Result<>(data, code, msg);
    }
}
