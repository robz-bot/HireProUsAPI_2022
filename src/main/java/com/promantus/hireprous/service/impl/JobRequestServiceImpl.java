/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.HireProUsDefaultMethods;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.JobRequestSearchDto;
import com.promantus.hireprous.entity.Candidate;
import com.promantus.hireprous.entity.JobRequest;
import com.promantus.hireprous.repository.CandidateRepository;
import com.promantus.hireprous.repository.JobRequestRepository;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CandidateService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.CustomerService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.service.MailService;
import com.promantus.hireprous.service.RecruitmentRoleService;
import com.promantus.hireprous.service.RoleService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class JobRequestServiceImpl implements JobRequestService {

	private static final Logger logger = LoggerFactory.getLogger(JobRequestServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	MailService mailService;

	@Autowired
	CustomerService customerService;

	@Autowired
	CandidateService candidateService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	RoleService roleService;

	@Autowired
	RecruitmentRoleService recruitmentRoleService;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	JobRequestRepository jobRequestRepository;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public JobRequestDto addJobRequest(final JobRequestDto jobRequestDto, String lang) throws Exception {

		JobRequestDto resultDto = new JobRequestDto();

		JobRequest jobRequest = new JobRequest();
		jobRequest.setId(commonService.nextSequenceNumber());

		this.setJobReferenceNumber(jobRequest);

		jobRequest.setCustomerId(jobRequestDto.getCustomerId());
		jobRequest.setBuId(jobRequestDto.getBuId());
		jobRequest.setRoleId(jobRequestDto.getRoleId());

		jobRequest.setNoOfOpenings(jobRequestDto.getNoOfOpenings());
		jobRequest.setClosedOpenings(0);
		jobRequest.setMinYearOfExp(jobRequestDto.getMinYearOfExp());

		jobRequest.setLocation(jobRequestDto.getLocation());
		jobRequest.setPayRange(jobRequestDto.getPayRange());
		jobRequest.setPayFrequency(jobRequestDto.getPayFrequency());
		jobRequest.setCurrency(jobRequestDto.getCurrency());
		jobRequest.setEmploymentType(jobRequestDto.getEmploymentType());
		jobRequest.setContractDuration(jobRequestDto.getContractDuration());
		jobRequest.setMonthOrYear(jobRequestDto.getMonthOrYear());

		jobRequest.setRequesterId(jobRequestDto.getRequesterId());
		jobRequest.setRecruiterId(jobRequestDto.getRecruiterId());

		jobRequest.setPlacementFor(jobRequestDto.getPlacementFor());
		jobRequest.setProjectStartDate(jobRequestDto.getProjectStartDate());
		jobRequest.setRemoteOption(jobRequestDto.getRemoteOption());

		jobRequest.setJobDescription(jobRequestDto.getJobDescription());
		jobRequest.setMandatorySkills(jobRequestDto.getMandatorySkills());
		jobRequest.setOptionalSkills(jobRequestDto.getOptionalSkills());

		jobRequest.setJobReqStatus(HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START);

		jobRequest.setCreatedBy(jobRequestDto.getCreatedBy());
		jobRequest.setUpdatedBy(jobRequestDto.getUpdatedBy());
		jobRequest.setCreatedDateTime(LocalDateTime.now());
		jobRequest.setUpdatedDateTime(LocalDateTime.now());

		jobRequest.setVendorPriority("Primary");
		jobRequest.setVendorActionStatus("Active");

		jobRequest.setVendorId(jobRequestDto.getVendorId());

		jobRequestRepository.save(jobRequest);

		jobRequestDto.setId(jobRequest.getId());
		jobRequestDto.setReferenceNumber(jobRequest.getReferenceNumber());
		jobRequestDto.setCreatedDateTime(jobRequest.getCreatedDateTime());

		// Send New Job Request Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendNewJobRequestEmail(jobRequestDto);
				} catch (Exception e) {

					logger.error("New Job Request Email is not Sent");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setId(jobRequest.getId());
		resultDto.setReferenceNumber(jobRequest.getReferenceNumber());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	/**
	 * @return
	 */
	private void setJobReferenceNumber(final JobRequest jobRequest) {

		int nextCounter = 0;
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String runningNumber = String
				.format("%0" + HireProUsConstants.JOB_REQUEST_REFERENCE_MAX_DIGIT_RUNNING_NUMBER + "d", 1);

		JobRequest jobRequestCheck = jobRequestRepository.findFirstByYear(year,
				HireProUsUtil.orderByNextCounterDescAndRunningNumberDesc());

		if (jobRequestCheck != null) {
			nextCounter = jobRequestCheck.getNextCounter();
			if (jobRequestCheck.getRunningNumber() >= HireProUsConstants.JOB_REQUEST_REFERENCE_MAX_RUNNING_NUMBER) {
				nextCounter += 1;
			} else {
				runningNumber = String.format(
						"%0" + HireProUsConstants.JOB_REQUEST_REFERENCE_MAX_DIGIT_RUNNING_NUMBER + "d",
						jobRequestCheck.getRunningNumber() + 1);
			}
		}

		jobRequest.setYear(year);
		jobRequest.setRunningNumber(Integer.parseInt(runningNumber));
		jobRequest.setNextCounter(nextCounter);
		jobRequest.setReferenceNumber(
				HireProUsConstants.JOB_REQUEST_REFERENCE_PREFIX + nextCounter + year + runningNumber);
	}

	@Override
	public JobRequestDto updateJobRequest(final JobRequestDto jobRequestDto, final String lang) throws Exception {

		JobRequestDto resultDto = new JobRequestDto();

		JobRequest jobRequest = jobRequestRepository.findById(jobRequestDto.getId());

		if (jobRequest == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "JobRequest Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		jobRequest.setCustomerId(jobRequestDto.getCustomerId());
		jobRequest.setBuId(jobRequestDto.getBuId());
		jobRequest.setRoleId(jobRequestDto.getRoleId());

		jobRequest.setNoOfOpenings(jobRequestDto.getNoOfOpenings());
		jobRequest.setMinYearOfExp(jobRequestDto.getMinYearOfExp());

		jobRequest.setLocation(jobRequestDto.getLocation());
		jobRequest.setPayRange(jobRequestDto.getPayRange());
		jobRequest.setPayFrequency(jobRequestDto.getPayFrequency());
		jobRequest.setCurrency(jobRequestDto.getCurrency());
		jobRequest.setEmploymentType(jobRequestDto.getEmploymentType());
		jobRequest.setContractDuration(jobRequestDto.getContractDuration());
		jobRequest.setMonthOrYear(jobRequestDto.getMonthOrYear());

		jobRequest.setRequesterId(jobRequestDto.getRequesterId());
		jobRequest.setRecruiterId(jobRequestDto.getRecruiterId());

		jobRequest.setPlacementFor(jobRequestDto.getPlacementFor());
		jobRequest.setProjectStartDate(jobRequestDto.getProjectStartDate());
		jobRequest.setRemoteOption(jobRequestDto.getRemoteOption());

		jobRequest.setJobDescription(jobRequestDto.getJobDescription());
		jobRequest.setMandatorySkills(jobRequestDto.getMandatorySkills());
		jobRequest.setOptionalSkills(jobRequestDto.getOptionalSkills());

		jobRequest.setJobReqStatus(jobRequestDto.getJobReqStatus());

		jobRequest.setUpdatedBy(jobRequestDto.getUpdatedBy());
		jobRequest.setUpdatedDateTime(LocalDateTime.now());

		jobRequest.setVendorId(jobRequestDto.getVendorId());

		jobRequest.setVendorPriority(jobRequestDto.getVendorPriority());
		jobRequest.setVendorActionStatus(jobRequestDto.getVendorActionStatus());

		jobRequestRepository.save(jobRequest);

		jobRequestDto.setUpdatedDateTime(jobRequest.getUpdatedDateTime());

		// Send mail for Hold, Terminated and Closed.
		if (jobRequestDto.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_HOLD)
				|| jobRequestDto.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_TERMINATED)
				|| jobRequestDto.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_CLOSED)) {

			// Send Job Request Closed Mail.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailService.sendJobRequestStatusUpdatedEmail(jobRequestDto);
					} catch (Exception e) {

						logger.error("Job Request Status updated Email is not Sent");
						logger.error(HireProUsUtil.getErrorMessage(e));
					}
				}
			}).start();

			if (jobRequestDto.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_TERMINATED)
					|| jobRequestDto.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_CLOSED)) {

				// Drop the candidates.
				List<Candidate> candidatesList = candidateRepository.findByJrNumberAndRecStatusIn(
						jobRequestDto.getReferenceNumber(),
						Arrays.asList(HireProUsConstants.REC_STATUS_UPLOADED,
								HireProUsConstants.REC_STATUS_SHORTLISTED_0, HireProUsConstants.REC_STATUS_PASSED_R1,
								HireProUsConstants.REC_STATUS_PASSED_R2, HireProUsConstants.REC_STATUS_PASSED_CR3,
								HireProUsConstants.REC_STATUS_PASSED_HR4, HireProUsConstants.REC_STATUS_APPROVED_BU,
								HireProUsConstants.REC_STATUS_SELECTED),
						HireProUsUtil.orderByUpdatedDateTimeDesc());

				if (candidatesList != null && candidatesList.size() > 0) {
					for (final Candidate candidate : candidatesList) {
						candidate.setRecStatus(HireProUsConstants.REC_STATUS_DROPPED);
						candidate.setUpdatedBy(jobRequestDto.getUpdatedBy());
						candidate.setUpdatedDateTime(LocalDateTime.now());

						candidateRepository.save(candidate);
					}
				}
			}
		} else {

			// Send Job Request Update Mail.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailService.sendJobRequestUpdateEmail(jobRequestDto);
					} catch (Exception e) {

						logger.error("Job Request Update Email is not Sent");
						logger.error(HireProUsUtil.getErrorMessage(e));
					}
				}
			}).start();
		}

		resultDto.setId(jobRequest.getId());
		resultDto.setReferenceNumber(jobRequest.getReferenceNumber());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public JobRequestDto updateRecruiter(final String jobRequestId, final String recruiterId, final String userId,
			final String lang) throws Exception {

		JobRequestDto resultDto = new JobRequestDto();

		JobRequest jobRequest = jobRequestRepository.findById(Long.parseLong(jobRequestId));

		if (jobRequest == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "JobRequest Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		jobRequest.setRecruiterId(Long.parseLong(recruiterId));

		jobRequest.setUpdatedBy(Long.parseLong(userId));
		jobRequest.setUpdatedDateTime(LocalDateTime.now());

		jobRequestRepository.save(jobRequest);

		// get data to send mail.
		JobRequestDto jobRequestDto = this.getJobRequestDto(jobRequest);

		// Send Job Request Update Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendJobRequestUpdateEmail(jobRequestDto);
				} catch (Exception e) {

					logger.error("Job Request Update Email is not Sent");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setId(jobRequest.getId());
		resultDto.setReferenceNumber(jobRequest.getReferenceNumber());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public JobRequestDto updateVendor(final String jobRequestId, final String vendorId, final String userId,
			final String lang) throws Exception {

		JobRequestDto resultDto = new JobRequestDto();

		JobRequest jobRequest = jobRequestRepository.findById(Long.parseLong(jobRequestId));

		if (jobRequest == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "JobRequest Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		jobRequest.setVendorId(Long.parseLong(vendorId));

		jobRequest.setUpdatedBy(Long.parseLong(userId));
		jobRequest.setUpdatedDateTime(LocalDateTime.now());

		jobRequestRepository.save(jobRequest);

		// get data to send mail.
		JobRequestDto jobRequestDto = this.getJobRequestDto(jobRequest);

		// Send Job Request Update Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendJobRequestUpdateEmail(jobRequestDto);
				} catch (Exception e) {

					logger.error("Job Request Update Email is not Sent");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setId(jobRequest.getId());
		resultDto.setReferenceNumber(jobRequest.getReferenceNumber());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public JobRequestDto updateJRStatus(final Long jobRequestId, final String status, final Long userId,
			final String lang) throws Exception {

		JobRequestDto resultDto = new JobRequestDto();

		JobRequest jobRequest = jobRequestRepository.findById(jobRequestId);

		if (jobRequest == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "JobRequest Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		jobRequest.setJobReqStatus(status);

		jobRequest.setUpdatedBy(userId);
		jobRequest.setUpdatedDateTime(LocalDateTime.now());

		jobRequestRepository.save(jobRequest);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public JobRequestDto updateJRStatusByJRNumber(final String jrNumber, final String status, final Long userId,
			final String lang) throws Exception {

		JobRequestDto resultDto = new JobRequestDto();

		JobRequest jobRequest = jobRequestRepository.getJobRequestByReferenceNumber(jrNumber);

		if (jobRequest == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Job Reference Number" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		jobRequest.setJobReqStatus(status);

		jobRequest.setUpdatedBy(userId);
		jobRequest.setUpdatedDateTime(LocalDateTime.now());

		jobRequestRepository.save(jobRequest);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public JobRequestDto getJobRequestById(final String jobRequestId) throws Exception {

		JobRequest jobRequest = jobRequestRepository.findById(Long.parseLong(jobRequestId));

		return jobRequest != null ? this.getJobRequestDto(jobRequest) : null;
	}

	@Override
	public JobRequestDto getJobRequestByJRNumber(final String jrNumber) throws Exception {

		JobRequest jobRequest = jobRequestRepository.getJobRequestByReferenceNumber(jrNumber);

		return jobRequest != null ? this.getJobRequestDto(jobRequest) : null;
	}

	@Override
	public JobRequestDto getJobRequestInfoByJRNumber(final String jrNumber) throws Exception {

		JobRequest jobRequest = jobRequestRepository.getJobRequestByReferenceNumber(jrNumber);

		return jobRequest != null ? this.getJobRequestInfoDto(jobRequest) : null;
	}

	@Override
	public String getRecruiterEmailByJRNumber(final String jrNumber) throws Exception {

		JobRequest jobRequest = jobRequestRepository.getJobRequestByReferenceNumber(jrNumber);

		return jobRequest != null && jobRequest.getRecruiterId() != null
				? CacheUtil.getUsersEmailMap().get(jobRequest.getRecruiterId())
				: null;
	}

	@Override
	public Long getBuIdByJRNumber(final String jrNumber) throws Exception {

		JobRequest jobRequest = jobRequestRepository.getJobRequestByReferenceNumber(jrNumber);

		return jobRequest != null ? jobRequest.getBuId() : null;
	}

	@Override
	public void updateClosedOpening(final String jrNumber) throws Exception {

		JobRequest jobRequest = jobRequestRepository.getJobRequestByReferenceNumber(jrNumber);

		if (jobRequest != null) {
			jobRequest.setClosedOpenings(jobRequest.getClosedOpenings() + 1);

			if (jobRequest.getNoOfOpenings() == jobRequest.getClosedOpenings()) {
				jobRequest.setJobReqStatus(HireProUsConstants.JOB_REQUEST_STATUS_CLOSED);

				jobRequestRepository.save(jobRequest);

				// get data to send mail.
				JobRequestDto jobRequestDto = this.getJobRequestDto(jobRequest);
				// Send Job Request Closed Mail.
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							mailService.sendJobRequestStatusUpdatedEmail(jobRequestDto);
						} catch (Exception e) {

							logger.error("Job Request Closed Email is not Sent");
							logger.error(HireProUsUtil.getErrorMessage(e));
						}
					}
				}).start();

				// Drop the candidates.
				List<Candidate> candidatesList = candidateRepository.findByJrNumberAndRecStatusIn(
						jobRequestDto.getReferenceNumber(),
						Arrays.asList(HireProUsConstants.REC_STATUS_UPLOADED,
								HireProUsConstants.REC_STATUS_SHORTLISTED_0, HireProUsConstants.REC_STATUS_PASSED_R1,
								HireProUsConstants.REC_STATUS_PASSED_R2, HireProUsConstants.REC_STATUS_PASSED_CR3,
								HireProUsConstants.REC_STATUS_PASSED_HR4),
						HireProUsUtil.orderByUpdatedDateTimeDesc());

				if (candidatesList != null && candidatesList.size() > 0) {
					for (final Candidate candidate : candidatesList) {
						candidate.setRecStatus(HireProUsConstants.REC_STATUS_DROPPED);
						candidate.setUpdatedBy(jobRequestDto.getUpdatedBy());
						candidate.setUpdatedDateTime(LocalDateTime.now());

						candidateRepository.save(candidate);
					}
				}
			} else {
				jobRequestRepository.save(jobRequest);
			}
		}
	}

	@Override
	public List<JobRequestDto> getAllJobRequests() throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getLatestJobRequests(String lang) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.findTop5ByReferenceNumberRegexAndJobReqStatusIn(".*",
				Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
						HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START),
				HireProUsUtil.orderByCreatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getLatestJobRequestsForVendor(String vendorId, String lang) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository
				.findTop5ByReferenceNumberRegexAndJobReqStatusInAndVendorId(".*",
						Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
								HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START),
						Long.parseLong(vendorId), HireProUsUtil.orderByCreatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getAllJobRequestNumbers() throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.findAll(HireProUsUtil.orderByJRNumberAsc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			if (jobRequest.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS)) {
				JobRequestDto jobRequestDto = new JobRequestDto();
				jobRequestDto.setId(jobRequest.getId());
				jobRequestDto.setReferenceNumber(jobRequest.getReferenceNumber());
				jobRequestDtoList.add(jobRequestDto);
			}
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getJobRequestsByCustomerId(final String customerId, final String lang) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.getJobRequestByCustomerId(Long.parseLong(customerId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getJobRequestsByVendorId(final String vendorId, final String lang) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.getJobRequestByVendorId(Long.parseLong(vendorId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getJobRequestsByBuId(final String buId, final String lang) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.getJobRequestByBuId(Long.parseLong(buId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<String> getActiveJobRequestNumbersByBuId(final String buId, final String lang) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.getJobRequestByBuIdAndJobReqStatusIn(
				Long.parseLong(buId),
				Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
						HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<String> jobRequestNumbers = new ArrayList<String>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestNumbers.add(jobRequest.getReferenceNumber());
		}

		return jobRequestNumbers;
	}

	@Override
	public List<JobRequestDto> getJobRequestsByRoleId(final String roleId, final String lang) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.getJobRequestByRoleId(Long.parseLong(roleId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getJobRequestsByRecruiterId(final String recId, final String lang) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.getJobRequestByRecruiterId(Long.parseLong(recId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public Long getMyJobRequestsCount(final Long userId, final String lang) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();

		criteriaList.add(new Criteria().orOperator(Criteria.where("requesterId").is(userId),
				Criteria.where("recruiterId").is(userId)));
		criteriaList
				.add(Criteria.where("jobReqStatus").nin(Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_TERMINATED,
						HireProUsConstants.JOB_REQUEST_STATUS_CLOSED, HireProUsConstants.JOB_REQUEST_STATUS_HOLD)));

		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			return mongoTemplate.count(searchQuery, JobRequest.class);
		}
		

		return 0L;
	}

	@Override
	public Long getMyJobRequestsCountForVendor(final Long vendorId, final String lang) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();

	//	criteriaList.add(Criteria.where("vendorId").is(vendorId));
		//criteriaList.add(new Criteria().orOperator(Criteria.where("requesterId").is(vendorId),
			//	Criteria.where("recruiterId").is(vendorId)));
		criteriaList
				.add(Criteria.where("jobReqStatus").nin(Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_TERMINATED,
						HireProUsConstants.JOB_REQUEST_STATUS_CLOSED, HireProUsConstants.JOB_REQUEST_STATUS_HOLD)));

		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			return mongoTemplate.count(searchQuery, JobRequest.class);
		}

		return 0L;
	}

	@Override
	public List<JobRequestDto> getMyJobRequests(final Long userId, final String lang) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();

		criteriaList.add(new Criteria().orOperator(Criteria.where("requesterId").is(userId),
				Criteria.where("recruiterId").is(userId)));
		criteriaList
				.add(Criteria.where("jobReqStatus").nin(Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_TERMINATED,
						HireProUsConstants.JOB_REQUEST_STATUS_CLOSED, HireProUsConstants.JOB_REQUEST_STATUS_HOLD)));

		List<JobRequest> jobRequestsList = new ArrayList<JobRequest>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			jobRequestsList = mongoTemplate.find(searchQuery, JobRequest.class);
		}

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		Comparator<JobRequestDto> compareByUpdatedDateTime = Comparator.comparing(JobRequestDto::getUpdatedDateTime);
		jobRequestDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> searchMyJobRequests(final JobRequestSearchDto jobRequestSearchDto, final Long userId,
			final String lang) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();

		criteriaList.add(new Criteria().orOperator(Criteria.where("requesterId").is(userId),
				Criteria.where("recruiterId").is(userId)));
		criteriaList
				.add(Criteria.where("jobReqStatus").nin(Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_TERMINATED,
						HireProUsConstants.JOB_REQUEST_STATUS_CLOSED, HireProUsConstants.JOB_REQUEST_STATUS_HOLD)));

		if (jobRequestSearchDto.getJrNumber() != null && !jobRequestSearchDto.getJrNumber().isEmpty()) {
			criteriaList
					.add(Criteria.where("referenceNumber").regex("(?i).*" + jobRequestSearchDto.getJrNumber() + ".*"));
		}
		if (jobRequestSearchDto.getCustomerId() != null && !jobRequestSearchDto.getCustomerId().equals(0L)) {
			criteriaList.add(Criteria.where("customerId").is(jobRequestSearchDto.getCustomerId()));
		}
		if (jobRequestSearchDto.getBuId() != null && !jobRequestSearchDto.getBuId().equals(0L)) {
			criteriaList.add(Criteria.where("buId").is(jobRequestSearchDto.getBuId()));
		}
		if (jobRequestSearchDto.getRoleId() != null && !jobRequestSearchDto.getRoleId().equals(0L)) {
			criteriaList.add(Criteria.where("roleId").is(jobRequestSearchDto.getRoleId()));
		}
		if (jobRequestSearchDto.getPlacementFor() != null && !jobRequestSearchDto.getPlacementFor().isEmpty()) {
			criteriaList.add(Criteria.where("placementFor").is(jobRequestSearchDto.getPlacementFor()));
		}
		if (jobRequestSearchDto.getEmploymentType() != null && !jobRequestSearchDto.getEmploymentType().isEmpty()) {
			criteriaList.add(Criteria.where("employmentType").is(jobRequestSearchDto.getEmploymentType()));
		}
		if (jobRequestSearchDto.getFromDateTime() != null && jobRequestSearchDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("createdDateTime").gte(jobRequestSearchDto.getFromDateTime())
					.lte(jobRequestSearchDto.getToDateTime()));
		}
		if (jobRequestSearchDto.getJobReqStatus() != null && !jobRequestSearchDto.getJobReqStatus().isEmpty()) {
			criteriaList.add(Criteria.where("jobReqStatus").is(jobRequestSearchDto.getJobReqStatus()));
		}
		if (jobRequestSearchDto.getVendorPriority() != null && !jobRequestSearchDto.getVendorPriority().isEmpty()) {
			criteriaList.add(Criteria.where("vendorPriority").is(jobRequestSearchDto.getVendorPriority()));
		}

		List<JobRequest> jobRequestsList = new ArrayList<JobRequest>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			jobRequestsList = mongoTemplate.find(searchQuery, JobRequest.class);
		}

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		Comparator<JobRequestDto> compareByUpdatedDateTime = Comparator.comparing(JobRequestDto::getUpdatedDateTime);
		jobRequestDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getJobRequestsByStatus(final String status, final String lang) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.getJobRequestByJobReqStatus(status,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> searchJobRequest(final String keyword) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.findByReferenceNumberRegex("(?i).*" + keyword + ".*",
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> searchJobRequest(final JobRequestSearchDto jobRequestSearchDto, final String lang)
			throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();

		if (jobRequestSearchDto.getVendorId() != null && jobRequestSearchDto.getVendorPriority().equals("All")) {
			criteriaList.add(Criteria.where("vendorPriority").is("All"));
		}

		if (jobRequestSearchDto.getJrNumber() != null && !jobRequestSearchDto.getJrNumber().isEmpty()) {
			criteriaList
					.add(Criteria.where("referenceNumber").regex("(?i).*" + jobRequestSearchDto.getJrNumber() + ".*"));
		}
		if (jobRequestSearchDto.getCustomerId() != null && !jobRequestSearchDto.getCustomerId().equals(0L)) {
			criteriaList.add(Criteria.where("customerId").is(jobRequestSearchDto.getCustomerId()));
		}
		if (jobRequestSearchDto.getBuId() != null && !jobRequestSearchDto.getBuId().equals(0L)) {
			criteriaList.add(Criteria.where("buId").is(jobRequestSearchDto.getBuId()));
		}
		if (jobRequestSearchDto.getRoleId() != null && !jobRequestSearchDto.getRoleId().equals(0L)) {
			criteriaList.add(Criteria.where("roleId").is(jobRequestSearchDto.getRoleId()));
		}
		if (jobRequestSearchDto.getRequesterId() != null && !jobRequestSearchDto.getRequesterId().equals(0L)) {
			criteriaList.add(Criteria.where("requesterId").is(jobRequestSearchDto.getRequesterId()));
		}
		if (jobRequestSearchDto.getRecruiterId() != null && !jobRequestSearchDto.getRecruiterId().equals(0L)) {
			criteriaList.add(Criteria.where("recruiterId").is(jobRequestSearchDto.getRecruiterId()));
		}
		if (jobRequestSearchDto.getPlacementFor() != null && !jobRequestSearchDto.getPlacementFor().isEmpty()) {
			criteriaList.add(Criteria.where("placementFor").is(jobRequestSearchDto.getPlacementFor()));
		}
		if (jobRequestSearchDto.getJobReqStatus() != null && !jobRequestSearchDto.getJobReqStatus().isEmpty()) {
			criteriaList.add(Criteria.where("jobReqStatus").is(jobRequestSearchDto.getJobReqStatus()));
		}
		if (jobRequestSearchDto.getEmploymentType() != null && !jobRequestSearchDto.getEmploymentType().isEmpty()) {
			criteriaList.add(Criteria.where("employmentType").is(jobRequestSearchDto.getEmploymentType()));
		}
		if (jobRequestSearchDto.getFromDateTime() != null && jobRequestSearchDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("createdDateTime").gte(jobRequestSearchDto.getFromDateTime())
					.lte(jobRequestSearchDto.getToDateTime()));
		}
//		if (jobRequestSearchDto.getVendorId() != null && !jobRequestSearchDto.getVendorId().equals(0L)) {
//			criteriaList.add(Criteria.where("vendorId").is(jobRequestSearchDto.getVendorId()));
//		}
		if (jobRequestSearchDto.getVendorPriority() != null && !jobRequestSearchDto.getVendorPriority().isEmpty()) {
			criteriaList.add(Criteria.where("vendorPriority").is(jobRequestSearchDto.getVendorPriority()));
		}

		List<JobRequest> jobRequestsList = new ArrayList<JobRequest>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			jobRequestsList = mongoTemplate.find(searchQuery, JobRequest.class);
		}

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		Comparator<JobRequestDto> compareByUpdatedDateTime = Comparator.comparing(JobRequestDto::getUpdatedDateTime);
		jobRequestDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return jobRequestDtoList;
	}

	@Override
	public List<String> getJRNumbersByPlacementFor(final String employmentFor) throws Exception {

		List<JobRequest> jobRequestsList = jobRequestRepository.getJobRequestByPlacementForAndJobReqStatus(
				employmentFor, HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<String> jrNumbersList = new ArrayList<String>();
		for (JobRequest jobRequest : jobRequestsList) {
			jrNumbersList.add(jobRequest.getReferenceNumber());
		}

		return jrNumbersList;
	}

	/**
	 * @param jobRequest
	 * @return
	 * @throws Exception
	 */
	private JobRequestDto getJobRequestDto(final JobRequest jobRequest) throws Exception {

		JobRequestDto jobRequestDto = new JobRequestDto();

		jobRequestDto.setId(jobRequest.getId());
		jobRequestDto.setReferenceNumber(jobRequest.getReferenceNumber());

		jobRequestDto.setCustomerId(jobRequest.getCustomerId());
		jobRequestDto.setCustomerName(customerService.getCustomerNameById(jobRequest.getCustomerId()));
		jobRequestDto.setCustomerNameInDetail(customerService.getCustomerNameInDetailById(jobRequest.getCustomerId()));

		jobRequestDto.setBuId(jobRequest.getBuId());
		jobRequestDto.setBuName(businessUnitService.getBusinessUnitNameById(jobRequest.getBuId()));

		jobRequestDto.setRoleId(jobRequest.getRoleId());
		jobRequestDto.setRoleName(recruitmentRoleService.getRecruitmentRoleNameById(jobRequest.getRoleId()));

		jobRequestDto.setNoOfOpenings(jobRequest.getNoOfOpenings());
		jobRequestDto.setClosedOpenings(jobRequest.getClosedOpenings());

		jobRequestDto.setProgress(jobRequest.getClosedOpenings() != 0
				? (int) Math.round(jobRequest.getClosedOpenings() * 100 / jobRequest.getNoOfOpenings())
				: 0);

		jobRequestDto.setLocation(jobRequest.getLocation());
		jobRequestDto.setPayRange(jobRequest.getPayRange());
		jobRequestDto.setPayFrequency(jobRequest.getPayFrequency());
		jobRequestDto.setCurrency(jobRequest.getCurrency());
		jobRequestDto.setEmploymentType(jobRequest.getEmploymentType());
		jobRequestDto.setContractDuration(jobRequest.getContractDuration());
		jobRequestDto.setMonthOrYear(jobRequest.getMonthOrYear());
		jobRequestDto.setMinYearOfExp(jobRequest.getMinYearOfExp());

		jobRequestDto.setRequesterId(jobRequest.getRequesterId());
		jobRequestDto.setRequesterName(CacheUtil.getUsersMap().get(jobRequest.getRequesterId()));

		jobRequestDto.setPlacementFor(jobRequest.getPlacementFor());
		jobRequestDto.setProjectStartDate(jobRequest.getProjectStartDate());
		jobRequestDto.setRemoteOption(jobRequest.getRemoteOption());

		jobRequestDto.setJobDescription(jobRequest.getJobDescription());
		jobRequestDto.setMandatorySkills(jobRequest.getMandatorySkills());
		jobRequestDto.setOptionalSkills(jobRequest.getOptionalSkills());

		jobRequestDto.setJobReqStatus(jobRequest.getJobReqStatus());

		jobRequestDto.setRecruiterId(jobRequest.getRecruiterId());
		if (jobRequest.getRecruiterId() != null) {
			jobRequestDto.setRecruiterName(CacheUtil.getUsersMap().get(jobRequest.getRecruiterId()));
		}

		jobRequestDto.setCreatedBy(jobRequest.getCreatedBy());
		jobRequestDto.setCreatedByName(CacheUtil.getUsersMap().get(jobRequest.getCreatedBy()));
		jobRequestDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(jobRequest.getCreatedDateTime()));

		jobRequestDto.setUpdatedBy(jobRequest.getUpdatedBy());
		jobRequestDto.setUpdatedByName(CacheUtil.getUsersMap().get(jobRequest.getUpdatedBy()));
		jobRequestDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(jobRequest.getUpdatedDateTime()));

		jobRequestDto.setDaysTotal(Duration
				.between(jobRequest.getCreatedDateTime(), jobRequest.getProjectStartDate().atStartOfDay()).toDays());
		jobRequestDto.setDaysSpent(Duration.between(jobRequest.getCreatedDateTime(), LocalDateTime.now()).toDays());
		jobRequestDto.setDaysLeft(
				Duration.between(LocalDateTime.now(), jobRequest.getProjectStartDate().atStartOfDay()).toDays());

		jobRequestDto.setVendorId(jobRequest.getVendorId());

		jobRequestDto.setVendorPriority(jobRequest.getVendorPriority());
		jobRequestDto.setVendorActionStatus(jobRequest.getVendorActionStatus());

		return jobRequestDto;
	}

	/**
	 * @param jobRequest
	 * @return
	 * @throws Exception
	 */
	private JobRequestDto getJobRequestInfoDto(final JobRequest jobRequest) throws Exception {

		JobRequestDto jobRequestDto = new JobRequestDto();

		jobRequestDto.setId(jobRequest.getId());
		jobRequestDto.setReferenceNumber(jobRequest.getReferenceNumber());
		jobRequestDto.setCustomerName(customerService.getCustomerNameById(jobRequest.getCustomerId()));
		jobRequestDto.setBuId(jobRequest.getBuId());
		jobRequestDto.setBuName(businessUnitService.getBusinessUnitNameById(jobRequest.getBuId()));
		jobRequestDto.setRoleName(recruitmentRoleService.getRecruitmentRoleNameById(jobRequest.getRoleId()));
		jobRequestDto.setNoOfOpenings(jobRequest.getNoOfOpenings());
		jobRequestDto.setClosedOpenings(jobRequest.getClosedOpenings());
		jobRequestDto.setLocation(jobRequest.getLocation());
		jobRequestDto.setEmploymentType(jobRequest.getEmploymentType());
		jobRequestDto.setPlacementFor(jobRequest.getPlacementFor());

		return jobRequestDto;
	}

	@Override
	public JobRequestDto deleteJobRequestById(final String jobRequestId) throws Exception {

		JobRequestDto resultDto = new JobRequestDto();

		JobRequest jobRequest = jobRequestRepository.findById(Long.parseLong(jobRequestId));

		if (jobRequest == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "JobRequest Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (!jobRequest.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START)) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("cannot.delete",
					new String[] { "Job Request - '" + jobRequest.getReferenceNumber() + "' " }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		jobRequestRepository.deleteById(Long.parseLong(jobRequestId));

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public int getUserDependencyCount(Long userId) {

		return jobRequestRepository.countByRequesterIdOrRecruiterIdOrCreatedByOrUpdatedBy(userId, userId, userId,
				userId);
	}

	@Override
	public int getBUDependencyCount(Long buId) {

		return jobRequestRepository.countByBuId(buId);
	}

	@Override
	public int getCustomerDependencyCount(Long customerId) {

		return jobRequestRepository.countByCustomerId(customerId);
	}

	@Override
	public int getVendorDependencyCount(Long vendorId) {

		return jobRequestRepository.countByVendorIdAndJobReqStatusIn(vendorId, Arrays.asList(
				HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START, HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS));
	}

	@Override
	public List<JobRequestDto> searchJobRequestForDownload(final JobRequestSearchDto jobRequestSearchDto,
			final String lang) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();

		if (jobRequestSearchDto.getJrNumber() != null && !jobRequestSearchDto.getJrNumber().isEmpty()) {
			criteriaList
					.add(Criteria.where("referenceNumber").regex("(?i).*" + jobRequestSearchDto.getJrNumber() + ".*"));
		}
		if (jobRequestSearchDto.getBuId() != null && !jobRequestSearchDto.getBuId().equals(0L)) {
			criteriaList.add(Criteria.where("buId").is(jobRequestSearchDto.getBuId()));
		}
		if (jobRequestSearchDto.getJobReqStatus() != null && !jobRequestSearchDto.getJobReqStatus().isEmpty()) {
			criteriaList.add(Criteria.where("jobReqStatus").is(jobRequestSearchDto.getJobReqStatus()));
		}
		if (jobRequestSearchDto.getEmploymentType() != null && !jobRequestSearchDto.getEmploymentType().isEmpty()) {
			criteriaList.add(Criteria.where("employmentType").is(jobRequestSearchDto.getEmploymentType()));
		}
		if (jobRequestSearchDto.getPlacementFor() != null && !jobRequestSearchDto.getPlacementFor().isEmpty()) {
			criteriaList.add(Criteria.where("placementFor").is(jobRequestSearchDto.getPlacementFor()));
		}
		if (jobRequestSearchDto.getVendorPriority() != null && !jobRequestSearchDto.getVendorPriority().equals(0L)) {
			criteriaList.add(Criteria.where("vendorPriority").is(jobRequestSearchDto.getVendorPriority()));
		}
		if (jobRequestSearchDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(jobRequestSearchDto.getFromDateTime()));
		}
		if (jobRequestSearchDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(jobRequestSearchDto.getToDateTime()));
		}

		List<JobRequest> jobRequestsList = new ArrayList<JobRequest>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			jobRequestsList = mongoTemplate.find(searchQuery, JobRequest.class);
		}

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		Comparator<JobRequestDto> compareByUpdatedDateTime = Comparator.comparing(JobRequestDto::getUpdatedDateTime);
		jobRequestDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return jobRequestDtoList;
	}

	@Override
	public byte[] downloadJobRequestDetails(List<JobRequestDto> jobRequestDtoList, String lang) throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/jobRequest_details.xlsx").getFile();
		try (Workbook jobRequestDetailsWB = new XSSFWorkbook(file)) {

			Sheet sheet = jobRequestDetailsWB.getSheetAt(0);
			HireProUsDefaultMethods.cleanSheet(sheet);
			int rowNum = 2;
			for (JobRequestDto jobRequestDto : jobRequestDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(jobRequestDto.getReferenceNumber());
				dataRow.createCell(2).setCellValue(jobRequestDto.getCustomerName());
				dataRow.createCell(3).setCellValue(jobRequestDto.getBuName());
				dataRow.createCell(4).setCellValue(jobRequestDto.getRoleName());
				dataRow.createCell(5).setCellValue(jobRequestDto.getLocation());
				dataRow.createCell(6).setCellValue(jobRequestDto.getPayRange());
				dataRow.createCell(7).setCellValue(jobRequestDto.getPayFrequency());
				dataRow.createCell(8).setCellValue(jobRequestDto.getCurrency());
				dataRow.createCell(9).setCellValue(jobRequestDto.getEmploymentType());
				dataRow.createCell(10).setCellValue(jobRequestDto.getContractDuration());
				dataRow.createCell(11).setCellValue(jobRequestDto.getPlacementFor());
				dataRow.createCell(12).setCellValue(jobRequestDto.getProjectStartDate().toString());
				dataRow.createCell(13).setCellValue(jobRequestDto.getRemoteOption());
				dataRow.createCell(14).setCellValue(jobRequestDto.getJobDescription());
				dataRow.createCell(15).setCellValue(jobRequestDto.getMandatorySkills());
				dataRow.createCell(16).setCellValue(jobRequestDto.getOptionalSkills());
				dataRow.createCell(17).setCellValue(jobRequestDto.getJobReqStatus());
				dataRow.createCell(18).setCellValue(jobRequestDto.getRecruiterName());
				dataRow.createCell(19).setCellValue(jobRequestDto.getRequesterName());
				dataRow.createCell(20)
						.setCellValue(jobRequestDto.getVendorPriority().equals("Primary") ? "Primary Vendor"
								: "Primary & Secondary Vendor");
				dataRow.createCell(21).setCellValue(jobRequestDto.getVendorActionStatus());
				dataRow.createCell(22).setCellValue(jobRequestDto.getCreatedDateTime().toLocalDate().toString());
				dataRow.createCell(23).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			jobRequestDetailsWB.write(outputStream);

			jobRequestDetailsWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during JobRequest Details download file", ex);
			return null;
		}
	}

	@Override
	public List<JobRequestDto> getJobRequestsByVendorPriority(final String vendorPriority, final String lang)
			throws Exception {

		List<JobRequest> jobRequestsList = new ArrayList<JobRequest>();

		if (vendorPriority.equals("Primary")) {
			jobRequestsList = jobRequestRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		} else {
			jobRequestsList = jobRequestRepository.getJobRequestByVendorPriority((vendorPriority),
					HireProUsUtil.orderByUpdatedDateTimeDesc());
		}

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

}
