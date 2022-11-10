/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.service.CommonService;

/**
 * Controller class to handle Common activities.
 * 
 * @author Sihab.
 *
 */
@RestController
public class CommonController {

	@Autowired
	private CommonService commonService;

	/**
	 * @param messageKey
	 * @param language
	 * @return
	 * @throws Exception
	 */
	public String getMessage(String messageKey, String[] params, String language) throws Exception {

		return commonService.getMessage(messageKey, params, language);
	}
}
