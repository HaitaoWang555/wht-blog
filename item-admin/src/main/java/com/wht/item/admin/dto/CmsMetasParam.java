package com.wht.item.admin.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * @author wht
 * @since 2020-05-30 22:15
 */
@Getter
@Setter
public class CmsMetasParam {

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("类型")
    private String type;
}
