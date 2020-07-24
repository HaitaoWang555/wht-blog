package com.wht.item.admin.service;

import com.wht.item.admin.dto.CmsNoteNode;
import com.wht.item.model.CmsNote;

import java.util.List;

/**
 * 笔记 Service
 *
 * @author wht
 * @since 2020-07-23 19:37
 */
public interface CmsNoteService {
    /**
     * 创建笔记菜单
     */
    int create(CmsNote cmsNote);

    /**
     * 修改笔记菜单
     */
    int update(Long id, CmsNote cmsNote);

    /**
     * 根据ID获取笔记菜单
     */
    CmsNote getItem(Long id);

    /**
     * 根据ID删除菜单
     */
    int delete(Long id);

    /**
     * 分页查询笔记菜单
     */
    List<CmsNote> list(Long parentId, Integer pageSize, Integer pageNum);


    /**
     * 查询所有笔记菜单
     */
    List<CmsNote> listAll();

    /**
     * 树形结构返回所有菜单列表
     */
    List<CmsNoteNode> treeList();
}
