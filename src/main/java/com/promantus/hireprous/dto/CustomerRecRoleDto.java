/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Dto class for the MongoDB collection - CustomerRecRole.
 * 
 * @author Sihab.
 *
 */
public class CustomerRecRoleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long customerId;
	private String customerName;
	private String recRoleList;
	private List<String> recRoleIdList;

	private int status;
	private String message;

	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the recRoleList
	 */
	public String getRecRoleList() {
		return recRoleList;
	}

	/**
	 * @param recRoleList the recRoleList to set
	 */
	public void setRecRoleList(String recRoleList) {
		this.recRoleList = recRoleList;
	}

	/**
	 * @return the recRoleIdList
	 */
	public List<String> getRecRoleIdList() {
		return recRoleIdList;
	}

	/**
	 * @param recRoleIdList the recRoleIdList to set
	 */
	public void setRecRoleIdList(List<String> recRoleIdList) {
		this.recRoleIdList = recRoleIdList;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
