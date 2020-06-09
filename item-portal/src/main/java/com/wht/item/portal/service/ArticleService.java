package com.wht.item.portal.service;

import com.wht.item.model.CmsArticle;

import java.util.List;

/**
 * 前台博客文章
 * @author wht
 * @since 2020-06-08 0:41
 */
public interface ArticleService {
    /**
     * 分页查询
     */
    List<CmsArticle> list(Integer pageSize, Integer pageNum);

    /**
     * 查询文章
     */
    CmsArticle getItem(Long id);

    /**
     * 更新点击量
     */
    void updateCmsArticleHits(CmsArticle cmsArticle);
}
