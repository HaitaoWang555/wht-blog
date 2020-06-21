package com.wht.item.search.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "pms", type = "poetry",shards = 1,replicas = 0)
@Getter
@Setter
public class EsCmsPoetry {
    @Id
    private Long id;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Keyword)
    private String dynasty;
    @Field(type = FieldType.Keyword)
    private String author;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String content;

}
