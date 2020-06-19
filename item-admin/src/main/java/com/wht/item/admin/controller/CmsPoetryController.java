package com.wht.item.admin.controller;

import com.wht.item.admin.dto.CmsPoetryParam;
import com.wht.item.admin.service.CmsPoetryService;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsPoetry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 诗词管理接口 Controller
 *
 * @author wht
 * @since 2020-05-30 19:36
 */

@Api(tags = "诗词管理接口")
@RequestMapping(value = "/poetry")
@RestController
public class CmsPoetryController {

    @Resource
    private CmsPoetryService poetryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsPoetryController.class);

    @ApiOperation(value = "获取全部诗词列表")
    @GetMapping("/listAll")
    public CommonResult<List<CmsPoetry>> getPoetryListALL() {
        return CommonResult.success(poetryService.listAllPoetry());
    }

    @ApiOperation(value = "获取诗词分页列表")
    @GetMapping("/list")
    public CommonResult getPoetryList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        List<CmsPoetry> poetryList = poetryService.listPoetryWithBLOBs(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(poetryList));
    }

    @ApiOperation(value = "根据ID获取诗词")
    @GetMapping("/{id}")
    public CommonResult getPoetry( @PathVariable("id") int id) {
        CmsPoetry poetry = poetryService.getPoetry(id);
        return CommonResult.success(poetry);
    }

    @ApiOperation(value = "新增诗词")
    @PostMapping("/create")
    public CommonResult createPoetry (
            @Validated @RequestBody CmsPoetryParam cmsPoetryParam,
            BindingResult result
    ) {
        if(result.hasErrors()){
            return CommonResult.validateFailed(result.getFieldError().getDefaultMessage());
        }
        CommonResult commonResult;
        int count = poetryService.createPoetry(cmsPoetryParam);
        if (count == 1) {
            commonResult = CommonResult.success(null);
            LOGGER.info("createPoetry success:{}", cmsPoetryParam);
        } else {
            commonResult = CommonResult.failed("添加失败");
            LOGGER.error("createPoetry failed:{}", cmsPoetryParam);
        }
        return commonResult;
    }

    @ApiOperation(value = "根据ID更新诗词")
    @PostMapping("/update/{id}")
    public CommonResult updatePoetry(
            @PathVariable("id") int id,
            @RequestBody CmsPoetryParam cmsPoetryParam,
            BindingResult result
    ) {
        if(result.hasErrors()){
            return CommonResult.validateFailed(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        CommonResult commonResult;
        int count = poetryService.updatePoetry(id, cmsPoetryParam);
        if (count == 1) {
            commonResult = CommonResult.success(null);
            LOGGER.info("updatePoetry success:{}", cmsPoetryParam);
        } else {
            commonResult = CommonResult.failed("修改失败");
            LOGGER.error("updatePoetry failed:{}", cmsPoetryParam);
        }
        return commonResult;
    }

    @ApiOperation(value = "根据ID删除诗词")
    @PostMapping("/{id}")
    public CommonResult delPoetry( @PathVariable("id") int id) {
        int count = poetryService.deletePoetry(id);
        if (count == 1) {
            LOGGER.info("deletePoetry success :id={}", id);
            return CommonResult.success(null);
        } else {
            LOGGER.error("deletePoetry failed :id={}", id);
            return CommonResult.failed("操作失败");
        }
    }
}
