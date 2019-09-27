package com.alien;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alien.repository.base.BaseRepositoryFactoryBean;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.alien"},
repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)//指定自己的工厂类
@EnableTransactionManagement
@EnableCaching
public class SpringDataJpaCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaCrudApplication.class, args);
	}

}
