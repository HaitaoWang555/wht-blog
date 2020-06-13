package com.wht.item.portal.controller;

import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
import com.wht.item.portal.dto.Archives;
import com.wht.item.portal.service.ArchiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 归档
 * @author wht
 * @since 2020-06-10 6:42
 */
@RestController
@Api(tags = "归档")
@RequestMapping("/archive")
public class ArchiveController {

    @Resource
    private ArchiveService archiveService;

    @ApiOperation("归档列表")
    @GetMapping("/list")
    public CommonResult getArchiveList() {
        List<CmsArticle> articleList = archiveService.getAll();
        List<Archives> archivesList = new ArrayList<>();
        archivesList = archiveService.archive(articleList, archivesList);
        return CommonResult.success(CommonPage.restPage(archivesList));
    }
}
