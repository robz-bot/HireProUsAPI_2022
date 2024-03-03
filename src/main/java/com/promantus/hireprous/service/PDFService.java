/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.promantus.hireprous.dto.PDFDto;
import com.promantus.hireprous.dto.ResumeDto;
import com.promantus.hireprous.entity.PDF;

/**
 * @author Sihab.
 *
 */
public interface PDFService {

	/**
	 * @param resumeName
	 * @param resumeType
	 * @param resumeFile
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	PDFDto uploadPDF(String resumeName, String resumeType, MultipartFile resumeFile, String lang)
			throws Exception;
//
//	/**
//	 * @param resumeName
//	 * @return
//	 * @throws Exception
//	 */
	PDFDto getPDFByName(String resumeName) throws Exception;
//
//	/**
//	 * @param resumeId
//	 * @throws Exception
//	 */
	void deletePDFByName(String resumeId) throws Exception;
	List<PDF> getAllPDF();

}
