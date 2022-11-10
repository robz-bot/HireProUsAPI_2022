/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.ResumeDto;
import com.promantus.hireprous.service.ResumeService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Resume related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ResumeController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

	@Autowired
	private ResumeService resumeService;

	@Value("${resume-file-path}")
	private String resumePath;

	/**
	 * @param resumeName
	 * @param resumeType
	 * @param resume
	 * @param lang
	 * @return
	 */
	@PostMapping("/uploadResume/{resumeName}/{resumeType}")
	public ResumeDto uploadResume(@PathVariable String resumeName, @PathVariable String resumeType,
			@RequestParam("resume") MultipartFile resume, @RequestHeader(name = "lang", required = false) String lang) {

		logger.info("Upload Resume is started.");

		ResumeDto resultDto = new ResumeDto();
		try {
			resultDto = resumeService.uploadResume(resumeName, resumeType, resume, lang);
		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		logger.info("Upload Resume is completed.");
		return resultDto;
	}

	/**
	 * @param resumeName
	 * @param lang
	 * @return
	 */
	@GetMapping("/getResume/{resumeName}")
	public ResumeDto getResume(@PathVariable String resumeName,
			@RequestHeader(name = "lang", required = false) String lang) {

		logger.info("Get Resume is started.");
		ResumeDto resultDto = new ResumeDto();
		try {

			resultDto = resumeService.getResumeByName(resumeName);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		logger.info("Get Resume is completed.");
		return resultDto;
	}

	/**
	 * @param resumeName
	 * @param lang
	 * @return
	 */
	@GetMapping("/downloadResume/{resumeName}")
	public void downloadResume(@PathVariable String resumeName, HttpServletResponse resonse,
			@RequestHeader(name = "lang", required = false) String lang) {

		logger.info("Download Resume is started.");
		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			ResumeDto resultDto = resumeService.getResumeByName(resumeName);

			File resumeFile = new File(resumePath + resultDto.getResumeName());
			FileUtils.writeByteArrayToFile(resumeFile, resultDto.getResumeBinary().getData());

			resonse.setContentType(Files.probeContentType(resumeFile.toPath()));
			resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resumeFile.getName());
			resonse.setContentLength((int) resumeFile.length());

			inStream = new BufferedInputStream(new FileInputStream(resumeFile));
			outStream = new BufferedOutputStream(resonse.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

		} catch (final Exception e) {

			logger.error(HireProUsUtil.getErrorMessage(e));

		} finally {
			try {
				if (outStream != null) {
					outStream.flush();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				logger.error(HireProUsUtil.getErrorMessage(e));
			}
		}

		logger.info("Download Resume is completed.");
	}

	/**
	 * @param resumeName
	 * @param lang
	 * @return
	 */
	@DeleteMapping("/deleteResume/{resumeName}")
	public Boolean deleteResume(@PathVariable String resumeName,
			@RequestHeader(name = "lang", required = false) String lang) {

		logger.info("Delete Resume is started.");

		try {
			resumeService.deleteResumeByName(resumeName);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
			return false;
		}

		logger.info("Delete Resume is completed.");
		return true;
	}
}
