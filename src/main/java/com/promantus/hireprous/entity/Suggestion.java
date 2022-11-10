/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class for the MongoDB collection - suggestions.
 * 
 * @author Sihab.
 *
 */
@Document(collection = "suggestions")
public class Suggestion {

	@Id
	private long id;
	private String codePrefix;
	private Long codeNumber;
	private String type;
	private String suggestion;
	private String sugStatus;
	private String reply;

	private long createdBy;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private LocalDateTime updatedDateTime;

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
	 * @return the codePrefix
	 */
	public String getCodePrefix() {
		return codePrefix;
	}

	/**
	 * @param codePrefix the codePrefix to set
	 */
	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

	/**
	 * @return the codeNumber
	 */
	public Long getCodeNumber() {
		return codeNumber;
	}

	/**
	 * @param codeNumber the codeNumber to set
	 */
	public void setCodeNumber(Long codeNumber) {
		this.codeNumber = codeNumber;
	}

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
	 * @return the suggestion
	 */
	public String getSuggestion() {
		return suggestion;
	}

	/**
	 * @param suggestion the suggestion to set
	 */
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
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
	 * @return the reply
	 */
	public String getReply() {
		return reply;
	}

	/**
	 * @param reply the reply to set
	 */
	public void setReply(String reply) {
		this.reply = reply;
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
}
