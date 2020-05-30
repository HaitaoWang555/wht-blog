package com.wht.item.mapper;

import com.wht.item.model.CmsMeta;
import com.wht.item.model.CmsMetaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsMetaMapper {
    long countByExample(CmsMetaExample example);

    int deleteByExample(CmsMetaExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CmsMeta record);

    int insertSelective(CmsMeta record);

    List<CmsMeta> selectByExample(CmsMetaExample example);

    CmsMeta selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CmsMeta record, @Param("example") CmsMetaExample example);

    int updateByExample(@Param("record") CmsMeta record, @Param("example") CmsMetaExample example);

    int updateByPrimaryKeySelective(CmsMeta record);

    int updateByPrimaryKey(CmsMeta record);
}