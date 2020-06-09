package com.wht.item.portal.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签与分类自定义Dao
 * @author wht
 * @since 2020-05-30 21:56
 */
public interface MetasDao {
    List selectMetasDtoPublish(@Param("type")String type);

}
