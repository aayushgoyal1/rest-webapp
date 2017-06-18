package com.sit.sso.web.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		 //set register error pagefilter false to avoid Liberty error
        setRegisterErrorPageFilter(false);
        
        return application.sources(Application.class);
    }
	
    public static void main(String[] args) {
    	//new SpringApplicationBuilder(Application.class).web(false).run(args);
    	new Application().configure(new SpringApplicationBuilder(Application.class)).run(args);
    }
    
}
