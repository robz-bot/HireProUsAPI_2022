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

import com.google.common.net.HttpHeaders;
import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.CandidateDto;
import com.promantus.hireprous.dto.CandidatesCountDto;
import com.promantus.hireprous.dto.EvaluateResumeDto;
import com.promantus.hireprous.service.CandidateService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Candidates related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class CandidateController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);

	@Autowired
	private CandidateService candidateService;

	@Value("${download.path}")
	private String downloadsPath;

	/**
	 * @param candidateDto
	 * @return
	 */
	@PostMapping("/addCandidate")
	public CandidateDto addCandidate(@RequestBody CandidateDto candidateDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		CandidateDto resultDto = new CandidateDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Job Request Number
			if (candidateDto.getJrNumber() == null || candidateDto.getJrNumber().isEmpty()) {
				errorParam.append("Job Request Number");
			}
			// First Name.
			if (candidateDto.getFirstName() == null || candidateDto.getFirstName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", First Name" : "First Name");
			}
			// Last Name.
			if (candidateDto.getLastName() == null || candidateDto.getLastName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Last Name" : "Last Name");
			}
			// Contact Number.
			if (candidateDto.getContactNumber() == null || candidateDto.getContactNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Contact Number" : "Contact Number");
			}
			// EmailConfDto.
			if (candidateDto.getEmail() == null || candidateDto.getEmail().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", EmailConfDto" : "EmailConfDto");
			}
			// Gender.
			if (candidateDto.getSex() == null || candidateDto.getSex().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Gender" : "Gender");
			}
			// SkillSet.
			if (candidateDto.getSkillSet() == null || candidateDto.getSkillSet().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", SkillSet" : "SkillSet");
			}
			// Experience.
			if (candidateDto.getExperience() == null || candidateDto.getExperience().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Experience" : "Experience");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = candidateService.addCandidate(candidateDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @param candidateDto
	 * @return
	 */
	@PutMapping("/updateCandidate")
	public CandidateDto updateCandidate(@RequestBody CandidateDto candidateDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		CandidateDto resultDto = new CandidateDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Candidate Id.
			if (candidateDto.getId() == 0) {
				errorParam.append("Candidate Id");
			}
			// Job Request Number
			if (candidateDto.getJrNumber() == null || candidateDto.getJrNumber().isEmpty()) {
				errorParam.append("Job Request Number");
			}
			// First Name.
			if (candidateDto.getFirstName() == null || candidateDto.getFirstName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", First Name" : "First Name");
			}
			// Last Name.
			if (candidateDto.getLastName() == null || candidateDto.getLastName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Last Name" : "Last Name");
			}
			// Contact Number.
			if (candidateDto.getContactNumber() == null || candidateDto.getContactNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Contact Number" : "Contact Number");
			}
			// Email.
			if (candidateDto.getEmail() == null || candidateDto.getEmail().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Email" : "Email");
			}
			// Gender.
			if (candidateDto.getSex() == null || candidateDto.getSex().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Gender" : "Gender");
			}
			// SkillSet.
			if (candidateDto.getSkillSet() == null || candidateDto.getSkillSet().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", SkillSet" : "SkillSet");
			}
			// Experience.
			if (candidateDto.getExperience() == null || candidateDto.getExperience().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Experience" : "Experience");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = candidateService.updateCandidate(candidateDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @param candidateDto
	 * @return
	 */
	@PutMapping("/updateShortlistResult")
	public CandidateDto updateInitialResult(@RequestBody CandidateDto candidateDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		CandidateDto resultDto = new CandidateDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Candidate Id.
			if (candidateDto.getId() == 0) {
				errorParam.append("Candidate Id");
			}
			// Recruitment Status
			if (candidateDto.getRecStatus() == null || candidateDto.getRecStatus().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Status" : "Status");
			}
			// Remarks.
			if (candidateDto.getRemarks() == null || candidateDto.getRemarks().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Remarks" : "Remarks");
			}
			// JR Number
			if (candidateDto.getJrNumber() == null || candidateDto.getJrNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", JR Number" : "JR Number");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = candidateService.updateRecStatus(candidateDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllCandidates")
	public List<CandidateDto> getAllCandidates(@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getAllCandidates();

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getCandidatesByJRNum/{jrNum}")
	public List<CandidateDto> getCandidatesByJRNum(@PathVariable String jrNum,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getCandidatesByJRNum(jrNum, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getCandidatesByJRNumAndRecStatus/{jrNum}/{recStatus}")
	public List<CandidateDto> getCandidatesByJRNumAndRecStatus(@PathVariable String jrNum,
			@PathVariable String recStatus, @RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getCandidatesByJRNumAndRecStatus(jrNum, recStatus, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getCandidatesByRecStatus/{recStatus}")
	public List<CandidateDto> getCandidatesByRecStatus(@PathVariable String recStatus,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getCandidatesByRecStatus(recStatus, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @return
	 */
	@PutMapping("/getCandidatesByRecStatusList")
	public List<CandidateDto> getCandidatesByRecStatusList(@RequestBody List<String> recStatusList,
			@RequestParam(name = "vendorId", required = false) String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getCandidatesByRecStatusList(recStatusList, vendorId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getCandidatesCount")
	public CandidatesCountDto getCandidatesCount(@RequestParam(name = "vendorId", required = false) String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getCandidatesCount(vendorId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new CandidatesCountDto();
	}

	/**
	 * @return
	 */
	@GetMapping("/getCandidatesCountVendorVsJR")
	public CandidatesCountDto getCandidatesCountVendorVsJR(
			@RequestParam(name = "vendorId", required = false) String vendorId,
			@RequestParam(name = "jrNumber", required = false) String jrNumber,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getCandidatesCountVendorVsJr(vendorId, jrNumber, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new CandidatesCountDto();
	}

	/**
	 * @return
	 */
	@PutMapping("/getCandidatesByJRNumAndRecStatusList/{jrNum}")
	public List<CandidateDto> getCandidatesByJRNumAndRecStatusList(@PathVariable String jrNum,
			@RequestBody List<String> recStatusList, @RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getCandidatesByJRNumAndRecStatusList(jrNum, recStatusList, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchCandidate")
	public List<CandidateDto> searchCandidate(@RequestParam String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.searchCandidate(key);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @param key
	 * @return
	 */
	@PutMapping("/searchCandidateByRecStatusList")
	public List<CandidateDto> searchCandidateByRecStatusList(@RequestParam String name, @RequestParam String jrNumber,
			@RequestBody List<String> recStatusList, @RequestParam(name = "vendorId", required = false) String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.searchCandidateByRecStatusList(name, jrNumber, recStatusList, vendorId);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @param candidateId
	 * @return
	 */
	@GetMapping("/getCandidate/{candidateId}")
	public CandidateDto getCandidateById(@PathVariable String candidateId,
			@RequestHeader(name = "lang", required = false) String lang) {

		CandidateDto candidateDto = new CandidateDto();
		try {
			candidateDto = candidateService.getCandidateById(candidateId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return candidateDto;
	}

	/**
	 * @param candidateId
	 * @return
	 */
	@DeleteMapping("/deleteCandidateById/{candidateId}")
	public CandidateDto deleteCandidateById(@PathVariable String candidateId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return candidateService.deleteCandidateById(candidateId);

		} catch (final Exception e) {

			CandidateDto resultDto = new CandidateDto();
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(HireProUsUtil.getErrorMessage(e));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
	}

	/**
	 * @param candidateDtoList
	 * @param lang
	 * @param response
	 */
	@PutMapping("/downloadSelectedCandidateDetails")
	public void downloadVendorDetails(@RequestBody List<CandidateDto> candidateDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File candidateFile = new File(downloadsPath + "HireProUs_Vendor_details.xlsx");
			FileUtils.writeByteArrayToFile(candidateFile,
					candidateService.downloadSelectedCandidateDetails(candidateDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + candidateFile.getName());
			response.setContentLength((int) candidateFile.length());

			inStream = new BufferedInputStream(new FileInputStream(candidateFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			candidateFile.deleteOnExit();

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

	/**
	 * @param candidateDto
	 * @return
	 */
	@PostMapping("/searchCandidateForDownload")
	public List<CandidateDto> searchCandidateForDownload(@RequestBody CandidateDto candidateDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.searchCandidateForDownload(candidateDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @param candidateDto
	 * @return
	 */
	@PostMapping("/searchRejectedCandidateForDownload")
	public List<CandidateDto> searchRejectedCandidateForDownload(@RequestBody CandidateDto candidateDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.searchRejectedCandidateForDownload(candidateDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	/**
	 * @param candidateDtoList
	 * @param lang
	 * @param response
	 */
	@PutMapping("/downloadRejectedCandidateDetails")
	public void downloadRejectedCandidateDetails(@RequestBody List<CandidateDto> candidateDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File candidateFile = new File(downloadsPath + "HireProUs_rejctedCandidate_details.xlsx");
			FileUtils.writeByteArrayToFile(candidateFile,
					candidateService.downloadRejectedCandidateDetails(candidateDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + candidateFile.getName());
			response.setContentLength((int) candidateFile.length());

			inStream = new BufferedInputStream(new FileInputStream(candidateFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			candidateFile.deleteOnExit();

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

	/**
	 * @param candidateDtoList
	 * @param lang
	 * @param response
	 */
	@PutMapping("/downloadCandidateDetails")
	public void downloadCandidateDetails(@RequestBody List<CandidateDto> candidateDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File candidateFile = new File(downloadsPath + "HireProUs_Candidate_details.xlsx");
			FileUtils.writeByteArrayToFile(candidateFile,
					candidateService.downloadCandidateDetails(candidateDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + candidateFile.getName());
			response.setContentLength((int) candidateFile.length());

			inStream = new BufferedInputStream(new FileInputStream(candidateFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			candidateFile.deleteOnExit();

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

	/**
	 * @param candidateDto
	 * @return
	 */
	@PostMapping("/searchAllCandidateForDownload")
	public List<CandidateDto> searchAllCandidateForDownload(@RequestBody CandidateDto candidateDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.searchAllCandidateForDownload(candidateDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateDto>();
	}

	// Added on 1/5/2022
	@PostMapping("/rejectToUploadedStatus/{jrNumber}")
	public CandidateDto rejectToUploadedStatus(@RequestBody CandidateDto candidateDto,  @PathVariable String jrNumber,
			@RequestHeader(name = "lang", required = false) String lang) {

		CandidateDto resultDto = new CandidateDto();
		try {
			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// jrNumber.
			if (jrNumber == null || jrNumber.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", JR Number" : "JR Number");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = candidateService.rejectToUploadedStatus(candidateDto,jrNumber, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}
	
	@GetMapping("/getEvaluateResume/{jrNumber}/{candidateId}")
	public EvaluateResumeDto getEvaluateResume(@PathVariable String jrNumber,
			@PathVariable String candidateId,@RequestHeader(name = "lang", required = false) String lang) {

		EvaluateResumeDto evaluateResumeDto = new EvaluateResumeDto();
		try {
			evaluateResumeDto = candidateService.getEvaluateResume(jrNumber,candidateId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return evaluateResumeDto;
	}

}
