package com.wht.item.portal.service;

import com.wht.item.model.CmsArticle;
import com.wht.item.portal.dto.Archives;

import java.util.List;

/**
 * @author wht
 * @since 2020-06-10 6:45
 */
public interface ArchiveService {
    /**
     * 获取所有文章
     */
    List<CmsArticle> getAll();
    /**
     * 获取归档
     */
    List<Archives> archive(List<CmsArticle> articleList, List<Archives> archivesList);

}
