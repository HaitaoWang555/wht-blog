package com.wht.item.admin.controller;

import com.wht.item.admin.dto.CmsArticleParam;
import com.wht.item.admin.dto.CmsNoteNode;
import com.wht.item.admin.service.CmsArticleService;
import com.wht.item.admin.service.CmsNoteService;
import com.wht.item.admin.util.SecurityUtil;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.CmsNote;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
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
    @Resource
    private CmsArticleService articleService;

    @ApiOperation("添加笔记列表")
    @PostMapping("/create")
    public CommonResult create(@RequestBody CmsNote cmsNote) {
        if (cmsNote.getMenuType().equals("file")) {
            CmsArticle cmsArticle = new CmsArticle();
            cmsArticle.setAuthorId(SecurityUtil.getCurrentUserId());
            cmsArticle.setArticleType("note");
            cmsArticle.setEditorType("markdownEditor");
            cmsArticle.setTitle(cmsNote.getName());
            int aCount = articleService.create(cmsArticle);
            if (aCount > 0) {
                cmsNote.setArticleId(cmsArticle.getId());
            } else {
                return CommonResult.failed();
            }
            int count = noteService.create(cmsNote);
            if (count > 0) {
                return CommonResult.success(cmsNote);
            } else {
                return CommonResult.failed();
            }
        } else {
            int count = noteService.create(cmsNote);
            if (count > 0) {
                return CommonResult.success(cmsNote);
            } else {
                return CommonResult.failed();
            }
        }

    }

    @ApiOperation("笔记上传文件夹")
    @PostMapping("/uploadDir")
    public CommonResult uploadDir(
            @RequestParam(required =false, value = "file") MultipartFile[] multipartFiles,
            @RequestParam(value = "id", defaultValue = "0") Long id) throws IOException {
        noteService.upload(multipartFiles, id);
        return CommonResult.success("导入成功");
    }

    @ApiOperation("笔记下载")
    @GetMapping("/download")
    public ResponseEntity download(@RequestParam(value = "id", defaultValue = "0") Long id) throws IOException {
        return noteService.download(id);
    }

    @ApiOperation("修改笔记菜单")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id,
            @RequestParam(value = "name", required = true) String name
    ) {
        CmsNote cmsNote = new CmsNote();
        cmsNote.setName(name);
        int count = noteService.update(id, cmsNote);
        if (count > 0) {
            return CommonResult.success(cmsNote.getId());
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("复制到文章")
    @PostMapping("/toArticle/{id}")
    public CommonResult changeToArticle(@PathVariable Long id, @RequestBody CmsArticleParam cmsArticleParam) {
        CmsArticle cmsArticle = new CmsArticle();
        BeanUtils.copyProperties(cmsArticleParam, cmsArticle);
        cmsArticle.setId(id);
        Long aid = noteService.changeToArticle(id, cmsArticle);
        if (aid > 0) {
            return CommonResult.success(aid, "已成功复制到文章");
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

    @ApiOperation("全部笔记列表")
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

    @ApiOperation("根据ID删除笔记")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = noteService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

}
