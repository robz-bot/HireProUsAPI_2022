/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class for the MongoDB collection - roles.
 * 
 * @author Sihab.
 *
 */
public class RoleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String roleName;
	private List<MenuDto> menuDtoList;
	private List<MenuDto> subMenuDtoList;
	private long createdBy;
	private String createdByName;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private String updatedByName;
	private LocalDateTime updatedDateTime;

	private int status;
	private String message;

	private List<String> mainMenuIds;
	private List<String> subMenuIds;

	private String mainMenuNames;
	private String subMenuNames;

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
	 * @return the menuDtoList
	 */
	public List<MenuDto> getMenuDtoList() {
		return menuDtoList;
	}

	/**
	 * @param menuDtoList the menuDtoList to set
	 */
	public void setMenuDtoList(List<MenuDto> menuDtoList) {
		this.menuDtoList = menuDtoList;
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
	 * @return the subMenuDtoList
	 */
	public List<MenuDto> getSubMenuDtoList() {
		return subMenuDtoList;
	}

	/**
	 * @param subMenuDtoList the subMenuDtoList to set
	 */
	public void setSubMenuDtoList(List<MenuDto> subMenuDtoList) {
		this.subMenuDtoList = subMenuDtoList;
	}

	/**
	 * @return the mainMenuIds
	 */
	public List<String> getMainMenuIds() {
		return mainMenuIds;
	}

	/**
	 * @param mainMenuIds the mainMenuIds to set
	 */
	public void setMainMenuIds(List<String> mainMenuIds) {
		this.mainMenuIds = mainMenuIds;
	}

	/**
	 * @return the subMenuIds
	 */
	public List<String> getSubMenuIds() {
		return subMenuIds;
	}

	/**
	 * @param subMenuIds the subMenuIds to set
	 */
	public void setSubMenuIds(List<String> subMenuIds) {
		this.subMenuIds = subMenuIds;
	}

	/**
	 * @return the mainMenuNames
	 */
	public String getMainMenuNames() {
		return mainMenuNames;
	}

	/**
	 * @param mainMenuNames the mainMenuNames to set
	 */
	public void setMainMenuNames(String mainMenuNames) {
		this.mainMenuNames = mainMenuNames;
	}

	/**
	 * @return the subMenuNames
	 */
	public String getSubMenuNames() {
		return subMenuNames;
	}

	/**
	 * @param subMenuNames the subMenuNames to set
	 */
	public void setSubMenuNames(String subMenuNames) {
		this.subMenuNames = subMenuNames;
	}
}
