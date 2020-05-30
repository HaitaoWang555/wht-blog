package com.wht.item.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis配置类
 * Created by wht on 2019/4/8.
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.wht.item.mapper","com.wht.item.admin.dao"})
public class MyBatisConfig {
}
