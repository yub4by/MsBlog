package com.yppah.service;

import com.yppah.dao.pojo.SysUser;
import com.yppah.vo.Result;
import com.yppah.vo.UserVo;

public interface SysUserService {

    UserVo findUserVoById(Long id);

    SysUser findUserById(Long authorId);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

}
