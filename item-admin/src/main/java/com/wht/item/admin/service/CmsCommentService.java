package com.wht.item.admin.service;

import com.wht.item.model.CmsComment;

import java.util.List;

public interface CmsCommentService {
    /**
     * 批量删除
     */
    int deletePoetry(List<Long> ids, List<Long> aids);

    List<CmsComment> listPoetry(String username, Long articleId, String ip, int pageNum, int pageSize, String sortBy);
}
