package com.wht.item.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = "com.wht.item")
public class ItemAdminApplication{
	public static void main(String[] args) {
		SpringApplication.run(ItemAdminApplication.class, args);
	}
}
