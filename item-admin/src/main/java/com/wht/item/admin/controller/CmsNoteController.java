package com.wht.item.admin.controller;

import com.wht.item.admin.dto.CmsNoteNode;
import com.wht.item.admin.service.CmsNoteService;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsNote;
import com.wht.item.model.UmsMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容管理 笔记
 *
 * @author wht
 * @since 2020-07-23 2:19
 */

@RestController
@Api(tags = "内容管理 笔记")
@RequestMapping("/note")
public class CmsNoteController {
    @Resource
    private CmsNoteService noteService;

    @ApiOperation("添加笔记列表")
    @PostMapping("/create")
    public CommonResult create(@RequestBody CmsNote cmsNote) {
        int count = noteService.create(cmsNote);
        if (count > 0) {
            return CommonResult.success(cmsNote.getId());
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("分页查询笔记列表")
    @GetMapping("/list/{parentId}")
    public CommonResult<CommonPage<CmsNote>> list(@PathVariable Long parentId,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsNote> cmsNotes = noteService.list(parentId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(cmsNotes));
    }

    @ApiOperation("分页查询笔记列表")
    @GetMapping("/allList")
    public CommonResult<CommonPage<CmsNote>> allList() {
        List<CmsNote> cmsNotes = noteService.listAll();
        return CommonResult.success(CommonPage.restPage(cmsNotes));
    }

    @ApiOperation("树形笔记列表")
    @GetMapping("/treeList")
    public CommonResult<CommonPage<CmsNoteNode>> treeList() {
        List<CmsNoteNode> cmsNoteNodes = noteService.treeList();
        return CommonResult.success(CommonPage.restPage(cmsNoteNodes));
    }

}
