/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.RecruitmentRoleDto;

/**
 * @author Sihab.
 *
 */
public interface RecruitmentRoleService {

	/**
	 * @param recruitmentRoleDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	RecruitmentRoleDto addRecruitmentRole(final RecruitmentRoleDto recruitmentRoleDto, final String lang)
			throws Exception;

	/**
	 * @param recruitmentRoleDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	RecruitmentRoleDto updateRecruitmentRole(final RecruitmentRoleDto recruitmentRoleDto, final String lang)
			throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<RecruitmentRoleDto> getAllRecruitmentRoles() throws Exception;

	/**
	 * @param recruitmentRoleId
	 * @return
	 * @throws Exception
	 */
	RecruitmentRoleDto getRecruitmentRoleById(String recruitmentRoleId) throws Exception;

	/**
	 * @param recruitmentRoleId
	 * @throws Exception
	 */
	void deleteRecruitmentRoleById(String recruitmentRoleId) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<RecruitmentRoleDto> searchRecruitmentRole(String keyword) throws Exception;

	/**
	 * @param recruitmentRoleId
	 * @return
	 * @throws Exception
	 */
	String getRecruitmentRoleNameById(long recruitmentRoleId) throws Exception;

	/**
	 * @param recRoleName
	 * @return
	 * @throws Exception
	 */
	Boolean checkRecRoleName(String recRoleName) throws Exception;

	/**
	 * @param recruitmentRoleIds
	 * @return
	 * @throws Exception
	 */
	String getRecruitmentRoleNameByIds(List<Long> recruitmentRoleIds) throws Exception;

	/**
	 * @param recruitmentRoleIds
	 * @return
	 * @throws Exception
	 */
	List<RecruitmentRoleDto> getRecruitmentRolesByIds(List<Long> recruitmentRoleIds) throws Exception;
	
	/**
	* @param recruitmentRoleDtoList
	* @param lang
	* @return
	* @throws Exception
	*/
	byte[] downloadRecruitmentRoleDetails(List<RecruitmentRoleDto> recruitmentRoleDtoList, String lang) throws Exception;



	/**
	* @param recruitmentRoleDto
	* @return
	* @throws Exception
	*/
	List<RecruitmentRoleDto> searchRecruitmentRoleForDownload(RecruitmentRoleDto recruitmentRoleDto) throws Exception;

}
