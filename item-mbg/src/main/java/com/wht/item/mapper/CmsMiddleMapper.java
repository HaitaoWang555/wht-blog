package com.wht.item.mapper;

import com.wht.item.model.CmsMiddle;
import com.wht.item.model.CmsMiddleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsMiddleMapper {
    long countByExample(CmsMiddleExample example);

    int deleteByExample(CmsMiddleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsMiddle record);

    int insertSelective(CmsMiddle record);

    List<CmsMiddle> selectByExample(CmsMiddleExample example);

    CmsMiddle selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsMiddle record, @Param("example") CmsMiddleExample example);

    int updateByExample(@Param("record") CmsMiddle record, @Param("example") CmsMiddleExample example);

    int updateByPrimaryKeySelective(CmsMiddle record);

    int updateByPrimaryKey(CmsMiddle record);
}