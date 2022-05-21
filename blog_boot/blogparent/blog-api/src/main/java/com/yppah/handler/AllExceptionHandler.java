package com.yppah.handler;

import com.yppah.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice // 对加了@Controller注解的方法进行拦截处理，实际就是AOP的实现
public class AllExceptionHandler {

    // 统一异常处理
    @ExceptionHandler(Exception.class) // 进行异常处理，处理Exception.class的异常
    @ResponseBody // 返回json数据，不加这个注解的话默认就是返回页面
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(-999, "系统异常");
    }
}
