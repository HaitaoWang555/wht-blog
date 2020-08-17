package com.wht.item.admin.controller;

import com.wht.item.admin.service.CmsCommentService;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsComment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "评论管理")
@RequestMapping("/comment")
public class CmsCommentController {
    @Resource
    private CmsCommentService commentService;

    @ApiOperation(value = "搜索评论分页列表")
    @GetMapping("/search")
    public CommonResult getList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long articleId,
            @RequestParam(required = false) String ip,
            @RequestParam(required = false) String link,
            @RequestParam(value = "sortBy", required = false, defaultValue = "create_time desc") String sortBy,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        List<CmsComment> List = commentService.listComment(username, articleId, ip, link, pageNum, pageSize, sortBy);
        return CommonResult.success(CommonPage.restPage(List));
    }


    @ApiOperation(value = "根据ID批量删除评论")
    @PostMapping("/delete")
    public CommonResult delComment(@RequestParam("ids") List<Long> ids, @RequestParam("aids") List<Long> aids) {
        int count = commentService.deletePoetry(ids, aids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "根据ID批量删除评论")
    @PostMapping("/deleteLinks")
    public CommonResult updateCommentLink(@RequestParam("ids") List<Long> ids) {
        int count = commentService.updateCommentLink(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

}
