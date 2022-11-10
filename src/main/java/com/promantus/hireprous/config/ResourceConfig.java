/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author Sihab.
 *
 */
@Configuration
public class ResourceConfig {

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		messageSource.setBasenames(new String[] {"classpath:messages", "classpath:email.properties"});
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(1800); // reload messages every 30 minutes.
		return messageSource;
	}
}