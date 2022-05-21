package com.yppah.controller;

import com.yppah.common.aop.LogAnnotation;
import com.yppah.common.cache.CacheAnnotation;
import com.yppah.service.ArticleService;
import com.yppah.vo.Result;
import com.yppah.vo.params.ArticleParams;
import com.yppah.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController //项目使用json数据进行交互
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService; //ArticleServiceImpl上加@Service注解后不报错，不然此处报错

    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @LogAnnotation(module="文章", operator="获取文章列表") // 加上此自定义注解，表示对此接口进行日志记录
    @CacheAnnotation(expire = 5 * 60 * 1000,name = "list_article")
    public Result listArticle(@RequestBody PageParams pageParams){
//        int i = 1/0; //测试AllExceptionHandler
        return articleService.listArticle(pageParams);
    }

    /**
     * 首页最热文章
     * @return
     */
    @PostMapping("hot")
    @CacheAnnotation(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){
        int limit = 5; // 取查看次数最多前五的文章作为最热文章
        return articleService.hotArticle(limit);
    }

    /**
     * 首页最新文章
     * @return
     */
    @PostMapping("new")
    @CacheAnnotation(expire = 5 * 60 * 1000,name = "new_article")
    public Result newArticle(){
        int limit = 5; // 取发布时间最近5篇文章作为最新文章
        return articleService.newArticle(limit);
    }

    /**
     * 首页文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    /**
     * 文章详情页展示
     * @param articleId
     * @return
     */
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParams articleParams){
        return articleService.publish(articleParams);
    }

}
