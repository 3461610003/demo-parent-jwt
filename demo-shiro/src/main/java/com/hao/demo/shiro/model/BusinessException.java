package com.hao.demo.shiro.model;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 2873663621979727942L;

    public BusinessException(String message) {
        super(message);
    }
}
