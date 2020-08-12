package com.wht.item.portal.service;

import com.wht.item.model.CmsComment;
import com.wht.item.portal.dto.CommentParams;

import java.util.List;

public interface CommentService {

    List<CmsComment> list(long id);

    int createComment(CommentParams commentParams);
}
