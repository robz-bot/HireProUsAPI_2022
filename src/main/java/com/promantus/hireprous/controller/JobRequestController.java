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
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.JobRequestSearchDto;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle JobRequests related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class JobRequestController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(JobRequestController.class);

	@Autowired
	private JobRequestService jobRequestService;

	@Value("${download.path}")
	private String downloadsPath;

	/**
	 * @param jobRequestDto
	 * @return
	 */
	@PostMapping("/addJobRequest")
	public JobRequestDto addJobRequest(@RequestBody JobRequestDto jobRequestDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		JobRequestDto resultDto = new JobRequestDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Customer Id.
			if (jobRequestDto.getCustomerId() == 0) {
				errorParam.append("Customer Id");
			}
			// BusninessUnit Id.
			if (jobRequestDto.getBuId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit Id" : "BusinessUnit Id");
			}
			// Role Id.
			if (jobRequestDto.getRoleId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Role Id" : "Role Id");
			}
			// No of Openings.
			if (jobRequestDto.getNoOfOpenings() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", No. Of Openings" : "No. Of Openings");
			}
			// Pay Range.
			if (jobRequestDto.getPayRange() == null || jobRequestDto.getPayRange().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", PayRange" : "PayRange");
			}
			// PayFrequency.
			if (jobRequestDto.getPayFrequency() == null || jobRequestDto.getPayFrequency().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", PayFrequency" : "PayFrequency");
			}
			// Currency.
			if (jobRequestDto.getCurrency() == null || jobRequestDto.getCurrency().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Currency" : "Currency");
			}
			// Employment Type.
			if (jobRequestDto.getEmploymentType() == null || jobRequestDto.getEmploymentType().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Employment Type" : "Employment Type");
			}
			// Requester Id.
//			if (jobRequestDto.getRequesterId() == 0) {
//				errorParam.append(errorParam.length() > 0 ? ", Requester Id" : "Requester Id");
//			}
			// Placement For.
			if (jobRequestDto.getPlacementFor() == null || jobRequestDto.getPlacementFor().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Placement For" : "Placement For");
			}
			// Project Start Date.
			if (jobRequestDto.getProjectStartDate() == null) {
				errorParam.append(errorParam.length() > 0 ? ", Project Start Date" : "Project Start Date");
			}
			// Job Description.
			if (jobRequestDto.getJobDescription() == null || jobRequestDto.getJobDescription().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Job Description" : "Job Description");
			}
			// Mandatory Skills.
			if (jobRequestDto.getMandatorySkills() == null || jobRequestDto.getMandatorySkills().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Mandatory Skills" : "Mandatory Skills");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = jobRequestService.addJobRequest(jobRequestDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @param jobRequestDto
	 * @return
	 */
	@PutMapping("/updateJobRequest")
	public JobRequestDto updateJobRequest(@RequestBody JobRequestDto jobRequestDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		JobRequestDto resultDto = new JobRequestDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// JobRequest Id.
			if (jobRequestDto.getId() == 0) {
				errorParam.append("JobRequest Id");
			}
			// Customer Id.
			if (jobRequestDto.getCustomerId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Customer Id" : "Customer Id");
			}
			// BusninessUnit Id.
			if (jobRequestDto.getBuId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit Id" : "BusinessUnit Id");
			}
			// Role Id.
			if (jobRequestDto.getRoleId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Role Id" : "Role Id");
			}
			// No of Openings.
			if (jobRequestDto.getNoOfOpenings() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", No. Of Openings" : "No. Of Openings");
			}
			// Pay Range.
			if (jobRequestDto.getPayRange() == null || jobRequestDto.getPayRange().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", PayRange" : "PayRange");
			}
			// PayFrequency.
			if (jobRequestDto.getPayFrequency() == null || jobRequestDto.getPayFrequency().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", PayFrequency" : "PayFrequency");
			}
			// Currency.
			if (jobRequestDto.getCurrency() == null || jobRequestDto.getCurrency().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Currency" : "Currency");
			}
			// Employment Type.
			if (jobRequestDto.getEmploymentType() == null || jobRequestDto.getEmploymentType().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Employment Type" : "Employment Type");
			}
			// Requester Id.
