/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Starter class for HireProUs application.
 * 
 * @author Sihab.
 *
 */
@SpringBootApplication
@EnableSwagger2
//@EnableAdminServer
public class HireProUsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HireProUsApplication.class, args);
	}
}
