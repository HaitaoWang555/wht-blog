package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.wht.item.admin.dao.CmsArticleDao;
import com.wht.item.admin.service.CmsArticleService;
import com.wht.item.mapper.CmsArticleMapper;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.CmsArticleExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 内容管理 博客文章
 *
 * @author wht
 * @since 2020-05-31 1:39
 */
@Service
public class CmsArticleServiceImpl implements CmsArticleService {
    @Resource
    private CmsArticleMapper articleMapper;
    @Resource
    private CmsArticleDao articleDao;

    @Override
    public List<CmsArticle> listAll() {
        return articleMapper.selectByExample(new CmsArticleExample());
    }

    @Override
    public List<CmsArticle> listAllWithContent() {
        return articleMapper.selectByExampleWithBLOBs(new CmsArticleExample());
    }

    @Override
    public CmsArticle getItem(Long id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, CmsArticle cmsArticle) {
        return articleMapper.updateByPrimaryKeySelective(cmsArticle);
    }

    @Override
    public List<CmsArticle> list(String title, String status, String meta, Long authorId, String sortBy, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<Integer> metaArr = new ArrayList<>();
        if (meta != null) {
            for (String ele : meta.split(",")) {
                Integer item = Integer.valueOf(ele);
                metaArr.add(item);
            }
        }
        return articleDao.search(title, status, authorId, metaArr);
    }

    @Override
    public int create(CmsArticle cmsArticle) {
        return articleMapper.insertSelective(cmsArticle);
    }

    @Override
    public int delete(Long id) {
        return articleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids) {
        CmsArticleExample example = new CmsArticleExample();
        example.createCriteria().andIdIn(ids);
        return articleMapper.deleteByExample(example);
    }
}
