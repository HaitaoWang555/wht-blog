package com.wht.item.admin.controller;

import com.wht.item.admin.dto.CmsNoteNode;
import com.wht.item.admin.service.CmsArticleService;
import com.wht.item.admin.service.CmsNoteService;
import com.wht.item.admin.service.UmsAdminService;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.CmsNote;
import com.wht.item.model.UmsAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
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
    private UmsAdminService adminService;
    @Resource
    private CmsArticleService articleService;

    @ApiOperation("添加笔记列表")
    @PostMapping("/create")
    public CommonResult create(@RequestBody CmsNote cmsNote, Principal principal) {
        if (cmsNote.getMenuType().equals("file")) {
            CmsArticle cmsArticle = new CmsArticle();
            String username = principal.getName();
            UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
            cmsArticle.setAuthorId(umsAdmin.getId());
            cmsArticle.setArticleType("note");
            String type;
            if (cmsNote.getName().substring(cmsNote.getName().lastIndexOf(".") + 1).equals("md")) {
                type = "markdownEditor";
            } else {
                type = "tinymceEditor";
            }
            cmsArticle.setEditorType(type);
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