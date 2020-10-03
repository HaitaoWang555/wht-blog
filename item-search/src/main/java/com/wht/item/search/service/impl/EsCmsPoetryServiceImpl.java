package com.wht.item.search.service.impl;

import com.github.pagehelper.PageHelper;
import com.wht.item.search.dao.EsCmsPoetryDao;
import com.wht.item.search.domain.EsCmsPoetry;
import com.wht.item.search.repository.EsCmsPoetryRepository;
import com.wht.item.search.service.EsCmsPoetryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  诗词搜索管理SService实现类
 * @author wht
 * @since 2020-04-29 19:31
 */
@Service
public class EsCmsPoetryServiceImpl implements EsCmsPoetryService {

    @Resource
    private EsCmsPoetryDao esPoetryDao;
    @Resource
    private EsCmsPoetryRepository esPoetryRepository;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    private static final String PERSON_INDEX_NAME = "pms";
    private static final String PERSON_INDEX_TYPE = "poetry";

    private static final Logger LOGGER = LoggerFactory.getLogger(EsCmsPoetryServiceImpl.class);

    @Override
    public int importAll() {
        int count = esPoetryDao.getCount();
        int len = (int) Math.ceil(count / 10000d);
        int insertCount = 0;
        for (int i = 0; i < len; i++) {
            PageHelper.startPage(i + 1, 10000, false);
            List<EsCmsPoetry> esPoetryList = esPoetryDao.getEsPoetryList();
            insertCount = insertCount + insertList(esPoetryList);
        }
        return insertCount;
    }

    @Override
    public int updateList(Map<String, Integer> uriVariables) {
        int id = uriVariables.get("id");
        int num = uriVariables.get("num");
        PageHelper.startPage(1, num, false);
        List<EsCmsPoetry> esPoetryList = esPoetryDao.getEsPoetryList(id);
        return insertList(esPoetryList);
    }

    @Override
    public void delete(Long id) {
        esPoetryRepository.deleteById(id);
    }

    @Override
    public EsCmsPoetry create(Long id) {
        createIndex();
        EsCmsPoetry result = null;
        List<EsCmsPoetry> esPoetryList = esPoetryDao.getAllEsPoetryList(id);
        if (esPoetryList.size() > 0) {
            EsCmsPoetry esPoetry = esPoetryList.get(0);
            result = esPoetryRepository.save(esPoetry);
        }
        return result;
    }

    @Override
    public EsCmsPoetry create(EsCmsPoetry esCmsPoetry) {
        createIndex();
        return esPoetryRepository.save(esCmsPoetry);
    }

    @Override
    public void delete(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<EsCmsPoetry> esProductList = new ArrayList<>();
            for (Long id : ids) {
                EsCmsPoetry esProduct = new EsCmsPoetry();
                esProduct.setId(id);
                esProductList.add(esProduct);
            }
            esPoetryRepository.deleteAll(esProductList);
        }
    }

    @Override
    public Page<EsCmsPoetry> search(String keyword, Integer pageNum, Integer pageSize) {
        if (!noIndex()) {
            return Page.empty();
        }
        if (pageNum * pageSize > 10000) pageNum = 10000 / pageSize - 1;
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return esPoetryRepository.findByTitleOrDynastyOrAuthorOrContent(keyword, keyword, keyword, keyword, pageable);
    }

    @Override
    public Page<EsCmsPoetry> search(String title, String dynasty, String author, String content, Integer pageNum, Integer pageSize) {
        if (!noIndex()) {
            return Page.empty();
        }
        if (pageNum * pageSize > 10000) pageNum = 10000 / pageSize - 1;
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "id");
        if ("".equals(title)) title = null;
        if ("".equals(dynasty)) dynasty = null;
        if ("".equals(author)) author = null;
        if ("".equals(content)) content = null;
        return esPoetryRepository.findByTitleAndDynastyAndAuthorAndContent(title, dynasty, author, content, pageable);
    }

    private void createIndex () {
        if (!elasticsearchTemplate.indexExists(PERSON_INDEX_NAME)) {
            elasticsearchTemplate.createIndex(PERSON_INDEX_NAME);
        }
    }
    private boolean noIndex () {
        return elasticsearchTemplate.indexExists(PERSON_INDEX_NAME);
    }
    private int insertList(List<EsCmsPoetry> esPoetryList) {
        int counter = 0;
        try {
            createIndex();
            List<IndexQuery> queries = new ArrayList<>();
            for (EsCmsPoetry esPoetry : esPoetryList) {
                IndexQuery indexQuery = new IndexQuery();
                indexQuery.setId(esPoetry.getId() + "");
                indexQuery.setObject(esPoetry);
                indexQuery.setIndexName(PERSON_INDEX_NAME);
                indexQuery.setType(PERSON_INDEX_TYPE);

                queries.add(indexQuery);
                if (counter % 500 == 0) {
                    elasticsearchTemplate.bulkIndex(queries);
                    queries.clear();
                    LOGGER.info("bulkIndex counter : " + counter);
                }
                counter++;
            }
            if (queries.size() > 0) {
                elasticsearchTemplate.bulkIndex(queries);
            }
            LOGGER.info("bulkIndex completed.");
        } catch (Exception e) {
            LOGGER.error("IndexerService.bulkIndex e;" + e.getMessage());
            throw e;
        }
        return counter;
    }
}
