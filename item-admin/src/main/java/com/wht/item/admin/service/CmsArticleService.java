package com.wht.item.admin.service;

import com.wht.item.model.CmsArticle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 内容管理 博客文章
 * @author wht
 * @since 2020-05-31 1:34
 */
public interface CmsArticleService {
    /**
     * 获取所有不包含 详细文章
     */
    List<CmsArticle> listAll();

    /**
     * 获取所有不包含 详细文章
     */
    List<CmsArticle> listAllWithContent();

    /**
     * 根据ID获取
     */
    CmsArticle getItem(Long id);

    /**
     * 修改
     */
    int update(Long id, CmsArticle cmsArticle);
    /**
     * 分页查询
     */
    List<CmsArticle> list(String title, String status, String meta, Long authorId, String sortBy, Integer pageSize, Integer pageNum);

    /**
     * 创建
     */
    int create(CmsArticle cmsMeta);

    /**
     * 根据ID删除
     */
    int delete(Long id);
    /**
     * 批量删除
     */
    int delete(List<Long> ids);

    String getArticleContent(String suffix, InputStream inputStream) throws ParserConfigurationException, TransformerException, IOException;
}
