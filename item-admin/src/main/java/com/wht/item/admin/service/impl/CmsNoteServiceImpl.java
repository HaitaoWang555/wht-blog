package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.wht.item.admin.dto.CmsNoteNode;
import com.wht.item.admin.service.CmsNoteService;
import com.wht.item.mapper.CmsNoteMapper;
import com.wht.item.model.CmsNote;
import com.wht.item.model.CmsNoteExample;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsNoteServiceImpl implements CmsNoteService {

    @Resource
    private CmsNoteMapper noteMapper;

    @Override
    public int create(CmsNote cmsNote) {
        if(cmsNote.getParentId() == null) {
            cmsNote.setParentId(0L);
        }
        //查询同一层级是否有相同的名称
        CmsNoteExample example = new CmsNoteExample();
        example.createCriteria()
                .andNameEqualTo(cmsNote.getName())
                .andParentIdEqualTo(cmsNote.getParentId());
        List<CmsNote> cmsNoteList = noteMapper.selectByExample(example);
        if (cmsNoteList.size() > 0) {
            return 0;
        }

        return noteMapper.insert(cmsNote);
    }

    @Override
    public int update(Long id, CmsNote cmsNote) {
        cmsNote.setId(id);
        return noteMapper.updateByPrimaryKeySelective(cmsNote);
    }

    @Override
    public CmsNote getItem(Long id) {
        return noteMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        return noteMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<CmsNote> list(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        CmsNoteExample example = new CmsNoteExample();
        example.setOrderByClause("updated_time desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        return noteMapper.selectByExample(example);
    }

    @Override
    public List<CmsNote> listAll() {
        return noteMapper.selectByExample(new CmsNoteExample());
    }

    @Override
    public List<CmsNoteNode> treeList() {
        List<CmsNote> noteList = listAll();
        List<CmsNoteNode> noteNodeList = Lists.newArrayList();
        for (CmsNote note: noteList) {
            noteNodeList.add(toNode(note));
        }
        return listToTree(noteNodeList);
    }

    private CmsNoteNode toNode(CmsNote note) {
        CmsNoteNode noteNode = new CmsNoteNode();
        BeanUtils.copyProperties(note, noteNode);
        return noteNode;
    }

    private List<CmsNoteNode> listToTree(List<CmsNoteNode> noteNodeList) {
        if (CollectionUtils.isEmpty(noteNodeList)) {
            return Lists.newArrayList();
        }
        Multimap<Long, CmsNoteNode> map= ArrayListMultimap.create();
        List<CmsNoteNode> rootList = Lists.newArrayList();
        for (CmsNoteNode note: noteNodeList) {
            map.put(note.getParentId(), note);
            if (note.getParentId().equals(0L)) {
                rootList.add(note);
            }
        }
        // 递归生成树
        transformTree(rootList, map);
        return rootList;
    }
    private void transformTree(List<CmsNoteNode> rootList, Multimap<Long, CmsNoteNode> map) {
        for (CmsNoteNode note: rootList) {
            // 遍历该层的每个元素
            // 处理当前层级的数据
            Long nextLevel = note.getId();
            // 处理下一层
            List<CmsNoteNode> tempList = (List<CmsNoteNode>) map.get(nextLevel);
            if (!CollectionUtils.isEmpty(tempList)) {
                note.setChildren(tempList);
                transformTree(tempList, map);
            }
        }
    }

}