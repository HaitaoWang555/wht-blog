package com.wht.item.portal.controller;

import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
import com.wht.item.portal.dto.NoteNode;
import com.wht.item.portal.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 笔记
 *
 * @author wht
 * @since 2020-08-09 17:31
 */
@RestController
@Api(tags = "笔记")
@RequestMapping("/note")
public class NoteController {

    @Resource
    private NoteService noteService;

    @ApiOperation("树形笔记列表")
    @GetMapping("/treeList")
    public CommonResult<CommonPage<NoteNode>> treeList() {
        List<NoteNode> cmsNoteNodes = noteService.treeList();
        return CommonResult.success(CommonPage.restPage(cmsNoteNodes));
    }

    @ApiOperation("根据文章ID获取")
    @GetMapping("/content/{id}")
    public CommonResult<CmsArticle> getItem(@PathVariable Long id) {
        CmsArticle cmsArticle = noteService.getContent(id);
        if (cmsArticle != null) {
            return CommonResult.success(cmsArticle);
        } else {
            return CommonResult.failed("文章不存在");
        }
    }

}
