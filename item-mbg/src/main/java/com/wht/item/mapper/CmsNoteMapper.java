package com.wht.item.mapper;

import com.wht.item.model.CmsNote;
import com.wht.item.model.CmsNoteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsNoteMapper {
    long countByExample(CmsNoteExample example);

    int deleteByExample(CmsNoteExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsNote record);

    int insertSelective(CmsNote record);

    List<CmsNote> selectByExample(CmsNoteExample example);

    CmsNote selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsNote record, @Param("example") CmsNoteExample example);

    int updateByExample(@Param("record") CmsNote record, @Param("example") CmsNoteExample example);

    int updateByPrimaryKeySelective(CmsNote record);

    int updateByPrimaryKey(CmsNote record);
}