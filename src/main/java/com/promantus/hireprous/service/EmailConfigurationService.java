/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.EmailConfDto;

/**
 * @author Sihab.
 *
 */
public interface EmailConfigurationService {

	/**
	 * @param emailDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	EmailConfDto addEmailConf(final EmailConfDto emailDto, final String lang) throws Exception;

	/**
	 * @param emailDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	EmailConfDto updateEmailConf(final EmailConfDto emailDto, final String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<EmailConfDto> getAllEmails() throws Exception;

	/**
	 * @param emailId
	 * @return
	 * @throws Exception
	 */
	EmailConfDto getEmailConfById(String emailId) throws Exception;

	/**
	 * @param emailId
	 * @throws Exception
	 */
	void deleteEmailConfById(String emailId) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<EmailConfDto> searchEmailConf(String keyword) throws Exception;

	/**
	 * @param purpose
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	Boolean checkPurpose(String purpose, long buId) throws Exception;

	/**
	 * @param buId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<EmailConfDto> getAllEmailConfByBuId(String buId, String lang) throws Exception;

	/**
	 * @param purpose
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	EmailConfDto getEmailConfByPurposeAndBuId(String purpose, Long buId) throws Exception;

	/**
	 * @param buId
	 * @return
	 */
	int getBUDependencyCount(Long buId);
	/**
	* @param emailConfDtoList
	* @param lang
	* @return
	* @throws Exception
	*/
	byte[] downloadEmailConfDetails(List<EmailConfDto> emailConfDtoList, String lang) throws Exception;



	/**
	* @param emailConfDto
	* @return
	* @throws Exception
	*/
	List<EmailConfDto> searchEmailConfForDownload(EmailConfDto emailConfDto) throws Exception;

}
