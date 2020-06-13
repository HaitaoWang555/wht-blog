package com.wht.item.portal.controller;

import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
import com.wht.item.portal.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 文章
 * @author wht
 * @since 2020-06-10 7:08
 */
@RestController
@Api(tags = "文章内容管理")
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @ApiOperation("根据ID获取")
    @GetMapping("/{id}")
    public CommonResult<CmsArticle> getItem(@PathVariable Long id) {
        CmsArticle cmsArticle = articleService.getItem(id);
        if (cmsArticle != null) {
            String status = cmsArticle.getStatus();
            if (status.equals("publish")) {
                Integer hits = cmsArticle.getHits() + 1;
                cmsArticle.setHits(hits);
                articleService.updateCmsArticleHits(cmsArticle);
                return CommonResult.success(cmsArticle);
            } else {
                return CommonResult.success(null,"文章暂未发布");
            }
        } else {
            return CommonResult.failed("文章不存在");
        }
    }
}
