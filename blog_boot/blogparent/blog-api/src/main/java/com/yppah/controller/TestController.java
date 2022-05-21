package com.yppah.controller;

import com.yppah.dao.pojo.SysUser;
import com.yppah.utils.UserThreadLocal;
import com.yppah.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    /*@RequestMapping
    public Result test(){
        return Result.success(null);
    }*/

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }

}
