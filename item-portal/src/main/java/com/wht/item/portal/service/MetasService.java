package com.wht.item.portal.service;

import java.util.List;

/**
 * @author wht
 * @since 2020-06-08 2:40
 */
public interface MetasService {
    /**
     * 查询分类 或 标签下文章
     * @param type tag / category
     */
    List list(String type);
}
