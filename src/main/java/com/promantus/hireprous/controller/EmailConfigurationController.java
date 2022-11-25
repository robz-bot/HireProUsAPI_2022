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
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.EmailConfDto;
import com.promantus.hireprous.service.EmailConfigurationService;
import com.promantus.hireprous.util.HireProUsUtil;


/**
 * Controller class to handle Emails related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class EmailConfigurationController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(EmailConfigurationController.class);

	@Autowired
	private EmailConfigurationService emailService;
	
	@Value("${download.path}")
	private String downloadsPath;

	/**
	 * @param emailDto
	 * @return
	 */
	@PostMapping("/addEmailConf")
	public EmailConfDto addEmailConf(@RequestBody EmailConfDto emailDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		EmailConfDto resultDto = new EmailConfDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			//To 
			if (emailDto.getTo() == null || emailDto.getTo().isEmpty()) {
				errorParam.append("To");
			}
			// Purpose.
			if (emailDto.getPurpose() == null || emailDto.getPurpose().isEmpty()) {
				errorParam.append("Purpose");
			}
			// BusinessUnit Id.
			if (emailDto.getBuId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit" : "BusinessUnit");
			}
//			// To.
//			if (emailDto.getTo() == null || emailDto.getTo().isEmpty()) {
//				errorParam.append(errorParam.length() > 0 ? ", To" : "To");
//			}
//			// Cc.
//			if (emailDto.getCc() == null || emailDto.getCc().isEmpty()) {
//				errorParam.append(errorParam.length() > 0 ? ", Cc" : "Cc");
//			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = emailService.addEmailConf(emailDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param emailDto
	 * @return
	 */
	@PutMapping("/updateEmailConf")
	public EmailConfDto updateEmailConf(@RequestBody EmailConfDto emailDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		EmailConfDto resultDto = new EmailConfDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// EmailConf Id.
			if (emailDto.getId() == 0) {
				errorParam.append("EmailConf Conf. Id");
			}
			// Purpose.
			if (emailDto.getPurpose() == null || emailDto.getPurpose().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Purpose" : "Purpose");
			}
			// BusinessUnit Id.
			if (emailDto.getBuId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit" : "BusinessUnit");
			}
			// To.
			if (emailDto.getTo() == null || emailDto.getPurpose().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ",To" : "To");
			}
//			// Cc.
//			if (emailDto.getCc() == null || emailDto.getCc().isEmpty()) {
//				errorParam.append(errorParam.length() > 0 ? ", Cc" : "Cc");
//			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = emailService.updateEmailConf(emailDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllEmailConf")
	public List<EmailConfDto> getAllEmailConf(@RequestHeader(name = "lang", required = false) String lang) {

		List<EmailConfDto> emailsDtoList = new ArrayList<EmailConfDto>();
		try {
			emailsDtoList = emailService.getAllEmails();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return emailsDtoList;
	}

	/**
	 * @param emailId
	 * @return
	 */
	@GetMapping("/getEmailConfById/{confId}")
	public EmailConfDto getEmailConfById(@PathVariable String confId,
			@RequestHeader(name = "lang", required = false) String lang) {

		EmailConfDto emailDto = new EmailConfDto();
		try {
			emailDto = emailService.getEmailConfById(confId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return emailDto;
	}

//	/**
//	 * @param emailId
//	 * @return
//	 */
//	@GetMapping("/getEmailConfByPurpose/{purpose}")
//	public EmailConfDto getEmailConfByPurpose(@PathVariable String purpose,
//			@RequestHeader(name = "lang", required = false) String lang) {
//
//		EmailConfDto emailDto = new EmailConfDto();
//		try {
//			emailDto = emailService.getEmailConfByPurpose(purpose);
//		} catch (final Exception e) {
//			logger.error(HireProUsUtil.getErrorMessage(e));
//		}
//
//		return emailDto;
//	}

	/**
	 * @return
	 */
	@GetMapping("/getAllEmailConfByBuId/{buId}")
	public List<EmailConfDto> getAllEmailConfByBuId(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return emailService.getAllEmailConfByBuId(buId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<EmailConfDto>();
	}

	/**
	 * @param emailId
	 * @return
	 */
	@DeleteMapping("/deleteEmailConfById/{emailId}")
	public Boolean deleteEmailConfById(@PathVariable String emailId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			emailService.deleteEmailConfById(emailId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchEmailConf/{key}")
	public List<EmailConfDto> searchEmailConf(@PathVariable String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<EmailConfDto> emailsDtoList = new ArrayList<EmailConfDto>();
		try {
			emailsDtoList = emailService.searchEmailConf(key);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return emailsDtoList;
	}
	
	@PostMapping("/searchEmailConfForDownload")
	public List<EmailConfDto> searchEmailConf(@RequestBody EmailConfDto emailConfDto,
			@RequestHeader(name = "lang", required = false) String lang){
		
		try {
			return emailService.searchEmailConfForDownload(emailConfDto);
		}catch(final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}
		
		return new ArrayList<EmailConfDto>();
	}
	
	@PutMapping("/downloadEmailConfDetails")
	public void downloadEmailConfDetails(@RequestBody List<EmailConfDto> emailConfDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response){
		
		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		
		try {
			File emailConfFile = new File(downloadsPath + "HireProUs_EmailConf_details.xlsx");
			FileUtils.writeByteArrayToFile(emailConfFile, emailService.downloadEmailConfDetails(emailConfDtoList, lang));
			
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + emailConfFile.getName());
			response.setContentLength((int) emailConfFile.length());
			
			inStream = new BufferedInputStream(new FileInputStream(emailConfFile));
			outStream = new BufferedOutputStream(response.getOutputStream());
			
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			emailConfFile.deleteOnExit();
		}catch( final Exception e) {
			
			logger.error(HireProUsUtil.getErrorMessage(e));
		}finally {
			try {
				if (outStream != null) {
					outStream.flush();
				}
				if (inStream != null) {
					inStream.close();
				}
			}catch(IOException e) {
				logger.error(HireProUsUtil.getErrorMessage(e));
			}
		}
		
	}
}
