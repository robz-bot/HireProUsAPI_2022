/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.formula.functions.Today;
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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.HireProUsDefaultMethods;
import com.promantus.hireprous.dto.CandidateDto;
import com.promantus.hireprous.dto.CandidateStatusDto;
import com.promantus.hireprous.dto.CandidatesCountDto;
import com.promantus.hireprous.dto.EvaluateResumeDto;
import com.promantus.hireprous.dto.InterviewScheduleDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.ResumeDto;
import com.promantus.hireprous.entity.Candidate;
import com.promantus.hireprous.entity.EvaluateResume;
import com.promantus.hireprous.entity.InterviewSchedule;
import com.promantus.hireprous.entity.JobRequest;
import com.promantus.hireprous.entity.RecruitmentRole;
import com.promantus.hireprous.repository.CandidateRepository;
import com.promantus.hireprous.repository.InterviewScheduleRepository;
import com.promantus.hireprous.repository.JobRequestRepository;
import com.promantus.hireprous.repository.RecruitmentRoleRepository;
import com.promantus.hireprous.service.CandidateService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.ImageService;
import com.promantus.hireprous.service.InterviewScheduleService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.service.MailService;
import com.promantus.hireprous.service.ResourceMgmtService;
import com.promantus.hireprous.service.ResumeService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class CandidateServiceImpl implements CandidateService {

	private static final Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);
	
	@Autowired
	RecruitmentRoleRepository recruitmentRepository;

	@Autowired
	CommonService commonService;

	@Autowired
	MailService mailService;

	@Autowired
	ImageService imageService;

	@Autowired
	ResumeService resumeService;

	@Autowired
	InterviewScheduleService interviewScheduleService;

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	ResourceMgmtService resourceMgmtService;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	CandidateRepository candidateRepository;

	
	@Autowired
	JobRequestRepository jobRequestRepository;
	
	@Autowired
	InterviewScheduleRepository interviewScheduleRepository;

	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public CandidateDto addCandidate(final CandidateDto candidateDto, String lang) throws Exception {

		CandidateDto resultDto = new CandidateDto();
		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(Criteria.where("jrNumber").is(candidateDto.getJrNumber()));
		criteriaList
				.add(new Criteria().orOperator(Criteria.where("email").regex("(?i).*" + candidateDto.getEmail() + ".*"),
						Criteria.where("contactNumber").is(candidateDto.getContactNumber())));

		Query searchQuery = new Query();
		searchQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
		List<Candidate> candidatesListCheck = mongoTemplate.find(searchQuery, Candidate.class);

		if (candidatesListCheck != null && candidatesListCheck.size() > 0) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists",
					new String[] { "JR Number, Email and/or Contact Number" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		String jobRecStatus = "";
		List<Candidate> candidatesList = candidateRepository.findByJrNumber(candidateDto.getJrNumber(),
				HireProUsUtil.orderByUpdatedDateTimeDesc());
		if (candidatesList == null || candidatesList.size() <= 0) {
			jobRecStatus = HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS;
		}

		Candidate candidate = new Candidate();
		candidate.setId(commonService.nextSequenceNumber());
		candidate.setJrNumber(candidateDto.getJrNumber());
		candidate.setFirstName(candidateDto.getFirstName());
		candidate.setLastName(candidateDto.getLastName());

		candidate.setEmail(candidateDto.getEmail());
		candidate.setContactNumber(candidateDto.getContactNumber());
		candidate.setSex(candidateDto.getSex());
		candidate.setSkillSet(candidateDto.getSkillSet());

		candidate.setExperience(candidateDto.getExperience());
		candidate.setCurrentCompany(candidateDto.getCurrentCompany());
		candidate.setCandidateType(candidateDto.getCandidateType());
		candidate.setCurrentDesignation(candidateDto.getCurrentDesignation());

		candidate.setRecStatus(HireProUsConstants.REC_STATUS_UPLOADED);

		candidate.setCreatedBy(candidateDto.getCreatedBy());
		candidate.setUpdatedBy(candidateDto.getUpdatedBy());
		candidate.setCreatedDateTime(LocalDateTime.now());
		candidate.setUpdatedDateTime(LocalDateTime.now());

		if (candidateDto.getIsBench() == 1) {
			candidate.setResourceId(candidateDto.getResourceId());
		}

		candidate.setVendorId(candidateDto.getVendorId());

		candidateRepository.save(candidate);

		CacheUtil.getCandidatesMap().put(candidate.getId(), candidate.getFirstName() + " " + candidate.getLastName());

		// Update in Interview schedule for uploaded.
		InterviewScheduleDto interviewScheduleDto = new InterviewScheduleDto();
		interviewScheduleDto.setJrNumber(candidate.getJrNumber());
		interviewScheduleDto.setCandidateId(candidate.getId());
		interviewScheduleDto.setInterviewerId(candidate.getUpdatedBy());
		interviewScheduleDto.setScheduleDateTime(LocalDateTime.now());

		interviewScheduleDto.setDuration("0");
		interviewScheduleDto.setScheduleRemarks("For Resume Shortlist");

		interviewScheduleDto.setMode("Remote");
		interviewScheduleDto.setVenue("System");

		interviewScheduleDto.setRound(HireProUsConstants.INTERVIEW_ROUND_INITIAL);
		interviewScheduleDto.setRecStatus(HireProUsConstants.REC_STATUS_UPLOADED);

		interviewScheduleDto.setCreatedBy(candidate.getCreatedBy());
		interviewScheduleDto.setUpdatedBy(candidate.getUpdatedBy());

		interviewScheduleService.addInterviewSchedule(interviewScheduleDto, lang);

		// Update JR status.
		if (!jobRecStatus.isEmpty()) {
			jobRequestService.updateJRStatusByJRNumber(candidate.getJrNumber(), jobRecStatus, candidate.getUpdatedBy(),
					lang);
		}

		// Send New Candidate Uploaded Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendNewCandidateUploaded(candidate);
				} catch (Exception e) {

					logger.error("New Candidate Upload Email is not Sent.");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

//		if (candidateDto.getIsBench() == 1) {
//			resourceMgmtService.updateProjectAllocationById(candidateDto.getResourceId(), candidate.getUpdatedBy());
//		}

		resultDto.setId(candidate.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public CandidateDto updateCandidate(final CandidateDto candidateDto, final String lang) throws Exception {

		CandidateDto resultDto = new CandidateDto();

		Candidate candidate = candidateRepository.findById(candidateDto.getId());

		if (candidate == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Candidate Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		// Added On 11/16/2021 - Bug- Candidate is not able to update

		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(Criteria.where("jrNumber").is(candidateDto.getJrNumber()));
		criteriaList.add(Criteria.where("email").regex("(?i).*" + candidateDto.getEmail() + ".*"));
		criteriaList.add(Criteria.where("contactNumber").is(candidateDto.getContactNumber()));

		Query searchQuery = new Query();
		searchQuery.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
		List<Candidate> candidatesListCheck = mongoTemplate.find(searchQuery, Candidate.class);

		if (candidatesListCheck != null && candidatesListCheck.size() > 0) {
			for (Candidate candidate2 : candidatesListCheck) {
				if (candidateDto.getId().equals(candidate2.getId())) {
					/*if (candidateDto.getEmail().equals(candidate2.getEmail())) {
						resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
						resultDto
								.setMessage(commonService.getMessage("already.exists", new String[] { "Email" }, lang));

						logger.info(resultDto.getMessage());
						return resultDto;
					}
					if (candidateDto.getContactNumber().equals(candidate2.getContactNumber())) {
						resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
						resultDto.setMessage(
								commonService.getMessage("already.exists", new String[] { "Contact Number" }, lang));

						logger.info(resultDto.getMessage());
						return resultDto;
						// }
					}
					if (candidateDto.getJrNumber().equals(candidate2.getJrNumber())) {
						resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
						resultDto
								.setMessage(commonService.getMessage("already.exists", new String[] { "Jr Number" }, lang));

						logger.info(resultDto.getMessage());
						return resultDto;
					}*/
				}
			}
		}

//		final List<Criteria> criteriaList = new ArrayList<>();
//		criteriaList.add(Criteria.where("jrNumber").is(candidateDto.getJrNumber()));
//		criteriaList
//				.add(new Criteria().orOperator(Criteria.where("email").regex("(?i).*" + candidateDto.getEmail() + ".*"),
//						Criteria.where("contactNumber").is(candidateDto.getContactNumber())));
//
//		Query searchQuery = new Query();
//		searchQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
//		List<Candidate> candidatesListCheck = mongoTemplate.find(searchQuery, Candidate.class);

//		if (candidatesListCheck != null && candidatesListCheck.size() > 0
//				&& !candidateDto.getId().equals(candidatesListCheck.get(0).getId())) {

//		if (candidatesListCheck != null && candidatesListCheck.size() > 0) {
//			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
//			resultDto.setMessage(commonService.getMessage("already.exists",
//					new String[] { "JR Number, Email and/or Contact Number" }, lang));
//
//			logger.info(resultDto.getMessage());
//			return resultDto;
//		}

		candidate.setFirstName(candidateDto.getFirstName());
		candidate.setLastName(candidateDto.getLastName());

		candidate.setEmail(candidateDto.getEmail());
		candidate.setContactNumber(candidateDto.getContactNumber());
		candidate.setSex(candidateDto.getSex());
		candidate.setSkillSet(candidateDto.getSkillSet());

		candidate.setExperience(candidateDto.getExperience());
		candidate.setCurrentCompany(candidateDto.getCurrentCompany());
		candidate.setCandidateType(candidateDto.getCandidateType());
		candidate.setCurrentDesignation(candidateDto.getCurrentDesignation());

		candidate.setRecStatus(candidateDto.getRecStatus());

		candidate.setVendorId(candidateDto.getVendorId());

		candidate.setUpdatedBy(candidateDto.getUpdatedBy());
		candidate.setUpdatedDateTime(LocalDateTime.now());

		candidateRepository.save(candidate);

		CacheUtil.getCandidatesMap().remove(candidate.getId());
		CacheUtil.getCandidatesMap().put(candidate.getId(), candidate.getFirstName() + " " + candidate.getLastName());

		// Send Candidate Updated Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendCandidateUpdated(candidate);
				} catch (Exception e) {

					logger.error("Candidate Updated Email is not Sent.");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setId(candidate.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public CandidateDto updateRecStatus(final CandidateDto candidateDto, final String lang) throws Exception {

		CandidateDto resultDto = new CandidateDto();

		Candidate candidate = candidateRepository.findById(candidateDto.getId());

		if (candidate == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Candidate Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		candidate.setRecStatus(candidateDto.getRecStatus());

		candidate.setUpdatedBy(candidateDto.getUpdatedBy());
		candidate.setUpdatedDateTime(LocalDateTime.now());

		candidateRepository.save(candidate);

		// Update in Interview schedule for status update.
		InterviewScheduleDto interviewScheduleDto = new InterviewScheduleDto();
		interviewScheduleDto.setJrNumber(candidateDto.getJrNumber());
		interviewScheduleDto.setCandidateId(candidateDto.getId());

		interviewScheduleDto.setRecStatus(candidateDto.getRecStatus());
		interviewScheduleDto.setResultRemarks(candidateDto.getRemarks());

		interviewScheduleDto.setCreatedBy(candidateDto.getCreatedBy());
		interviewScheduleDto.setUpdatedBy(candidateDto.getUpdatedBy());

		interviewScheduleService.updateShortlistedResult(interviewScheduleDto, lang);

		// Send Resume Shortlisted Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendResumeShortlistedEmail(candidate, candidateDto.getRemarks());
				} catch (Exception e) {

					logger.error("Email for Resume shortlist is not Sent.");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public CandidateDto updateRecStatus(final Long candidateId, final String recStatus, final Long updatedBy)
			throws Exception {

		CandidateDto resultDto = new CandidateDto();

		Candidate candidate = candidateRepository.findById(candidateId);

		if (candidate == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Candidate Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		candidate.setRecStatus(recStatus);

		candidate.setUpdatedBy(updatedBy);
		candidate.setUpdatedDateTime(LocalDateTime.now());

		candidateRepository.save(candidate);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public CandidateDto getCandidateById(final String candidateId) throws Exception {

		Candidate candidate = candidateRepository.findById(Long.parseLong(candidateId));

		return candidate != null ? this.getCandidateDto(candidate, true) : null;
	}

	@Override
	public CandidateDto getCandidateShortInfoById(final String candidateId) throws Exception {

		Candidate candidate = candidateRepository.findById(Long.parseLong(candidateId));

		return candidate != null ? this.getCandidateShortInfoDto(candidate) : null;
	}

	@Override
	public String getCandidateNameById(final long userId) throws Exception {

		Candidate candidate = candidateRepository.findById(userId);

		return candidate != null ? candidate.getFirstName() + " " + candidate.getLastName() : "";
	}

	@Override
	public String getCandidateEmailById(final long userId) throws Exception {

		Candidate candidate = candidateRepository.findById(userId);

		return candidate != null ? candidate.getEmail() : null;
	}

	@Override
	public List<CandidateDto> getAllCandidates() throws Exception {

		List<Candidate> candidatesList = candidateRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		return candidateDtoList;
	}

	@Override
	public List<CandidateDto> getCandidatesByJRNum(final String jrNumber, final String lang) throws Exception {

		List<Candidate> candidatesList = candidateRepository.findByJrNumber(jrNumber,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		return candidateDtoList;
	}

	@Override
	public List<CandidateDto> getCandidatesByJRNumAndRecStatus(final String jrNumber, final String recStatus,
			final String lang) throws Exception {

		List<Candidate> candidatesList = candidateRepository.findByJrNumberAndRecStatus(jrNumber, recStatus,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		return candidateDtoList;
	}

	@Override
	public List<CandidateDto> getCandidatesByRecStatus(final String recStatus, final String lang) throws Exception {

		List<Candidate> candidatesList = candidateRepository.findByRecStatus(recStatus,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		return candidateDtoList;
	}

	@Override
	public List<CandidateDto> getCandidatesByRecStatusList(final List<String> recStatusList, final String vendorId,
			final String lang) throws Exception {

		List<Candidate> candidatesList = new ArrayList<Candidate>();

		if (vendorId == null || vendorId.isEmpty()) {
			candidatesList = candidateRepository.findByRecStatusIn(recStatusList,
					HireProUsUtil.orderByUpdatedDateTimeDesc());
		} else {
			candidatesList = candidateRepository.findByRecStatusInAndVendorId(recStatusList, Long.parseLong(vendorId),
					HireProUsUtil.orderByUpdatedDateTimeDesc());
		}

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		return candidateDtoList;
	}

	@Override
	public CandidatesCountDto getCandidatesCount(final String vendorId, final String lang) throws Exception {

		CandidatesCountDto resultDto = new CandidatesCountDto();

		List<Candidate> candidatesList = new ArrayList<Candidate>();

		if (vendorId == null || vendorId.isEmpty()) {
			candidatesList = candidateRepository.findAll();
		} else {
			candidatesList = candidateRepository.findByVendorId(Long.parseLong(vendorId));
		}

		resultDto.setTotalTagged((long) candidatesList.size());

		long uploaded = 0;
		long shortlisted = 0;
		long holded = 0;
		long rejected = 0;
		long selected = 0;
		long onboarded = 0;
		long dropped = 0;
		for (Candidate candidate : candidatesList) {

			String recStatus = candidate.getRecStatus();
			if (recStatus.equals(HireProUsConstants.REC_STATUS_UPLOADED)) {
				uploaded += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_SHORTLISTED_0)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_R1)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_R2)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_CR3)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_HR4)) {
				shortlisted += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_0)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_R1)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_R2)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_CR3)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_HR4)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_BU)) {
				holded += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_0)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_R1)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_R2)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_CR3)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_HR4)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_BU)) {
				rejected += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_SELECTED)) {
				selected += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_ONBOARDED)) {
				onboarded += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_DROPPED)) {
				dropped += 1;
			}
		}

		resultDto.setUploaded(uploaded);
		resultDto.setShortlisted(shortlisted);
		resultDto.setHold(holded);
		resultDto.setRejected(rejected);
		resultDto.setSelected(selected);
		resultDto.setOnboarded(onboarded);
		resultDto.setDropped(dropped);

		return resultDto;
	}

	@Override
	public List<CandidateDto> getCandidatesByJRNumAndRecStatusList(final String jrNumber,
			final List<String> recStatusList, final String lang) throws Exception {

		List<Candidate> candidatesList = candidateRepository.findByJrNumberAndRecStatusIn(jrNumber, recStatusList,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		return candidateDtoList;
	}

	@Override
	public List<CandidateDto> searchCandidate(final String candidateName) throws Exception {

		AggregationOperation project = Aggregation.project(Candidate.class)
				.andExpression("concat(firstName,' ', lastName)").as("fullName");
		AggregationOperation match = Aggregation
				.match(Criteria.where("fullName").regex("(?i).*" + candidateName + ".*"));
		Aggregation aggregation = Aggregation.newAggregation(project, match);
		List<Candidate> candidatesList = mongoTemplate.aggregate(aggregation, Candidate.class, Candidate.class)
				.getMappedResults();

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		return candidateDtoList;
	}

	@Override
	public List<Long> searchCadidateIdsByName(final String candidateName) throws Exception {

		AggregationOperation project = Aggregation.project(Candidate.class)
				.andExpression("concat(firstName,' ', lastName)").as("fullName");
		AggregationOperation match = Aggregation
				.match(Criteria.where("fullName").regex("(?i).*" + candidateName + ".*"));
		Aggregation aggregation = Aggregation.newAggregation(project, match);
		List<Candidate> candidatesList = mongoTemplate.aggregate(aggregation, Candidate.class, Candidate.class)
				.getMappedResults();

		List<Long> candidateIds = new ArrayList<Long>();
		for (Candidate candidate : candidatesList) {
			candidateIds.add(candidate.getId());
		}

		return candidateIds;
	}

	@Override
	public List<CandidateDto> searchCandidateByRecStatusList(final String candidateName, final String jrNumber,
			final List<String> recStatusList, final String vendorId) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();
		if (candidateName != null && !candidateName.isEmpty()) {

			AggregationOperation project = Aggregation.project(Candidate.class)
					.andExpression("concat(firstName,' ', lastName)").as("fullName");
			AggregationOperation match = Aggregation
					.match(Criteria.where("fullName").regex("(?i).*" + candidateName + ".*"));
			Aggregation aggregation = Aggregation.newAggregation(project, match);
			List<Candidate> candidatesList = mongoTemplate.aggregate(aggregation, Candidate.class, Candidate.class)
					.getMappedResults();

			List<Long> candidateIds = new ArrayList<Long>();
			for (Candidate candidate : candidatesList) {
				candidateIds.add(candidate.getId());
			}

			if (candidateIds.size() > 0) {
				criteriaList.add(Criteria.where("id").in(candidateIds));
			} else {
				return new ArrayList<CandidateDto>();
			}
		}
		if (jrNumber != null && !jrNumber.isEmpty()) {
			criteriaList.add(Criteria.where("jrNumber").regex("(?i).*" + jrNumber + ".*"));
		}
		if (recStatusList != null && recStatusList.size() > 0) {
			if (recStatusList.get(0) != null) {
				criteriaList.add(Criteria.where("recStatus").in(recStatusList));
			}
		}
		if (!vendorId.equals("null")) {
			criteriaList.add(Criteria.where("vendorId").is(Long.parseLong(vendorId)));
		}

		List<Candidate> candidatesList = new ArrayList<Candidate>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			candidatesList = mongoTemplate.find(searchQuery, Candidate.class);
		}

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		Comparator<CandidateDto> compareByUpdatedDateTime = Comparator.comparing(CandidateDto::getUpdatedDateTime);
		candidateDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return candidateDtoList;
	}

	/**
	 * @param candidate
	 * @return
	 * @throws Exception
	 */
	private CandidateDto getCandidateDto(final Candidate candidate, final boolean withImageResume) throws Exception {

		CandidateDto candidateDto = new CandidateDto();

		candidateDto.setId(candidate.getId());

		candidateDto.setJrNumber(candidate.getJrNumber());
		candidateDto.setFirstName(candidate.getFirstName());
		candidateDto.setLastName(candidate.getLastName());
		candidateDto.setFullName(candidate.getFirstName() + " " + candidate.getLastName());

		candidateDto.setEmail(candidate.getEmail());
		candidateDto.setContactNumber(candidate.getContactNumber());
		candidateDto.setSex(candidate.getSex());
		candidateDto.setSkillSet(candidate.getSkillSet());

		candidateDto.setExperience(candidate.getExperience());
		candidateDto.setCurrentCompany(candidate.getCurrentCompany());
		candidateDto.setCandidateType(candidate.getCandidateType());

		candidateDto.setRecStatus(candidate.getRecStatus());
		candidateDto.setCurrentDesignation(candidate.getCurrentDesignation());

		if (withImageResume) {
			candidateDto.setImage(imageService.getImage(HireProUsConstants.CANDIDATE_IMAGE_PREFIX + candidate.getId()));

			ResumeDto resumeDto = resumeService
					.getResumeByName(HireProUsConstants.CANDIDATE_RESUME_PREFIX + candidate.getId());
			if (resumeDto != null) {
				candidateDto.setResumeName(resumeDto.getResumeName() + "." + resumeDto.getResumeType());
			}
		}

//addded on 12/30/2021 - Scenario - Candidate initated and updated by vendor
		if (candidate.getVendorId() != null) {
			candidateDto.setCreatedByName(CacheUtil.getVendorsMap().get(candidate.getCreatedBy()));
		} else {
			candidateDto.setCreatedByName(CacheUtil.getUsersMap().get(candidate.getCreatedBy()));
		}
		if (candidate.getVendorId() != null && candidate.getVendorId().equals(candidate.getUpdatedBy())) {
			candidateDto.setUpdatedByName(CacheUtil.getVendorsMap().get(candidate.getUpdatedBy()));
		} else {
			candidateDto.setUpdatedByName(CacheUtil.getUsersMap().get(candidate.getUpdatedBy()));
		}

		candidateDto.setCreatedBy(candidate.getCreatedBy());
//		candidateDto.setCreatedByName(CacheUtil.getUsersMap().get(candidate.getCreatedBy()));
		candidateDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(candidate.getCreatedDateTime()));

		candidateDto.setUpdatedBy(candidate.getUpdatedBy());
