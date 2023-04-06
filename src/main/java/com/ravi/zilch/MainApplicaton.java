package com.ravi.zilch;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@SpringBootApplication   
@EnableJpaRepositories
@EnableTransactionManagement

public class MainApplicaton {

	public static void main(String[] args) {
		SpringApplication.run(MainApplicaton.class, args);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
