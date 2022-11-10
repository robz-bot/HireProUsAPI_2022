/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.promantus.hireprous.filter.APIKeyAuthFilter;

/**
 * @author Sihab.
 *
 */
@Configuration
@EnableWebSecurity
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${auth-token-header-name}")
	private String principalRequestHeader;

	@Value("${auth-token-header-value}")
	private String principalRequestValue;

	private static final String[] AUTH_LIST = { "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs",
			"/webjars/**", "**/v2/api-docs", "**/configuration/ui", "**/swagger-resources/**",
			"**/configuration/security", "**/swagger-ui.html", "**/webjars/**", "**/actuator/***", "/actuator*", "/applications/***" };

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**",
				"**/v2/api-docs", "**/configuration/ui", "**/swagger-resources/**", "**/configuration/security",
				"**/swagger-ui.html", "**/webjars/**", "**/actuator/***");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);

		filter.setAuthenticationManager(new AuthenticationManager() {

			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String principal = (String) authentication.getPrincipal();
				if (!principalRequestValue.equals(principal)) {
					throw new BadCredentialsException("The API key was not found or not the expected value.");
				}
				authentication.setAuthenticated(true);
				return authentication;
			}
		});

		httpSecurity.cors().configurationSource(corsConfigurationSource()).and().authorizeRequests()
				.antMatchers(AUTH_LIST).permitAll().and().httpBasic()
				.authenticationEntryPoint(swaggerAuthenticationEntryPoint()).and().csrf().disable()
				.antMatcher("/api/****").csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().addFilter(filter).authorizeRequests()
				.anyRequest().authenticated();
	}

	/**
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration corsConfig = new CorsConfiguration();

		corsConfig.setAllowCredentials(false);
		corsConfig.addAllowedOrigin("*");
		corsConfig.addAllowedHeader("*");
		corsConfig.addAllowedMethod("OPTIONS");
		corsConfig.addAllowedMethod("GET");
		corsConfig.addAllowedMethod("POST");
		corsConfig.addAllowedMethod("PUT");
		corsConfig.addAllowedMethod("DELETE");

		UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
		corsSource.registerCorsConfiguration("/**", corsConfig);
		return corsSource;
	}

	@Bean
	public BasicAuthenticationEntryPoint swaggerAuthenticationEntryPoint() {
		BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
		entryPoint.setRealmName("Swagger Realm");
		return entryPoint;
	}
}