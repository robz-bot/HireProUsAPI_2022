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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.dto.BUsCountDto;
import com.promantus.hireprous.dto.CandidateStatusDto;
import com.promantus.hireprous.dto.JobRequestAgingCountDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.JobRequestStagesCountDto;
import com.promantus.hireprous.dto.WidgetDto;
import com.promantus.hireprous.service.CandidateService;
import com.promantus.hireprous.service.DashboardService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Dashboard related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class DashboardController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CandidateService candidateService;

	@GetMapping("/getWidgetData/{userId}")
	public List<WidgetDto> getWidgetData(@PathVariable String userId,
			@RequestParam(name = "vendorId", required = false) String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			if (vendorId == null || vendorId.isEmpty()) {
			
				return dashboardService.getWidgetData(userId, lang);
			} else {
				
				return dashboardService.getWidgetDataForVendor(vendorId, lang);
			}
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<WidgetDto>();
	}

	@GetMapping("/getBUsAndJobRequestCount")
	public List<BUsCountDto> getBUsAndJobRequestCount(
			@RequestParam(name = "vendorId", required = false) String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			if (vendorId == null || vendorId.isEmpty()) {
				return dashboardService.getBUsAndJobRequestCount(lang);
			} else {
				return dashboardService.getBUsAndJobRequestCountForVendor(vendorId, lang);
			}
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<BUsCountDto>();
	}

	@GetMapping("/getJobRequestAgingCount")
	public JobRequestAgingCountDto getJobRequestAgingCount(
			@RequestParam(name = "vendorId", required = false) String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			if (vendorId == null || vendorId.isEmpty()) {
				return dashboardService.getJobRequestAgingCount(lang);
			} else {
				return dashboardService.getJobRequestAgingCountForVendor(vendorId, lang);
			}
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new JobRequestAgingCountDto();
	}

	@GetMapping("/getJobRequestAgingCount/{buId}")
	public JobRequestAgingCountDto getJobRequestAgingCount(@PathVariable String buId,
			@RequestParam(name = "vendorId", required = false) String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			if (vendorId == null || vendorId.isEmpty()) {
				return dashboardService.getJobRequestAgingCountByBuId(buId, lang);
			} else {
				return dashboardService.getJobRequestAgingCountByBuIdForVendor(buId, vendorId, lang);
			}
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new JobRequestAgingCountDto();
	}

	@GetMapping("/getAllJobRequestStagesCount")
	public JobRequestStagesCountDto getAllJobRequestStagesCount(
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return dashboardService.getAllJobRequestStagesCount(lang);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new JobRequestStagesCountDto();
	}

	@GetMapping("/getAllJobRequestStagesCountByBuId/{buId}")
	public JobRequestStagesCountDto getAllJobRequestStagesCountByBuId(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return dashboardService.getAllJobRequestStagesCountByBuId(buId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new JobRequestStagesCountDto();
	}

	@GetMapping("/getLatestJobRequests")
	public List<JobRequestDto> getLatestJobRequests(@RequestParam(name = "vendorId", required = false) String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			if (vendorId == null || vendorId.isEmpty()) {
				return dashboardService.getLatestJobRequests(lang);
			} else {
				return dashboardService.getLatestJobRequestsForVendor(vendorId, lang);
			}
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

//	@GetMapping("/getAllCandidateStages")
//	public List<CandidateStatusDto> getAllCandidateStages(@RequestHeader(name = "lang", required = false) String lang) {
//
//		try {
//			return candidateService.getAllCandidateStages(lang);
//
//		} catch (final Exception e) {
//			logger.error(HireProUsUtil.getErrorMessage(e));
//		}
//
//		return new ArrayList<CandidateStatusDto>();
//	}
//
//	@GetMapping("/getAllCandidateStagesByJrNumber/{jrNumber}")
//	public List<CandidateStatusDto> getAllCandidateStatusByJrNumber(@PathVariable String jrNumber,
//			@RequestHeader(name = "lang", required = false) String lang) {
//
//		try {
//			return candidateService.getCandidatesStatusByJRNum(jrNumber, lang);
//
//		} catch (final Exception e) {
//			logger.error(HireProUsUtil.getErrorMessage(e));
//		}
//
//		return new ArrayList<CandidateStatusDto>();
//	}

	@GetMapping("/getAllCandidateStagesByBU/{buId}")
	public List<CandidateStatusDto> getAllCandidateStagesByBU(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getAllCandidateStagesByBU(buId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateStatusDto>();
	}
}
