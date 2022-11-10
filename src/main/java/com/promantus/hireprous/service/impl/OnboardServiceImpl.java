/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.CandidateDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.OnboardDto;
import com.promantus.hireprous.dto.OnboardSearchDto;
import com.promantus.hireprous.dto.ResourceMgmtDto;
import com.promantus.hireprous.entity.Candidate;
import com.promantus.hireprous.entity.Onboard;
import com.promantus.hireprous.repository.CandidateRepository;
import com.promantus.hireprous.repository.OnboardRepository;
import com.promantus.hireprous.service.CandidateService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.service.MailService;
import com.promantus.hireprous.service.OnboardService;
import com.promantus.hireprous.service.ProjectService;
import com.promantus.hireprous.service.ResourceMgmtService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class OnboardServiceImpl implements OnboardService {

	private static final Logger logger = LoggerFactory.getLogger(OnboardServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MailService mailService;

	@Autowired
	CandidateService candidateService;

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	ProjectService projectService;

	@Autowired
	ResourceMgmtService resourceMgmtService;

	@Autowired
	OnboardRepository onboardRepository;

	@Autowired
	CandidateRepository candidateRepository;

	/**
	 * @param jrNumber
	 * @param candidateId
	 * @param employeeIdByHR
	 * @param workOrderNumber
	 * @return
	 */
	private String checkOnboard(String jrNumber, Long candidateId, String employeeIdByHR, boolean isAdd,
			Long onboardId) {

		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(new Criteria().andOperator(Criteria.where("jrNumber").is(jrNumber),
				Criteria.where("candidateId").is(candidateId)));
		// criteriaList.add(Criteria.where("employeeIdByHR").regex("(?i).*" +
		// employeeIdByHR + ".*"));
		criteriaList.add(Criteria.where("employeeIdByHR").is(employeeIdByHR));

		Query searchQuery = new Query();
		searchQuery.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
		List<Onboard> onboardListCheck = mongoTemplate.find(searchQuery, Onboard.class);

		String errorMessage = "";
		if ((isAdd && onboardListCheck != null && onboardListCheck.size() > 0) || (onboardListCheck != null
				&& onboardListCheck.size() > 0 && !onboardId.equals(onboardListCheck.get(0).getId()))) {

			if (employeeIdByHR.equalsIgnoreCase(onboardListCheck.get(0).getEmployeeIdByHR())) {
				errorMessage = "EmployeeId By HR";
			} else {
				errorMessage = "Job Request and Candidate";
			}
		}

		return errorMessage;
	}

	private void checkCandidate(Long candidateId, String email, String contactNumber) {

		List<Candidate> candidate = new ArrayList<>();

		final List<Criteria> criteriaList = new ArrayList<>();

		criteriaList.add(Criteria.where("email").is(email));
		criteriaList.add(Criteria.where("contactNumber").is(contactNumber));

		Query searchQuery = new Query();
		searchQuery.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
		List<Candidate> candidateListCheck = mongoTemplate.find(searchQuery, Candidate.class);

		if ((candidateListCheck != null && candidateListCheck.size() > 0)
				|| (candidateListCheck != null && candidateListCheck.size() > 0)) {
			for (Candidate candidate1 : candidateListCheck) {
				if (email.equals(candidate1.getEmail()) && contactNumber.equals(candidate1.getContactNumber())
						&& candidate1.getRecStatus() != HireProUsConstants.REC_STATUS_SELECTED) {
					candidate.add(candidate1);

				}
			}

			if (candidate.size() > 0) {
				for (Candidate candidate2 : candidate) {
					candidate2.setRecStatus(HireProUsConstants.REC_STATUS_DROPPED);
					candidateRepository.save(candidate2);
				}
			}

		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OnboardDto addOnboard(final OnboardDto onboardDto, String lang) throws Exception {

		OnboardDto resultDto = new OnboardDto();

		String errorMessage = this.checkOnboard(onboardDto.getJrNumber(), onboardDto.getCandidateId(),
				onboardDto.getEmployeeIdByHR(), true, null);
		if (errorMessage != null && !errorMessage.isEmpty()) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { errorMessage }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		this.checkCandidate(onboardDto.getCandidateId(), onboardDto.getEmail(), onboardDto.getContactNumber());

		Onboard onboard = new Onboard();
		onboard.setId(commonService.nextSequenceNumber());

		onboard.setJrNumber(onboardDto.getJrNumber());
		onboard.setCandidateId(onboardDto.getCandidateId());

		this.setEmployeeId(onboard);
		onboardDto.setEmployeeId(onboard.getEmployeeId());

		onboard.setEmployeeIdByHR(onboardDto.getEmployeeIdByHR());
		onboard.setWorkOrderNumber(onboardDto.getWorkOrderNumber());
		onboard.setJoiningDate(onboardDto.getJoiningDate());

		if (onboardDto.getProjectId() != null && !onboardDto.getProjectId().equals(0L)) {
			onboard.setProjectId(onboardDto.getProjectId());
		}
		if (onboardDto.getResourceId() != null && !onboardDto.getResourceId().equals(0L)) {
			onboard.setResourceId(onboardDto.getResourceId());
		}

		onboard.setDocsVerified(onboardDto.getDocsVerified());
		onboard.setJoined(onboardDto.getJoined());
		onboard.setEmail(onboardDto.getEmail());

		onboard.setRecStatus(HireProUsConstants.REC_STATUS_ONBOARDED);

		onboard.setCreatedBy(onboardDto.getCreatedBy());
		onboard.setUpdatedBy(onboardDto.getUpdatedBy());
		onboard.setCreatedDateTime(LocalDateTime.now());
		onboard.setUpdatedDateTime(LocalDateTime.now());

//		onboardRepository.save(onboard);
//
//		candidateService.updateRecStatus(onboardDto.getCandidateId(), HireProUsConstants.REC_STATUS_ONBOARDED,
//				onboardDto.getUpdatedBy());

		// get Candidate details.
		CandidateDto candidateDto = candidateService.getCandidateById(onboardDto.getCandidateId() + "");

		// Update in Resource management.
//		if (candidateDto.getCandidateType().equals(HireProUsConstants.CANDIDATE_TYPE_EXTERNAL)) {
		if (onboardDto.getResourceId() == null || onboardDto.getResourceId().equals(0L)) {

			ResourceMgmtDto resourceMgmtDto = new ResourceMgmtDto();
			resourceMgmtDto.setEmployeeId(onboardDto.getEmployeeId());
			resourceMgmtDto.setEmployeeIdByHR(onboardDto.getEmployeeIdByHR());
			resourceMgmtDto.setWorkOrderNumber(onboardDto.getWorkOrderNumber());

			resourceMgmtDto.setFirstName(candidateDto.getFirstName());
			resourceMgmtDto.setLastName(candidateDto.getLastName());
			resourceMgmtDto.setEmail(onboardDto.getEmail());
			resourceMgmtDto.setContactNumber(candidateDto.getContactNumber());
			resourceMgmtDto.setSex(candidateDto.getSex());
			resourceMgmtDto.setSkillSet(candidateDto.getSkillSet());
			resourceMgmtDto.setExperience(candidateDto.getExperience());

			resourceMgmtDto.setEmploymentType(candidateDto.getEmploymentType());
			resourceMgmtDto.setBuId(onboardDto.getBuId());

			resourceMgmtDto.setProjectAllocation("No");
			if (onboardDto.getProjectId() != null && !onboardDto.getProjectId().equals(0L)) {
				resourceMgmtDto.setProjectAllocation("Yes");
				resourceMgmtDto.setProjectId(onboardDto.getProjectId());
				resourceMgmtDto.setCustomerId(projectService.getCustomerIdById(onboardDto.getProjectId()));
			}

			resourceMgmtDto.setCreatedBy(onboardDto.getCreatedBy());
			resourceMgmtDto.setUpdatedBy(onboardDto.getUpdatedBy());

			ResourceMgmtDto resourceMgmtDtoResult = resourceMgmtService.addResourceMgmt(resourceMgmtDto, lang);
			if (resourceMgmtDtoResult.getStatus() == 1) {
				throw new Exception(resourceMgmtDtoResult.getMessage());
//				return resultDto;
			}

			onboard.setResourceId(resourceMgmtDtoResult.getId());

//			onboardRepository.save(onboard);

		} else {

			try {
				ResourceMgmtDto resourceMgmtDtoResult = resourceMgmtService.updateProjectAllocationById(
						onboardDto.getResourceId() + "", onboardDto.getProjectId(),
						projectService.getCustomerIdById(onboardDto.getProjectId()), onboardDto.getUpdatedBy(),
						onboardDto.getWorkOrderNumber(), onboardDto.getEmployeeIdByHR(), onboardDto.getEmail(),
						onboardDto.getBuId());
				if (resourceMgmtDtoResult.getStatus() == 1) {
					throw new Exception(resourceMgmtDtoResult.getMessage());
//					return resultDto;
				}
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}

		candidateService.updateRecStatus(onboardDto.getCandidateId(), HireProUsConstants.REC_STATUS_ONBOARDED,
				onboardDto.getUpdatedBy());

		onboardRepository.save(onboard);

		// Send OnBoarded Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendOnBoardedEmail(candidateDto, onboardDto);
				} catch (Exception e) {

					logger.error("Email for Onboard is not Sent.");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	/**
	 * @return
	 */
	private void setEmployeeId(final Onboard onboard) {

		int year = Calendar.getInstance().get(Calendar.YEAR);
		String runningNumber = String.format("%0" + HireProUsConstants.ON_BOARD_MAX_DIGIT_RUNNING_NUMBER + "d", 1);

		Onboard onboardCheck = onboardRepository.findFirstByYear(year, HireProUsUtil.orderByRunningNumberDesc());

		if (onboardCheck != null) {
			runningNumber = String.format("%0" + HireProUsConstants.ON_BOARD_MAX_DIGIT_RUNNING_NUMBER + "d",
					onboardCheck.getRunningNumber() + 1);
		}

		onboard.setYear(year);
		onboard.setRunningNumber(Integer.parseInt(runningNumber));
		onboard.setEmployeeId(year + runningNumber);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OnboardDto updateOnboard(final OnboardDto onboardDto, String lang) throws Exception {

		OnboardDto resultDto = new OnboardDto();

		Onboard onboard = onboardRepository.findById(onboardDto.getId());

		if (onboard == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Onboard Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		String errorMessage = this.checkOnboard(onboardDto.getJrNumber(), onboardDto.getCandidateId(),
				onboardDto.getEmployeeIdByHR(), false, onboardDto.getId());
		if (errorMessage != null && !errorMessage.isEmpty()) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { errorMessage }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

//Added on 11/18/2021 -> Email and Employee Id By HR duplicate Issue

		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(Criteria.where("employeeIdByHR").regex("(?i).*" + onboardDto.getEmployeeIdByHR() + ".*"));
		criteriaList.add(Criteria.where("email").regex("(?i).*" + onboardDto.getEmail() + ".*"));

		Query searchQuery = new Query();
		searchQuery.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
		List<Onboard> onboardListCheck = mongoTemplate.find(searchQuery, Onboard.class);

		if (onboardListCheck != null && onboardListCheck.size() > 0) {
			for (Onboard candidate2 : onboardListCheck) {
				if (!onboardDto.getId().equals(candidate2.getId())) {
					if (onboardDto.getEmployeeIdByHR().equals(candidate2.getEmployeeIdByHR())) {
						resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
						resultDto.setMessage(
								commonService.getMessage("already.exists", new String[] { "EmployeeId By HR" }, lang));

						logger.info(resultDto.getMessage());
						return resultDto;
					}
					if (onboardDto.getEmail().equals(candidate2.getEmail())) {
						resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
						resultDto
								.setMessage(commonService.getMessage("already.exists", new String[] { "Email" }, lang));

						logger.info(resultDto.getMessage());
						return resultDto;
					}
				}
			}
		}

		onboard.setJrNumber(onboardDto.getJrNumber());
		onboard.setCandidateId(onboardDto.getCandidateId());

		onboard.setEmployeeId(onboardDto.getEmployeeId());
		onboard.setEmployeeIdByHR(onboardDto.getEmployeeIdByHR());
		onboard.setWorkOrderNumber(onboardDto.getWorkOrderNumber());
		onboard.setJoiningDate(onboardDto.getJoiningDate());

		if (onboardDto.getProjectId() != null && !onboardDto.getProjectId().equals(0L)) {
			onboard.setProjectId(onboardDto.getProjectId());
		}
		if (onboardDto.getResourceId() != null && !onboardDto.getResourceId().equals(0L)) {
			onboard.setResourceId(onboardDto.getResourceId());
		}

		onboard.setDocsVerified(onboardDto.getDocsVerified());
		onboard.setJoined(onboardDto.getJoined());
		onboard.setEmail(onboardDto.getEmail());

		onboard.setUpdatedBy(onboardDto.getUpdatedBy());
		onboard.setUpdatedDateTime(LocalDateTime.now());

//		onboardRepository.save(onboard);

		// get the Candidate details.
		CandidateDto candidateDto = candidateService.getCandidateById(onboardDto.getCandidateId() + "");

		// Update in Resource management.
		if (onboardDto.getResourceId() != null && !onboardDto.getResourceId().equals(0L)) {
			try {
				ResourceMgmtDto resourceMgmtDtoResult = resourceMgmtService.updateProjectAllocationById(
						onboardDto.getResourceId() + "", onboardDto.getProjectId(),
						projectService.getCustomerIdById(onboardDto.getProjectId()), onboardDto.getUpdatedBy(),
						onboardDto.getWorkOrderNumber(), onboardDto.getEmployeeIdByHR(), onboardDto.getEmail(), null);
				if (resourceMgmtDtoResult.getStatus() == 1) {
					throw new Exception(resourceMgmtDtoResult.getMessage());
//					return resultDto;
				}
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		} else {

			ResourceMgmtDto resourceMgmtDto = new ResourceMgmtDto();
			resourceMgmtDto.setEmployeeId(onboard.getEmployeeId());
			resourceMgmtDto.setEmployeeIdByHR(onboardDto.getEmployeeIdByHR());
			resourceMgmtDto.setWorkOrderNumber(onboardDto.getWorkOrderNumber());

			resourceMgmtDto.setFirstName(candidateDto.getFirstName());
			resourceMgmtDto.setLastName(candidateDto.getLastName());
			resourceMgmtDto.setEmail(onboardDto.getEmail());
			resourceMgmtDto.setContactNumber(candidateDto.getContactNumber());
			resourceMgmtDto.setSex(candidateDto.getSex());
			resourceMgmtDto.setSkillSet(candidateDto.getSkillSet());

			resourceMgmtDto.setExperience(candidateDto.getExperience());

			resourceMgmtDto.setEmploymentType(candidateDto.getEmploymentType());
			resourceMgmtDto.setBuId(candidateDto.getBuId());

			resourceMgmtDto.setProjectAllocation("No");
			if (onboardDto.getProjectId() != null && !onboardDto.getProjectId().equals(0L)) {
				resourceMgmtDto.setProjectAllocation("Yes");
				resourceMgmtDto.setProjectId(onboardDto.getProjectId());
				resourceMgmtDto.setCustomerId(projectService.getCustomerIdById(onboardDto.getProjectId()));
			}

			resourceMgmtDto.setCreatedBy(onboardDto.getCreatedBy());
			resourceMgmtDto.setUpdatedBy(onboardDto.getUpdatedBy());

			ResourceMgmtDto resourceMgmtDtoResult = resourceMgmtService.addResourceMgmt(resourceMgmtDto, lang);
			if (resourceMgmtDtoResult.getStatus() == 1) {
				throw new Exception(resourceMgmtDtoResult.getMessage());
//				return resultDto;
			}

			onboard.setResourceId(resourceMgmtDtoResult.getId());
//			onboardRepository.save(onboard);
		}

		onboardRepository.save(onboard);

		// Send Updated OnBoarded Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendOnBoardUpdatedEmail(candidateDto, onboardDto);
				} catch (Exception e) {

					logger.error("Email for Onboard Updated is not Sent.");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<OnboardDto> getAllOnboards() throws Exception {

		List<OnboardDto> onboardDtoList = new ArrayList<OnboardDto>();

		List<Onboard> onboardsList = onboardRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Onboard onboard : onboardsList) {
			onboardDtoList.add(this.getOnboardDto(onboard));
		}

		return onboardDtoList;
	}

	@Override
	public OnboardDto getOnboardById(final String onboardId) throws Exception {

		Onboard onboard = onboardRepository.findById(Long.parseLong(onboardId));

		return onboard != null ? this.getOnboardDto(onboard) : new OnboardDto();
	}

	/**
	 * @param onboard
	 * @return
	 * @throws Exception
	 */
	private OnboardDto getOnboardDto(final Onboard onboard) throws Exception {

		OnboardDto onboardDto = new OnboardDto();

		onboardDto.setId(onboard.getId());

		onboardDto.setJrNumber(onboard.getJrNumber());
		onboardDto.setCandidateId(onboard.getCandidateId());

		onboardDto.setEmployeeId(onboard.getEmployeeId());
		onboardDto.setEmployeeIdByHR(onboard.getEmployeeIdByHR());
		onboardDto.setWorkOrderNumber(onboard.getWorkOrderNumber());
		onboardDto.setJoiningDate(onboard.getJoiningDate());

		if (onboard.getProjectId() != null && !onboard.getProjectId().equals(0L)) {
			onboardDto.setProjectId(onboard.getProjectId());
			onboardDto.setProjectName(CacheUtil.getProjectsMap().get(onboard.getProjectId()));
		}

		if (onboard.getResourceId() != null && !onboard.getResourceId().equals(0L)) {
			onboardDto.setResourceId(onboard.getResourceId());
		}

		onboardDto.setDocsVerified(onboard.getDocsVerified());
		onboardDto.setJoined(onboard.getJoined());
		onboardDto.setEmail(onboard.getEmail());

		onboardDto.setRecStatus(onboard.getRecStatus());

		onboardDto.setCreatedBy(onboard.getCreatedBy());
		onboardDto.setCreatedByName(CacheUtil.getUsersMap().get(onboard.getCreatedBy()));
		onboardDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(onboard.getCreatedDateTime()));

		onboardDto.setUpdatedBy(onboard.getUpdatedBy());
		onboardDto.setUpdatedByName(CacheUtil.getUsersMap().get(onboard.getUpdatedBy()));
		onboardDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(onboard.getUpdatedDateTime()));

		JobRequestDto jobRequestDto = jobRequestService.getJobRequestInfoByJRNumber(onboard.getJrNumber());
		if (jobRequestDto != null) {
			onboardDto.setJrId(jobRequestDto.getId());
			onboardDto.setCustomerName(jobRequestDto.getCustomerName());
			onboardDto.setBuId(jobRequestDto.getBuId());
			onboardDto.setBuName(jobRequestDto.getBuName());
			onboardDto.setRoleName(jobRequestDto.getRoleName());
			onboardDto.setNoOfOpenings(jobRequestDto.getNoOfOpenings());
			onboardDto.setLocation(jobRequestDto.getLocation());
			onboardDto.setEmploymentType(jobRequestDto.getEmploymentType());
			onboardDto.setPlacementFor(jobRequestDto.getPlacementFor());
		}

		CandidateDto candidateDto = candidateService.getCandidateShortInfoById(onboard.getCandidateId() + "");
		if (candidateDto != null) {
			onboardDto.setCandidateName(candidateDto.getFullName());
			onboardDto.setContactNumber(candidateDto.getContactNumber());
//			onboardDto.setEmail(candidateDto.getEmail());
			onboardDto.setSex(candidateDto.getSex());
			onboardDto.setExperience(candidateDto.getExperience());
		}
		return onboardDto;
	}

	@Override
	public void deleteOnboardById(final String onboardId) throws Exception {

		onboardRepository.deleteById(Long.parseLong(onboardId));
	}

	@Override
	public List<OnboardDto> searchOnboard(final OnboardSearchDto onboardSearchDto) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();
		if (onboardSearchDto.getJrNumber() != null && !onboardSearchDto.getJrNumber().isEmpty()) {
			criteriaList.add(Criteria.where("jrNumber").regex("(?i).*" + onboardSearchDto.getJrNumber() + ".*"));
		}
		if (onboardSearchDto.getEmployeeId() != null && !onboardSearchDto.getEmployeeId().isEmpty()) {
			criteriaList.add(Criteria.where("employeeId").regex("(?i).*" + onboardSearchDto.getEmployeeId() + ".*"));
		}
		if (onboardSearchDto.getEmployeeIdByHR() != null && !onboardSearchDto.getEmployeeIdByHR().isEmpty()) {
			criteriaList.add(
					Criteria.where("employeeIdByHR").regex("(?i).*" + onboardSearchDto.getEmployeeIdByHR() + ".*"));
		}
		if (onboardSearchDto.getWorkOrderNumber() != null && !onboardSearchDto.getWorkOrderNumber().isEmpty()) {
			criteriaList.add(
					Criteria.where("workOrderNumber").regex("(?i).*" + onboardSearchDto.getWorkOrderNumber() + ".*"));
		}

		List<Onboard> onboardList = new ArrayList<Onboard>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			onboardList = mongoTemplate.find(searchQuery, Onboard.class);
		}

		List<OnboardDto> onboardDtoList = new ArrayList<OnboardDto>();
		for (Onboard onboard : onboardList) {
			onboardDtoList.add(this.getOnboardDto(onboard));
		}

		Comparator<OnboardDto> compareByUpdatedDateTime = Comparator.comparing(OnboardDto::getUpdatedDateTime);
		onboardDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return onboardDtoList;
	}

	@Override
	public int getProjectDependencyCount(Long projectId) {

		return onboardRepository.countByProjectId(projectId);
	}
}
