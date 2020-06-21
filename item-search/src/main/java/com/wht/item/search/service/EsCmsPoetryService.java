package com.wht.item.search.service;

import com.wht.item.search.domain.EsCmsPoetry;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EsCmsPoetryService {
    /**
     * 从数据库中导入所有商品到ES
     */
    int importAll();

    /**
     * 根据id删除商品
     */
    void delete(Long id);

    /**
     * 根据id创建商品
     */
    EsCmsPoetry create(Long id);

    /**
     * 根据EsCmsPoetry创建商品
     */
    EsCmsPoetry create(EsCmsPoetry esCmsPoetry);

    /**
     * 批量删除商品
     */
    void delete(List<Long> ids);

    /**
     * 根据关键字搜索名称或者副标题
     */
    Page<EsCmsPoetry> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 根据关键字搜索名称或者副标题
     */
    Page<EsCmsPoetry> search(String title, String dynasty, String author, String content, Integer pageNum, Integer pageSize);
}
