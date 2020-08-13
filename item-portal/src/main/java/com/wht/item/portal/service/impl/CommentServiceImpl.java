package com.wht.item.portal.service.impl;

import com.wht.item.mapper.CmsCommentMapper;
import com.wht.item.model.CmsComment;
import com.wht.item.model.CmsCommentExample;
import com.wht.item.portal.dto.CommentParams;
import com.wht.item.portal.service.ArticleService;
import com.wht.item.portal.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CmsCommentMapper commentMapper;
    @Resource
    private ArticleService articleService;

    @Override
    public List<CmsComment> list(long id) {
        CmsCommentExample example = new CmsCommentExample();
        example.createCriteria().andArticleIdEqualTo(id);
        return commentMapper.selectByExample(example);
    }

    @Override
    public Long createComment(CommentParams commentParams) {
        CmsComment cmsComment = new CmsComment();
        BeanUtils.copyProperties(commentParams, cmsComment);
        int count = commentMapper.insertSelective(cmsComment);
        if (count > 0) articleService.updateCmsArticleCommentCount(cmsComment.getArticleId());
        return cmsComment.getId();
    }
}
