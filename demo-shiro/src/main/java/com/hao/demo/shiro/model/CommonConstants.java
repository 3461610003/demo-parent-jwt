package com.hao.demo.shiro.model;


/**
 * 通用常量类
 */
public class CommonConstants {

    public interface Content_Type {
        String TEXT_TYPE = "text/plain";
        String JSON_TYPE = "application/json";
        String XML_TYPE = "text/xml";
        String HTML_TYPE = "text/html";
        String JS_TYPE = "text/javascript";
        String EXCEL_TYPE = "application/vnd.ms-excel";
    }

    public interface ENCODE {
        String UTF_8 = "UTF-8";
        String ISO_8859_1 = "ISO-8859-1";
        String GB2312 = "GB2312";
        String GET = "GET";
        String POST = "POST";
    }

    //状态
    public interface Status {
        Integer DISABLE = 0;
        Integer ENABLED = 1;
    }

    //显示状态
    public interface IsShow {
        Integer NO = 0;
        Integer YES = 1;
    }

    //删除状态
    public interface IsDeleted {
        Integer NO = 0;
        Integer YES = 1;
    }

    //客户端类型
    public interface ClientType {
        Integer APP = 1;
        Integer PC = 2;
        Integer H5 = 3;
    }


}
