package com.wht.item.admin.dao;

import com.wht.item.model.CmsComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论管理
 *
 * @author wht
 * @since 2020-08-18 5:32
 */
public interface CmsCommentDao {
    /**
     * 批量更新评论链接
     */
    int updateBatch(@Param("list") List<CmsComment> list);
}
