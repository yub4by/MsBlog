package com.yppah.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yppah.dao.dos.Archives;
import com.yppah.dao.mapper.ArticleBodyMapper;
import com.yppah.dao.mapper.ArticleMapper;
import com.yppah.dao.mapper.ArticleTagMapper;
import com.yppah.dao.pojo.Article;
import com.yppah.dao.pojo.ArticleBody;
import com.yppah.dao.pojo.ArticleTag;
import com.yppah.dao.pojo.SysUser;
import com.yppah.service.*;
import com.yppah.utils.UserThreadLocal;
import com.yppah.vo.ArticleBodyVo;
import com.yppah.vo.ArticleVo;
import com.yppah.vo.Result;
import com.yppah.vo.TagVo;
import com.yppah.vo.params.ArticleParams;
import com.yppah.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth()
        );
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records, true, true));
    }

    /*
    @Override
    public Result listArticle(PageParams pageParams) {
        //注意：Page来自mybatisplus
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (pageParams.getCategoryId() != null){
            // and category_id=#{categoryId}
            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
        }
        List<Long> articleIdList = new ArrayList<>();
        if (pageParams.getTagId() != null){
            //加入标签 条件查询
            //article表中 并没有tag字段 一篇文章 有多个标签
            //关联表article_tag中：  article_id 1 : n tag_id
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList.size() > 0){
                // and id in(1,2,3)
                queryWrapper.in(Article::getId, articleIdList);
            }
        }

//        queryWrapper.orderByDesc(Article::getWeight);  //先按照置顶排序
//        queryWrapper.orderByDesc(Article::getCreateDate); //再按照创建日期倒序排列，相当于order by create_date desc
        //以上两行可以简写为如下一行
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords(); //records能直接返回吗？ 很明显不能！应转为vo再返回（即数据库数据与页面数据解耦）
        List<ArticleVo> articleVoList = copyList(records, true, true);
        return Result.success(articleVoList);
    }
    */

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle); // 仅需id和标题就行
        queryWrapper.last("limit " + limit); // 注意空格一定要有
        // 以上作用：select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        // 以上作用：select id,title from article order by create_date desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    /*
    select year(create_date) as year,
            month(create_date) as month,
            count(*) as count
    from ms_article group by year,month
     */
    @Override
    public Result listArchives() {
        /*List<Archives> archivesList = articleMapper.listArchives();
        for (Archives archives : archivesList) {
            System.out.println(archives);
        }
        return Result.success(archivesList);*/
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record: records){
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    //重载
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record: records){
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    /*private ArticleVo copy(Article article, boolean isTag, boolean isAuthor){ //并不是所有文章都带有标签和作者
        ArticleVo articleVo = new ArticleVo();
        //转移数据
        BeanUtils.copyProperties(article, articleVo);
        //因为Article中的属性createDate是long，转不过来，需要单独处理一下
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签 ，作者信息
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        return articleVo;
    }*/
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyId和categoryid 去做关联查询
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
        /*查看完文章了，新增阅读数，有没有问题呢？
            查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
            更新 增加了此次接口的 耗时 如果一旦更新出问题，不能影响 查看文章的操作
        解决：利用线程池进行优化
              可以把更新操作扔到线程池中去执行，和主线程就不相关了*/
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParams articleParams) {
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        SysUser sysUser = UserThreadLocal.get(); //需要在WebMVCConfig中进行拦截/articles/publish，不然空指针
        // 1
        Article article = new Article();
        // 2
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common); //0 默认不置顶
        article.setViewCounts(0);
        article.setCommentCounts(0);
        article.setSummary(articleParams.getSummary());
        article.setTitle(articleParams.getTitle());
        article.setCategoryId(Long.parseLong(articleParams.getCategory().getId()));
        article.setCreateDate(System.currentTimeMillis());
        articleMapper.insert(article); //插入之后会生成文章id
        // 3
        List<TagVo> tagVoList = articleParams.getTags();
        if (tagVoList != null){
            for (TagVo tagVo : tagVoList) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tagVo.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        // 4
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParams.getBody().getContent());
        articleBody.setContentHtml(articleParams.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

        Map<String, String> map = new HashMap<>();
        map.put("id", article.getId().toString());
        return Result.success(map);
    }
}
