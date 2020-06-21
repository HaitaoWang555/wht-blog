package com.wht.item.mapper;

import com.wht.item.model.CmsPoetry;
import com.wht.item.model.CmsPoetryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsPoetryMapper {
    long countByExample(CmsPoetryExample example);

    int deleteByExample(CmsPoetryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CmsPoetry record);

    int insertSelective(CmsPoetry record);

    List<CmsPoetry> selectByExampleWithBLOBs(CmsPoetryExample example);

    List<CmsPoetry> selectByExample(CmsPoetryExample example);

    CmsPoetry selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CmsPoetry record, @Param("example") CmsPoetryExample example);

    int updateByExampleWithBLOBs(@Param("record") CmsPoetry record, @Param("example") CmsPoetryExample example);

    int updateByExample(@Param("record") CmsPoetry record, @Param("example") CmsPoetryExample example);

    int updateByPrimaryKeySelective(CmsPoetry record);

    int updateByPrimaryKeyWithBLOBs(CmsPoetry record);

    int updateByPrimaryKey(CmsPoetry record);
}