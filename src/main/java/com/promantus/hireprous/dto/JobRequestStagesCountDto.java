/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;

/**
 * Dto class for the collection - JobRequestStagesCount.
 * 
 * @author Sihab.
 *
 */
public class JobRequestStagesCountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long totalRequest;
	private Long contract;
	private Long fullTime;
	private Long resumesTagged;
	private Long resumesShortlisted;
	private Long ir1Cleared;
	private Long ir2Cleared;
	private Long crCleared;
	private Long hrCleared;
	private Long selected;
	private Long onboarded;

	private int status;
	private String message;

	/**
	 * @return the totalRequest
	 */
	public Long getTotalRequest() {
		return totalRequest;
	}

	/**
	 * @param totalRequest the totalRequest to set
	 */
	public void setTotalRequest(Long totalRequest) {
		this.totalRequest = totalRequest;
	}

	/**
	 * @return the contract
	 */
	public Long getContract() {
		return contract;
	}

	/**
	 * @param contract the contract to set
	 */
	public void setContract(Long contract) {
		this.contract = contract;
	}

	/**
	 * @return the fullTime
	 */
	public Long getFullTime() {
		return fullTime;
	}

	/**
	 * @param fullTime the fullTime to set
	 */
	public void setFullTime(Long fullTime) {
		this.fullTime = fullTime;
	}

	/**
	 * @return the resumesTagged
	 */
	public Long getResumesTagged() {
		return resumesTagged;
	}

	/**
	 * @param resumesTagged the resumesTagged to set
	 */
	public void setResumesTagged(Long resumesTagged) {
		this.resumesTagged = resumesTagged;
	}

	/**
	 * @return the resumesShortlisted
	 */
	public Long getResumesShortlisted() {
		return resumesShortlisted;
	}

	/**
	 * @param resumesShortlisted the resumesShortlisted to set
	 */
	public void setResumesShortlisted(Long resumesShortlisted) {
		this.resumesShortlisted = resumesShortlisted;
	}

	/**
	 * @return the ir1Cleared
	 */
	public Long getIr1Cleared() {
		return ir1Cleared;
	}

	/**
	 * @param ir1Cleared the ir1Cleared to set
	 */
	public void setIr1Cleared(Long ir1Cleared) {
		this.ir1Cleared = ir1Cleared;
	}

	/**
	 * @return the ir2Cleared
	 */
	public Long getIr2Cleared() {
		return ir2Cleared;
	}

	/**
	 * @param ir2Cleared the ir2Cleared to set
	 */
	public void setIr2Cleared(Long ir2Cleared) {
		this.ir2Cleared = ir2Cleared;
	}

	/**
	 * @return the crCleared
	 */
	public Long getCrCleared() {
		return crCleared;
	}

	/**
	 * @param crCleared the crCleared to set
	 */
	public void setCrCleared(Long crCleared) {
		this.crCleared = crCleared;
	}

	/**
	 * @return the hrCleared
	 */
	public Long getHrCleared() {
		return hrCleared;
	}

	/**
	 * @param hrCleared the hrCleared to set
	 */
	public void setHrCleared(Long hrCleared) {
		this.hrCleared = hrCleared;
	}

	/**
	 * @return the selected
	 */
	public Long getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(Long selected) {
		this.selected = selected;
	}

	/**
	 * @return the onboarded
	 */
	public Long getOnboarded() {
		return onboarded;
	}

	/**
	 * @param onboarded the onboarded to set
	 */
	public void setOnboarded(Long onboarded) {
		this.onboarded = onboarded;
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
