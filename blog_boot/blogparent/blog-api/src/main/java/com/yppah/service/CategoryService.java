package com.yppah.service;

import com.yppah.vo.CategoryVo;
import com.yppah.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
