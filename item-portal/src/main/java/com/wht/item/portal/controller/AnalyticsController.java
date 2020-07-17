package com.wht.item.portal.controller;


import com.wht.item.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 统计分析服务
 * @author wht
 * @since 2020-07-17 7:08
 */
@RestController
@Api(tags = "统计分析服务")
@RequestMapping("/analytics")
public class AnalyticsController {
    @Value("${baidu.site}")
    private String site;
    @Value("${baidu.token}")
    private String token;
    @Resource
    private RestTemplate restTemplate;

    @ApiOperation(value = "百度统计推送")
    @PostMapping("/baidu")
    public CommonResult baidu(@RequestParam String url) {
        String baseUrl = "http://data.zz.baidu.com/urls?site="+ site + "&token=" + token;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(baseUrl, url, String.class);
        return CommonResult.success(responseEntity.getBody());
    }
}
