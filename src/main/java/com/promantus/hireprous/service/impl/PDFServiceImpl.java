/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.util.Base64;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.PDFDto;
import com.promantus.hireprous.dto.ResumeDto;
import com.promantus.hireprous.entity.PDF;
import com.promantus.hireprous.entity.Resume;
import com.promantus.hireprous.repository.PDFRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.PDFService;
import com.promantus.hireprous.service.ResumeService;

/**
 * @author Sihab.
 *
 */
@Service
public class PDFServiceImpl implements PDFService {

	private static final Logger logger = LoggerFactory.getLogger(PDFServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	PDFRepository pdfRepository;

	@Override
	public PDFDto uploadPDF(final String resumeName, final String resumeType, final MultipartFile resumeFile,
			final String lang) throws Exception {

		PDFDto resultDto = new PDFDto();

		PDF resume = pdfRepository.findByPdfName(resumeName);

		if (resume == null) {

			PDF resume1 = new PDF();
			resume1.setPdfName(resumeName);
			resume1.setResumeType(resumeType);
			resume1.setResume(new Binary(BsonBinarySubType.BINARY, resumeFile.getBytes()));

			pdfRepository.insert(resume1);

			logger.info("Resume Added.");

		} else {

			resume.setResumeType(resumeType);
			resume.setResume(new Binary(BsonBinarySubType.BINARY, resumeFile.getBytes()));

			pdfRepository.save(resume);

			logger.info("Resume Updated.");
		}

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}
//
	@Override
	public PDFDto getPDFByName(String resumeName) throws Exception {

		PDFDto resumeDto = new PDFDto();

		PDF resume = pdfRepository.findByPdfName(resumeName);
		if (resume != null) {

			resumeDto.setId(resume.getId());
			resumeDto.setResumeName(resume.getPdfName());
			resumeDto.setResumeType(resume.getResumeType());
			resumeDto.setResumeBinary(resume.getResume());

			resumeDto.setResume(HireProUsConstants.BASE64_FILE_PREFIX.replace("[fileType]", resume.getResumeType())
					+ Base64.getEncoder().encodeToString(resume.getResume().getData()));
		}

		return resumeDto;
	}

	@Override
	public void deletePDFByName(final String resumeName) throws Exception {

	

	

		pdfRepository.deleteByPdfName(resumeName);
	}
	@Override
	public List<PDF> getAllPDF() {
		
		return pdfRepository.findAll();
	}
}
