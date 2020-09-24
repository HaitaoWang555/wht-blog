package com.wht.item.search.dao;

import com.wht.item.search.domain.EsCmsPoetry;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 搜索系统中的诗词管理自定义Dao
 * @author wht
 * @since 2020-04-29 19:11
 */
public interface EsCmsPoetryDao {
    List<EsCmsPoetry> getAllEsPoetryList(@Param("id") long id);
    List<EsCmsPoetry> getEsPoetryList(@Param("id") Integer id);
    List<EsCmsPoetry> getEsPoetryList();
    int getCount();
}
