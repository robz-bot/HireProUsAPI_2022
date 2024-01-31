/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Sihab.
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private final String releaseVersion = "3.0.4";
	
	// Get the current date and time
    LocalDateTime now = LocalDateTime.now();

    // Define a custom date and time format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Format the current date and time using the formatter
    String formattedDateTime = now.format(formatter);

	@Bean
	public Docket hireProApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.promantus.hireprous")).build()
				.globalOperationParameters(operationParameters()).apiInfo(metaData());
	}

	/**
	 * @return
	 */
	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("HireProUs - Spring Boot REST API - " + releaseVersion + " - Deployed On: " + formattedDateTime )
				.description("\"An internal hiring web application of Promantus Private Limited, Chennai.\"")
				.version(releaseVersion)
//				.license("Promantus License Version 1.0").licenseUrl("https://promantusinc.com/licenses/LICENSE-1.0\"")
//				.contact(new Contact("Kamal Purushothaman", "https://promantusinc.com/", "kamal1@promantusinc.com"))
				.build();
	}

	/**
	 * @return
	 */
	private List<Parameter> operationParameters() {

		List<Parameter> headers = new ArrayList<Parameter>();

		headers.add(new ParameterBuilder().name("PRO-API-KEY").description("Security token to be sent.")
				.modelRef(new ModelRef("string")).parameterType("header").required(true).build());
		return headers;
	}
}