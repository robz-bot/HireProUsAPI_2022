/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.util.Base64;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.ResumeDto;
import com.promantus.hireprous.entity.Resume;
import com.promantus.hireprous.repository.ResumeRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.ResumeService;

/**
 * @author Sihab.
 *
 */
@Service
public class ResumeServiceImpl implements ResumeService {

	private static final Logger logger = LoggerFactory.getLogger(ResumeServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	ResumeRepository resumeRepository;

	@Override
	public ResumeDto uploadResume(final String resumeName, final String resumeType, final MultipartFile resumeFile,
			final String lang) throws Exception {

		ResumeDto resultDto = new ResumeDto();

		Resume resume = resumeRepository.getResumeByResumeName(resumeName);

		if (resume == null) {

			resume = new Resume();
			resume.setResumeName(resumeName);
			resume.setResumeType(resumeType);
			resume.setResume(new Binary(BsonBinarySubType.BINARY, resumeFile.getBytes()));

			resumeRepository.insert(resume);

			logger.info("Resume Added.");

		} else {

			resume.setResumeType(resumeType);
			resume.setResume(new Binary(BsonBinarySubType.BINARY, resumeFile.getBytes()));

			resumeRepository.save(resume);

			logger.info("Resume Updated.");
		}

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public ResumeDto getResumeByName(String resumeName) throws Exception {

		ResumeDto resumeDto = new ResumeDto();

		Resume resume = resumeRepository.getResumeByResumeName(resumeName);
		if (resume != null) {

			resumeDto.setId(resume.getId());
			resumeDto.setResumeName(resume.getResumeName());
			resumeDto.setResumeType(resume.getResumeType());
			resumeDto.setResumeBinary(resume.getResume());

			resumeDto.setResume(HireProUsConstants.BASE64_FILE_PREFIX.replace("[fileType]", resume.getResumeType())
					+ Base64.getEncoder().encodeToString(resume.getResume().getData()));
		}

		return resumeDto;
	}

	@Override
	public void deleteResumeByName(final String resumeName) throws Exception {

		resumeRepository.deleteByResumeName(resumeName);
	}
}
