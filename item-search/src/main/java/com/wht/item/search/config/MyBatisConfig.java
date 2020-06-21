package com.wht.item.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis配置类
 * @author wht
 * @since 2020-06-08 0:25
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.wht.item.mapper","com.wht.item.search.dao"})
public class MyBatisConfig {
}
