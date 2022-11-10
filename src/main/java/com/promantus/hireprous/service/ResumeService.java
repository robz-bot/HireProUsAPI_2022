/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import org.springframework.web.multipart.MultipartFile;

import com.promantus.hireprous.dto.ResumeDto;

/**
 * @author Sihab.
 *
 */
public interface ResumeService {

	/**
	 * @param resumeName
	 * @param resumeType
	 * @param resumeFile
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	ResumeDto uploadResume(String resumeName, String resumeType, MultipartFile resumeFile, String lang)
			throws Exception;

	/**
	 * @param resumeName
	 * @return
	 * @throws Exception
	 */
	ResumeDto getResumeByName(String resumeName) throws Exception;

	/**
	 * @param resumeId
	 * @throws Exception
	 */
	void deleteResumeByName(String resumeId) throws Exception;

}
