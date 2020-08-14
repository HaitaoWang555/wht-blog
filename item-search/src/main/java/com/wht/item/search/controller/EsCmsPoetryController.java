package com.wht.item.search.controller;

import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.search.domain.EsCmsPoetry;
import com.wht.item.search.service.EsCmsPoetryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 诗词搜索管理
 * @author wht
 * @since 2020-04-29 20:14
 */
@Api(tags = "诗词搜索管理接口")
@RequestMapping(value = "/esPoetry")
@RestController
public class EsCmsPoetryController {
    @Resource
    private EsCmsPoetryService esPoetryService;

    @ApiOperation(value = "导入所有数据库中诗词到ES")
    @PostMapping("/importAll")
    public CommonResult<Integer> importAllList() {
        int count = esPoetryService.importAll();
        return CommonResult.success(count);
    }

    @ApiOperation(value = "根据id删除诗词")
    @PostMapping("/delete/{id}")
    public CommonResult<Object> delete(@PathVariable Long id) {
        esPoetryService.delete(id);
        return CommonResult.success(null);
    }

    @ApiOperation(value = "根据id批量删除诗词")
    @GetMapping("/delete/batch")
    public CommonResult<Object> delete(@RequestParam("ids") List<Long> ids) {
        esPoetryService.delete(ids);
        return CommonResult.success(null);
    }

    @ApiOperation(value = "根据id创建诗词")
    @PostMapping("/create/{id}")
    public CommonResult<EsCmsPoetry> create(@PathVariable Long id) {
        EsCmsPoetry esPoetry = esPoetryService.create(id);
        if (esPoetry != null) {
            return CommonResult.success(esPoetry);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "根据EsCmsPoetry创建诗词")
    @PostMapping("/create")
    public CommonResult<EsCmsPoetry> create(
            @RequestBody EsCmsPoetry esCmsPoetry
    ) {
        EsCmsPoetry esPoetry = esPoetryService.create(esCmsPoetry);
        if (esPoetry != null) {
            return CommonResult.success(esPoetry);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "简单搜索")
    @GetMapping("/search")
    public CommonResult<CommonPage<EsCmsPoetry>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsCmsPoetry> esProductPage = esPoetryService.search(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(esProductPage));
    }

    @ApiOperation(value = "简单搜索")
    @GetMapping("/searchA")
    public CommonResult<CommonPage<EsCmsPoetry>> searchA(
            @RequestParam(required = false) String title, @RequestParam(required = false) String dynasty,
            @RequestParam(required = false) String author, @RequestParam(required = false) String content,
            @RequestParam(required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsCmsPoetry> esProductPage = esPoetryService.search(title, dynasty, author, content,  pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(esProductPage));
    }
}
