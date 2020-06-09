package com.wht.item.portal.controller;

import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
import com.wht.item.portal.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 首页
 * @author wht
 * @since 2020-06-08 0:34
 */
@Controller
@Api(tags = "首页内容管理")
@RequestMapping("/home")
public class HomeController {

    @Resource
    private ArticleService articleService;

    @ApiOperation("分页查询首页内容")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<CmsArticle>> list(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsArticle> resourceList = articleService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }
}
