package com.yppah.common.cache;


import java.lang.annotation.*;

/**
 * 优化：统一缓存处理
 *      访问内存比访问磁盘快1000倍以上
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheAnnotation {

    long expire() default 1 * 60 * 1000; //缓存过期时间

    String name() default ""; //缓存标识 key

}
