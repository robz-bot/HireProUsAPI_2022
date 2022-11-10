/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.MainMenuDto;
import com.promantus.hireprous.dto.RoleDto;
import com.promantus.hireprous.entity.Role;

/**
 * @author Sihab.
 *
 */
public interface RoleMenuMappingService {

	/**
	 * @param roleId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	RoleDto getMenusByRoleId(String roleId, String lang) throws Exception;

	/**
	 * @param roleId
	 * @param userId
	 * @param mainMenuIds
	 * @param subMenuIds
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	RoleDto addRoleMenuMapping(String roleId, final String userId, List<String> mainMenuIds, List<String> subMenuIds,
			String lang) throws Exception;

	/**
	 * @param roleId
	 * @param userId
	 * @param mainMenuIds
	 * @param subMenuIds
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	RoleDto updateRoleWithMenus(String roleId, final String userId, List<String> mainMenuIds, List<String> subMenuIds,
			String lang) throws Exception;

	/**
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	List<MainMenuDto> getMenusForLogin(long roleId) throws Exception;

	/**
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<RoleDto> getMenuSubMenuList(String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<MainMenuDto> getMenusForLoginSa() throws Exception;

	/**
	 * @param roleId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<RoleDto> getMenuSubMenuListByRoleId(String roleId, String lang) throws Exception;

	/**
	 * @param role
	 * @return
	 * @throws Exception
	 */
	String getMainMenusForLogin(Role role) throws Exception;

	/**
	 * @param role
	 * @return
	 * @throws Exception
	 */
	String getSubMenusForLogin(Role role) throws Exception;

}
