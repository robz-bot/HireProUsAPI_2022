/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class for the MongoDB collection - customers.
 * 
 * @author Sihab.
 *
 */
@Document(collection = "customers")
public class Customer {

	@Id
	private long id;
	private String customerName;
	private String recRoleIds;
	private String location;
	private String region;
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
	 * @return the recRoleIds
	 */
	public String getRecRoleIds() {
		return recRoleIds;
	}

	/**
	 * @param recRoleIds the recRoleIds to set
	 */
	public void setRecRoleIds(String recRoleIds) {
		this.recRoleIds = recRoleIds;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
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
