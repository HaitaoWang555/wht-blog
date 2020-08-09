package com.wht.item.portal.dto;

import com.wht.item.model.CmsNote;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wht
 * @since 2020-08-09 17:35
 */
@Getter
@Setter
public class NoteNode extends CmsNote {

    private List<NoteNode> children;
}
