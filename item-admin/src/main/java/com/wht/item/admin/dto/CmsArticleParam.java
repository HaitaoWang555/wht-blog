package com.wht.item.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author wht
 * @since 2020-05-31 2:30
 */
@Getter
@Setter
public class CmsArticleParam {

    @ApiModelProperty(value = "文章标题" , required = true)
    @NotEmpty(message = "文章标题不能为空")
    private String title;

    @ApiModelProperty(value = "文章标签")
    private String tags;

    @ApiModelProperty(value = "文章分类")
    private String category;

    @ApiModelProperty(value = "文章状态 publish 已发布 draft 草稿")
    private String status;

    @ApiModelProperty(value = "编辑器类型 markdownEditor tinymceEditor")
    private String editorType;

    @ApiModelProperty(value = "文章类型 note blog")
    private String articleType;

    @ApiModelProperty(value = "文章内容", required = true)
    @NotEmpty(message = "文章内容不能为空")
    private String content;
}
