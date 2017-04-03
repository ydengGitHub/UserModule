package com.usermodule.config;

import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter{
	@Override
	public void addViewControllers(ViewControllerRegistry registry){
		registry.addViewController("/").setViewName("home");
	}
	
	@Bean
	public Validator validator() {
	    LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
	    MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
	    factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
	    return factoryBean;
	}
}
