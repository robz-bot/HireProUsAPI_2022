/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Intercepter class to know the performance of the API.
 * 
 * @author Sihab.
 *
 */
public class ExecuteTimeInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ExecuteTimeInterceptor.class);

	/**
	 *
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);

		// logger.info("[" + handler + "] executeTime : " + startTime + "ms");
		return true;
	}

	/**
	 *
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		long startTime = (Long) request.getAttribute("startTime");
		long endTime = System.currentTimeMillis();

		long executeTime = endTime - startTime;

		logger.info("[" + handler + "] - ExecutionTime : " + executeTime + " Milli Seconds.");
	}
}
