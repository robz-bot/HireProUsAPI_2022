/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.dto.UserSearchDto;

/**
 * @author Sihab.
 *
 */
public interface UserService {

	/**
	 * @param userDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	UserDto addUser(final UserDto userDto, String lang) throws Exception;

	/**
	 * @param userDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	UserDto updateUser(final UserDto userDto, final String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getAllUsers() throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserDto getUserById(String userId) throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	UserDto deleteUserById(String userId) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<UserDto> searchUser(String keyword) throws Exception;

	/**
	 * @param userSearchDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<UserDto> searchUser(UserSearchDto userSearchDto, String lang) throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getUserNameById(long userId) throws Exception;

	/**
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getUsersByBusinessUnitId(String buId) throws Exception;

	/**
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getUsersByIdList(List<String> userIds) throws Exception;

	/**
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	Boolean checkUserName(String userName) throws Exception;

	/**
	 * @param buId
	 * @param userIds
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	UserDto updatePanelUsers(String buId, List<String> userIds, String lang) throws Exception;

	/**
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getPanelUsersByBusinessUnitId(String buId) throws Exception;

	/**
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	String getPanelUsersStringByBusinessUnitId(String buId) throws Exception;

	/**
	 * @param buId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getAllUsersByBU(String buId, String lang) throws Exception;

	/**
	 * @param roleId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getAllUsersByRole(String roleId, String lang) throws Exception;

	/**
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getRecruiters(String lang) throws Exception;

	/**
	 * @param buId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<UserDto> getInterviewersByBuId(String buId, String lang) throws Exception;

	/**
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	List<String> getPanelUsersIdsByBuId(String buId) throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getEmailIdById(long userId) throws Exception;

	/**
	 * @param buId
	 * @return
	 */
	int getBUDependencyCount(Long buId);

	List<Long> searchUsersIdsByName(String interviewerName)  throws Exception;

}
