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
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Resource
    private RestTemplate restTemplate;

    private String HOST_ITEM_SEARCH = "http://localhost:8081/api/search";

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
        pageNum = pageNum -1;

        String url = HOST_ITEM_SEARCH + "/esPoetry/searchA" +
                "?pageNum={pageNum}&pageSize={pageSize}&title={title}" +
                "&dynasty={dynasty}&author={author}&content={content}";

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("pageNum", pageNum);
        uriVariables.put("pageSize", pageSize);
        uriVariables.put("title", title);
        uriVariables.put("dynasty", dynasty);
        uriVariables.put("author", author);
        uriVariables.put("content", content);

        ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(url, CommonResult.class, uriVariables);
        return responseEntity.getBody();
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
        int id = poetryService.createPoetry(cmsPoetryParam);
        if (id != 0) {
            commonResult = CommonResult.success(null);
            CmsPoetry poetry = new CmsPoetry();
            BeanUtils.copyProperties(cmsPoetryParam, poetry);
            poetry.setId(id);
            LOGGER.info("createPoetry success:{}", poetry);
            String url = HOST_ITEM_SEARCH + "/esPoetry/create";
            restTemplate.postForEntity(url, poetry, CommonResult.class);
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
            String deleteUrl = HOST_ITEM_SEARCH + "/esPoetry/delete/" + id;
            String addUrl = HOST_ITEM_SEARCH + "/esPoetry/create/" + id;
            restTemplate.postForEntity(deleteUrl, id, CommonResult.class);
            restTemplate.postForEntity(addUrl, id, CommonResult.class);
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
            String url = HOST_ITEM_SEARCH + "/esPoetry/delete/batch?ids={ids}";
            Map<String, Object> uriVariables = new HashMap<>();
            StringBuilder idStr = new StringBuilder();
            for (Integer id:ids) {
                if (idStr.toString().equals("")) {
                    idStr.append(id);
                } else {
                    idStr.append(",").append(id);
                }
            }
            uriVariables.put("ids", idStr);
            restTemplate.getForEntity(url, CommonResult.class, uriVariables);
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
