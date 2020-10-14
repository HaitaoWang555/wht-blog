package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.wht.item.admin.dao.CmsCommentDao;
import com.wht.item.admin.service.CmsCommentService;
import com.wht.item.mapper.CmsCommentMapper;
import com.wht.item.model.CmsComment;
import com.wht.item.model.CmsCommentExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CmsCommentServiceImpl implements CmsCommentService {
    @Resource
    private CmsCommentMapper commentMapper;
    @Resource
    private CmsCommentDao commentDao;
    @Resource
    private CmsArticleServiceImpl articleService;

    @Override
    public int deleteComment(List<Long> ids, List<Long> aids) {
        CmsCommentExample example = new CmsCommentExample();
        example.createCriteria().andIdIn(ids);
        int count  = commentMapper.deleteByExample(example);
        if (count > 0) delArticleCommentCount(aids);
        return count;
    }
    private int deleteComment(List<Long> ids) {
        CmsCommentExample example = new CmsCommentExample();
        example.createCriteria().andIdIn(ids);
        return commentMapper.deleteByExample(example);
    }

    private void delArticleCommentCount(List<Long> aids) {
        Map<Long, Integer> map = new HashMap<>();
        for (Long aid : aids) {
            map.merge(aid, 1, Integer::sum);
        }
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            articleService.updateCmsArticleCommentCount(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public List<CmsComment> listComment(String username, Long articleId, String ip, String link, int pageNum, int pageSize, String sortBy) {
        PageHelper.startPage(pageNum, pageSize, sortBy);
        CmsCommentExample example = new  CmsCommentExample();
        CmsCommentExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(username)) criteria.andUsernameLike('%' + username + '%');
        if (articleId != null) criteria.andArticleIdEqualTo(articleId);
        if (!StringUtils.isEmpty(ip)) criteria.andIpLike( '%' + ip + '%');
        if (!StringUtils.isEmpty(link)) criteria.andLinkLike( '%' + link + '%');
        return commentMapper.selectByExample(example);
    }

    @Override
    public int updateCommentLink(List<Long> ids) {
        List<CmsComment> commentList  = new ArrayList<>();
        for (Long id : ids) {
            CmsComment comment = new CmsComment();
            comment.setId(id);
            comment.setLink("");
            commentList.add(comment);
        }
        return commentDao.updateBatch(commentList);
    }

    @Override
    public int deleteCommentByAids(List<Long> aids) {
        CmsCommentExample example = new  CmsCommentExample();
        CmsCommentExample.Criteria criteria = example.createCriteria();
        criteria.andArticleIdIn(aids);
        List<CmsComment> commentList = commentMapper.selectByExample(example);
        if (commentList.size() == 0) return 0;
        List<Long> ids = commentList.stream().map(CmsComment::getId).collect(Collectors.toList());
        return deleteComment(ids);
    }
}
