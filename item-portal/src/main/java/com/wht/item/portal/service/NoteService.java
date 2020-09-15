package com.wht.item.portal.service;

import com.wht.item.portal.dto.NoteNode;

import java.util.List;

/**
 * 笔记
 *
 * @author wht
 * @since 2020-08-09 17:32
 */
public interface NoteService {

    /**
     * 树形结构返回所有菜单列表
     */
    List<NoteNode> treeList();
}
