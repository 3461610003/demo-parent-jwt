package com.hao.demo.shiro.util;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EntityToDto工具类
 */
public class EntityToDtoUtil {


    public static <T> T copyObject(Object obj, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (null != obj) {
            assert t != null;
            BeanUtils.copyProperties(obj, t);
        }
        return t;
    }

    public static <T> List<T> copyList(Object obj, Class<T> clazz) {
        if (null == obj) {
            return new ArrayList<>();
        }
        return JSON.parseArray(JSON.toJSONString(obj), clazz);
    }

    public static Map<String, Object> copyMap(Map map) {
        return JSON.parseObject(JSON.toJSONString(map));
    }
}
