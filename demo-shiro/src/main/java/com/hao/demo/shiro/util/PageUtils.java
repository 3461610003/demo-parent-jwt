package com.hao.demo.shiro.util;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 分页工具类
 */
@Data
public class PageUtils<T> implements Serializable {

    private static final long serialVersionUID = 7725491844599057308L;
    /** 默认当前页 */
    private static final Integer DEFAULT_PAGE = 1;
    /** 默认单页条数 */
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    /** 默认排序关键字 */
    private static final String DEFAULT_SORT ="id";
    /** 默认排序顺序-倒序 */
    private static final String DEFAULT_ORDER ="desc";
    /** 记录 */
    private List<T> data;
    /** 总记录数 */
    private Long total;
    /** 当前页数 */
    @JSONField(serialize=false)
    private Integer page;
    /** 单页总条数 */
    @JSONField(serialize=false)
    private Integer pageSize;
    /** 分页起始 */
    @JSONField(serialize=false)
    private Integer rowStart;
    /** 分页结束 */
    @JSONField(serialize=false)
    private Integer rowEnd;
    /** 排序关键字 */
    @JSONField(serialize=false)
    private String sort;
    /** 排序顺序 asc 顺序 desc 倒序 */
    @JSONField(serialize=false)
    private String order;
    /** 排序规则 例如：order by id asc ,name desc */
    @JSONField(serialize=false)
    private String orders;
    /** 显示条目数 例如：limit 0,10*/
    @JSONField(serialize=false)
    private String limits;
    /** 筛选条件 */
    @JSONField(serialize=false)
    private Map<String, Object> condition = new HashMap<>();

    public PageUtils() {

    }

    public PageUtils(Integer page, Integer pageSize) {
        if (null == page || page <= 0) {
            this.page = DEFAULT_PAGE;
        } else {
            this.page = page;
        }
        if (null == pageSize || pageSize <= 0) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        } else {
            this.pageSize = pageSize;
        }
        this.rowStart = (this.page - 1) * this.pageSize;
        this.rowEnd = this.pageSize;
        this.limits = " limit "+rowStart+","+rowEnd;
    }

    public PageUtils(Integer page, Integer pageSize, String sort, String order) {
        this(page, pageSize);
        if (null != sort) {
            //防止sql注入
            if (Pattern.matches("[a-zA-Z0-9_]*",sort)){
                this.sort = sort;
            }else {
                this.sort = DEFAULT_SORT;
            }
        } else {
            this.sort = DEFAULT_SORT;
        }
        if (null != order) {
            //防止sql注入
            if (order.toUpperCase().equals("ASC") || order.toUpperCase().equals("DESC")){
                this.order = order;
            }else {
                this.order = DEFAULT_ORDER;
            }
        } else {
            this.order = DEFAULT_ORDER;
        }
        this.orders = "order by " + this.sort + " " + this.order;
    }

    public PageUtils(List<T> data, Long total) {
        super();
        this.data = data;
        this.total = total;
    }
}
