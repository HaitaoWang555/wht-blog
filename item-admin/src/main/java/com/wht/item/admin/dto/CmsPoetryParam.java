package com.wht.item.admin.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CmsPoetryParam {
    @ApiModelProperty(value = "诗词名称", required = true)
    @NotNull(message = "诗词名称不能为空")
    @ExcelProperty("诗词名称")
    private String title;

    @ApiModelProperty(value = "朝代", required = true)
    @Length(message = "朝代最大长度不能超过10", max = 10)
    @NotNull(message = "朝代不能为空")
    @ExcelProperty("朝代")
    private String dynasty;

    @ApiModelProperty(value = "作者", required = true)
    @Length(message = "作者最大长度不能超过10", max = 10)
    @NotNull(message = "作者不能为空")
    @ExcelProperty("作者")
    private String author;

    @ApiModelProperty(value = "内容", required = true)
    @NotNull(message = "内容不能为空")
    @ExcelProperty("内容")
    private String content;
}
