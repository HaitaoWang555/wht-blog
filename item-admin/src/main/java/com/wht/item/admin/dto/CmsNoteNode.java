package com.wht.item.admin.dto;

import com.wht.item.model.CmsNote;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 笔记菜单子节点
 * @author wht
 * @since 2020-07-23 22:52
 */
@Getter
@Setter
public class CmsNoteNode extends CmsNote {
    private List<CmsNoteNode> children;
}
