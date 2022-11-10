/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class for the MongoDB collection - roles.
 * 
 * @author Sihab.
 *
 */
@Document(collection = "roles")
public class Role {

	@Id
	private Long id;
	private String roleName;
	private String menuIds;
	private String subMenuIds;
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
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the menuIds
	 */
	public String getMenuIds() {
		return menuIds;
	}

	/**
	 * @param menuIds the menuIds to set
	 */
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
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
	 * @return the subMenuIds
	 */
	public String getSubMenuIds() {
		return subMenuIds;
	}

	/**
	 * @param subMenuIds the subMenuIds to set
	 */
	public void setSubMenuIds(String subMenuIds) {
		this.subMenuIds = subMenuIds;
	}
}
