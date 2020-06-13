package com.wht.item.admin.service;

import com.wht.item.admin.dto.CmsMetasParam;
import com.wht.item.model.CmsMeta;

import java.util.List;

/**
 * 属性(标签和分类)管理 Service
 *
 * @author wht
 * @since 2020-05-30 19:37
 */
public interface CmsMetasService {
    /**
     * 获取所有
     */
    List<CmsMeta> listAll();
    /**
     * 根据Ids获取
     */
    List<CmsMeta> searchByIds(String[] ids);
    /**
     * 根据ID获取
     */
    CmsMeta getItem(Long id);

    /**
     * 修改
     */
    int update(Long id, CmsMeta cmsMeta);
    /**
     * 分页查询
     */
    List<CmsMeta> list(String type, String keyword, String sortBy, Integer pageSize, Integer pageNum);

    /**
     * 创建
     */
    int create(CmsMeta cmsMeta);

    /**
     * 根据ID删除
     */
    int delete(Long id);
    /**
     * 批量删除
     */
    int delete(List<Long> ids);

    /**
     * 批量创建
     */
    void insertList(List<CmsMetasParam> list);

    /**
     * 文章关联标签
     */
    void saveOrRemoveMeta(String meta, String type, Long articleId);
}
