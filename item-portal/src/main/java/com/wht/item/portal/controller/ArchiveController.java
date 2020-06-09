package com.wht.item.portal.controller;

import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
import com.wht.item.portal.dto.Archives;
import com.wht.item.portal.service.ArchiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 归档
 * @author wht
 * @since 2020-06-10 6:42
 */
@Controller
@Api(tags = "归档")
@RequestMapping("/archive")
public class ArchiveController {

    @Resource
    private ArchiveService archiveService;

    @ApiOperation("归档列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getArchiveList() {
        List<CmsArticle> articleList = archiveService.getAll();
        List<Archives> archivesList = new ArrayList<>();
        archivesList = archiveService.archive(articleList, archivesList);
        return CommonResult.success(CommonPage.restPage(archivesList));
    }
}
