package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.wht.item.admin.dao.CmsMetasDao;
import com.wht.item.admin.dto.CmsMetasParam;
import com.wht.item.admin.service.CmsMetasService;
import com.wht.item.mapper.CmsMetaMapper;
import com.wht.item.model.CmsMeta;
import com.wht.item.model.CmsMetaExample;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wht
 * @since 2020-05-30 19:46
 */
@Service
public class CmsMetasServiceImpl implements CmsMetasService {

    @Resource
    private CmsMetaMapper metaMapper;
    @Resource
    private CmsMetasDao metasDao;

    @Override
    public List<CmsMeta> listAll() {
        return metaMapper.selectByExample(new CmsMetaExample());
    }

    @Override
    public CmsMeta getItem(Long id) {
        return metaMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, CmsMeta cmsMeta) {
        cmsMeta.setId(id);
        return metaMapper.updateByPrimaryKeySelective(cmsMeta);
    }

    @Override
    public List<CmsMeta> list(String type, String keyword, String sortBy, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        CmsMetaExample example = new CmsMetaExample();
        example.setOrderByClause(sortBy);
        CmsMetaExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(type)) {
            criteria.andTypeEqualTo(type);
        }
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }
        return metaMapper.selectByExample(example);
    }

    @Override
    public int create(CmsMeta cmsMeta) {
        return metaMapper.insert(cmsMeta);
    }

    @Override
    public int delete(Long id) {
        return metaMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids) {
        CmsMetaExample example = new CmsMetaExample();
        example.createCriteria().andIdIn(ids);
        return metaMapper.deleteByExample(example);
    }

    @Override
    public int insertList(List<CmsMetasParam> list) {
        List<CmsMeta> cmsMetaList = new ArrayList<>();
        for (CmsMetasParam cmsMetasParam: list) {
            CmsMeta cmsMeta = new CmsMeta();
            BeanUtils.copyProperties(cmsMetasParam, cmsMeta);
            cmsMetaList.add(cmsMeta);
        }
        return metasDao.insertList(cmsMetaList);
    }
}
