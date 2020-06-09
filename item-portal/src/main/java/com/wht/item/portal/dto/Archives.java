package com.wht.item.portal.dto;

import com.wht.item.model.CmsArticle;
import lombok.Data;

import java.util.List;

/**
 * @author wht
 * @since 2020-06-10 6:47
 */
@Data
public class Archives {

    private String dateStr;

    private Integer count;

    private List<CmsArticle> article;
}

