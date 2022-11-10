/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.OnboardDto;
import com.promantus.hireprous.dto.OnboardSearchDto;

/**
 * @author Sihab.
 *
 */
public interface OnboardService {

	/**
	 * @param onboardDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	OnboardDto addOnboard(final OnboardDto onboardDto, String lang) throws Exception;

	/**
	 * @param onboardDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	OnboardDto updateOnboard(final OnboardDto onboardDto, String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<OnboardDto> getAllOnboards() throws Exception;

	/**
	 * @param onboardId
	 * @return
	 * @throws Exception
	 */
	OnboardDto getOnboardById(String onboardId) throws Exception;

	/**
	 * @param onboardId
	 * @throws Exception
	 */
	void deleteOnboardById(String onboardId) throws Exception;

	/**
	 * @param onboardSearchDto
	 * @return
	 * @throws Exception
	 */
	List<OnboardDto> searchOnboard(OnboardSearchDto onboardSearchDto) throws Exception;

	/**
	 * @param id
	 * @return
	 */
	int getProjectDependencyCount(Long projectId);

}
