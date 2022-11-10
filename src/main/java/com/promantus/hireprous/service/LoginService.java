/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import com.promantus.hireprous.dto.UserDto;

/**
 * @author Sihab.
 *
 */
public interface LoginService {

	/**
	 * @param userName
	 * @param password
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	UserDto login(final String userName, final String password, final String lang) throws Exception;

	/**
	 * @param userDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	UserDto changePassword(final UserDto userDto, final String lang) throws Exception;

	/**
	 * @param userDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	UserDto updateProfile(UserDto userDto, final String lang) throws Exception;

	/**
	 * @param userName
	 * @param password
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	UserDto loginsa(String userName, String password, String lang) throws Exception;

	/**
	 * @param email
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	UserDto checkEmailAndSendOtp(String email, String lang) throws Exception;

	/**
	 * @param userDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	UserDto resetPassword(UserDto userDto, String lang) throws Exception;

}
