
/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.dto.CandidateStatusDto;
import com.promantus.hireprous.dto.CustomerDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.dto.UserSearchDto;
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
	
	@Value("${download.path}")
	private String downloadsPath;

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
	
	//adding new feature(16-02-2024)
		//get all user reports
		@GetMapping("/getAllUsersReports")
		public List<UserDto> getAllUsers(@RequestHeader(name = "lang", required = false) String lang) {

			try {

				return reportJobRequestService.getAllUsersReports();

			} catch (final Exception e) {
				logger.error(HireProUsUtil.getErrorMessage(e));
			}

			return new ArrayList<UserDto>();
		}
		
		//search user reports using UserSearchDto
		@PostMapping("/searchUsersReports")
		public List<UserDto> searchUserReports(@RequestBody UserSearchDto userSearchDto,
				@RequestHeader(name = "lang", required = false) String lang) {

			try {

				return reportJobRequestService.searchUserReports(userSearchDto, lang);

			} catch (final Exception e) {
				logger.error(HireProUsUtil.getErrorMessage(e));
			}

			return new ArrayList<UserDto>();
		}
		
		//download user reports
		@PutMapping("/downloadUsersReportsDetails")
		public void downloadUserReportsDetails(@RequestBody List<UserDto> UserDtoList,
				@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

			BufferedInputStream inStream = null;
			BufferedOutputStream outStream = null;
			try {

				File userFile = new File(downloadsPath + "HireProUs_User_Details.xlsx");
				FileUtils.writeByteArrayToFile(userFile,
						reportJobRequestService.downloadUserReportsDetails(UserDtoList, lang));

				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + userFile.getName());
				response.setContentLength((int) userFile.length());

				inStream = new BufferedInputStream(new FileInputStream(userFile));
				outStream = new BufferedOutputStream(response.getOutputStream());

				byte[] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}

				response.flushBuffer();
				userFile.deleteOnExit();

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
