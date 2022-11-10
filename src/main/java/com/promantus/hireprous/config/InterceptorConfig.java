/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.promantus.hireprous.interceptor.ExecuteTimeInterceptor;

/**
 * @author Sihab.
 *
 */
@SuppressWarnings("deprecation")
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ExecuteTimeInterceptor());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedHeaders("*")
			.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "OPTIONS", "PATCH");
	}
}