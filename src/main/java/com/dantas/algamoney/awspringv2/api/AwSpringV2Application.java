package com.dantas.algamoney.awspringv2.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AwSpringV2Application {

    public static void main(String[] args) {
        SpringApplication.run(AwSpringV2Application.class, args);
    }


    /**
     * If this bean has load by spring container, its filter requests comes from another origins. Other way to solve
     * CORS problem is creating a filter for CORS request.
     * */
//    @Bean
    public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/*").allowedOrigins("http://localhost:4200");
			}
		};
	}

}
