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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.RecruitmentRoleDto;
import com.promantus.hireprous.service.RecruitmentRoleService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle RecruitmentRoles related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class RecruitmentRoleController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(RecruitmentRoleController.class);

	@Autowired
	private RecruitmentRoleService recruitmentRoleService;
	
	@Value("${download.path}")
	private String downloadsPath;

	/**
	 * @param recruitmentRoleDto
	 * @return
	 */
	@PostMapping("/addRecruitmentRole")
	public RecruitmentRoleDto addRecruitmentRole(@RequestBody RecruitmentRoleDto recruitmentRoleDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		RecruitmentRoleDto resultDto = new RecruitmentRoleDto();

		try {

			if (recruitmentRoleDto.getRecruitmentRoleName() == null
					|| recruitmentRoleDto.getRecruitmentRoleName().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { "Recruitment Role Name" }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = recruitmentRoleService.addRecruitmentRole(recruitmentRoleDto, lang);
		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param recRoleName
	 * @param lang
	 * @return
	 */
	@PostMapping("/checkRecRoleName")
	public Boolean checkRecRoleName(@RequestBody String recRoleName,
			@RequestHeader(name = "lang", required = false) String lang) {
		try {
			return recruitmentRoleService.checkRecRoleName(recRoleName);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param recruitmentRoleDto
	 * @return
	 */
	@PutMapping("/updateRecruitmentRole")
	public RecruitmentRoleDto updateRecruitmentRole(@RequestBody RecruitmentRoleDto recruitmentRoleDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		RecruitmentRoleDto resultDto = new RecruitmentRoleDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Recruitment Role Id.
			if (recruitmentRoleDto.getId() == 0) {
				errorParam.append("Role Id");
			}
			// Recruitment Role Name.
			if (recruitmentRoleDto.getRecruitmentRoleName() == null
					|| recruitmentRoleDto.getRecruitmentRoleName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Recruitment Role Name" : "Recruitment Role Name");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = recruitmentRoleService.updateRecruitmentRole(recruitmentRoleDto, lang);

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
	@GetMapping("/getAllRecruitmentRoles")
	public List<RecruitmentRoleDto> getAllRecruitmentRoles(
			@RequestHeader(name = "lang", required = false) String lang) {

		List<RecruitmentRoleDto> recruitmentRolesDtoList = new ArrayList<RecruitmentRoleDto>();
		try {
			recruitmentRolesDtoList = recruitmentRoleService.getAllRecruitmentRoles();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return recruitmentRolesDtoList;
	}

	/**
	 * @param recruitmentRoleId
	 * @return
	 */
	@GetMapping("/getRecruitmentRole/{recruitmentRoleId}")
	public RecruitmentRoleDto getRecruitmentRoleById(@PathVariable String recruitmentRoleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		RecruitmentRoleDto recruitmentRoleDto = new RecruitmentRoleDto();
		try {
			recruitmentRoleDto = recruitmentRoleService.getRecruitmentRoleById(recruitmentRoleId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return recruitmentRoleDto;
	}

	/**
	 * @param recruitmentRoleId
	 * @return
	 */
	@DeleteMapping("/deleteRecruitmentRoleById/{recruitmentRoleId}")
	public Boolean deleteRecruitmentRoleById(@PathVariable String recruitmentRoleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			recruitmentRoleService.deleteRecruitmentRoleById(recruitmentRoleId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchRecruitmentRole")
	public List<RecruitmentRoleDto> searchRecruitmentRole(@RequestParam String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<RecruitmentRoleDto> recruitmentRolesDtoList = new ArrayList<RecruitmentRoleDto>();
		try {
			recruitmentRolesDtoList = recruitmentRoleService.searchRecruitmentRole(key);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return recruitmentRolesDtoList;
	}
	
	/**
	 * @param key
	 * @return
	 */
	@PostMapping("/searchRecruitmentRoleForDownload")
	public List<RecruitmentRoleDto> searchRecruitmentRoleForDownload(@RequestBody RecruitmentRoleDto recruitmentRoleDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return recruitmentRoleService.searchRecruitmentRoleForDownload(recruitmentRoleDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<RecruitmentRoleDto>();
	}

	
	/**
	 * @param recruitmentRoleDtoList
	 * @param lang
	 * @param response
	 */
	@PutMapping("/downloadRecruitmentRoleDetails")
	public void downloadRecruitmentRoleDetails(@RequestBody List<RecruitmentRoleDto> recruitmentRoleDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File recruitmentRoleFile = new File(downloadsPath + "HireProUs_RecruitmentRole_details.xlsx");
			FileUtils.writeByteArrayToFile(recruitmentRoleFile,
					recruitmentRoleService.downloadRecruitmentRoleDetails(recruitmentRoleDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + recruitmentRoleFile.getName());
			response.setContentLength((int) recruitmentRoleFile.length());

			inStream = new BufferedInputStream(new FileInputStream(recruitmentRoleFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			recruitmentRoleFile.deleteOnExit();

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
	}

}
