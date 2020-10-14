package com.hao.demo.shiro.model;


/**
  * 返回消息
*/
public class ResultMessage {
	/** 会话失效 */
	public static final String LOGINOUT = "common.loginout";
	/** 权限不足 */
	public static final String NO_PERMISSION = "common.no.permission";
	/** 操作成功 */
	public static final String OPERATION_SUCCESS = "common.success.operation";
	/** 操作失败 */
	public static final String OPERATION_ERROR = "common.failed.operation";
	/** 查询成功 */
	public static final String FIND_SUCCESS = "common.success.query";
	/** 查询失败 */
	public static final String FIND_ERROR = "common.failed.query";
	/** 保存成功 */
	public static final String SAVE_SUCCESS = "common.success.save";
	/** 保存失败 */
	public static final String SAVE_ERROR = "common.failed.save";
	/** 删除成功 */
	public static final String REMOVE_SUCCESS = "common.remove.success";
	/** 删除失败 */
	public static final String REMOVE_ERROR = "common.remove.error";
	/** 撤销成功 */
	public static final String REPEAL_SUCCESS = "common.repeal.success";
	/** 撤销失败 */
	public static final String REPEAL_ERROR = "common.repeal.error";
	/** 连接成功 */
	public static final String LINK_SUCCESS = "common.link.success";
	/** 连接失败 */
	public static final String LINK_ERROR = "common.link.error";
	/** 程序异常 */
	public static final String EXCEPTION = "common.exception";
	/** 缺少必填项 */
	public static final String LACK_VALUE = "common.required";
	/** 无对应数据 */
	public static final String NO_RETURN_DATA = "common.no.return.data";
	/** 重复提交 */
	public static final String REPEATED_SUBMISSION = "common.repeated";
}
