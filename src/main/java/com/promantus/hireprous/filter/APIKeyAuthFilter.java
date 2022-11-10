/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.filter;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * Filter class for Key Authentication.
 * 
 * @author Sihab.
 *
 */
public class APIKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

	private String principalRequestHeader;

	public APIKeyAuthFilter(String principalRequestHeader) {
		this.principalRequestHeader = principalRequestHeader;
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		return request.getHeader(principalRequestHeader);
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return null;
	}
}
