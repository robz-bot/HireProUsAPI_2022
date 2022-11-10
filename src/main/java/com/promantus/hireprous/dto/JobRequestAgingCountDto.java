/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;

/**
 * Dto class for the collection - JobRequestAgingCountDto.
 * 
 * @author Sihab.
 *
 */
public class JobRequestAgingCountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long totalCount;
	private Long age10Days;
	private Long age20Days;
	private Long age30Days;
	private Long age60Days;
	private Long age90Days;
	private Long moreThan90Days;

	private int status;
	private String message;

	/**
	 * @return the totalCount
	 */
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the age10Days
	 */
	public Long getAge10Days() {
		return age10Days;
	}

	/**
	 * @param age10Days the age10Days to set
	 */
	public void setAge10Days(Long age10Days) {
		this.age10Days = age10Days;
	}

	/**
	 * @return the age20Days
	 */
	public Long getAge20Days() {
		return age20Days;
	}

	/**
	 * @param age20Days the age20Days to set
	 */
	public void setAge20Days(Long age20Days) {
		this.age20Days = age20Days;
	}

	/**
	 * @return the age30Days
	 */
	public Long getAge30Days() {
		return age30Days;
	}

	/**
	 * @param age30Days the age30Days to set
	 */
	public void setAge30Days(Long age30Days) {
		this.age30Days = age30Days;
	}

	/**
	 * @return the age60Days
	 */
	public Long getAge60Days() {
		return age60Days;
	}

	/**
	 * @param age60Days the age60Days to set
	 */
	public void setAge60Days(Long age60Days) {
		this.age60Days = age60Days;
	}

	/**
	 * @return the age90Days
	 */
	public Long getAge90Days() {
		return age90Days;
	}

	/**
	 * @param age90Days the age90Days to set
	 */
	public void setAge90Days(Long age90Days) {
		this.age90Days = age90Days;
	}

	/**
	 * @return the moreThan90Days
	 */
	public Long getMoreThan90Days() {
		return moreThan90Days;
	}

	/**
	 * @param moreThan90Days the moreThan90Days to set
	 */
	public void setMoreThan90Days(Long moreThan90Days) {
		this.moreThan90Days = moreThan90Days;
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
