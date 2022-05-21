package com.yppah.vo.params;

import com.yppah.vo.CategoryVo;
import com.yppah.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParams {
    private Long id;

    private ArticleBodyParams body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
