package com.hao.service;

import com.hao.bean.User;

public interface LoginService{

    User getUserByName(String userName);

    void error();
}
