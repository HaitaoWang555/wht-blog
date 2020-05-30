package com.wht.item.admin.dao;

import com.wht.item.model.CmsArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章自定义Dao
 * @author wht
 * @since 2020-05-31 6:10
 */
public interface CmsArticleDao {
    List<CmsArticle> search(
            @Param("title")String title,
            @Param("status")String status,
            @Param("authorId")Long authorId,
            @Param("meta")List meta
    );
}
