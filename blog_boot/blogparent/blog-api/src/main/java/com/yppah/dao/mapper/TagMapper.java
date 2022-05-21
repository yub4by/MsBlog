package com.yppah.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yppah.dao.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章id查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * 查询最热的前limit条标签
     * @param limit
     * @return
     */
    List<Long> findHotTagIds(int limit);

    /**
     * 根据标签id查询标签对象
     * @param tagIds
     * @return
     */
    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
