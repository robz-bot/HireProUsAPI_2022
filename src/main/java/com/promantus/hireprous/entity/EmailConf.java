/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class for the MongoDB collection - emails.
 * 
 * @author Sihab.
 *
 */
@Document(collection = "email_conf")
public class EmailConf {

	@Id
	private Long id;
	private Long buId;
	private String purpose;
	private String to;
	private String cc;
	private String bcc;
	private long createdBy;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private LocalDateTime updatedDateTime;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the buId
	 */
	public Long getBuId() {
		return buId;
	}

	/**
	 * @param buId the buId to set
	 */
	public void setBuId(Long buId) {
		this.buId = buId;
	}

	/**
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose;
	}

	/**
	 * @param purpose the purpose to set
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * @return the bcc
	 */
	public String getBcc() {
		return bcc;
	}

	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
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
