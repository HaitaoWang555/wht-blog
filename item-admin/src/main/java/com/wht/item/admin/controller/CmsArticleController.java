package com.wht.item.admin.controller;

import com.wht.item.admin.dto.CmsArticleParam;
import com.wht.item.admin.service.CmsArticleService;
import com.wht.item.admin.service.CmsMetasService;
import com.wht.item.admin.service.UmsAdminService;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.UmsAdmin;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

/**
 * 内容管理 博客文章
 *
 * @author wht
 * @since 2020-05-31 2:19
 */

@Controller
@Api(tags = "CmsArticleController", description = "内容管理 博客文章")
@RequestMapping("/article")
public class CmsArticleController {
    @Resource
    private CmsArticleService articleService;
    @Resource
    private UmsAdminService adminService;
    @Resource
    private CmsMetasService metasService;

    @ApiOperation("添加属性")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody CmsArticleParam cmsArticleParam, Principal principal) {
        CmsArticle cmsArticle = new CmsArticle();
        BeanUtils.copyProperties(cmsArticleParam, cmsArticle);
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        cmsArticle.setAuthorId(umsAdmin.getId());
        int count = articleService.create(cmsArticle);
        if (count > 0) {
            //存储分类和标签
            Long articleId = cmsArticle.getId();
            String tags = cmsArticle.getTags();
            String category = cmsArticle.getCategory();
            if (StringUtils.isNotBlank(tags)) metasService.saveOrRemoveMeta(tags, "tag", articleId);
            if (StringUtils.isNotBlank(category)) metasService.saveOrRemoveMeta(category, "category", articleId);
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("分页模糊查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<CmsArticle>> list(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "authorId", required = false) Long authorId,
            @RequestParam(value = "meta", required = false) String meta,
            @RequestParam(value = "sortBy", required = false, defaultValue = "updated_time desc") String sortBy,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsArticle> resourceList = articleService.list(title, status, meta, authorId, sortBy, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @ApiOperation("根据ID获取")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CmsArticle> getItem(@PathVariable Long id) {
        CmsArticle cmsMeta = articleService.getItem(id);
        return CommonResult.success(cmsMeta);
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = articleService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

}