//		candidateDto.setUpdatedByName(CacheUtil.getUsersMap().get(candidate.getUpdatedBy()));
		candidateDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(candidate.getUpdatedDateTime()));

		if (candidate.getResourceId() != null) {
			candidateDto.setResourceId(candidate.getResourceId());
		}

		if (candidate.getVendorId() != null) {
			candidateDto.setVendorId(candidate.getVendorId());
			candidateDto.setVendorName(CacheUtil.getVendorsMap().get(candidate.getVendorId()));
		}

		JobRequestDto jobRequestDto = jobRequestService.getJobRequestInfoByJRNumber(candidate.getJrNumber());

		if (jobRequestDto != null) {
			candidateDto.setJrId(jobRequestDto.getId());
			candidateDto.setCustomerName(jobRequestDto.getCustomerName());
			candidateDto.setBuId(jobRequestDto.getBuId());
			candidateDto.setBuName(jobRequestDto.getBuName());
			candidateDto.setRoleName(jobRequestDto.getRoleName());
			candidateDto.setNoOfOpenings(jobRequestDto.getNoOfOpenings());
			candidateDto.setLocation(jobRequestDto.getLocation());
			candidateDto.setEmploymentType(jobRequestDto.getEmploymentType());
			candidateDto.setPlacementFor(jobRequestDto.getPlacementFor());
		}

		if (HireProUsConstants.REC_STATUS_SHORTLISTED_0.equals(candidateDto.getRecStatus())) {
			candidateDto.setRecStatusDisplay("Resume Shortlisted");
		}
		if (HireProUsConstants.REC_STATUS_PASSED_R1.equals(candidateDto.getRecStatus())) {
			candidateDto.setRecStatusDisplay("Passed @ IR1");
		}
		if (HireProUsConstants.REC_STATUS_PASSED_R2.equals(candidateDto.getRecStatus())) {
			candidateDto.setRecStatusDisplay("Passed @ IR2");
			if (HireProUsConstants.PLACEMENT_FOR_CUSTOMER.equals(candidateDto.getPlacementFor())) {
				candidateDto.setRecStatusDisplay("Passed @ IR1 (IR2 Skipped)");
			}
		}
		if (HireProUsConstants.REC_STATUS_PASSED_CR3.equals(candidateDto.getRecStatus())) {
			candidateDto.setRecStatusDisplay("Passed @ CR");
			if (HireProUsConstants.PLACEMENT_FOR_INTERNAL.equals(candidateDto.getPlacementFor())) {
				candidateDto.setRecStatusDisplay("Passed @ IR2 (CR Skipped)");
			}
		}
		if (HireProUsConstants.REC_STATUS_PASSED_HR4.equals(candidateDto.getRecStatus())) {
			candidateDto.setRecStatusDisplay("Passed @ HR");
			if (HireProUsConstants.CANDIDATE_TYPE_INTERNAL.equals(candidateDto.getCandidateType())) {
				candidateDto.setRecStatusDisplay("Passed @ CR (HR Skipped)");
			}
		}

		return candidateDto;
	}

	/**
	 * @param candidate
	 * @return
	 * @throws Exception
	 */
	private CandidateDto getCandidateShortInfoDto(final Candidate candidate) throws Exception {

		CandidateDto candidateDto = new CandidateDto();

		candidateDto.setId(candidate.getId());

		candidateDto.setJrNumber(candidate.getJrNumber());
		candidateDto.setFirstName(candidate.getFirstName());
		candidateDto.setLastName(candidate.getLastName());
		candidateDto.setFullName(candidate.getFirstName() + " " + candidate.getLastName());

		candidateDto.setEmail(candidate.getEmail());
		candidateDto.setContactNumber(candidate.getContactNumber());
		candidateDto.setSex(candidate.getSex());
		candidateDto.setSkillSet(candidate.getSkillSet());

		candidateDto.setExperience(candidate.getExperience());
		candidateDto.setCurrentCompany(candidate.getCurrentCompany());
		candidateDto.setCandidateType(candidate.getCandidateType());

		candidateDto.setRecStatus(candidate.getRecStatus());

		ResumeDto resumeDto = resumeService
				.getResumeByName(HireProUsConstants.CANDIDATE_RESUME_PREFIX + candidate.getId());
		if (resumeDto != null) {
			candidateDto.setResumeName(resumeDto.getResumeName() + "." + resumeDto.getResumeType());
		}

		if (candidate.getResourceId() != null) {
			candidateDto.setResourceId(candidate.getResourceId());
		}

		return candidateDto;
	}

	@Override
	public CandidateDto deleteCandidateById(final String candidateId) throws Exception {

		CandidateDto resultDto = new CandidateDto();

		Candidate candidate = candidateRepository.findById(Long.parseLong(candidateId));

		if (candidate == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Candidate Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (!candidate.getRecStatus().equals(HireProUsConstants.REC_STATUS_UPLOADED)) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("cannot.delete",
					new String[] { "Candidate - '" + candidate.getFirstName() + " " + candidate.getLastName() + "' " },
					null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		resumeService.deleteResumeByName(HireProUsConstants.CANDIDATE_RESUME_PREFIX + candidateId);
		imageService.deleteImageByName(HireProUsConstants.CANDIDATE_IMAGE_PREFIX + candidateId);
		candidateRepository.deleteById(Long.parseLong(candidateId));

		CacheUtil.getCandidatesMap().remove(candidate.getId());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<CandidateDto> getCandidatesByRecStatusList(List<String> passedCandidateList) {
		List<Candidate> candidatesList = candidateRepository.findByRecStatusIn(passedCandidateList,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			try {
				candidateDtoList.add(this.getCandidateDto(candidate, false));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return candidateDtoList;

	}

	@Override
	public List<CandidateDto> getCandidatesByJRNumAndCandidateId(String jrNum, final String candidateId, String lang) {
		List<Candidate> candidatesList = candidateRepository.findByJrNumberAndId(jrNum, Long.parseLong(candidateId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();
		for (Candidate candidate : candidatesList) {
			try {
				candidateDtoList.add(this.getCandidateDto(candidate, false));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return candidateDtoList;
	}

	@Override
	public List<CandidateStatusDto> getAllCandidateStages(final String lang) throws Exception {

		List<Candidate> candidatesList = candidateRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateStatusDto> candidateStatusDtoList = new ArrayList<CandidateStatusDto>();

		int recStatusUploaded = 0;
		int recStatusShortListed = 0;
		int recStatusHolded = 0;
		int recStatusRejected = 0;
		int recStatusScheduledR1 = 0;
		int recStatusPassedR1 = 0;
		int recStatusHoldedR1 = 0;
		int recStatusRejectedR1 = 0;
		int recStatusScheduledR2 = 0;
		int recStatusPassedR2 = 0;
		int recStatusHoldedR2 = 0;
		int recStatusRejectedR2 = 0;
		int recStatusScheduledCR3 = 0;
		int recStatusPassedCR3 = 0;
		int recStatusHoldedCR3 = 0;
		int recStatusRejectedCR3 = 0;
		int recStatusScheduledHR4 = 0;
		int recStatusPassedHR4 = 0;
		int recStatusHoldedHR4 = 0;
		int recStatusRejectedHR4 = 0;
		int recStatusScheduledBU = 0;
		int recStatusApprovedBU = 0;
		int recStatusHoldedBU = 0;
		int recStatusRejectedBU = 0;
		int recStatusSelected = 0;
		int recStatusOnBoarded = 0;

		for (Candidate candidate : candidatesList) {
			if (candidate.getRecStatus().equals("00")) {
				recStatusUploaded++;
			}
			if (candidate.getRecStatus().equals("01")) {
				recStatusShortListed++;
			}
			if (candidate.getRecStatus().equals("02")) {
				recStatusHolded++;
			}
			if (candidate.getRecStatus().equals("03")) {
				recStatusRejected++;
			}
			if (candidate.getRecStatus().equals("04")) {
				recStatusScheduledR1++;
			}
			if (candidate.getRecStatus().equals("05")) {
				recStatusPassedR1++;
			}
			if (candidate.getRecStatus().equals("06")) {
				recStatusHoldedR1++;
			}
			if (candidate.getRecStatus().equals("07")) {
				recStatusRejectedR1++;
			}
			if (candidate.getRecStatus().equals("08")) {
				recStatusScheduledR2++;
			}
			if (candidate.getRecStatus().equals("09")) {
				recStatusPassedR2++;
			}
			if (candidate.getRecStatus().equals("10")) {
				recStatusHoldedR2++;
			}
			if (candidate.getRecStatus().equals("11")) {
				recStatusRejectedR2++;
			}
			if (candidate.getRecStatus().equals("12")) {
				recStatusScheduledCR3++;
			}
			if (candidate.getRecStatus().equals("13")) {
				recStatusPassedCR3++;
			}
			if (candidate.getRecStatus().equals("14")) {
				recStatusHoldedCR3++;
			}
			if (candidate.getRecStatus().equals("15")) {
				recStatusRejectedCR3++;
			}
			if (candidate.getRecStatus().equals("16")) {
				recStatusScheduledHR4++;
			}
			if (candidate.getRecStatus().equals("17")) {
				recStatusPassedHR4++;
			}
			if (candidate.getRecStatus().equals("18")) {
				recStatusHoldedHR4++;
			}
			if (candidate.getRecStatus().equals("19")) {
				recStatusRejectedHR4++;
			}
			if (candidate.getRecStatus().equals("20")) {
				recStatusScheduledBU++;
			}
			if (candidate.getRecStatus().equals("21")) {
				recStatusApprovedBU++;
			}
			if (candidate.getRecStatus().equals("22")) {
				recStatusHoldedBU++;
			}
			if (candidate.getRecStatus().equals("23")) {
				recStatusRejectedBU++;
			}
			if (candidate.getRecStatus().equals("24")) {
				recStatusSelected++;
			}
			if (candidate.getRecStatus().equals("25")) {
				recStatusOnBoarded++;
			}
		}
		CandidateStatusDto candidateStatusDto = new CandidateStatusDto();
		candidateStatusDto.setRecStatusUploaded(recStatusUploaded);
		candidateStatusDto.setRecStatusShortListed(recStatusShortListed);
		candidateStatusDto.setRecStatusHolded(recStatusHolded);
		candidateStatusDto.setRecStatusRejected(recStatusRejected);
		candidateStatusDto.setRecStatusScheduledR1(recStatusScheduledR1);
		candidateStatusDto.setRecStatusPassedR1(recStatusPassedR1);
		candidateStatusDto.setRecStatusHoldedR1(recStatusHoldedR1);
		candidateStatusDto.setRecStatusRejectedR1(recStatusRejectedR1);
		candidateStatusDto.setRecStatusScheduledR2(recStatusScheduledR2);
		candidateStatusDto.setRecStatusPassedR2(recStatusPassedR2);
		candidateStatusDto.setRecStatusHoldedR2(recStatusHoldedR2);
		candidateStatusDto.setRecStatusRejectedR2(recStatusRejectedR2);
		candidateStatusDto.setRecStatusScheduledCR3(recStatusScheduledCR3);
		candidateStatusDto.setRecStatusPassedCR3(recStatusPassedCR3);
		candidateStatusDto.setRecStatusHoldedCR3(recStatusHoldedCR3);
		candidateStatusDto.setRecStatusRejectedCR3(recStatusRejectedCR3);
		candidateStatusDto.setRecStatusScheduledHR4(recStatusScheduledHR4);
		candidateStatusDto.setRecStatusPassedHR4(recStatusPassedHR4);
		candidateStatusDto.setRecStatusHoldedHR4(recStatusHoldedHR4);
		candidateStatusDto.setRecStatusRejectedHR4(recStatusRejectedHR4);
		candidateStatusDto.setRecStatusScheduledBU(recStatusScheduledBU);
		candidateStatusDto.setRecStatusAprrovedBU(recStatusApprovedBU);
		candidateStatusDto.setRecStatusHoldedBU(recStatusHoldedBU);
		candidateStatusDto.setRecStatusRejectedBU(recStatusRejectedBU);
		candidateStatusDto.setRecStatusSelected(recStatusSelected);
		candidateStatusDto.setRecStatusOnBoarded(recStatusOnBoarded);

		// candidateStatusDtoList.add(candidateStatusDto);

		List<Candidate> candidatesListRecent = candidateRepository.findByCreatedDateTimeBetween(
				LocalDateTime.now().minusMonths(1), LocalDateTime.now(), HireProUsUtil.orderByUpdatedDateTimeDesc());

		int recStatusUploadedRecent = 0;
		int recStatusShortListedRecent = 0;
		int recStatusHoldedRecent = 0;
		int recStatusRejectedRecent = 0;
		int recStatusScheduledR1Recent = 0;
		int recStatusPassedR1Recent = 0;
		int recStatusHoldedR1Recent = 0;
		int recStatusRejectedR1Recent = 0;
		int recStatusScheduledR2Recent = 0;
		int recStatusPassedR2Recent = 0;
		int recStatusHoldedR2Recent = 0;
		int recStatusRejectedR2Recent = 0;
		int recStatusScheduledCR3Recent = 0;
		int recStatusPassedCR3Recent = 0;
		int recStatusHoldedCR3Recent = 0;
		int recStatusRejectedCR3Recent = 0;
		int recStatusScheduledHR4Recent = 0;
		int recStatusPassedHR4Recent = 0;
		int recStatusHoldedHR4Recent = 0;
		int recStatusRejectedHR4Recent = 0;
		int recStatusScheduledBURecent = 0;
		int recStatusApprovedBURecent = 0;
		int recStatusHoldedBURecent = 0;
		int recStatusRejectedBURecent = 0;
		int recStatusSelectedRecent = 0;
		int recStatusOnBoardedRecent = 0;

		for (Candidate candidate : candidatesListRecent) {
			if (candidate.getRecStatus().equals("00")) {
				recStatusUploadedRecent++;
			}
			if (candidate.getRecStatus().equals("01")) {
				recStatusShortListedRecent++;
			}
			if (candidate.getRecStatus().equals("02")) {
				recStatusHoldedRecent++;
			}
			if (candidate.getRecStatus().equals("03")) {
				recStatusRejectedRecent++;
			}
			if (candidate.getRecStatus().equals("04")) {
				recStatusScheduledR1Recent++;
			}
			if (candidate.getRecStatus().equals("05")) {
				recStatusPassedR1Recent++;
			}
			if (candidate.getRecStatus().equals("06")) {
				recStatusHoldedR1Recent++;
			}
			if (candidate.getRecStatus().equals("07")) {
				recStatusRejectedR1Recent++;
			}
			if (candidate.getRecStatus().equals("08")) {
				recStatusScheduledR2Recent++;
			}
			if (candidate.getRecStatus().equals("09")) {
				recStatusPassedR2Recent++;
			}
			if (candidate.getRecStatus().equals("10")) {
				recStatusHoldedR2Recent++;
			}
			if (candidate.getRecStatus().equals("11")) {
				recStatusRejectedR2Recent++;
			}
			if (candidate.getRecStatus().equals("12")) {
				recStatusScheduledCR3Recent++;
			}
			if (candidate.getRecStatus().equals("13")) {
				recStatusPassedCR3Recent++;
			}
			if (candidate.getRecStatus().equals("14")) {
				recStatusHoldedCR3Recent++;
			}
			if (candidate.getRecStatus().equals("15")) {
				recStatusRejectedCR3Recent++;
			}
			if (candidate.getRecStatus().equals("16")) {
				recStatusScheduledHR4Recent++;
			}
			if (candidate.getRecStatus().equals("17")) {
				recStatusPassedHR4Recent++;
			}
			if (candidate.getRecStatus().equals("18")) {
				recStatusHoldedHR4Recent++;
			}
			if (candidate.getRecStatus().equals("19")) {
				recStatusRejectedHR4Recent++;
			}
			if (candidate.getRecStatus().equals("20")) {
				recStatusScheduledBURecent++;
			}
			if (candidate.getRecStatus().equals("21")) {
				recStatusApprovedBURecent++;
			}
			if (candidate.getRecStatus().equals("22")) {
				recStatusHoldedBURecent++;
			}
			if (candidate.getRecStatus().equals("23")) {
				recStatusRejectedBURecent++;
			}
			if (candidate.getRecStatus().equals("24")) {
				recStatusSelectedRecent++;
			}
			if (candidate.getRecStatus().equals("25")) {
				recStatusOnBoardedRecent++;
			}
		}
		// CandidateStatusDto candidateStatusRecentDto = new CandidateStatusDto();
		candidateStatusDto.setRecStatusUploadedRecent(recStatusUploadedRecent);
		candidateStatusDto.setRecStatusShortListedRecent(recStatusShortListedRecent);
		candidateStatusDto.setRecStatusHoldedRecent(recStatusHoldedRecent);
		candidateStatusDto.setRecStatusRejectedRecent(recStatusRejectedRecent);
		candidateStatusDto.setRecStatusScheduledR1Recent(recStatusScheduledR1Recent);
		candidateStatusDto.setRecStatusPassedR1Recent(recStatusPassedR1Recent);
		candidateStatusDto.setRecStatusHoldedR1Recent(recStatusHoldedR1Recent);
		candidateStatusDto.setRecStatusRejectedR1Recent(recStatusRejectedR1Recent);
		candidateStatusDto.setRecStatusScheduledR2Recent(recStatusScheduledR2Recent);
		candidateStatusDto.setRecStatusPassedR2Recent(recStatusPassedR2Recent);
		candidateStatusDto.setRecStatusHoldedR2Recent(recStatusHoldedR2Recent);
		candidateStatusDto.setRecStatusRejectedR2Recent(recStatusRejectedR2Recent);
		candidateStatusDto.setRecStatusScheduledCR3Recent(recStatusScheduledCR3Recent);
		candidateStatusDto.setRecStatusPassedCR3Recent(recStatusPassedCR3Recent);
		candidateStatusDto.setRecStatusHoldedCR3Recent(recStatusHoldedCR3Recent);
		candidateStatusDto.setRecStatusRejectedCR3Recent(recStatusRejectedCR3Recent);
		candidateStatusDto.setRecStatusScheduledHR4Recent(recStatusScheduledHR4Recent);
		candidateStatusDto.setRecStatusPassedHR4Recent(recStatusPassedHR4Recent);
		candidateStatusDto.setRecStatusHoldedHR4Recent(recStatusHoldedHR4Recent);
		candidateStatusDto.setRecStatusRejectedHR4Recent(recStatusRejectedHR4Recent);
		candidateStatusDto.setRecStatusScheduledBURecent(recStatusScheduledBURecent);
		candidateStatusDto.setRecStatusAprrovedBURecent(recStatusApprovedBURecent);
		candidateStatusDto.setRecStatusHoldedBURecent(recStatusHoldedBURecent);
		candidateStatusDto.setRecStatusRejectedBURecent(recStatusRejectedBURecent);
		candidateStatusDto.setRecStatusSelectedRecent(recStatusSelectedRecent);
		candidateStatusDto.setRecStatusOnBoardedRecent(recStatusOnBoardedRecent);

		candidateStatusDtoList.add(candidateStatusDto);

		return candidateStatusDtoList;
	}

	@Override
	public List<CandidateStatusDto> getCandidatesStatusByJRNum(final String jrNumber, final String lang)
			throws Exception {

		List<Candidate> candidatesList = candidateRepository.findByJrNumber(jrNumber,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateStatusDto> candidateStatusDtoList = new ArrayList<CandidateStatusDto>();

		int recStatusUploaded = 0;
		int recStatusShortListed = 0;
		int recStatusHolded = 0;
		int recStatusRejected = 0;
		int recStatusScheduledR1 = 0;
		int recStatusPassedR1 = 0;
		int recStatusHoldedR1 = 0;
		int recStatusRejectedR1 = 0;
		int recStatusScheduledR2 = 0;
		int recStatusPassedR2 = 0;
		int recStatusHoldedR2 = 0;
		int recStatusRejectedR2 = 0;
		int recStatusScheduledCR3 = 0;
		int recStatusPassedCR3 = 0;
		int recStatusHoldedCR3 = 0;
		int recStatusRejectedCR3 = 0;
		int recStatusScheduledHR4 = 0;
		int recStatusPassedHR4 = 0;
		int recStatusHoldedHR4 = 0;
		int recStatusRejectedHR4 = 0;
		int recStatusScheduledBU = 0;
		int recStatusApprovedBU = 0;
		int recStatusHoldedBU = 0;
		int recStatusRejectedBU = 0;
		int recStatusSelected = 0;
		int recStatusOnBoarded = 0;

		for (Candidate candidate : candidatesList) {
			if (candidate.getRecStatus().equals("00")) {
				recStatusUploaded++;
			}
			if (candidate.getRecStatus().equals("01")) {
				recStatusShortListed++;
			}
			if (candidate.getRecStatus().equals("02")) {
				recStatusHolded++;
			}
			if (candidate.getRecStatus().equals("03")) {
				recStatusRejected++;
			}
			if (candidate.getRecStatus().equals("04")) {
				recStatusScheduledR1++;
			}
			if (candidate.getRecStatus().equals("05")) {
				recStatusPassedR1++;
			}
			if (candidate.getRecStatus().equals("06")) {
				recStatusHoldedR1++;
			}
			if (candidate.getRecStatus().equals("07")) {
				recStatusRejectedR1++;
			}
			if (candidate.getRecStatus().equals("08")) {
				recStatusScheduledR2++;
			}
			if (candidate.getRecStatus().equals("09")) {
				recStatusPassedR2++;
			}
			if (candidate.getRecStatus().equals("10")) {
				recStatusHoldedR2++;
			}
			if (candidate.getRecStatus().equals("11")) {
				recStatusRejectedR2++;
			}
			if (candidate.getRecStatus().equals("12")) {
				recStatusScheduledCR3++;
			}
			if (candidate.getRecStatus().equals("13")) {
				recStatusPassedCR3++;
			}
			if (candidate.getRecStatus().equals("14")) {
				recStatusHoldedCR3++;
			}
			if (candidate.getRecStatus().equals("15")) {
				recStatusRejectedCR3++;
			}
			if (candidate.getRecStatus().equals("16")) {
				recStatusScheduledHR4++;
			}
			if (candidate.getRecStatus().equals("17")) {
				recStatusPassedHR4++;
			}
			if (candidate.getRecStatus().equals("18")) {
				recStatusHoldedHR4++;
			}
			if (candidate.getRecStatus().equals("19")) {
				recStatusRejectedHR4++;
			}
			if (candidate.getRecStatus().equals("20")) {
				recStatusScheduledBU++;
			}
			if (candidate.getRecStatus().equals("21")) {
				recStatusApprovedBU++;
			}
			if (candidate.getRecStatus().equals("22")) {
				recStatusHoldedBU++;
			}
			if (candidate.getRecStatus().equals("23")) {
				recStatusRejectedBU++;
			}
			if (candidate.getRecStatus().equals("24")) {
				recStatusSelected++;
			}
			if (candidate.getRecStatus().equals("25")) {
				recStatusOnBoarded++;
			}
		}
		CandidateStatusDto candidateStatusDto = new CandidateStatusDto();
		candidateStatusDto.setJrNumber(jrNumber);
		candidateStatusDto.setRecStatusUploaded(recStatusUploaded);
		candidateStatusDto.setRecStatusShortListed(recStatusShortListed);
		candidateStatusDto.setRecStatusHolded(recStatusHolded);
		candidateStatusDto.setRecStatusRejected(recStatusRejected);
		candidateStatusDto.setRecStatusScheduledR1(recStatusScheduledR1);
		candidateStatusDto.setRecStatusPassedR1(recStatusPassedR1);
		candidateStatusDto.setRecStatusHoldedR1(recStatusHoldedR1);
		candidateStatusDto.setRecStatusRejectedR1(recStatusRejectedR1);
		candidateStatusDto.setRecStatusScheduledR2(recStatusScheduledR2);
		candidateStatusDto.setRecStatusPassedR2(recStatusPassedR2);
		candidateStatusDto.setRecStatusHoldedR2(recStatusHoldedR2);
		candidateStatusDto.setRecStatusRejectedR2(recStatusRejectedR2);
		candidateStatusDto.setRecStatusScheduledCR3(recStatusScheduledCR3);
		candidateStatusDto.setRecStatusPassedCR3(recStatusPassedCR3);
		candidateStatusDto.setRecStatusHoldedCR3(recStatusHoldedCR3);
		candidateStatusDto.setRecStatusRejectedCR3(recStatusRejectedCR3);
		candidateStatusDto.setRecStatusScheduledHR4(recStatusScheduledHR4);
		candidateStatusDto.setRecStatusPassedHR4(recStatusPassedHR4);
		candidateStatusDto.setRecStatusHoldedHR4(recStatusHoldedHR4);
		candidateStatusDto.setRecStatusRejectedHR4(recStatusRejectedHR4);
		candidateStatusDto.setRecStatusScheduledBU(recStatusScheduledBU);
		candidateStatusDto.setRecStatusAprrovedBU(recStatusApprovedBU);
		candidateStatusDto.setRecStatusHoldedBU(recStatusHoldedBU);
		candidateStatusDto.setRecStatusRejectedBU(recStatusRejectedBU);
		candidateStatusDto.setRecStatusSelected(recStatusSelected);
		candidateStatusDto.setRecStatusOnBoarded(recStatusOnBoarded);

		// candidateStatusDtoList.add(candidateStatusDto);

		List<Candidate> candidatesListRecent = candidateRepository.findByCreatedDateTimeBetween(
				LocalDateTime.now().minusMonths(1), LocalDateTime.now(), HireProUsUtil.orderByUpdatedDateTimeDesc());

		int recStatusUploadedRecent = 0;
		int recStatusShortListedRecent = 0;
		int recStatusHoldedRecent = 0;
		int recStatusRejectedRecent = 0;
		int recStatusScheduledR1Recent = 0;
		int recStatusPassedR1Recent = 0;
		int recStatusHoldedR1Recent = 0;
		int recStatusRejectedR1Recent = 0;
		int recStatusScheduledR2Recent = 0;
		int recStatusPassedR2Recent = 0;
		int recStatusHoldedR2Recent = 0;
		int recStatusRejectedR2Recent = 0;
		int recStatusScheduledCR3Recent = 0;
		int recStatusPassedCR3Recent = 0;
		int recStatusHoldedCR3Recent = 0;
		int recStatusRejectedCR3Recent = 0;
		int recStatusScheduledHR4Recent = 0;
		int recStatusPassedHR4Recent = 0;
		int recStatusHoldedHR4Recent = 0;
		int recStatusRejectedHR4Recent = 0;
		int recStatusScheduledBURecent = 0;
		int recStatusApprovedBURecent = 0;
		int recStatusHoldedBURecent = 0;
		int recStatusRejectedBURecent = 0;
		int recStatusSelectedRecent = 0;
		int recStatusOnBoardedRecent = 0;

		for (Candidate candidate : candidatesListRecent) {
			if (candidate.getRecStatus().equals("00")) {
				recStatusUploadedRecent++;
			}
			if (candidate.getRecStatus().equals("01")) {
				recStatusShortListedRecent++;
			}
			if (candidate.getRecStatus().equals("02")) {
				recStatusHoldedRecent++;
			}
			if (candidate.getRecStatus().equals("03")) {
				recStatusRejectedRecent++;
			}
			if (candidate.getRecStatus().equals("04")) {
				recStatusScheduledR1Recent++;
			}
			if (candidate.getRecStatus().equals("05")) {
				recStatusPassedR1Recent++;
			}
			if (candidate.getRecStatus().equals("06")) {
				recStatusHoldedR1Recent++;
			}
			if (candidate.getRecStatus().equals("07")) {
				recStatusRejectedR1Recent++;
			}
			if (candidate.getRecStatus().equals("08")) {
				recStatusScheduledR2Recent++;
			}
			if (candidate.getRecStatus().equals("09")) {
				recStatusPassedR2Recent++;
			}
			if (candidate.getRecStatus().equals("10")) {
				recStatusHoldedR2Recent++;
			}
			if (candidate.getRecStatus().equals("11")) {
				recStatusRejectedR2Recent++;
			}
			if (candidate.getRecStatus().equals("12")) {
				recStatusScheduledCR3Recent++;
			}
			if (candidate.getRecStatus().equals("13")) {
				recStatusPassedCR3Recent++;
			}
			if (candidate.getRecStatus().equals("14")) {
				recStatusHoldedCR3Recent++;
			}
			if (candidate.getRecStatus().equals("15")) {
				recStatusRejectedCR3Recent++;
			}
			if (candidate.getRecStatus().equals("16")) {
				recStatusScheduledHR4Recent++;
			}
			if (candidate.getRecStatus().equals("17")) {
				recStatusPassedHR4Recent++;
			}
			if (candidate.getRecStatus().equals("18")) {
				recStatusHoldedHR4Recent++;
			}
			if (candidate.getRecStatus().equals("19")) {
				recStatusRejectedHR4Recent++;
			}
			if (candidate.getRecStatus().equals("20")) {
				recStatusScheduledBURecent++;
			}
			if (candidate.getRecStatus().equals("21")) {
				recStatusApprovedBURecent++;
			}
			if (candidate.getRecStatus().equals("22")) {
				recStatusHoldedBURecent++;
			}
			if (candidate.getRecStatus().equals("23")) {
				recStatusRejectedBURecent++;
			}
			if (candidate.getRecStatus().equals("24")) {
				recStatusSelectedRecent++;
			}
			if (candidate.getRecStatus().equals("25")) {
				recStatusOnBoardedRecent++;
			}
		}

		// CandidateStatusDto candidateStatusRecentDto = new CandidateStatusDto();
		candidateStatusDto.setRecStatusUploadedRecent(recStatusUploadedRecent);
		candidateStatusDto.setRecStatusShortListedRecent(recStatusShortListedRecent);
		candidateStatusDto.setRecStatusHoldedRecent(recStatusHoldedRecent);
		candidateStatusDto.setRecStatusRejectedRecent(recStatusRejectedRecent);
		candidateStatusDto.setRecStatusScheduledR1Recent(recStatusScheduledR1Recent);
		candidateStatusDto.setRecStatusPassedR1Recent(recStatusPassedR1Recent);
		candidateStatusDto.setRecStatusHoldedR1Recent(recStatusHoldedR1Recent);
		candidateStatusDto.setRecStatusRejectedR1Recent(recStatusRejectedR1Recent);
		candidateStatusDto.setRecStatusScheduledR2Recent(recStatusScheduledR2Recent);
		candidateStatusDto.setRecStatusPassedR2Recent(recStatusPassedR2Recent);
		candidateStatusDto.setRecStatusHoldedR2Recent(recStatusHoldedR2Recent);
		candidateStatusDto.setRecStatusRejectedR2Recent(recStatusRejectedR2Recent);
		candidateStatusDto.setRecStatusScheduledCR3Recent(recStatusScheduledCR3Recent);
		candidateStatusDto.setRecStatusPassedCR3Recent(recStatusPassedCR3Recent);
		candidateStatusDto.setRecStatusHoldedCR3Recent(recStatusHoldedCR3Recent);
		candidateStatusDto.setRecStatusRejectedCR3Recent(recStatusRejectedCR3Recent);
		candidateStatusDto.setRecStatusScheduledHR4Recent(recStatusScheduledHR4Recent);
		candidateStatusDto.setRecStatusPassedHR4Recent(recStatusPassedHR4Recent);
		candidateStatusDto.setRecStatusHoldedHR4Recent(recStatusHoldedHR4Recent);
		candidateStatusDto.setRecStatusRejectedHR4Recent(recStatusRejectedHR4Recent);
		candidateStatusDto.setRecStatusScheduledBURecent(recStatusScheduledBURecent);
		candidateStatusDto.setRecStatusAprrovedBURecent(recStatusApprovedBURecent);
		candidateStatusDto.setRecStatusHoldedBURecent(recStatusHoldedBURecent);
		candidateStatusDto.setRecStatusRejectedBURecent(recStatusRejectedBURecent);
		candidateStatusDto.setRecStatusSelectedRecent(recStatusSelectedRecent);
		candidateStatusDto.setRecStatusOnBoardedRecent(recStatusOnBoardedRecent);

		candidateStatusDtoList.add(candidateStatusDto);

		return candidateStatusDtoList;
	}

	@Override
	public List<CandidateStatusDto> getAllCandidateStagesByBU(final String buId, final String lang) throws Exception {

		List<String> jrNumbers = jobRequestService.getActiveJobRequestNumbersByBuId(buId, lang);

		List<Candidate> candidatesList = candidateRepository.findByJrNumberIn(jrNumbers,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<CandidateStatusDto> candidateStatusDtoList = new ArrayList<CandidateStatusDto>();

		int recStatusUploaded = 0;
		int recStatusShortListed = 0;
		int recStatusHolded = 0;
		int recStatusRejected = 0;
		int recStatusScheduledR1 = 0;
		int recStatusPassedR1 = 0;
		int recStatusHoldedR1 = 0;
		int recStatusRejectedR1 = 0;
		int recStatusScheduledR2 = 0;
		int recStatusPassedR2 = 0;
		int recStatusHoldedR2 = 0;
		int recStatusRejectedR2 = 0;
		int recStatusScheduledCR3 = 0;
		int recStatusPassedCR3 = 0;
		int recStatusHoldedCR3 = 0;
		int recStatusRejectedCR3 = 0;
		int recStatusScheduledHR4 = 0;
		int recStatusPassedHR4 = 0;
		int recStatusHoldedHR4 = 0;
		int recStatusRejectedHR4 = 0;
		int recStatusScheduledBU = 0;
		int recStatusApprovedBU = 0;
		int recStatusHoldedBU = 0;
		int recStatusRejectedBU = 0;
		int recStatusSelected = 0;
		int recStatusOnBoarded = 0;

		for (Candidate candidate : candidatesList) {
			if (candidate.getRecStatus().equals("00")) {
				recStatusUploaded++;
			}
			if (candidate.getRecStatus().equals("01")) {
				recStatusShortListed++;
			}
			if (candidate.getRecStatus().equals("02")) {
				recStatusHolded++;
			}
			if (candidate.getRecStatus().equals("03")) {
				recStatusRejected++;
			}
			if (candidate.getRecStatus().equals("04")) {
				recStatusScheduledR1++;
			}
			if (candidate.getRecStatus().equals("05")) {
				recStatusPassedR1++;
			}
			if (candidate.getRecStatus().equals("06")) {
				recStatusHoldedR1++;
			}
			if (candidate.getRecStatus().equals("07")) {
				recStatusRejectedR1++;
			}
			if (candidate.getRecStatus().equals("08")) {
				recStatusScheduledR2++;
			}
			if (candidate.getRecStatus().equals("09")) {
				recStatusPassedR2++;
			}
			if (candidate.getRecStatus().equals("10")) {
				recStatusHoldedR2++;
			}
			if (candidate.getRecStatus().equals("11")) {
				recStatusRejectedR2++;
			}
			if (candidate.getRecStatus().equals("12")) {
				recStatusScheduledCR3++;
			}
			if (candidate.getRecStatus().equals("13")) {
				recStatusPassedCR3++;
			}
			if (candidate.getRecStatus().equals("14")) {
				recStatusHoldedCR3++;
			}
			if (candidate.getRecStatus().equals("15")) {
				recStatusRejectedCR3++;
			}
			if (candidate.getRecStatus().equals("16")) {
				recStatusScheduledHR4++;
			}
			if (candidate.getRecStatus().equals("17")) {
				recStatusPassedHR4++;
			}
			if (candidate.getRecStatus().equals("18")) {
				recStatusHoldedHR4++;
			}
			if (candidate.getRecStatus().equals("19")) {
				recStatusRejectedHR4++;
			}
			if (candidate.getRecStatus().equals("20")) {
				recStatusScheduledBU++;
			}
			if (candidate.getRecStatus().equals("21")) {
				recStatusApprovedBU++;
			}
			if (candidate.getRecStatus().equals("22")) {
				recStatusHoldedBU++;
			}
			if (candidate.getRecStatus().equals("23")) {
				recStatusRejectedBU++;
			}
			if (candidate.getRecStatus().equals("24")) {
				recStatusSelected++;
			}
			if (candidate.getRecStatus().equals("25")) {
				recStatusOnBoarded++;
			}
		}
		CandidateStatusDto candidateStatusDto = new CandidateStatusDto();
		candidateStatusDto.setRecStatusUploaded(recStatusUploaded);
		candidateStatusDto.setRecStatusShortListed(recStatusShortListed);
		candidateStatusDto.setRecStatusHolded(recStatusHolded);
		candidateStatusDto.setRecStatusRejected(recStatusRejected);
		candidateStatusDto.setRecStatusScheduledR1(recStatusScheduledR1);
		candidateStatusDto.setRecStatusPassedR1(recStatusPassedR1);
		candidateStatusDto.setRecStatusHoldedR1(recStatusHoldedR1);
		candidateStatusDto.setRecStatusRejectedR1(recStatusRejectedR1);
		candidateStatusDto.setRecStatusScheduledR2(recStatusScheduledR2);
		candidateStatusDto.setRecStatusPassedR2(recStatusPassedR2);
		candidateStatusDto.setRecStatusHoldedR2(recStatusHoldedR2);
		candidateStatusDto.setRecStatusRejectedR2(recStatusRejectedR2);
		candidateStatusDto.setRecStatusScheduledCR3(recStatusScheduledCR3);
		candidateStatusDto.setRecStatusPassedCR3(recStatusPassedCR3);
		candidateStatusDto.setRecStatusHoldedCR3(recStatusHoldedCR3);
		candidateStatusDto.setRecStatusRejectedCR3(recStatusRejectedCR3);
		candidateStatusDto.setRecStatusScheduledHR4(recStatusScheduledHR4);
		candidateStatusDto.setRecStatusPassedHR4(recStatusPassedHR4);
		candidateStatusDto.setRecStatusHoldedHR4(recStatusHoldedHR4);
		candidateStatusDto.setRecStatusRejectedHR4(recStatusRejectedHR4);
		candidateStatusDto.setRecStatusScheduledBU(recStatusScheduledBU);
		candidateStatusDto.setRecStatusAprrovedBU(recStatusApprovedBU);
		candidateStatusDto.setRecStatusHoldedBU(recStatusHoldedBU);
		candidateStatusDto.setRecStatusRejectedBU(recStatusRejectedBU);
		candidateStatusDto.setRecStatusSelected(recStatusSelected);
		candidateStatusDto.setRecStatusOnBoarded(recStatusOnBoarded);

		// candidateStatusDtoList.add(candidateStatusDto);

		List<Candidate> candidatesListRecent = candidateRepository.findByCreatedDateTimeBetween(
				LocalDateTime.now().minusMonths(1), LocalDateTime.now(), HireProUsUtil.orderByUpdatedDateTimeDesc());

		int recStatusUploadedRecent = 0;
		int recStatusShortListedRecent = 0;
		int recStatusHoldedRecent = 0;
		int recStatusRejectedRecent = 0;
		int recStatusScheduledR1Recent = 0;
		int recStatusPassedR1Recent = 0;
		int recStatusHoldedR1Recent = 0;
		int recStatusRejectedR1Recent = 0;
		int recStatusScheduledR2Recent = 0;
		int recStatusPassedR2Recent = 0;
		int recStatusHoldedR2Recent = 0;
		int recStatusRejectedR2Recent = 0;
		int recStatusScheduledCR3Recent = 0;
		int recStatusPassedCR3Recent = 0;
		int recStatusHoldedCR3Recent = 0;
		int recStatusRejectedCR3Recent = 0;
		int recStatusScheduledHR4Recent = 0;
		int recStatusPassedHR4Recent = 0;
		int recStatusHoldedHR4Recent = 0;
		int recStatusRejectedHR4Recent = 0;
		int recStatusScheduledBURecent = 0;
		int recStatusApprovedBURecent = 0;
		int recStatusHoldedBURecent = 0;
		int recStatusRejectedBURecent = 0;
		int recStatusSelectedRecent = 0;
		int recStatusOnBoardedRecent = 0;

		for (Candidate candidate : candidatesListRecent) {
			if (candidate.getRecStatus().equals("00")) {
				recStatusUploadedRecent++;
			}
			if (candidate.getRecStatus().equals("01")) {
				recStatusShortListedRecent++;
			}
			if (candidate.getRecStatus().equals("02")) {
				recStatusHoldedRecent++;
			}
			if (candidate.getRecStatus().equals("03")) {
				recStatusRejectedRecent++;
			}
			if (candidate.getRecStatus().equals("04")) {
				recStatusScheduledR1Recent++;
			}
			if (candidate.getRecStatus().equals("05")) {
				recStatusPassedR1Recent++;
			}
			if (candidate.getRecStatus().equals("06")) {
				recStatusHoldedR1Recent++;
			}
			if (candidate.getRecStatus().equals("07")) {
				recStatusRejectedR1Recent++;
			}
			if (candidate.getRecStatus().equals("08")) {
				recStatusScheduledR2Recent++;
			}
			if (candidate.getRecStatus().equals("09")) {
				recStatusPassedR2Recent++;
			}
			if (candidate.getRecStatus().equals("10")) {
				recStatusHoldedR2Recent++;
			}
			if (candidate.getRecStatus().equals("11")) {
				recStatusRejectedR2Recent++;
			}
			if (candidate.getRecStatus().equals("12")) {
				recStatusScheduledCR3Recent++;
			}
			if (candidate.getRecStatus().equals("13")) {
				recStatusPassedCR3Recent++;
			}
			if (candidate.getRecStatus().equals("14")) {
				recStatusHoldedCR3Recent++;
			}
			if (candidate.getRecStatus().equals("15")) {
				recStatusRejectedCR3Recent++;
			}
			if (candidate.getRecStatus().equals("16")) {
				recStatusScheduledHR4Recent++;
			}
			if (candidate.getRecStatus().equals("17")) {
				recStatusPassedHR4Recent++;
			}
			if (candidate.getRecStatus().equals("18")) {
				recStatusHoldedHR4Recent++;
			}
			if (candidate.getRecStatus().equals("19")) {
				recStatusRejectedHR4Recent++;
			}
			if (candidate.getRecStatus().equals("20")) {
				recStatusScheduledBURecent++;
			}
			if (candidate.getRecStatus().equals("21")) {
				recStatusApprovedBURecent++;
			}
			if (candidate.getRecStatus().equals("22")) {
				recStatusHoldedBURecent++;
			}
			if (candidate.getRecStatus().equals("23")) {
				recStatusRejectedBURecent++;
			}
			if (candidate.getRecStatus().equals("24")) {
				recStatusSelectedRecent++;
			}
			if (candidate.getRecStatus().equals("25")) {
				recStatusOnBoardedRecent++;
			}
		}

		// CandidateStatusDto candidateStatusRecentDto = new CandidateStatusDto();
		candidateStatusDto.setRecStatusUploadedRecent(recStatusUploadedRecent);
		candidateStatusDto.setRecStatusShortListedRecent(recStatusShortListedRecent);
		candidateStatusDto.setRecStatusHoldedRecent(recStatusHoldedRecent);
		candidateStatusDto.setRecStatusRejectedRecent(recStatusRejectedRecent);
		candidateStatusDto.setRecStatusScheduledR1Recent(recStatusScheduledR1Recent);
		candidateStatusDto.setRecStatusPassedR1Recent(recStatusPassedR1Recent);
		candidateStatusDto.setRecStatusHoldedR1Recent(recStatusHoldedR1Recent);
		candidateStatusDto.setRecStatusRejectedR1Recent(recStatusRejectedR1Recent);
		candidateStatusDto.setRecStatusScheduledR2Recent(recStatusScheduledR2Recent);
		candidateStatusDto.setRecStatusPassedR2Recent(recStatusPassedR2Recent);
		candidateStatusDto.setRecStatusHoldedR2Recent(recStatusHoldedR2Recent);
		candidateStatusDto.setRecStatusRejectedR2Recent(recStatusRejectedR2Recent);
		candidateStatusDto.setRecStatusScheduledCR3Recent(recStatusScheduledCR3Recent);
		candidateStatusDto.setRecStatusPassedCR3Recent(recStatusPassedCR3Recent);
		candidateStatusDto.setRecStatusHoldedCR3Recent(recStatusHoldedCR3Recent);
		candidateStatusDto.setRecStatusRejectedCR3Recent(recStatusRejectedCR3Recent);
		candidateStatusDto.setRecStatusScheduledHR4Recent(recStatusScheduledHR4Recent);
		candidateStatusDto.setRecStatusPassedHR4Recent(recStatusPassedHR4Recent);
		candidateStatusDto.setRecStatusHoldedHR4Recent(recStatusHoldedHR4Recent);
		candidateStatusDto.setRecStatusRejectedHR4Recent(recStatusRejectedHR4Recent);
		candidateStatusDto.setRecStatusScheduledBURecent(recStatusScheduledBURecent);
		candidateStatusDto.setRecStatusAprrovedBURecent(recStatusApprovedBURecent);
		candidateStatusDto.setRecStatusHoldedBURecent(recStatusHoldedBURecent);
		candidateStatusDto.setRecStatusRejectedBURecent(recStatusRejectedBURecent);
		candidateStatusDto.setRecStatusSelectedRecent(recStatusSelectedRecent);
		candidateStatusDto.setRecStatusOnBoardedRecent(recStatusOnBoardedRecent);

		candidateStatusDtoList.add(candidateStatusDto);

		return candidateStatusDtoList;
	}

	@Override
	public byte[] downloadSelectedCandidateDetails(List<CandidateDto> candidateDtoList, String lang) throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/selectedCandidate.xlsx").getFile();
		try (Workbook resourcesMgmtWB = new XSSFWorkbook(file)) {

			Sheet sheet = resourcesMgmtWB.getSheetAt(0);
			HireProUsDefaultMethods.cleanSheet(sheet);
			int rowNum = 2;
			for (CandidateDto candidateDto : candidateDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(candidateDto.getJrNumber());
				dataRow.createCell(2).setCellValue(candidateDto.getBuName());
				dataRow.createCell(3).setCellValue(candidateDto.getCustomerName());
				dataRow.createCell(4).setCellValue(candidateDto.getRoleName());
				dataRow.createCell(5).setCellValue(candidateDto.getLocation());
				dataRow.createCell(6).setCellValue(candidateDto.getPlacementFor());
				dataRow.createCell(7).setCellValue(candidateDto.getFullName());
				dataRow.createCell(8).setCellValue(candidateDto.getContactNumber());
				dataRow.createCell(9).setCellValue(candidateDto.getEmail());
				dataRow.createCell(10).setCellValue(candidateDto.getSex());
				dataRow.createCell(11).setCellValue(candidateDto.getExperience());
				dataRow.createCell(12).setCellValue(candidateDto.getCurrentCompany());
				dataRow.createCell(13).setCellValue(candidateDto.getCandidateType());
				dataRow.createCell(14).setCellValue(candidateDto.getSkillSet());
//				dataRow.createCell(15).setCellValue(candidateDto.getVendorName());
				dataRow.createCell(15).setCellValue(candidateDto.getUpdatedByName());
				dataRow.createCell(16).setCellValue(candidateDto.getUpdatedDateTime().toLocalDate().toString());
				dataRow.createCell(17).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			resourcesMgmtWB.write(outputStream);

			resourcesMgmtWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during Selected Candidate Details download file", ex);
			return null;
		}
	}

	@Override
	public List<CandidateDto> searchCandidateForDownload(CandidateDto candidateDto) throws Exception {

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();

		final List<Criteria> criteriaList = new ArrayList<>();

		criteriaList.add(Criteria.where("recStatus").is("24"));

		if (candidateDto.getJrNumber() != null && !candidateDto.getJrNumber().isEmpty()) {
			criteriaList.add(Criteria.where("jrNumber").regex("(?i).*" + candidateDto.getJrNumber() + ".*"));
		}
		if (candidateDto.getFullName() != null && !candidateDto.getFullName().isEmpty()) {
			criteriaList.add(new Criteria().orOperator(
					(Criteria.where("firstName").regex("(?i).*" + candidateDto.getFullName() + ".*")),
					Criteria.where("lastName").regex("(?i).*" + candidateDto.getFullName() + ".*")));

		}
		if (candidateDto.getVendorId() != null) {
			criteriaList.add(Criteria.where("vendorId").is(candidateDto.getVendorId()));
		}
		if (candidateDto.getCandidateType() != null && !candidateDto.getCandidateType().isEmpty()) {
			criteriaList.add(Criteria.where("candidateType").regex("(?i).*" + candidateDto.getCandidateType() + ".*"));
		}
		if (candidateDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(candidateDto.getFromDateTime()));
		}
		if (candidateDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(candidateDto.getToDateTime()));
		}

		List<Candidate> candidatesList = new ArrayList<Candidate>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			candidatesList = mongoTemplate.find(searchQuery, Candidate.class);
		}

		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		Comparator<CandidateDto> compareByUpdatedDateTime = Comparator.comparing(CandidateDto::getUpdatedDateTime);
		candidateDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return candidateDtoList;
	}

	@Override
	public List<CandidateDto> searchRejectedCandidateForDownload(CandidateDto candidateDto) throws Exception {

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();

		final List<Criteria> criteriaList = new ArrayList<>();

		criteriaList.add(Criteria.where("recStatus").in("03", "07", "11", "15", "19", "23"));

		if (candidateDto.getJrNumber() != null && !candidateDto.getJrNumber().isEmpty()) {
			criteriaList.add(Criteria.where("jrNumber").regex("(?i).*" + candidateDto.getJrNumber() + ".*"));
		}
		if (candidateDto.getFullName() != null && !candidateDto.getFullName().isEmpty()) {
			criteriaList.add(new Criteria().orOperator(
					(Criteria.where("firstName").regex("(?i).*" + candidateDto.getFullName() + ".*")),
					Criteria.where("lastName").regex("(?i).*" + candidateDto.getFullName() + ".*")));

		}
		if (candidateDto.getVendorId() != null) {
			criteriaList.add(Criteria.where("vendorId").is(candidateDto.getVendorId()));
		}
		if (candidateDto.getCandidateType() != null && !candidateDto.getCandidateType().isEmpty()) {
			criteriaList.add(Criteria.where("candidateType").regex("(?i).*" + candidateDto.getCandidateType() + ".*"));
		}
		if (candidateDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(candidateDto.getFromDateTime()));
		}
		if (candidateDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(candidateDto.getToDateTime()));
		}

		List<Candidate> candidatesList = new ArrayList<Candidate>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			candidatesList = mongoTemplate.find(searchQuery, Candidate.class);
		}

		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		Comparator<CandidateDto> compareByUpdatedDateTime = Comparator.comparing(CandidateDto::getUpdatedDateTime);
		candidateDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return candidateDtoList;
	}

	@Override
	public byte[] downloadRejectedCandidateDetails(List<CandidateDto> candidateDtoList, String lang) throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/rejectedCandidate_details.xlsx").getFile();
		try (Workbook resourcesMgmtWB = new XSSFWorkbook(file)) {

			Sheet sheet = resourcesMgmtWB.getSheetAt(0);
			int rowNum = 2;
			for (CandidateDto candidateDto : candidateDtoList) {

				Row dataRow = sheet.createRow(rowNum);
				HireProUsDefaultMethods.cleanSheet(sheet);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(candidateDto.getJrNumber());
				dataRow.createCell(2).setCellValue(candidateDto.getBuName());
				dataRow.createCell(3).setCellValue(candidateDto.getCustomerName());
				dataRow.createCell(4).setCellValue(candidateDto.getRoleName());
				dataRow.createCell(5).setCellValue(candidateDto.getLocation());
				dataRow.createCell(6).setCellValue(candidateDto.getPlacementFor());
				dataRow.createCell(7).setCellValue(candidateDto.getFullName());
				dataRow.createCell(8).setCellValue(candidateDto.getContactNumber());
				dataRow.createCell(9).setCellValue(candidateDto.getEmail());
				dataRow.createCell(10).setCellValue(candidateDto.getSex());
				dataRow.createCell(11).setCellValue(candidateDto.getExperience());
				dataRow.createCell(12).setCellValue(candidateDto.getCurrentCompany());
				dataRow.createCell(13).setCellValue(candidateDto.getCandidateType());
				dataRow.createCell(14).setCellValue(candidateDto.getSkillSet());
				dataRow.createCell(15).setCellValue(candidateDto.getVendorName());
				dataRow.createCell(16).setCellValue(candidateDto.getUpdatedByName());
				dataRow.createCell(17).setCellValue(candidateDto.getUpdatedDateTime().toLocalDate().toString());
				dataRow.createCell(18).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			resourcesMgmtWB.write(outputStream);

			resourcesMgmtWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during Rejected Candidate Details download file", ex);
			return null;
		}
	}

	// getCandidate count by vendorID And JR

	@Override
	public CandidatesCountDto getCandidatesCountVendorVsJr(final String vendorId, final String jrNumber,
			final String lang) throws Exception {

		CandidatesCountDto resultDto = new CandidatesCountDto();

		List<Candidate> candidatesList = new ArrayList<Candidate>();
		candidatesList = candidateRepository.findByJrNumberAndVendorId(jrNumber, Long.parseLong(vendorId));
		if (candidatesList.size() > 0) {
			resultDto.setTotalTagged((long) candidatesList.size());
		} else {
			return resultDto;
		}
		return resultDto;
	}

	@Override
	public byte[] downloadCandidateDetails(List<CandidateDto> candidateDtoList, String lang) throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/allJRCandidate.xlsx").getFile();
		try (Workbook resourcesMgmtWB = new XSSFWorkbook(file)) {

			Sheet sheet = resourcesMgmtWB.getSheetAt(0);
			HireProUsDefaultMethods.cleanSheet(sheet);
			int rowNum = 2;
			for (CandidateDto candidateDto : candidateDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(candidateDto.getJrNumber());
				dataRow.createCell(2).setCellValue(candidateDto.getBuName());
				dataRow.createCell(3).setCellValue(candidateDto.getCustomerName());
				dataRow.createCell(4).setCellValue(candidateDto.getRoleName());
//				dataRow.createCell(5)
//						.setCellValue(candidateDto.getLocation() == null ? "-" : candidateDto.getLocation());
				dataRow.createCell(5).setCellValue(candidateDto.getPlacementFor());
				dataRow.createCell(6).setCellValue(candidateDto.getFullName());
				dataRow.createCell(7).setCellValue(candidateDto.getContactNumber());
				dataRow.createCell(8).setCellValue(candidateDto.getEmail());
				dataRow.createCell(9).setCellValue(candidateDto.getSex());
				dataRow.createCell(10).setCellValue(candidateDto.getExperience());
				dataRow.createCell(11).setCellValue(candidateDto.getCurrentCompany());
				dataRow.createCell(12).setCellValue(candidateDto.getCandidateType());
				dataRow.createCell(13).setCellValue(candidateDto.getSkillSet());
				dataRow.createCell(14).setCellValue(this.getRecStatus(candidateDto.getRecStatus()));
//				dataRow.createCell(16)
//						.setCellValue(candidateDto.getVendorName().equals(null) ? "-" : candidateDto.getVendorName());

				dataRow.createCell(15).setCellValue(candidateDto.getUpdatedByName());
				dataRow.createCell(16).setCellValue(candidateDto.getUpdatedDateTime().toLocalDate().toString());
				dataRow.createCell(17).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			resourcesMgmtWB.write(outputStream);

			resourcesMgmtWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during Selected Candidate Details download file", ex);
			return null;
		}
	}

	private String getRecStatus(String recStatus) {
		String roundName = "";
		switch (recStatus) {
		case "00":
			roundName = "Resume Uploaded";
			break;
		case "01":
			roundName = "Resume Shortlisted";
			break;
		case "02":
			roundName = "Hold @ Resume Shortlist";
			break;
		case "03":
			roundName = "Rejected @ Resume Shortlist";
			break;
		case "04":
			roundName = "Scheduled Internal Round 1";
			break;
		case "05":
			roundName = "Passed Internal Round 1";
			break;
		case "06":
			roundName = "Hold @ Internal Round 1";
			break;
		case "07":
			roundName = "Rejected @ Internal Round 1";
			break;
		case "08":
			roundName = "Scheduled Internal Round 2";
			break;
		case "09":
			roundName = "Passed Internal Round 2";
			break;
		case "10":
			roundName = "Hold @ Internal Round 2";
			break;
		case "11":
			roundName = "Rejected @ Internal Round 2";
			break;
		case "12":
			roundName = "Scheduled Customer Round";
			break;
		case "13":
			roundName = "Passed Customer Round";
			break;
		case "14":
			roundName = "Hold @ Customer Round";
			break;
		case "15":
			roundName = "Rejected @ Customer Round";
			break;
		case "16":
			roundName = "Scheduled HR Round";
			break;
		case "17":
			roundName = "Passed HR Round";
			break;
		case "18":
			roundName = "Hold @ HR Round";
			break;
		case "19":
			roundName = "Rejected @ HR Round";
			break;
		case "20":
			roundName = "Scheduled BU Round";
			break;
		case "21":
			roundName = "Approved @ BU Round";
			break;
		case "22":
			roundName = "Hold @ BU Round";
			break;
		case "23":
			roundName = "Rejected @ BU Round";
			break;
		case "24":
			roundName = "Selected";
			break;
		case "25":
			roundName = "On Boarded";
			break;
		case "26":
			roundName = "Dropped";
			break;
		}
		return roundName;

	}

	@Override
	public List<CandidateDto> searchAllCandidateForDownload(CandidateDto candidateDto) throws Exception {

		List<CandidateDto> candidateDtoList = new ArrayList<CandidateDto>();

		final List<Criteria> criteriaList = new ArrayList<>();
		if (candidateDto.getRecStatus() != null && !candidateDto.getRecStatus().isEmpty()) {
			List<String> recStatusList = Arrays.asList(candidateDto.getRecStatus().split(","));
		System.out.println(	criteriaList.add(Criteria.where("recStatus").in(recStatusList)));
		}

		if (candidateDto.getJrNumber() != null && !candidateDto.getJrNumber().isEmpty()) {
			criteriaList.add(Criteria.where("jrNumber").regex("(?i).*" + candidateDto.getJrNumber() + ".*"));
		}
		if (candidateDto.getFullName() != null && !candidateDto.getFullName().isEmpty()) {
			criteriaList.add(new Criteria().orOperator(
					(Criteria.where("firstName").regex("(?i).*" + candidateDto.getFullName() + ".*")),
					Criteria.where("lastName").regex("(?i).*" + candidateDto.getFullName() + ".*")));

		}
		if (candidateDto.getVendorId() != null) {
			criteriaList.add(Criteria.where("vendorId").is(candidateDto.getVendorId()));
		}
		if (candidateDto.getCandidateType() != null && !candidateDto.getCandidateType().isEmpty()) {
			criteriaList.add(Criteria.where("candidateType").regex("(?i).*" + candidateDto.getCandidateType() + ".*"));
		}
		if (candidateDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(candidateDto.getFromDateTime()));
		}
		if (candidateDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(candidateDto.getToDateTime()));
		}

		List<Candidate> candidatesList = new ArrayList<Candidate>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			candidatesList = mongoTemplate.find(searchQuery, Candidate.class);
		}

		for (Candidate candidate : candidatesList) {
			candidateDtoList.add(this.getCandidateDto(candidate, false));
		}

		Comparator<CandidateDto> compareByUpdatedDateTime = Comparator.comparing(CandidateDto::getUpdatedDateTime);
		candidateDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());
		System.out.println(candidateDtoList);

		return candidateDtoList;
	}

	@Override
	public CandidateDto rejectToUploadedStatus(CandidateDto candidateDto, String jrNumber, String lang) {
		InterviewScheduleDto interviewScheduleDto = new InterviewScheduleDto();
		CandidateDto resultDto = new CandidateDto();

		Candidate checkDuplicate = candidateRepository.findByJrNumberAndEmailIgnoreCase(jrNumber,
				candidateDto.getEmail());

		Candidate candidate = new Candidate();
		if (checkDuplicate != null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			try {
				resultDto.setMessage(
						commonService.getMessage("already.exists", new String[] { "JR Number, Email" }, lang));
			} catch (Exception e) {
				e.printStackTrace();
			}

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		try {
			candidate.setId(commonService.nextSequenceNumber());
		} catch (Exception e) {
			e.printStackTrace();
		}
		candidate.setFirstName(candidateDto.getFirstName());
		candidate.setLastName(candidateDto.getLastName());

		candidate.setEmail(candidateDto.getEmail());
		candidate.setContactNumber(candidateDto.getContactNumber());
		candidate.setSex(candidateDto.getSex());
		candidate.setSkillSet(candidateDto.getSkillSet());

		candidate.setExperience(candidateDto.getExperience());
		candidate.setCurrentCompany(candidateDto.getCurrentCompany());
		candidate.setCandidateType(candidateDto.getCandidateType());

		candidate.setRecStatus(HireProUsConstants.REC_STATUS_UPLOADED);

		candidate.setVendorId(candidateDto.getVendorId());

		candidate.setJrNumber(jrNumber);
		candidate.setCreatedBy(candidateDto.getUpdatedBy());
		candidate.setCreatedDateTime(LocalDateTime.now());
		candidate.setUpdatedBy(candidateDto.getUpdatedBy());
		candidate.setUpdatedDateTime(LocalDateTime.now());

		candidateRepository.save(candidate);

		CacheUtil.getCandidatesMap().remove(candidate.getId());
		CacheUtil.getCandidatesMap().put(candidate.getId(), candidate.getFirstName() + " " + candidate.getLastName());

		Candidate getNewCandidate = candidateRepository.findByJrNumberAndEmailIgnoreCase(jrNumber,
				candidateDto.getEmail());

		InterviewSchedule interview = new InterviewSchedule();
		try {
			interview.setId(commonService.nextSequenceNumber());
		} catch (Exception e) {
			e.printStackTrace();
		}
		interview.setJrNumber(jrNumber);
		interview.setCandidateId(getNewCandidate.getId());
    	interview.setInterviewerId(candidateDto.getUpdatedBy());
		interview.setScheduleDateTime(LocalDateTime.now());
		interview.setDuration("0");
		interview.setScheduleRemarks("For Resume Shortlist");
		interview.setMode("Remote");
		interview.setVenue("System");
		interview.setRound(HireProUsConstants.INTERVIEW_ROUND_INITIAL);
		interview.setCompleted(0);
		interview.setRecStatus(HireProUsConstants.REC_STATUS_UPLOADED);
		interview.setCreatedBy(candidateDto.getUpdatedBy());
		interview.setUpdatedBy(candidateDto.getUpdatedBy());
		interview.setCreatedDateTime(LocalDateTime.now());
		interview.setUpdatedDateTime(LocalDateTime.now());
		interview.setResultRemarks("");

		interviewScheduleRepository.save(interview);

		// Send Candidate Updated Mail.
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					mailService.sendCandidateUpdated(candidate);
//				} catch (Exception e) {
//
//					logger.error("Candidate Updated Email is not Sent.");
//					logger.error(HireProUsUtil.getErrorMessage(e));
//				}
//			}
//		}).start();

		resultDto.setId(candidate.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public EvaluateResumeDto getEvaluateResume(String jrNumber, String candidateId) {
		
		EvaluateResumeDto resultDto = new EvaluateResumeDto();
		 
		Candidate candidateDet = candidateRepository.findById(Long.parseLong(candidateId));
		try {
		if(candidateDet !=null) {
			resultDto.setCandiDesig(candidateDet.getCurrentDesignation());
			resultDto.setCandiId(candidateDet.getId());
			resultDto.setCandiName(candidateDet.getFirstName() + " " + candidateDet.getLastName());
			resultDto.setCandiSkillset(candidateDet.getSkillSet().replaceAll(",", " "));
			resultDto.setCandiYOE(candidateDet.getExperience());
		}
		
		JobRequest jobReqDet = jobRequestRepository.getJobRequestByReferenceNumber(jrNumber);
		RecruitmentRole recRoleDet = recruitmentRepository.findById(jobReqDet.getRoleId());
		
		if(jobReqDet !=null) {
			//resultDto.setCandiDesig(candidateDet.get);
			resultDto.setJdDesig(recRoleDet.getRecruitmentRoleName());
			resultDto.setJdNum(jrNumber);
			resultDto.setJdSkillset(jobReqDet.getMandatorySkills() + " " +jobReqDet.getOptionalSkills());
			resultDto.setJdYOE(jobReqDet.getMinYearOfExp());
		}
		}
		catch(Exception E){
			System.out.print(E);
		}
		return resultDto;
		
	}

	
}