//			if (jobRequestDto.getRequesterId() == 0) {
//				errorParam.append(errorParam.length() > 0 ? ", Requester Id" : "Requester Id");
//			}
			// Placement For.
			if (jobRequestDto.getPlacementFor() == null || jobRequestDto.getPlacementFor().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Placement For" : "Placement For");
			}
			// Project Start Date.
			if (jobRequestDto.getProjectStartDate() == null) {
				errorParam.append(errorParam.length() > 0 ? ", Project Start Date" : "Project Start Date");
			}
			// Job Description.
			if (jobRequestDto.getJobDescription() == null || jobRequestDto.getJobDescription().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Job Description" : "Job Description");
			}
			// Mandatory Skills.
			if (jobRequestDto.getMandatorySkills() == null || jobRequestDto.getMandatorySkills().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Mandatory Skills" : "Mandatory Skills");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = jobRequestService.updateJobRequest(jobRequestDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @param jobRequestId
	 * @param recruiterId
	 * @param userId
	 * @param lang
	 * @return
	 */
	@PutMapping("/updateRecruiter/{jobRequestId}/{recruiterId}/{userId}")
	public JobRequestDto updateRecruiter(@PathVariable String jobRequestId, @PathVariable String recruiterId,
			@PathVariable String userId, @RequestHeader(name = "lang", required = false) String lang) {

		JobRequestDto resultDto = new JobRequestDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// JobRequest Id.
			if (jobRequestId == null || jobRequestId.isEmpty() || jobRequestId.equals("0")) {
				errorParam.append(errorParam.length() > 0 ? ", JobRequest Id" : "JobRequest Id");
			}
			// Recruiter Id.
			if (recruiterId == null || recruiterId.isEmpty() || recruiterId.equals("0")) {
				errorParam.append(errorParam.length() > 0 ? ", Recruiter Id" : "Recruiter Id");
			}
			// User Id.
			if (userId == null || userId.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", User Id" : "User Id");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = jobRequestService.updateRecruiter(jobRequestId, recruiterId, userId, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @param jobRequestId
	 * @param vendorId
	 * @param userId
	 * @param lang
	 * @return
	 */
	@PutMapping("/updateVendor/{jobRequestId}/{vendorId}/{userId}")
	public JobRequestDto updateVendor(@PathVariable String jobRequestId, @PathVariable String vendorId,
			@PathVariable String userId, @RequestHeader(name = "lang", required = false) String lang) {

		JobRequestDto resultDto = new JobRequestDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// JobRequest Id.
			if (jobRequestId == null || jobRequestId.isEmpty() || jobRequestId.equals("0")) {
				errorParam.append(errorParam.length() > 0 ? ", JobRequest Id" : "JobRequest Id");
			}
			// Vendor Id.
			if (vendorId == null || vendorId.isEmpty() || vendorId.equals("0")) {
				errorParam.append(errorParam.length() > 0 ? ", Vendor Id" : "Vendor Id");
			}
			// User Id.
			if (userId == null || userId.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", User Id" : "User Id");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = jobRequestService.updateVendor(jobRequestId, vendorId, userId, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @param jobRequestId
	 * @return
	 */
	@GetMapping("/getJobRequest/{jobRequestId}")
	public JobRequestDto getJobRequestById(@PathVariable String jobRequestId,
			@RequestHeader(name = "lang", required = false) String lang) {

		JobRequestDto resultDto = new JobRequestDto();
		try {

			resultDto = jobRequestService.getJobRequestById(jobRequestId);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @param jobRequestId
	 * @return
	 */
	@GetMapping("/getJobRequestsByJRNumber/{jrNumber}")
	public JobRequestDto getJobRequestsByJRNumber(@PathVariable String jrNumber,
			@RequestHeader(name = "lang", required = false) String lang) {

		JobRequestDto resultDto = new JobRequestDto();
		try {

			resultDto = jobRequestService.getJobRequestByJRNumber(jrNumber);

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
	@GetMapping("/getAllJobRequests")
	public List<JobRequestDto> getAllJobRequests(@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.getAllJobRequests();

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllJobRequestNumbers")
	public List<JobRequestDto> getAllJobRequestNumbers(@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.getAllJobRequestNumbers();

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getJRsByCustomerId/{custId}")
	public List<JobRequestDto> getJobRequestsByCustomerId(@PathVariable String custId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.getJobRequestsByCustomerId(custId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getJRsByVendorId/{vendorId}")
	public List<JobRequestDto> getJobRequestsByVendorId(@PathVariable String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.getJobRequestsByVendorId(vendorId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getJRsByBuId/{buId}")
	public List<JobRequestDto> getJobRequestsByBuId(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.getJobRequestsByBuId(buId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getJRsByRoleId/{roleId}")
	public List<JobRequestDto> getJobRequestsByRoleId(@PathVariable String roleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.getJobRequestsByRoleId(roleId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getJRsByRecruiterId/{recId}")
	public List<JobRequestDto> getJRsByRecruiterId(@PathVariable String recId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.getJobRequestsByRecruiterId(recId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getMyJobRequests/{userId}")
	public List<JobRequestDto> getMyJobRequests(@PathVariable String userId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return jobRequestService.getMyJobRequests(Long.parseLong(userId), lang);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @param jobRequestSearchDto
	 * @param lang
	 * @return
	 */
	@PostMapping("/searchMyJobRequests/{userId}")
	public List<JobRequestDto> searchMyJobRequests(@PathVariable String userId,
			@RequestBody JobRequestSearchDto jobRequestSearchDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return jobRequestService.searchMyJobRequests(jobRequestSearchDto, Long.parseLong(userId), lang);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @return
	 */
	@PutMapping("/getJRsByStatus")
	public List<JobRequestDto> getJRsByStatus(@RequestBody String status,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.getJobRequestsByStatus(status, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchJobRequest/{key}")
	public List<JobRequestDto> searchJobRequest(@PathVariable String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.searchJobRequest(key);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @param key
	 * @return
	 */
	@PostMapping("/searchJobRequest")
	public List<JobRequestDto> searchJobRequest(@RequestBody JobRequestSearchDto jobRequestSearchDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return jobRequestService.searchJobRequest(jobRequestSearchDto, lang);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

	/**
	 * @param jobRequestId
	 * @return
	 */
	@DeleteMapping("/deleteJobRequestById/{jobRequestId}")
	public JobRequestDto deleteJobRequestById(@PathVariable String jobRequestId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.deleteJobRequestById(jobRequestId);

		} catch (final Exception e) {

			JobRequestDto resultDto = new JobRequestDto();
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(HireProUsUtil.getErrorMessage(e));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
	}

	@PutMapping("/downloadJobRequestDetails")
	public void downloadJobRequestDetails(@RequestBody List<JobRequestDto> jobRequestDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File jobRequestFile = new File(downloadsPath + "HireProUs_JobRequest_details.xlsx");
			FileUtils.writeByteArrayToFile(jobRequestFile,
					jobRequestService.downloadJobRequestDetails(jobRequestDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + jobRequestFile.getName());
			response.setContentLength((int) jobRequestFile.length());

			inStream = new BufferedInputStream(new FileInputStream(jobRequestFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			jobRequestFile.deleteOnExit();

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
	 * @param jobRequestSearchDto
	 * @return
	 */
	@PostMapping("/searchJobRequestForDownload")
	public List<JobRequestDto> searchJobRequestForDownload(@RequestBody JobRequestSearchDto jobRequestSearchDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return jobRequestService.searchJobRequestForDownload(jobRequestSearchDto, lang);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}
	
	/**
	 * @return
	 */
	@GetMapping("/getJRsByVendorPriority/{vendorPriority}")
	public List<JobRequestDto> getJobRequestsByVendorPriority(@PathVariable String vendorPriority,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return jobRequestService.getJobRequestsByVendorPriority(vendorPriority, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<JobRequestDto>();
	}

}
