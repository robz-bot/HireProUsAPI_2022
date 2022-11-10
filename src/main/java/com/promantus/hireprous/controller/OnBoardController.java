/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.promantus.hireprous.dto.OnboardDto;
import com.promantus.hireprous.dto.OnboardSearchDto;
import com.promantus.hireprous.service.OnboardService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Onboards related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class OnBoardController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(OnBoardController.class);

	@Autowired
	private OnboardService onboardService;

	/**
	 * @param onboardDto
	 * @return
	 */
	@PostMapping("/addOnboard")
	public OnboardDto addOnboard(@RequestBody OnboardDto onboardDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		OnboardDto resultDto = new OnboardDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Job Request Number.
			if (onboardDto.getJrNumber() == null || onboardDto.getJrNumber().isEmpty()) {
				errorParam.append("Job Request Number");
			}
			// Candidate Id.
			if (onboardDto.getCandidateId() == null || onboardDto.getCandidateId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Candidate Id" : "Candidate Id");
			}
			// Date of joining.
			if (onboardDto.getJoiningDate() == null) {
				errorParam.append(errorParam.length() > 0 ? ", Date of joining" : "Date of joining");
			}
			// Email.
			if (onboardDto.getEmail() == null || onboardDto.getEmail().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Email" : "Email");
			}
			// EmployeeIdByHR.
			if (onboardDto.getEmployeeIdByHR() == null || onboardDto.getEmployeeIdByHR().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", EmployeeId ByHR" : "EmployeeId ByHR");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = onboardService.addOnboard(onboardDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param onboardDto
	 * @return
	 */
	@PutMapping("/updateOnboard")
	public OnboardDto updateOnboard(@RequestBody OnboardDto onboardDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		OnboardDto resultDto = new OnboardDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Onboard Id.
			if (onboardDto.getId() == 0) {
				errorParam.append("Onboard Id");
			}
			// Job Request Number.
			if (onboardDto.getJrNumber() == null || onboardDto.getJrNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Job Request Number" : "Job Request Number");
			}
			// Candidate Id.
			if (onboardDto.getCandidateId() == null || onboardDto.getCandidateId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Candidate Id" : "Candidate Id");
			}
			// Date of joining.
			if (onboardDto.getJoiningDate() == null) {
				errorParam.append(errorParam.length() > 0 ? ", Date of joining" : "Date of joining");
			}
			// Email.
			if (onboardDto.getEmail() == null || onboardDto.getEmail().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Email" : "Email");
			}
			// EmployeeIdByHR.
			if (onboardDto.getEmployeeIdByHR() == null || onboardDto.getEmployeeIdByHR().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", EmployeeId ByHR" : "EmployeeId ByHR");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = onboardService.updateOnboard(onboardDto, lang);

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
	@GetMapping("/getAllOnboards")
	public List<OnboardDto> getAllOnboards(@RequestHeader(name = "lang", required = false) String lang) {

		List<OnboardDto> onboardsDtoList = new ArrayList<OnboardDto>();
		try {
			onboardsDtoList = onboardService.getAllOnboards();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return onboardsDtoList;
	}

	/**
	 * @param onboardId
	 * @return
	 */
	@GetMapping("/getOnboard/{onboardId}")
	public OnboardDto getOnboardById(@PathVariable String onboardId,
			@RequestHeader(name = "lang", required = false) String lang) {

		OnboardDto onboardDto = new OnboardDto();
		try {
			onboardDto = onboardService.getOnboardById(onboardId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return onboardDto;
	}

	/**
	 * @param onboardId
	 * @return
	 */
	@DeleteMapping("/deleteOnboardById/{onboardId}")
	public Boolean deleteOnboardById(@PathVariable String onboardId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			onboardService.deleteOnboardById(onboardId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @return
	 */
	@PutMapping("/searchOnboard")
	public List<OnboardDto> searchOnboard(@RequestBody OnboardSearchDto onboardSearchDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<OnboardDto> onboardDtoList = new ArrayList<OnboardDto>();
		try {
			onboardDtoList = onboardService.searchOnboard(onboardSearchDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return onboardDtoList;
	}
}
