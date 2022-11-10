/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

/**
 * @author Sihab.
 *
 */
public interface CommonService {

	/**
	 * @return
	 */
	public long nextSequenceNumber() throws Exception;

	/**
	 * @param messageKey
	 * @param language
	 * @return
	 */
	String getMessage(String messageKey, String[] params, String language) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	void truncateData() throws Exception;

}
