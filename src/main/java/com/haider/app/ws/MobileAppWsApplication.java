package com.haider.app.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MobileAppWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}
	
	
	// CREATING BEAN FOR AUTOWIRING 
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
	// Need a bean so otherwise CONTEXT won't be initialized by spring
//	@Bean
//	public SpringApplicationContext springApplicationContext() {
//		return new SpringApplicationContext();
//	}
	
//	@Bean(name ="AppProperties")
//	public AppProperties appProperties() {
//		return new AppProperties();
//	}
}

