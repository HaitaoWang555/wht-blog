package com.wht.item.portal.controller;

import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsComment;
import com.wht.item.portal.dto.CommentParams;
import com.wht.item.portal.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@Api(tags = "评论管理")
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @ApiOperation(value = "获取全部评论列表")
    @GetMapping("/list/{id}")
    public CommonResult<List<CmsComment>> getListALLByAid(@PathVariable Long id) {
        return CommonResult.success(commentService.list(id));
    }

    @ApiOperation(value = "新增评论")
    @PostMapping("/create")
    public CommonResult createComment (
            @Validated @RequestBody CommentParams commentParams,
            BindingResult result
    ) {
        if(result.hasErrors()){
            return CommonResult.validateFailed(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        Long id = commentService.createComment(commentParams);
        if (id != null) {
            return CommonResult.success(id);
        } else {
            return CommonResult.failed("添加失败");
        }
    }
    // TODO: 更新评论数量
}
