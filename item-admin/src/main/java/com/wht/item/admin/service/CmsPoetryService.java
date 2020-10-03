package com.wht.item.admin.service;

import com.wht.item.admin.dto.CmsPoetryParam;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsPoetry;

import java.io.InputStream;
import java.util.List;

public interface CmsPoetryService {
    List<CmsPoetry> listAllPoetry();

    int createPoetry(CmsPoetryParam cmsPoetryParam);

    int updatePoetry(int id, CmsPoetryParam cmsPoetryParam);

    int deletePoetry(int id);
    /**
     * 批量删除
     */
    int deletePoetry(List<Integer> ids);

    int initPoetry(String path);

    List<CmsPoetry> listPoetry(int pageNum, int pageSize);

    List<CmsPoetryParam> downloadPoetry(String ids);
    /**
     * 根据Ids获取
     */
    List<CmsPoetry> searchByIds(List<Integer> ids);
    List<CmsPoetry> listPoetryWithBLOBs(int pageNum, int pageSize);

    CommonResult esSearch (String title, String dynasty, String author, String content, Integer pageNum, Integer pageSize);

    CmsPoetry getPoetry(int id);

    int uploadCsv(InputStream inputStream);
}
