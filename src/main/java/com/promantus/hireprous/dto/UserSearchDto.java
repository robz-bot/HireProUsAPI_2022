/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;

/**
 * Entity class for the MongoDB collection - users.
 * 
 * @author Sihab.
 *
 */
public class UserSearchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String email;
	private String name;
	private Long buId;
	private Long roleId;
	private String sex;
	private String location;
	private String panelMember;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
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
	 * @return the panelMember
	 */
	public String getPanelMember() {
		return panelMember;
	}

	/**
	 * @param panelMember the panelMember to set
	 */
	public void setPanelMember(String panelMember) {
		this.panelMember = panelMember;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
}
