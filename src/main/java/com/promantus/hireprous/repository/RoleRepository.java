/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Role;

/**
 * @author Sihab.
 *
 */
public interface RoleRepository extends MongoRepository<Role, String> {

	/**
	 * @param roleName
	 * @return
	 */
	List<Role> findByRoleNameRegex(String roleName, Sort sort);

	/**
	 * @param roleId
	 * @return
	 */
	Role findById(long roleId);

	/**
	 * @param roleName
	 * @return
	 */
	Role getRoleByRoleName(String roleName);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param roleNames
	 * @return
	 */
	List<Role> findByRoleNameIn(List<String> roleNames);

	/**
	 * @param roleName
	 * @return
	 */
	Role getRoleByRoleNameIgnoreCase(String roleName);

}
