package com.wht.item.search.service.impl;

import com.wht.item.search.dao.EsCmsPoetryDao;
import com.wht.item.search.domain.EsCmsPoetry;
import com.wht.item.search.repository.EsCmsPoetryRepository;
import com.wht.item.search.service.EsCmsPoetryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
        List<EsCmsPoetry> esPoetryList = esPoetryDao.getAllEsPoetryList(null);
        int counter = 0;
        try {
            if (!elasticsearchTemplate.indexExists(PERSON_INDEX_NAME)) {
                elasticsearchTemplate.createIndex(PERSON_INDEX_TYPE);
            }
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
                    System.out.println("bulkIndex counter : " + counter);
                }
                counter++;
            }
            if (queries.size() > 0) {
                elasticsearchTemplate.bulkIndex(queries);
            }
            System.out.println("bulkIndex completed.");
        } catch (Exception e) {
            System.out.println("IndexerService.bulkIndex e;" + e.getMessage());
            throw e;
        }
        return counter;
    }

    @Override
    public void delete(Long id) {
        esPoetryRepository.deleteById(id);
    }

    @Override
    public EsCmsPoetry create(Long id) {
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
        if (pageNum * pageSize > 10000) pageNum = 10000 / pageSize - 1;
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return esPoetryRepository.findByTitleOrDynastyOrAuthorOrContent(keyword, keyword, keyword, keyword, pageable);
    }

    @Override
    public Page<EsCmsPoetry> search(String title, String dynasty, String author, String content, Integer pageNum, Integer pageSize) {
        if (pageNum * pageSize > 10000) pageNum = 10000 / pageSize - 1;
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        if ("".equals(title)) title = null;
        if ("".equals(dynasty)) dynasty = null;
        if ("".equals(author)) author = null;
        if ("".equals(content)) content = null;
        if (title != null || dynasty != null || author != null || content != null) {
            LOGGER.info("title {}, dynasty {}, author {}, content {}", title, dynasty, author, content);
        }
        return esPoetryRepository.findByTitleOrDynastyOrAuthorOrContent(title, dynasty, author, content, pageable);
    }
}
