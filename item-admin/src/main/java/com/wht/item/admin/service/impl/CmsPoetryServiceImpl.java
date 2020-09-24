package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.wht.item.admin.async.TaskFactory;
import com.wht.item.admin.dao.CmsPoetryDao;
import com.wht.item.admin.dto.CmsPoetryParam;
import com.wht.item.admin.service.CmsPoetryService;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.common.api.ResultCode;
import com.wht.item.common.exception.ApiException;
import com.wht.item.mapper.CmsPoetryMapper;
import com.wht.item.model.CmsPoetry;
import com.wht.item.model.CmsPoetryExample;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CmsPoetryServiceImpl implements CmsPoetryService {
    @Resource
    private CmsPoetryMapper poetryMapper;
    @Resource
    private CmsPoetryDao poetryDao;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private TaskFactory task;

    private String HOST_ITEM_SEARCH = "http://localhost:8081/api/search";

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsPoetryServiceImpl.class);


    @Override
    public List<CmsPoetry> listAllPoetry() {
        return poetryMapper.selectByExample(new CmsPoetryExample());
    }

    @Override
    public int createPoetry(CmsPoetryParam cmsPoetryParam) {
        CmsPoetry poetry = new CmsPoetry();
        BeanUtils.copyProperties(cmsPoetryParam, poetry);
        int count = poetryMapper.insertSelective(poetry);
        if (count > 0) esCreate(poetry);
        return count;
    }

    @Override
    public int updatePoetry(int id, CmsPoetryParam cmsPoetryParam) {
        CmsPoetry poetry = new CmsPoetry();
        BeanUtils.copyProperties(cmsPoetryParam, poetry);
        poetry.setId(id);
        int count = poetryMapper.updateByPrimaryKeySelective(poetry);
        if (count > 0) esUpdate(id);
        return count;
    }

    @Override
    public int deletePoetry(int id) {
        return poetryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deletePoetry(List<Integer> ids) {
        CmsPoetryExample example = new CmsPoetryExample();
        example.createCriteria().andIdIn(ids);
        int count = poetryMapper.deleteByExample(example);
        if (count > 0) esDel(ids);
        return count;
    }

    @Override
    public List<CmsPoetry> listPoetry(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize, "id desc");
        return poetryMapper.selectByExample(new CmsPoetryExample());
    }

    @Override
    public List<CmsPoetryParam> downloadPoetry(String ids) {
        if (StringUtils.isEmpty(ids)) {
            throw new ApiException(ResultCode.VALIDATE_FAILED, "请选择要导出的诗词");
        } else {
            String[] idArr = ids.split(",");
            ArrayList<Integer> idsArr = new ArrayList<Integer>();
            for (String s : idArr) {
                idsArr.add(Integer.parseInt(s));
            }
            return toCmsPoetryParam(searchByIds(idsArr));
        }
    }

    private List<CmsPoetryParam> toCmsPoetryParam(List<CmsPoetry> CmsPoetryList) {
        return CmsPoetryList.stream().map(cmsMeta -> {
            CmsPoetryParam obj = new CmsPoetryParam();
            BeanUtils.copyProperties(cmsMeta, obj);
            return obj;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CmsPoetry> searchByIds(List<Integer> ids) {
        CmsPoetryExample example = new  CmsPoetryExample();
        example.createCriteria().andIdIn(ids);
        return poetryMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<CmsPoetry> listPoetryWithBLOBs(int pageNum, int pageSize) {
        if (pageNum * pageSize > 10000) pageNum = 10000 / pageSize;
        PageHelper.startPage(pageNum, pageSize, false);
        return poetryMapper.selectByExampleWithBLOBs(new CmsPoetryExample());
    }

    @Override
    public CmsPoetry getPoetry(int id) {
        return poetryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int uploadCsv(InputStream inputStream) {
        BufferedReader BufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<String> list = BufferedReader.lines().collect(Collectors.toList());
        list.remove(0);
        List<CmsPoetryParam> poetryList = new ArrayList<>();
        for (String str :  list) {
            String[] strList =  str.split(",");
            if (strList[0].length() > 49) continue;
            CmsPoetryParam poetryParam = new CmsPoetryParam();
            poetryParam.setTitle(strList[0]);
            poetryParam.setDynasty(strList[1]);
            poetryParam.setAuthor(strList[2]);
            poetryParam.setContent(strList[3]);
            poetryList.add(poetryParam);
        }

        List<List<CmsPoetryParam>> result = ListUtils.partition(poetryList, 5000);
        int count = 0;
        PageHelper.startPage(1, 1, "id desc");
        List<CmsPoetry> listPoetry = poetryDao.select();
        Integer startId;
        if (listPoetry.size() == 0) {
            startId = 0;
        } else {
            startId = listPoetry.get(0).getId();
        }

        for (List<CmsPoetryParam> CmsPoetryParamList : result) {
            // TODO: 锁
            int num = poetryDao.insertList(CmsPoetryParamList);
            task.importPoetryList(startId, num);
            count = count + num;
        }
        return count;
    }

    @Override
    public CommonResult esSearch(String title, String dynasty, String author, String content, Integer pageNum, Integer pageSize) {
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
        try {
            ResponseEntity<CommonResult> responseEntity = restTemplate.getForEntity(url, CommonResult.class, uriVariables);
            return responseEntity.getBody();
        } catch (Exception e) {
            LOGGER.error("esSearch ERROR: {}", e.getMessage());
            List<CmsPoetry> poetryList =  searchListPoetryWithBLOBs(title, dynasty, author, content, pageNum + 1, pageSize);
            return CommonResult.success(CommonPage.restPage(poetryList));
        }

    }

    /**
     * 将诗词插入 ES
     * @param poetry 诗词
     */
    private void esCreate (CmsPoetry poetry) {
        String url = HOST_ITEM_SEARCH + "/esPoetry/create";
        try {
            restTemplate.postForEntity(url, poetry, CommonResult.class);
        } catch (Exception e) {
            LOGGER.error("esCreate {},  ERROR: {}", poetry, e.getMessage());
        }
    }

    /**
     * 搜索诗词
     */
    private List<CmsPoetry> searchListPoetryWithBLOBs(String title, String dynasty, String author, String content, Integer pageNum, Integer pageSize) {
        if (pageNum * pageSize > 10000) pageNum = 10000 / pageSize;
        PageHelper.startPage(pageNum, pageSize, false);
        CmsPoetryExample example = new  CmsPoetryExample();
        CmsPoetryExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(title)) criteria.andTitleLike('%' + title + '%');
        if (!StringUtils.isEmpty(dynasty)) criteria.andDynastyLike('%' + dynasty + '%');
        if (!StringUtils.isEmpty(author)) criteria.andAuthorLike('%' + author + '%');
        return poetryMapper.selectByExampleWithBLOBs(example);
    }


    /**
     * ES 更新诗词
     */
    private void esUpdate(long id) {
        String deleteUrl = HOST_ITEM_SEARCH + "/esPoetry/delete/" + id;
        String addUrl = HOST_ITEM_SEARCH + "/esPoetry/create/" + id;
        try {
            restTemplate.postForEntity(deleteUrl, id, CommonResult.class);
            restTemplate.postForEntity(addUrl, id, CommonResult.class);
        } catch (Exception e) {
            LOGGER.error("esUpdate {},  ERROR: {}", id, e.getMessage());
        }
    }

    /**
     * ES 删除诗词
     */
    private void esDel(List<Integer> ids) {
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
        try {
            restTemplate.getForEntity(url, CommonResult.class, uriVariables);
        } catch (Exception e) {
            LOGGER.error("esDel {},  ERROR: {}", ids, e.getMessage());
        }
    }
}
