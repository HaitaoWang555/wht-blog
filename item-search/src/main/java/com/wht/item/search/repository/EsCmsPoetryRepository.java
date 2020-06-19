package com.wht.item.search.repository;

import com.wht.item.search.domain.EsCmsPoetry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsCmsPoetryRepository extends ElasticsearchRepository<EsCmsPoetry, Long> {
    /**
     * 搜索查询
     * @param title 诗词名称
     * @param dynasty 朝代
     * @param author 作者
     * @param content 内容
     * @param page 分页
     * @return Page<EsPoetry>
     */
    Page<EsCmsPoetry> findByTitleOrDynastyOrAuthorOrContent(String title, String dynasty, String author, String content, Pageable page);
}
