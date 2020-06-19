package com.wht.item.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CmsPoetryParam {
    @ApiModelProperty(value = "诗词名称", required = true)
    @NotNull(message = "诗词名称不能为空")
    private String title;

    @ApiModelProperty(value = "朝代", required = true)
    @NotNull(message = "朝代不能为空")
    private String dynasty;

    @ApiModelProperty(value = "作者", required = true)
    @NotNull(message = "作者不能为空")
    private String author;

    @ApiModelProperty(value = "内容", required = true)
    @NotNull(message = "内容不能为空")
    private String content;
}
