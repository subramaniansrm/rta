package com.srm.rta;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "com.srm.**" })
@EntityScan(basePackages = { "com.srm.rta.entity", "com.srm.coreframework.entity" })
@EnableJpaRepositories(basePackages = { "com.srm.coreframework.repository.**", "com.srm.rta.repository.**" })
@EnableSwagger2
public class RTABootApp extends SpringBootServletInitializer {

	static org.slf4j.Logger logger = LoggerFactory.getLogger(RTABootApp.class);

	static ConfigurableApplicationContext ctx = null;

	public static void main(String[] args) {

		try {
			ctx = SpringApplication.run(RTABootApp.class, args);
		} catch (Exception e) {
			logger.error("error", e);
			ctx.close();
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RTABootApp.class);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
	
}
