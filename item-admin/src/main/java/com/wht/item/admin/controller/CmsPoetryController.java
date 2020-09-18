package com.wht.item.admin.controller;

import com.alibaba.excel.EasyExcel;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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

    @ApiOperation(value = "搜索诗词分页列表")
    @GetMapping("/search")
    public CommonResult searchPoetryList(
            @RequestParam(required = false) String title, @RequestParam(required = false) String dynasty,
            @RequestParam(required = false) String author, @RequestParam(required = false) String content,
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        return poetryService.esSearch(title, dynasty, author, content, pageNum, pageSize);
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
            return CommonResult.validateFailed(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        CommonResult commonResult;
        int count = poetryService.createPoetry(cmsPoetryParam);
        if (count > 0) {
            commonResult = CommonResult.success(null);
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
        } else {
            commonResult = CommonResult.failed("修改失败");
            LOGGER.error("updatePoetry failed:{}", cmsPoetryParam);
        }
        return commonResult;
    }

    @ApiOperation(value = "根据ID批量删除诗词")
    @PostMapping("/delete")
    public CommonResult delPoetry(@RequestParam("ids") List<Integer> ids) {
        int count = poetryService.deletePoetry(ids);
        if (count > 0) {

            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("导出数据")
    @GetMapping("/export")
    public void download(
            @RequestParam(value = "ids", required = false) String ids,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("诗词", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), CmsPoetryParam.class).sheet("诗词").doWrite(poetryService.downloadPoetry(ids));
    }
    @ApiOperation("批量上传")
    @PostMapping("/uploadCsv")
    public CommonResult uploadDir(@RequestParam(required = false, value = "file") MultipartFile[] multipartFiles) throws IOException {
        for (MultipartFile file : multipartFiles) {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);

            if (suffix.equals("csv")) {
                poetryService.uploadCsv(inputStream);
            }

        }
        return CommonResult.failed();
    }
}
