/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Entity class for the MongoDB collection - Role Menu.
 * 
 * @author Sihab.
 *
 */
public class MainMenuDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long menuId;
	private String menuName;
	private String pageLink;
	private List<SubMenuDto> subMenuList;

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

	/**
	 * @return the subMenuList
	 */
	public List<SubMenuDto> getSubMenuList() {
		return subMenuList;
	}

	/**
	 * @param subMenuList the subMenuList to set
	 */
	public void setSubMenuList(List<SubMenuDto> subMenuList) {
		this.subMenuList = subMenuList;
	}
}
