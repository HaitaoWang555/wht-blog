package com.wht.item.admin.dto;

import com.wht.item.model.UmsMenu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wht
 * @since 2020-05-26 22:52
 */
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    private List<UmsMenuNode> children;
}
