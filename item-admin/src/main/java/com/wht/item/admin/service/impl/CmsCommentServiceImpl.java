package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.wht.item.admin.service.CmsCommentService;
import com.wht.item.mapper.CmsCommentMapper;
import com.wht.item.model.CmsComment;
import com.wht.item.model.CmsCommentExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CmsCommentServiceImpl implements CmsCommentService {
    @Resource
    private CmsCommentMapper commentMapper;
    @Resource
    private CmsArticleServiceImpl articleService;

    @Override
    public int deletePoetry(List<Long> ids, List<Long> aids) {
        CmsCommentExample example = new CmsCommentExample();
        example.createCriteria().andIdIn(ids);
        int count  = commentMapper.deleteByExample(example);
        if (count > 0) delArticleCommentCount(aids);
        return count;
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
    public List<CmsComment> listPoetry(String username, Long articleId, String ip, int pageNum, int pageSize, String sortBy) {
        PageHelper.startPage(pageNum, pageSize, sortBy);
        CmsCommentExample example = new  CmsCommentExample();
        CmsCommentExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(username)) criteria.andUsernameLike('%' + username + '%');
        if (articleId != null) criteria.andArticleIdEqualTo(articleId);
        if (!StringUtils.isEmpty(ip)) criteria.andIpLike( '%' + ip + '%');
        return commentMapper.selectByExample(example);
    }
}
