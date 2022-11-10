/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.User;

/**
 * @author Sihab.
 *
 */
public interface UserRepository extends MongoRepository<User, String> {

	/**
	 * @param userId
	 * @return
	 */
	User findById(long userId);

	/**
	 * @param roleName
	 * @return
	 */
	List<User> findByEmailRegex(String roleName, Sort sort);

	/**
	 * @param userName
	 * @return
	 */
	User findByEmail(String userName);

	/**
	 * @param buId
	 * @return
	 */
	List<User> getUserByBusinessUnitId(long buId, Sort sort);

	/**
	 * @param roleId
	 * @param sort
	 * @return
	 */
	List<User> getUserByRoleId(long roleId, Sort sort);

	/**
	 * @param list
	 * @return
	 */
	List<User> findAllByIdIn(List<Long> list, Sort sort);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param firstName
	 * @param lastName
	 * @param sort
	 * @return
	 */
	List<User> findByFirstNameRegexOrLastNameRegex(String firstName, String lastName, Sort sort);

	/**
	 * @param parseLong
	 * @param panelMemberYes
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<User> getUserByBusinessUnitIdAndPanelMember(long parseLong, String panelMemberYes,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param roleIds
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<User> getUserByRoleIdIn(List<Long> roleIds, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param userName
	 * @return
	 */
	User findByEmailIgnoreCase(String userName);

	/**
	 * @param buId
	 * @return
	 */
	int countByBusinessUnitId(Long buId);

}
