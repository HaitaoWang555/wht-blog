package com.wht.item.admin.service;

import com.wht.item.model.CmsComment;

import java.util.List;

public interface CmsCommentService {
    /**
     * 批量删除
     */
    int deleteComment(List<Long> ids, List<Long> aids);
    /**
     * 通过文章ID批量删除
     */
    int deleteCommentByAids(List<Long> aids);

    List<CmsComment> listComment(String username, Long articleId, String ip, String link, int pageNum, int pageSize, String sortBy);
    /**
     * 批量删除评论用户链接
     */
    int updateCommentLink(List<Long> ids);
}
