package com.wht.item.portal.controller;

import com.wht.item.common.api.CommonResult;
import com.wht.item.portal.service.MetasService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签与分类
 * @author wht
 * @since 2020-06-08 1:18
 */
@RestController
@Api(tags = "标签与分类")
@RequestMapping("/metas")
public class MetasController {

    @Resource
    private MetasService metasService;

    @ApiOperation("所有分类下的文章")
    @GetMapping("/category")
    public CommonResult categoryList() {
        List resourceList = metasService.list("category");
        return CommonResult.success(resourceList);
    }

    @ApiOperation("所有标签下的文章")
    @GetMapping("/tag")
    public CommonResult tagList() {
        List resourceList = metasService.list("tag");
        return CommonResult.success(resourceList);
    }


}
