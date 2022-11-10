/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.RoleDto;
import com.promantus.hireprous.entity.Role;

/**
 * @author Sihab.
 *
 */
public interface RoleService {

	/**
	 * @param roleDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	RoleDto addRole(final RoleDto roleDto, String lang) throws Exception;

	/**
	 * @param roleDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	RoleDto updateRole(final RoleDto roleDto, String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<RoleDto> getAllRoles() throws Exception;

	/**
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	RoleDto getRoleById(String roleId) throws Exception;

	/**
	 * @param roleId
	 * @throws Exception
	 */
	void deleteRoleById(String roleId) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<RoleDto> searchRole(String keyword) throws Exception;

	/**
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	String getRoleNameById(long roleId) throws Exception;

	/**
	 * @param roleName
	 * @return
	 * @throws Exception
	 */
	Boolean checkRoleName(String roleName) throws Exception;

	/**
	 * @param string
	 * @return
	 */
	Long getRoleIdByRoleName(String string);

	/**
	 * @param roleId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	String getMainMenusStringByRoleId(String roleId, String lang) throws Exception;

	/**
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	Role getRoleById(long roleId) throws Exception;

	/**
	 * @param roleNames
	 * @return
	 */
	List<Long> getRoleIdsByRoleNames(List<String> roleNames);
}
