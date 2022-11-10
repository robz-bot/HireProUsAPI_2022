/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;

/**
 * Entity class for the MongoDB collection - Role Menu.
 * 
 * @author Sihab.
 *
 */
public class SubMenuDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long menuId;
	private String menuName;
	private String pageLink;

	/**
	 * @return the menuId
	 */
	public long getMenuId() {
		return menuId;
	}

	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * @return the pageLink
	 */
	public String getPageLink() {
		return pageLink;
	}

	/**
	 * @param pageLink the pageLink to set
	 */
	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}
}
