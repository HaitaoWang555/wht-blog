package com.wht.item.portal.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentParams {
    @ApiModelProperty(value = "评论用户", required = true)
    @NotNull(message = "评论用户不能为空")
    private String username;

    @ApiModelProperty(value = "文章ID", required = true)
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @ApiModelProperty(value = "评论内容", required = true)
    @NotNull(message = "评论内容不能为空")
    private String content;

    @ApiModelProperty(value = "个人网址")
    private String link;

}
