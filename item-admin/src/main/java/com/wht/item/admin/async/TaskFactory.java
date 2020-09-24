package com.wht.item.admin.async;

import com.wht.item.common.api.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class TaskFactory {
    @Resource
    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskFactory.class);
    private String HOST_ITEM_SEARCH = "http://localhost:8081/api/search";

    @Async
    public void importPoetryList(Integer id, Integer insertNum) {
        String url = HOST_ITEM_SEARCH + "/esPoetry/updateList";
        Map<String, Integer> uriVariables = new HashMap<>();
        uriVariables.put("id", id);
        uriVariables.put("num", insertNum);
        try {
            restTemplate.postForEntity(url, uriVariables, CommonResult.class);
        } catch (Exception e) {
            LOGGER.error("esCreate {},  ERROR: {}", "importList", e.getMessage());
        }
    }
}
