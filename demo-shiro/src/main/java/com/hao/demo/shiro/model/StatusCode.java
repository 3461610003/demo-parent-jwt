package com.hao.demo.shiro.model;


/**
 * 返回状态码
 */
public class StatusCode {
	/** 会话失效 */
	public static final Integer LOGOUT = 403;
	/** 权限不足 */
	public static final Integer NO_PERMISSION = 403;
	/** 404 */
	public static final Integer NOT_FOUND = 404;
	/** 操作成功 */
	public static final Integer OPERATION_SUCCESS = 200;
	/** 操作失败 */
	public static final Integer OPERATION_ERROR = 500;
	/** 查询成功 */
	public static final Integer FIND_SUCCESS = 200;
	/** 两步认证 */
	public static final Integer AUTH = 201;
	/** 查询失败 */
	public static final Integer FIND_ERROR = 500;
	/** 保存成功 */
	public static final Integer SAVE_SUCCESS = 200;
	/** 保存失败 */
	public static final Integer SAVE_ERROR = 500;
	/** 删除成功 */
	public static final Integer REMOVE_SUCCESS = 200;
	/** 删除失败 */
	public static final Integer REMOVE_ERROR = 500;
	/** 撤销成功 */
	public static final Integer REPEAL_SUCCESS = 200;
	/** 撤销失败 */
	public static final Integer REPEAL_ERROR = 500;
	/** 连接成功 */
	public static final Integer LINK_SUCCESS = 200;
	/** 连接失败 */
	public static final Integer LINK_ERROR = 500;
	/** 程序异常 */
	public static final Integer EXCEPTION = 510;
	/** 缺少必填项 */
	public static final Integer LACK_VALUE = 511;
	/** 无对应数据 */
	public static final Integer NO_RETURN_DATA = 513;
	/** 限制登陆 */
	public static final Integer LIMITE_LOGIN = 410;

	/** 业务逻辑异常 */
	public static final Integer BUSINESS_EXCEPTION = 500;
}
