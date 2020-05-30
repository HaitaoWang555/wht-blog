package com.wht.item.admin.dao;

import com.wht.item.model.CmsMeta;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签与分类自定义Dao
 * @author wht
 * @since 2020-05-30 21:56
 */
public interface CmsMetasDao {
    void insertList(@Param("list") List<CmsMeta> list);

    List<CmsMeta> selectByArticle(@Param("articleId") Long articleId, @Param("type") String type);

    CmsMeta selectByName(String name);
}
