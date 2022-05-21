package com.yppah.config;

import com.yppah.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置
        registry.addMapping("/**").allowedOrigins("http://localhost:8080"); //8080是前端vue工程的启动/访问端口
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 暂时假设仅拦截test接口，后续实际遇到需要拦截的接口时，在配置为真正的拦截接口
        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/register")
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change") // 评论、发布的前提是登录
                .addPathPatterns("/articles/publish");
    }
}
