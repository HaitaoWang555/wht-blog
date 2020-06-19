package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.wht.item.admin.dto.CmsPoetryParam;
import com.wht.item.admin.service.CmsPoetryService;
import com.wht.item.mapper.CmsPoetryMapper;
import com.wht.item.model.CmsPoetry;
import com.wht.item.model.CmsPoetryExample;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsPoetryServiceImpl implements CmsPoetryService {
    @Resource
    private CmsPoetryMapper poetryMapper;

    @Override
    public List<CmsPoetry> listAllPoetry() {
        return poetryMapper.selectByExample(new CmsPoetryExample());
    }

    @Override
    public int createPoetry(CmsPoetryParam cmsPoetryParam) {
        CmsPoetry poetry = new CmsPoetry();
        BeanUtils.copyProperties(cmsPoetryParam, poetry);
        return poetryMapper.insertSelective(poetry);
    }

    @Override
    public int updatePoetry(int id, CmsPoetryParam cmsPoetryParam) {
        CmsPoetry poetry = new CmsPoetry();
        BeanUtils.copyProperties(cmsPoetryParam, poetry);
        poetry.setId(id);
        return poetryMapper.updateByPrimaryKeySelective(poetry);
    }

    @Override
    public int deletePoetry(int id) {
        return poetryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<CmsPoetry> listPoetry(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return poetryMapper.selectByExample(new CmsPoetryExample());
    }

    @Override
    public List<CmsPoetry> listPoetryWithBLOBs(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return poetryMapper.selectByExampleWithBLOBs(new CmsPoetryExample());
    }

    @Override
    public CmsPoetry getPoetry(int id) {
        return poetryMapper.selectByPrimaryKey(id);
    }
}
