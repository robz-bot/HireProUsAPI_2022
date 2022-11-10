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
import org.springframework.web.bind.annotation.RestController;

import com.google.common.net.HttpHeaders;
import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.InterviewScheduleDto;
import com.promantus.hireprous.dto.InterviewScheduleSearchDto;
import com.promantus.hireprous.dto.TimeZoneDto;
import com.promantus.hireprous.service.InterviewScheduleService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle InterviewSchedule related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class InterviewScheduleController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(InterviewScheduleController.class);

	@Autowired
	private InterviewScheduleService interviewScheduleService;

	@Value("${download.path}")
	private String downloadsPath;

	/**
	 * @return
	 */
	@GetMapping("/getTimeZoneList")
	public List<TimeZoneDto> getTimeZoneList() {

		List<TimeZoneDto> timeZoneList = new ArrayList<TimeZoneDto>();
		try {

			return HireProUsUtil.getTimeZoneList();

		} catch (final Exception e) {

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return timeZoneList;
	}

	/**
	 * @param interviewScheduleDto
	 * @return
	 */
	@PostMapping("/addInterviewSchedule")
	public InterviewScheduleDto addInterviewSchedule(@RequestBody InterviewScheduleDto interviewScheduleDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		InterviewScheduleDto resultDto = new InterviewScheduleDto();
		try {
			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Interviewer Id
			if (interviewScheduleDto.getInterviewerId() == null || interviewScheduleDto.getInterviewerId() == 0) {
				errorParam.append("Interviewer Id");
			}
			// Schedule DateTime
			if (interviewScheduleDto.getScheduleDateTime() == null) {
				errorParam.append(errorParam.length() > 0 ? ", Schedule DateTime" : "Schedule DateTime");
			}
			// Duration
			if (interviewScheduleDto.getDuration() == null || interviewScheduleDto.getDuration().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Duration" : "Duration");
			}
			// JR Number
			if (interviewScheduleDto.getJrNumber() == null || interviewScheduleDto.getJrNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", JR Number" : "JR Number");
			}
			// Rec Status
			if (interviewScheduleDto.getRecStatus() == null || interviewScheduleDto.getRecStatus().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", RecStatus" : "RecStatus");
			}
			// Round
			if (interviewScheduleDto.getRound() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Round" : "Round");
			}
			// Schedule Remarks
			if (interviewScheduleDto.getScheduleRemarks() == null
					|| interviewScheduleDto.getScheduleRemarks().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Schedule Remarks" : "Schedule Remarks");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = interviewScheduleService.addInterviewSchedule(interviewScheduleDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param interviewScheduleDto
	 * @return
	 */
	@PutMapping("/updateInterviewSchedule")
	public InterviewScheduleDto updateInterviewSchedule(@RequestBody InterviewScheduleDto interviewScheduleDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		InterviewScheduleDto resultDto = new InterviewScheduleDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// InterviewSchedule Id.
			if (interviewScheduleDto.getId() == 0) {
				errorParam.append("Interview Schedule Id");
			}
			// Interviewer Id
			if (interviewScheduleDto.getInterviewerId() == null || interviewScheduleDto.getInterviewerId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Interviewer Id" : "Interviewer Id");
			}
			// Schedule DateTime
			if (interviewScheduleDto.getScheduleDateTime() == null) {
				errorParam.append(errorParam.length() > 0 ? ", Schedule DateTime" : "Schedule DateTime");
			}
			// Duration
			if (interviewScheduleDto.getDuration() == null || interviewScheduleDto.getDuration().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Duration" : "Duration");
			}
			// JR Number
			if (interviewScheduleDto.getJrNumber() == null || interviewScheduleDto.getJrNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", JR Number" : "JR Number");
			}
			// Rec Status
			if (interviewScheduleDto.getRecStatus() == null || interviewScheduleDto.getRecStatus().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", RecStatus" : "RecStatus");
			}
			// Round
			if (interviewScheduleDto.getRound() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Round" : "Round");
			}
			// Schedule Remarks
			if (interviewScheduleDto.getScheduleRemarks() == null
					|| interviewScheduleDto.getScheduleRemarks().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Schedule Remarks" : "Schedule Remarks");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = interviewScheduleService.updateInterviewSchedule(interviewScheduleDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param interviewScheduleDto
	 * @return
	 */
	@PutMapping("/updateResult")
	public InterviewScheduleDto updateResult(@RequestBody InterviewScheduleDto interviewScheduleDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		InterviewScheduleDto resultDto = new InterviewScheduleDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// InterviewSchedule Id.
			if (interviewScheduleDto.getId() == 0) {
				errorParam.append("Interview Schedule Id");
			}
			// Rec Status
			if (interviewScheduleDto.getRecStatus() == null || interviewScheduleDto.getRecStatus().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Status" : "Status");
			}
			// Result Remarks
			if (interviewScheduleDto.getResultRemarks() == null || interviewScheduleDto.getResultRemarks().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Result Remarks" : "Result Remarks");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = interviewScheduleService.updateResult(interviewScheduleDto, lang);

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
	@GetMapping("/getAllInterviewSchedules")
	public List<InterviewScheduleDto> getAllInterviewSchedules(
			@RequestHeader(name = "lang", required = false) String lang) {

		List<InterviewScheduleDto> interviewSchedulesDtoList = new ArrayList<InterviewScheduleDto>();
		try {
			interviewSchedulesDtoList = interviewScheduleService.getAllInterviewSchedules();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewSchedulesDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getInterviewScheduledList/{round}")
	public List<InterviewScheduleDto> getInterviewScheduledList(@PathVariable String round,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<InterviewScheduleDto> interviewSchedulesDtoList = new ArrayList<InterviewScheduleDto>();
		try {
			interviewSchedulesDtoList = interviewScheduleService.getInterviewScheduledList(Integer.parseInt(round));
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewSchedulesDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getMyInterviews/{userId}")
	public List<InterviewScheduleDto> getMyInterviews(@PathVariable String userId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return interviewScheduleService.getMyInterviews(Long.parseLong(userId));
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<InterviewScheduleDto>();
	}

	/**
	 * @return
	 */
	@PutMapping("/searchMyInterviews/{userId}")
	public List<InterviewScheduleDto> searchMyInterviews(
			@RequestBody InterviewScheduleSearchDto interviewScheduleSearchDto, @PathVariable String userId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return interviewScheduleService.searchMyInterviews(interviewScheduleSearchDto, Long.parseLong(userId));
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<InterviewScheduleDto>();
	}

	/**
	 * @return
	 */
	@PutMapping("/searchInterviewScheduledList/{round}")
	public List<InterviewScheduleDto> searchInterviewScheduledList(
			@RequestBody InterviewScheduleSearchDto interviewScheduleSearchDto, @PathVariable String round,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<InterviewScheduleDto> interviewSchedulesDtoList = new ArrayList<InterviewScheduleDto>();
		try {
			interviewSchedulesDtoList = interviewScheduleService
					.searchInterviewScheduledList(interviewScheduleSearchDto, Integer.parseInt(round));
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewSchedulesDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getForSchedule/{round}")
	public List<InterviewScheduleDto> getForSchedule(@PathVariable String round,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<InterviewScheduleDto> interviewSchedulesDtoList = new ArrayList<InterviewScheduleDto>();
		try {
			interviewSchedulesDtoList = interviewScheduleService.getForSchedule(Integer.parseInt(round));
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewSchedulesDtoList;
	}

	/**
	 * @return
	 */
	@PutMapping("/searchForSchedule/{round}")
	public List<InterviewScheduleDto> searchForSchedule(
			@RequestBody InterviewScheduleSearchDto interviewScheduleSearchDto, @PathVariable String round,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<InterviewScheduleDto> interviewSchedulesDtoList = new ArrayList<InterviewScheduleDto>();
		try {
			interviewSchedulesDtoList = interviewScheduleService.searchForSchedule(interviewScheduleSearchDto,
					Integer.parseInt(round));
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewSchedulesDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllForSchedule")
	public List<InterviewScheduleDto> getAllForSchedule(@RequestHeader(name = "lang", required = false) String lang) {

		List<InterviewScheduleDto> interviewSchedulesDtoList = new ArrayList<InterviewScheduleDto>();
		try {
			interviewSchedulesDtoList = interviewScheduleService.getAllForSchedule();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewSchedulesDtoList;
	}

	/**
	 * @return
	 */
	@PutMapping("/getAllSearchForSchedule")
	public List<InterviewScheduleDto> getAllSearchForSchedule(
			@RequestBody InterviewScheduleSearchDto interviewScheduleSearchDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<InterviewScheduleDto> interviewSchedulesDtoList = new ArrayList<InterviewScheduleDto>();
		try {
			interviewSchedulesDtoList = interviewScheduleService.searchAllForSchedule(interviewScheduleSearchDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewSchedulesDtoList;
	}

	/**
	 * @param interviewScheduleId
	 * @return
	 */
	@GetMapping("/getInterviewSchedule/{interviewScheduleId}")
	public InterviewScheduleDto getInterviewScheduleById(@PathVariable String interviewScheduleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		InterviewScheduleDto interviewScheduleDto = new InterviewScheduleDto();
		try {
			interviewScheduleDto = interviewScheduleService.getInterviewScheduleById(interviewScheduleId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewScheduleDto;
	}

	/**
	 * @param interviewScheduleId
	 * @return
	 */
	@DeleteMapping("/deleteInterviewScheduleById/{interviewScheduleId}")
	public InterviewScheduleDto deleteInterviewScheduleById(@PathVariable String interviewScheduleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return interviewScheduleService.deleteInterviewScheduleById(interviewScheduleId);
		} catch (final Exception e) {

			InterviewScheduleDto resultDto = new InterviewScheduleDto();
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(HireProUsUtil.getErrorMessage(e));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
	}

	/**
	 * @return
	 */
	@GetMapping("/viewHistory/{jrNumber}/{candidateId}")
	public List<InterviewScheduleDto> viewHistory(@PathVariable String jrNumber, @PathVariable String candidateId,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<InterviewScheduleDto> interviewSchedulesDtoList = new ArrayList<InterviewScheduleDto>();
		try {
			interviewSchedulesDtoList = interviewScheduleService.getViewHistory(jrNumber, Long.parseLong(candidateId),
					lang);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewSchedulesDtoList;
	}

	/**
	 * 
	 * @param vendorDtoList
	 * 
	 * @param lang
	 * 
	 * @param response
	 * 
	 */

	@PutMapping("/downloadInterviewScheduleSummary")

	public void downloadInterviewScheduleSummary(@RequestBody List<InterviewScheduleDto> interviewScheduleDtoList,

			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;

		BufferedOutputStream outStream = null;

		try {

			File interviewScheduleFile = new File(downloadsPath + "HireProUs_interviewscheduleSummary_details.xlsx");

			FileUtils.writeByteArrayToFile(interviewScheduleFile,
					interviewScheduleService.downloadInterviewScheduleSummary(interviewScheduleDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
					"attachment;filename=" + interviewScheduleFile.getName());

			response.setContentLength((int) interviewScheduleFile.length());

			inStream = new BufferedInputStream(new FileInputStream(interviewScheduleFile));

			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];

			int bytesRead = 0;

			while ((bytesRead = inStream.read(buffer)) != -1) {

				outStream.write(buffer, 0, bytesRead);

			}

			response.flushBuffer();

			interviewScheduleFile.deleteOnExit();

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
	 * 
	 * @param key
	 * 
	 * @return
	 * 
	 */

	@PostMapping("/searchInterviewScheduleForDownload")

	public List<InterviewScheduleDto> searchInterviewScheduleForDownload(
			@RequestBody InterviewScheduleDto interviewScheduleDto,

			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return interviewScheduleService.searchInterviewScheduleForDownload(interviewScheduleDto);

		} catch (final Exception e) {

			logger.error(HireProUsUtil.getErrorMessage(e));

		}

		return new ArrayList<InterviewScheduleDto>();

	}
}
