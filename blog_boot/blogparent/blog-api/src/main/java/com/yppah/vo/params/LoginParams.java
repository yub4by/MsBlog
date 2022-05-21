package com.yppah.vo.params;

import com.yppah.vo.Result;
import lombok.Data;

@Data
public class LoginParams {

    private String account;
    private String password;

    private String nickname;

    // 登录时用前两个属性；注册时三个属性全用
}
