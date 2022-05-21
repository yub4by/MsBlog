package com.yppah.service;


import com.yppah.vo.Result;
import com.yppah.vo.params.CommentParams;

public interface CommentsService {

    /**
     * 根据文章id查询所有评论(列表)
     * @param articleId
     * @return
     */
    Result findCommentsByArticleId(Long articleId);

    Result comment(CommentParams commentParams);
}
