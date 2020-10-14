package com.hao.demo.shiro.controller;

import com.alibaba.fastjson.JSON;
import com.hao.demo.shiro.model.AdminEntity;
import com.hao.demo.shiro.model.BaseEntity;
import com.hao.demo.shiro.model.BusinessException;
import com.hao.demo.shiro.model.CustomConstants;
import com.hao.demo.shiro.util.*;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户端基础Controller
 */
public class BaseAdminController extends BaseController {

    /**
     * 获取当前登陆用户
     *
     * @return AdminEntity
     */
    protected AdminEntity getCurrentUser(String token) {
        return JSON.parseObject(UserUtils.getCurrentUser(token).toString(), AdminEntity.class);
    }


    /**
     * 获取当前登陆用户
     *
     * @return AdminEntity
     */
    protected AdminEntity getCurrentUser(HttpServletRequest request) {
        return JSON.parseObject(UserUtils.getCurrentUser(request).toString(), AdminEntity.class);
    }

    /**
     * 对密码进行加盐处理
     *
     * @return String
     */
    protected String getSaltPassword(String token, String password) {
        return EncryptUtil.md5(password + getCurrentUser(token).getSalt());
    }

    /**
     * 对密码进行加盐处理
     *
     * @return String
     */
    protected String encryptPassword(String password, String salt) {
        return EncryptUtil.md5(password + salt);
    }

    /**
     * 校验图形验证码
     */
    protected boolean validateImgCode(HttpServletRequest request, RedisUtil redisUtil, String imgCode) {
        boolean flag = true;
        String codeId = request.getHeader(CustomConstants.Cookie_Name.IMG_CODE);
        if (StringUtils.isEmpty(codeId)) {
            return true;
        }
        String code = (String) redisUtil.get("imgCode:" + codeId);
        if (!StringUtils.equals(code, imgCode.toLowerCase())) {
            flag = false;
        }
        //删除redis中验证码
        redisUtil.del("imgCode:" + codeId);
        return !flag;
    }

    /**
     * 校验短信或邮箱验证码
     */
    protected boolean validateMsgCode(String userName, RedisUtil redisUtil, String msgCode) {
        boolean flag = true;
        String key = "validateCode:admin:" + userName;
        Object code = redisUtil.get(key);
        Integer limit = (Integer) redisUtil.get("validateCode:admin:limit:" + userName);
        if (limit != null && limit >= 6) {
            throw new BusinessException(MessageUtils.get("user.validate.limit"));
        }
        if (code == null || !StringUtils.equals(code + "", msgCode)) {
            if (limit == null) {
                limit = 0;
            }
            redisUtil.set("validateCode:admin:limit:" + userName, limit + 1, 1800);
            flag = false;
        }
        //验证成功后删除redis中验证码
        if (flag) {
            redisUtil.del(key, "validateCode:admin:limit:" + userName);
        }
        return flag;
    }

    /**
     * 设置创建人信息
     */
    protected void setCreateInfo(HttpServletRequest request, BaseEntity entity) {
        entity.setCreateTime(new Date());
        AdminEntity admin = getCurrentUser(request);
        if (null != admin) {
            entity.setCreator(admin.getId());
        } else {
            entity.setCreator(0L);
        }
    }

    /**
     * 设置修改人信息
     */
    protected void setUpdateInfo(HttpServletRequest request, BaseEntity entity) {
        entity.setUpdateTime(new Date());
        AdminEntity admin = getCurrentUser(request);
        if (null != admin) {
            entity.setUpdater(admin.getId());
        } else {
            entity.setUpdater(0L);
        }

    }

    protected <T> PageUtils<T> newPageUtils(Integer page, Integer pageSize, String sort, String order, Object query) {
        PageUtils<T> pageInfo = new PageUtils<>(page, pageSize, sort, order);
        Map<String, Object> condition = new HashMap<>();
        condition.put("query", query);
        pageInfo.setCondition(condition);
        return pageInfo;
    }
}
