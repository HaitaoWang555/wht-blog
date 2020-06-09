package com.wht.item.portal.config;

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
@MapperScan({"com.wht.item.mapper","com.wht.item.portal.dao"})
public class MyBatisConfig {
}
