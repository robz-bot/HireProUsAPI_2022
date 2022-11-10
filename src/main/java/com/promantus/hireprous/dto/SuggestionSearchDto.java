/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;

/**
 * Dto class for the MongoDB collection - Suggestion.
 * 
 * @author Sihab.
 *
 */
public class SuggestionSearchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;
	private String sugStatus;
	private String code;
	private String createdBy;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the sugStatus
	 */
	public String getSugStatus() {
		return sugStatus;
	}

	/**
	 * @param sugStatus the sugStatus to set
	 */
	public void setSugStatus(String sugStatus) {
		this.sugStatus = sugStatus;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
