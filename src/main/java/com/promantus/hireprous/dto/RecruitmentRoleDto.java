/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity class for the MongoDB collection - recruitment_roles.
 * 
 * @author Sihab.
 *
 */
public class RecruitmentRoleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String recruitmentRoleName;
	private long createdBy;
	private String createdByName;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private String updatedByName;
	private LocalDateTime updatedDateTime;

	private int status;
	private String message;

	private String selected;
	
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the recruitmentRoleName
	 */
	public String getRecruitmentRoleName() {
		return recruitmentRoleName;
	}

	/**
	 * @param recruitmentRoleName the recruitmentRoleName to set
	 */
	public void setRecruitmentRoleName(String recruitmentRoleName) {
		this.recruitmentRoleName = recruitmentRoleName;
	}

	/**
	 * @return the createdBy
	 */
	public long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDateTime
	 */
	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	/**
	 * @return the updatedBy
	 */
	public long getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedDateTime
	 */
	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}

	/**
	 * @param updatedDateTime the updatedDateTime to set
	 */
	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}

	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	/**
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}

	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
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

	/**
	 * @return the selected
	 */
	public String getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(String selected) {
		this.selected = selected;
	}

	public LocalDateTime getFromDateTime() {
		return fromDateTime;
	}

	public void setFromDateTime(LocalDateTime fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public LocalDateTime getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(LocalDateTime toDateTime) {
		this.toDateTime = toDateTime;
	}
}
