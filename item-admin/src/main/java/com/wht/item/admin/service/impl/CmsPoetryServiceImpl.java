package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.wht.item.admin.dto.CmsPoetryParam;
import com.wht.item.admin.service.CmsPoetryService;
import com.wht.item.common.api.ResultCode;
import com.wht.item.common.exception.ApiException;
import com.wht.item.mapper.CmsPoetryMapper;
import com.wht.item.model.CmsPoetry;
import com.wht.item.model.CmsPoetryExample;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        poetryMapper.insertSelective(poetry);
        return poetry.getId();
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
    public int deletePoetry(List<Integer> ids) {
        CmsPoetryExample example = new CmsPoetryExample();
        example.createCriteria().andIdIn(ids);
        return poetryMapper.deleteByExample(example);
    }

    @Override
    public List<CmsPoetry> listPoetry(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return poetryMapper.selectByExample(new CmsPoetryExample());
    }

    @Override
    public List<CmsPoetryParam> downloadPoetry(String ids) {
        if (StringUtils.isEmpty(ids)) {
            throw new ApiException(ResultCode.VALIDATE_FAILED, "请选择要导出的诗词");
        } else {
            String[] idArr = ids.split(",");
            ArrayList<Integer> idsArr = new ArrayList<Integer>();
            for (String s : idArr) {
                idsArr.add(Integer.parseInt(s));
            }
            return toCmsPoetryParam(searchByIds(idsArr));
        }
    }

    private List<CmsPoetryParam> toCmsPoetryParam(List<CmsPoetry> CmsPoetryList) {
        return CmsPoetryList.stream().map(cmsMeta -> {
            CmsPoetryParam obj = new CmsPoetryParam();
            BeanUtils.copyProperties(cmsMeta, obj);
            return obj;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CmsPoetry> searchByIds(List<Integer> ids) {
        CmsPoetryExample example = new  CmsPoetryExample();
        example.createCriteria().andIdIn(ids);
        return poetryMapper.selectByExampleWithBLOBs(example);
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
