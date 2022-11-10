/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;

/**
 * Dto class for the - RecStatus.
 * 
 * @author Sihab.
 *
 */
public class RecStatusDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String recStatusCode;
	private String recStatusName;

	/**
	 * @return the recStatusCode
	 */
	public String getRecStatusCode() {
		return recStatusCode;
	}

	/**
	 * @param recStatusCode the recStatusCode to set
	 */
	public void setRecStatusCode(String recStatusCode) {
		this.recStatusCode = recStatusCode;
	}

	/**
	 * @return the recStatusName
	 */
	public String getRecStatusName() {
		return recStatusName;
	}

	/**
	 * @param recStatusName the recStatusName to set
	 */
	public void setRecStatusName(String recStatusName) {
		this.recStatusName = recStatusName;
	}
}
