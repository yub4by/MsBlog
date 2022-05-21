package com.yppah.utils;

import com.yppah.dao.pojo.SysUser;

public class UserThreadLocal {

    private UserThreadLocal(){}

    // 线程隔离变量
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
