package com.wht.item.admin.dao;

import com.wht.item.admin.dto.CmsPoetryParam;
import com.wht.item.model.CmsPoetry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wht
 * @since 2020-06-21 0:26
 */
public interface CmsPoetryDao {
    long insertSelective(CmsPoetry record);
    int insertList(@Param("list") List<CmsPoetryParam> list);}
