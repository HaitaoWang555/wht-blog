package com.wht.item.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wht
 * @since 2020-06-08 0:23
 */
@SpringBootApplication(scanBasePackages = "com.wht.item")
public class ItemPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemPortalApplication.class, args);
    }
}
