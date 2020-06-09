package com.wht.item.portal.dto;

import com.wht.item.model.CmsMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wht
 * @since 2020-06-10 5:50
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MetaDto extends CmsMeta {
    List<ArticleInfoDto> articles;
}
