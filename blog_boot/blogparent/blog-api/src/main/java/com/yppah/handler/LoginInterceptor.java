package com.yppah.handler;

import com.alibaba.fastjson.JSON;
import com.yppah.dao.pojo.SysUser;
import com.yppah.service.LoginService;
import com.yppah.utils.UserThreadLocal;
import com.yppah.vo.ErrorCode;
import com.yppah.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    // preHandle()在执行controller方法(Handler)之前进行执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 1. 需要判断 请求的接口路径 是否为 HandlerMethod (controller方法)
         * 2. 判断 token是否为空，如果为空 未登录
         * 3. 如果token 不为空，登录验证 loginService checkToken
         * 4. 如果认证成功 放行即可
         */
        // 1
        if ( !(handler instanceof HandlerMethod) ){
            // handler 可能是 RequestResourceHandler
            // springboot 程序 访问静态资源 默认去classpath下的static目录去查询
            return true; //放行
        }
        // 2
        String token = request.getHeader("Authorization");

        // 需要@Slf4j注解，不然log报错
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JSON.toJSONString(result));
            return false;
        }
        // 3
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JSON.toJSONString(result));
            return false;
        }
        // 4 登录验证成功，放行

        // 希望在controller中 直接获取用户的信息 怎么获取?采用ThreadLocal
        UserThreadLocal.put(sysUser);

        return true;
    }

    // 所有方法执行完后执行afterCompletion()
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除 ThreadLocal中用完的信息 会有内存泄漏的风险
        // ThreadLocal内存泄露原理详解：https://www.bilibili.com/video/BV1Gb4y1d7zb?p=17&spm_id_from=pageDriver
        UserThreadLocal.remove(); // 技术重难点
    }
}
