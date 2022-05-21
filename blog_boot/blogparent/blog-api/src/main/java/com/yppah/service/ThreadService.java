package com.yppah.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yppah.dao.mapper.ArticleMapper;
import com.yppah.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    //期望此操作在线程池 执行 不会影响原有的主线程
    @Async("taskExecutor") // +ThreadPoolConfig线程池配置类
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
        /*
        //阻塞演示
        try{
            Thread.sleep(5000);
            System.out.println("更新完成了。。。");
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/
        Integer viewCounts = article.getViewCounts();
        Article article1Update = new Article();
        article1Update.setViewCounts(viewCounts + 1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        updateWrapper.eq(Article::getViewCounts, viewCounts); //为了在多线程的环境下线程安全，乐观锁思想，相当于CAS操作
        // update article set view_count=100 where view_count=99 and id=11
        articleMapper.update(article1Update, updateWrapper);
    }

}
