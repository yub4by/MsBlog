package com.yppah.service;

import com.yppah.dao.pojo.SysUser;
import com.yppah.vo.Result;
import com.yppah.vo.params.LoginParams;

public interface LoginService {

    /**
     * 登录功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    SysUser checkToken(String token);

    /**
     * 退出登录功能
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 用户注册功能
     * @param loginParams
     * @return
     */
    Result register(LoginParams loginParams);
}
