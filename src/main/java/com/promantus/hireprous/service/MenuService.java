/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.MenuDto;

/**
 * @author Sihab.
 *
 */
public interface MenuService {

	/**
	 * @param menuDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	MenuDto addMenu(final MenuDto menuDto, String lang) throws Exception;

	/**
	 * @param menuDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	MenuDto updateMenu(final MenuDto menuDto, String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<MenuDto> getAllMenus() throws Exception;

	/**
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	MenuDto getMenuById(String menuId) throws Exception;

	/**
	 * @param menuId
	 * @throws Exception
	 */
	void deleteMenuById(String menuId) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<MenuDto> searchMenu(String keyword) throws Exception;

	/**
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	String getMenuNameById(long menuId) throws Exception;

	/**
	 * @param menuName
	 * @return
	 * @throws Exception
	 */
	Boolean checkMenuName(String menuName) throws Exception;

	/**
	 * @param needShort
	 * @return
	 * @throws Exception
	 */
	List<MenuDto> getAllMenus(boolean needShort) throws Exception;

	/**
	 * @param menuIds
	 * @return
	 * @throws Exception
	 */
	String getMenuNamesByMenuIds(List<Long> menuIds) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<MenuDto> getAllMainMenuList() throws Exception;

	/**
	 * @param menuIds
	 * @return
	 * @throws Exception
	 */
	List<MenuDto> getAllMainMenuListByIds(List<Long> menuIds) throws Exception;

	/**
	 * @param menuId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<MenuDto> getSubMenuList(String menuId, String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<MenuDto> getAllSubMenuList() throws Exception;

	/**
	 * @param subMenuIds
	 * @return
	 * @throws Exception
	 */
	List<MenuDto> getAllSubMenuListByIds(List<Long> subMenuIds) throws Exception;

	/**
	 * @param subMenuIds
	 * @return
	 * @throws Exception
	 */
	List<MenuDto> getAllSubMenuListByIdsForLogin(List<Long> subMenuIds) throws Exception;

}
