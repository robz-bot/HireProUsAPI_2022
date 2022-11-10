
/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.dto.CandidateStatusDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.service.CandidateService;
import com.promantus.hireprous.service.ReportJobRequestService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle JobRequests related APIs.
 * 
 * @author Krish.
 * @author Sihab.
 */
@RestController
@RequestMapping("/api/v1")
public class ReportController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private ReportJobRequestService reportJobRequestService;

	@Autowired
	private CandidateService candidateService;

	/**
	 * @param jobRequestDto
	 * @return
	 */

	@GetMapping("/getAllJobRequestsByDate/{startDate}/{endDate}")
	public List<JobRequestDto> getAllJobRequests(@PathVariable String startDate, @PathVariable String endDate,
			@RequestHeader(name = "lang", required = false) String lang) {

		LocalDate localStartDate = LocalDate.parse(startDate);
		LocalDate localEndDate = LocalDate.parse(endDate);

		try {

			return reportJobRequestService.getJobRequestByProjectStartDateBetween(localStartDate, localEndDate);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	@GetMapping("/downloadJobRequestExcel")
	public void downloadJobRequestExcel(HttpServletResponse response,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			List<JobRequestDto> jobRequestDtoLis = reportJobRequestService.getAllJobRequestsReport();

			ByteArrayInputStream byteArrayInputStream = reportJobRequestService.exportasExel(jobRequestDtoLis);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=JobRequest.xlsx");

			IOUtils.copy(byteArrayInputStream, response.getOutputStream());

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

	}

	@GetMapping("/downloadJobRequestPdf")

	public void downloadJobRequestPdf(HttpServletResponse response,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			List<JobRequestDto> jobRequestDtoLis = reportJobRequestService.getAllJobRequestsReport();

			ByteArrayInputStream byteArrayInputStream = reportJobRequestService.exportasPdf(jobRequestDtoLis);

			response.setHeader("Content-Disposition", "attachment; filename=JobRequest.pdf");

			IOUtils.copy(byteArrayInputStream, response.getOutputStream());

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

	}

	@GetMapping("/getAllJobRequestsReportCount")
	public List<JobRequestDto> getAllJobRequestsReportCount(
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return reportJobRequestService.getAllJobRequestsReportCount();

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	@GetMapping("/getAllJobRequestsReport")
	public List<JobRequestDto> getAllJobRequests(@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return reportJobRequestService.getAllJobRequestsReport();

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	@GetMapping("/getAllCandidateStatusByJrNumber/{jrNumber}")
	public List<CandidateStatusDto> getAllCandidateStatusByJrNumber(@PathVariable String jrNumber,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return candidateService.getCandidatesStatusByJRNum(jrNumber, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CandidateStatusDto>();
	}

	@GetMapping("/getJobRequestsByStatus/{jobRequeststatus}")
	public List<JobRequestDto> getJobRequestsByStatus(@PathVariable String jobRequeststatus,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return reportJobRequestService.searchJobRequestByStatus(jobRequeststatus);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}
}
