package com.yppah.controller;

import com.yppah.service.CommentsService;
import com.yppah.vo.Result;
import com.yppah.vo.params.CommentParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long articleId){
        return commentsService.findCommentsByArticleId(articleId);
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParams commentParams){
        return commentsService.comment(commentParams);
    }

}
