package com.wht.item.admin.dao;

/**
 * 文章 标签/分类 关联
 *
 * @author wht
 * @since 2020-05-31 3:34
 */
public interface CmsMiddleDao {
    void deleteByMiddle(Long articleId, Long metaId);
}
