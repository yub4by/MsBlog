package com.yppah.controller;

import com.yppah.service.LoginService;
import com.yppah.vo.Result;
import com.yppah.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    // 登录
    @PostMapping
    public Result login(@RequestBody LoginParams loginParams){
//        System.out.println(loginParams); // LoginParams(account=admin, password=admin)
        return loginService.login(loginParams);
    }

}
