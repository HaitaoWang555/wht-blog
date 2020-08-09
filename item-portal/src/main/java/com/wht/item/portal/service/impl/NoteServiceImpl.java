package com.wht.item.portal.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.wht.item.mapper.CmsArticleMapper;
import com.wht.item.mapper.CmsNoteMapper;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.CmsNote;
import com.wht.item.model.CmsNoteExample;
import com.wht.item.portal.dto.NoteNode;
import com.wht.item.portal.service.NoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 笔记
 *
 * @author wht
 * @since 2020-08-09 17:37
 */
@Service
public class NoteServiceImpl implements NoteService {
    @Resource
    private CmsNoteMapper noteMapper;
    @Resource
    private CmsArticleMapper articleMapper;
    
    @Override
    public List<NoteNode> treeList() {
        List<CmsNote> noteList = listAll();
        List<NoteNode> noteNodeList = Lists.newArrayList();
        for (CmsNote note: noteList) {
            noteNodeList.add(toNode(note));
        }
        return listToTree(noteNodeList);
    }

    @Override
    public CmsArticle getContent(Long id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    private List<CmsNote> listAll() {
        CmsNoteExample example = new CmsNoteExample();
        example.setOrderByClause("menu_type desc");
        return noteMapper.selectByExample(example);
    }

    private List<NoteNode> listToTree(List<NoteNode> noteNodeList) {
        if (CollectionUtils.isEmpty(noteNodeList)) {
            return Lists.newArrayList();
        }
        Multimap<Long, NoteNode> map= ArrayListMultimap.create();
        List<NoteNode> rootList = Lists.newArrayList();
        for (NoteNode note: noteNodeList) {
            map.put(note.getParentId(), note);
            if (note.getParentId().equals(0L)) {
                rootList.add(note);
            }
        }
        // 递归生成树
        transformTree(rootList, map);
        return rootList;
    }
    private void transformTree(List<NoteNode> rootList, Multimap<Long, NoteNode> map) {
        for (NoteNode note: rootList) {
            // 遍历该层的每个元素
            // 处理当前层级的数据
            Long nextLevel = note.getId();
            // 处理下一层
            List<NoteNode> tempList = (List<NoteNode>) map.get(nextLevel);
            if (!CollectionUtils.isEmpty(tempList)) {
                note.setChildren(tempList);
                transformTree(tempList, map);
            } else {
                note.setChildren(new ArrayList<>());
            }
        }
    }
    private NoteNode toNode(CmsNote note) {
        NoteNode noteNode = new NoteNode();
        BeanUtils.copyProperties(note, noteNode);
        return noteNode;
    }
}
