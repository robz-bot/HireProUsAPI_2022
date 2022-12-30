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
import com.promantus.hireprous.dto.CandidateDto;
import com.promantus.hireprous.dto.InterviewScheduleDto;
import com.promantus.hireprous.dto.InterviewScheduleSearchDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.VendorDto;
import com.promantus.hireprous.entity.InterviewSchedule;
import com.promantus.hireprous.repository.InterviewScheduleRepository;
import com.promantus.hireprous.service.CandidateService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.InterviewScheduleService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.service.MailService;
import com.promantus.hireprous.service.UserService;
import com.promantus.hireprous.service.VendorService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class InterviewScheduleServiceImpl implements InterviewScheduleService {
	private static final Logger logger = LoggerFactory.getLogger(InterviewScheduleServiceImpl.class);
	@Autowired
	CommonService commonService;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	MailService mailService;
	@Autowired
	JobRequestService jobRequestService;
	@Autowired
	CandidateService candidateService;
	@Autowired
	VendorService vendorService;
	@Autowired
	InterviewScheduleRepository interviewScheduleRepository;
	@Autowired
	UserService userService;
	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public InterviewScheduleDto addInterviewSchedule(final InterviewScheduleDto interviewScheduleDto, String lang)
			throws Exception {
		InterviewScheduleDto resultDto = new InterviewScheduleDto();
		LocalDateTime newStartDateTime = interviewScheduleDto.getScheduleDateTime();
		String newHourMinute = HireProUsUtil.getHourMinute(interviewScheduleDto.getDuration());
		LocalDateTime newEndDateTime = interviewScheduleDto.getScheduleDateTime()
				.plusHours(Long.parseLong(newHourMinute.split(",")[0]))
				.plusMinutes(Long.parseLong(newHourMinute.split(",")[1]));
		List<InterviewSchedule> interviewScheduleList = interviewScheduleRepository
				.findByInterviewerIdAndResultRemarksNull(interviewScheduleDto.getInterviewerId());
		boolean alreadyScheduled = false;
		for (InterviewSchedule interviewSchedule : interviewScheduleList) {
			String hourMinute = HireProUsUtil.getHourMinute(interviewSchedule.getDuration());
			LocalDateTime startDateTime = interviewSchedule.getScheduleDateTime();
			LocalDateTime endDateTime = interviewSchedule.getScheduleDateTime()
					.plusHours(Long.parseLong(hourMinute.split(",")[0]))
					.plusMinutes(Long.parseLong(hourMinute.split(",")[1]));
			if (newStartDateTime.equals(startDateTime) || newEndDateTime.equals(endDateTime)
					|| (newStartDateTime.isAfter(startDateTime) && newStartDateTime.isBefore(endDateTime))
					|| newEndDateTime.isAfter(startDateTime) && newEndDateTime.isBefore(endDateTime)
					|| newStartDateTime.isBefore(startDateTime) && newEndDateTime.isAfter(endDateTime)) {
				alreadyScheduled = true;
				break;
			}
		}
		if (alreadyScheduled) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists.interview",
					new String[] { CacheUtil.getUsersMap().get(interviewScheduleDto.getInterviewerId()) }, lang));
			logger.info(resultDto.getMessage());
			return resultDto;
		}
		InterviewSchedule interviewSchedule = new InterviewSchedule();
		interviewSchedule.setId(commonService.nextSequenceNumber());
		interviewSchedule.setJrNumber(interviewScheduleDto.getJrNumber());
		interviewSchedule.setCandidateId(interviewScheduleDto.getCandidateId());
		interviewSchedule.setInterviewerId(interviewScheduleDto.getInterviewerId());
		interviewSchedule.setScheduleDateTime(interviewScheduleDto.getScheduleDateTime());
		interviewSchedule.setTimeZone(interviewScheduleDto.getTimeZone());
		interviewSchedule.setDuration(interviewScheduleDto.getDuration());
		interviewSchedule.setScheduleRemarks(interviewScheduleDto.getScheduleRemarks());
		interviewSchedule.setMode(interviewScheduleDto.getMode());
		interviewSchedule.setVenue(interviewScheduleDto.getVenue());
		interviewSchedule.setRecStatus(interviewScheduleDto.getRecStatus());
		interviewSchedule.setRound(interviewScheduleDto.getRound());
		interviewSchedule.setCreatedBy(interviewScheduleDto.getCreatedBy());
		interviewSchedule.setUpdatedBy(interviewScheduleDto.getUpdatedBy());
		interviewSchedule.setCreatedDateTime(LocalDateTime.now());
		interviewSchedule.setUpdatedDateTime(LocalDateTime.now());
		interviewScheduleRepository.save(interviewSchedule);
		this.updateCompletedPreviousRound(interviewScheduleDto);
		if (interviewScheduleDto.getRound() != HireProUsConstants.INTERVIEW_ROUND_INITIAL
				&& interviewScheduleDto.getRound() != HireProUsConstants.INTERVIEW_ROUND_BU) {
			// Send Scheduled Mail.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailService.sendInterviewScheduledEmail(interviewScheduleDto, interviewSchedule);
					} catch (Exception e) {
						logger.error("Email for Interview Scheduled is not Sent.");
						logger.error(HireProUsUtil.getErrorMessage(e));
					}
				}
			}).start();
		}
		resultDto.setId(interviewSchedule.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	/**
	 * @param interviewScheduleDto
	 * @throws Exception
	 */
	private void updateCompletedPreviousRound(InterviewScheduleDto interviewScheduleDto) throws Exception {
		int round = interviewScheduleDto.getRound();
		// 3 - customer
		if (round == 3) {
			JobRequestDto jobRequestDto = jobRequestService
					.getJobRequestInfoByJRNumber(interviewScheduleDto.getJrNumber());
			if (jobRequestDto != null && !jobRequestDto.getPlacementFor().isEmpty()
					&& jobRequestDto.getPlacementFor().equals(HireProUsConstants.PLACEMENT_FOR_CUSTOMER)) {
				round = 2;
			}
			// 4 - HR
		} else if (round == 4) {
			JobRequestDto jobRequestDto = jobRequestService
					.getJobRequestInfoByJRNumber(interviewScheduleDto.getJrNumber());
			if (jobRequestDto != null && !jobRequestDto.getPlacementFor().isEmpty()
					&& jobRequestDto.getPlacementFor().equals(HireProUsConstants.PLACEMENT_FOR_INTERNAL)) {
				round = 3;
			}
			// 5 - BU approval
		} else if (round == 5) {
			CandidateDto candidateDto = candidateService
					.getCandidateShortInfoById(interviewScheduleDto.getCandidateId() + "");
			if (candidateDto != null && !candidateDto.getCandidateType().isEmpty()
					&& candidateDto.getCandidateType().equals(HireProUsConstants.CANDIDATE_TYPE_INTERNAL)) {
				round = 4;
			}
		}
		InterviewSchedule interviewScheduleOld = interviewScheduleRepository.findByJrNumberAndCandidateIdAndRound(
				interviewScheduleDto.getJrNumber(), interviewScheduleDto.getCandidateId(), round - 1);
		if (interviewScheduleOld != null) {
			interviewScheduleOld.setCompleted(1);
//			interviewScheduleOld.setUpdatedBy(interviewScheduleDto.getUpdatedBy());
//			interviewScheduleOld.setUpdatedDateTime(LocalDateTime.now());
			interviewScheduleRepository.save(interviewScheduleOld);
		}
	}

	@Override
	public InterviewScheduleDto updateInterviewSchedule(final InterviewScheduleDto interviewScheduleDto,
			final String lang) throws Exception {
		InterviewScheduleDto resultDto = new InterviewScheduleDto();
		InterviewSchedule interviewSchedule = interviewScheduleRepository.findById(interviewScheduleDto.getId());
		if (interviewSchedule == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "InterviewSchedule Id" }, lang));
			logger.info(resultDto.getMessage());
			return resultDto;
		}
		LocalDateTime newStartDateTime = interviewScheduleDto.getScheduleDateTime();
		String newHourMinute = HireProUsUtil.getHourMinute(interviewScheduleDto.getDuration());
		LocalDateTime newEndDateTime = interviewScheduleDto.getScheduleDateTime()
				.plusHours(Long.parseLong(newHourMinute.split(",")[0]))
				.plusMinutes(Long.parseLong(newHourMinute.split(",")[1]));
		List<InterviewSchedule> interviewScheduleList = interviewScheduleRepository
				.findByInterviewerIdAndResultRemarksNull(interviewScheduleDto.getInterviewerId());
		InterviewSchedule interScheduleList = interviewScheduleRepository.findByInterviewerIdAndScheduleDateTime(
				interviewScheduleDto.getInterviewerId(), interviewScheduleDto.getScheduleDateTime());
		// added on 11/23/2021 Not able to Update the schedule
		if (interScheduleList != null) {
			interviewSchedule.setScheduleRemarks(interviewScheduleDto.getScheduleRemarks());
			interviewSchedule.setMode(interviewScheduleDto.getMode());
			interviewSchedule.setVenue(interviewScheduleDto.getVenue());
			interviewSchedule.setRecStatus(interviewScheduleDto.getRecStatus());
			interviewSchedule.setRound(interviewScheduleDto.getRound());
			interviewSchedule.setUpdatedBy(interviewScheduleDto.getUpdatedBy());
			interviewSchedule.setUpdatedDateTime(LocalDateTime.now());
			interviewScheduleRepository.save(interviewSchedule);
		}
		boolean alreadyScheduled = false;
		for (InterviewSchedule interviewScheduleCheck : interviewScheduleList) {
			String hourMinute = HireProUsUtil.getHourMinute(interviewScheduleCheck.getDuration());
			LocalDateTime startDateTime = interviewScheduleCheck.getScheduleDateTime();
			LocalDateTime endDateTime = interviewScheduleCheck.getScheduleDateTime()
					.plusHours(Long.parseLong(hourMinute.split(",")[0]))
					.plusMinutes(Long.parseLong(hourMinute.split(",")[1]));
			if (newStartDateTime.equals(startDateTime) || newEndDateTime.equals(endDateTime)
					|| (newStartDateTime.isAfter(startDateTime) && newStartDateTime.isBefore(endDateTime))
					|| newEndDateTime.isAfter(startDateTime) && newEndDateTime.isBefore(endDateTime)
					|| newStartDateTime.isBefore(startDateTime) && newEndDateTime.isAfter(endDateTime)) {
				if (!interviewScheduleCheck.getCandidateId().equals(interviewScheduleDto.getCandidateId())) {
					alreadyScheduled = true;
					break;
				}
			}
		}
		if (alreadyScheduled) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists.interview",
					new String[] { CacheUtil.getUsersMap().get(interviewScheduleDto.getInterviewerId()) }, lang));
			logger.info(resultDto.getMessage());
			return resultDto;
		}
		interviewSchedule.setJrNumber(interviewScheduleDto.getJrNumber());
		interviewSchedule.setCandidateId(interviewScheduleDto.getCandidateId());
		interviewSchedule.setInterviewerId(interviewScheduleDto.getInterviewerId());
		interviewSchedule.setScheduleDateTime(interviewScheduleDto.getScheduleDateTime());
		interviewSchedule.setTimeZone(interviewScheduleDto.getTimeZone());
		interviewSchedule.setDuration(interviewScheduleDto.getDuration());
		interviewSchedule.setScheduleRemarks(interviewScheduleDto.getScheduleRemarks());
		interviewSchedule.setMode(interviewScheduleDto.getMode());
		interviewSchedule.setVenue(interviewScheduleDto.getVenue());
		interviewSchedule.setRecStatus(interviewScheduleDto.getRecStatus());
		interviewSchedule.setRound(interviewScheduleDto.getRound());
		interviewSchedule.setUpdatedBy(interviewScheduleDto.getUpdatedBy());
		interviewSchedule.setUpdatedDateTime(LocalDateTime.now());
		interviewScheduleRepository.save(interviewSchedule);
		if (interviewScheduleDto.getRound() != HireProUsConstants.INTERVIEW_ROUND_INITIAL
				&& interviewScheduleDto.getRound() != HireProUsConstants.INTERVIEW_ROUND_BU) {
			// Send Schedule Updated Mail.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailService.sendInterviewScheduleUpdatedEmail(interviewScheduleDto, interviewSchedule);
					} catch (Exception e) {
						logger.error("Email for Interview Schedule Update is not Sent.");
						logger.error(HireProUsUtil.getErrorMessage(e));
					}
				}
			}).start();
		}
		resultDto.setId(interviewSchedule.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public InterviewScheduleDto updateResult(final InterviewScheduleDto interviewScheduleDto, final String lang)
			throws Exception {
		InterviewScheduleDto resultDto = new InterviewScheduleDto();
		InterviewSchedule interviewSchedule = interviewScheduleRepository.findById(interviewScheduleDto.getId());
		if (interviewSchedule == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "InterviewSchedule Id" }, lang));
			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (HireProUsConstants.REC_STATUS_PASSED_R1.equals(interviewScheduleDto.getRecStatus())) {
			JobRequestDto jobRequestDto = jobRequestService
					.getJobRequestInfoByJRNumber(interviewSchedule.getJrNumber());
			if (jobRequestDto != null && !jobRequestDto.getPlacementFor().isEmpty()
					&& jobRequestDto.getPlacementFor().equals(HireProUsConstants.PLACEMENT_FOR_CUSTOMER)) {
				interviewScheduleDto.setRecStatus(HireProUsConstants.REC_STATUS_PASSED_R2);
			}
		} else if (HireProUsConstants.REC_STATUS_PASSED_R2.equals(interviewScheduleDto.getRecStatus())) {
			JobRequestDto jobRequestDto = jobRequestService
					.getJobRequestInfoByJRNumber(interviewSchedule.getJrNumber());
			if (jobRequestDto != null && !jobRequestDto.getPlacementFor().isEmpty()
					&& jobRequestDto.getPlacementFor().equals(HireProUsConstants.PLACEMENT_FOR_INTERNAL)) {
				interviewScheduleDto.setRecStatus(HireProUsConstants.REC_STATUS_PASSED_CR3);
			}
		} else if (HireProUsConstants.REC_STATUS_PASSED_CR3.equals(interviewScheduleDto.getRecStatus())) {
			CandidateDto candidateDto = candidateService
					.getCandidateShortInfoById(interviewSchedule.getCandidateId() + "");
			if (candidateDto != null && !candidateDto.getCandidateType().isEmpty()
					&& candidateDto.getCandidateType().equals(HireProUsConstants.CANDIDATE_TYPE_INTERNAL)) {
				interviewScheduleDto.setRecStatus(HireProUsConstants.REC_STATUS_PASSED_HR4);
			}
		}
		interviewSchedule.setRecStatus(interviewScheduleDto.getRecStatus());
		interviewSchedule.setResultRemarks(interviewScheduleDto.getResultRemarks());
		interviewSchedule.setUpdatedBy(interviewScheduleDto.getUpdatedBy());
		interviewSchedule.setUpdatedDateTime(LocalDateTime.now());
		interviewScheduleRepository.save(interviewSchedule);
		if (HireProUsConstants.REC_STATUS_PASSED_HR4.equals(interviewScheduleDto.getRecStatus())) {
			// Update in Interview schedule for BU approval.
			InterviewSchedule interviewScheduleNew = new InterviewSchedule();
			interviewScheduleNew.setId(commonService.nextSequenceNumber());
			interviewScheduleNew.setJrNumber(interviewSchedule.getJrNumber());
			interviewScheduleNew.setCandidateId(interviewSchedule.getCandidateId());
			interviewScheduleNew.setInterviewerId(interviewSchedule.getCreatedBy());
			interviewScheduleNew.setScheduleDateTime(LocalDateTime.now());
			interviewScheduleNew.setTimeZone(interviewScheduleDto.getTimeZone());
			interviewScheduleNew.setDuration("0");
			interviewScheduleNew.setScheduleRemarks("For Approval by BU");
			interviewScheduleNew.setMode(interviewScheduleDto.getMode());
			interviewScheduleNew.setVenue(interviewScheduleDto.getVenue());
			interviewScheduleNew.setRecStatus(HireProUsConstants.REC_STATUS_SCHEDULED_BU);
			interviewScheduleNew.setRound(HireProUsConstants.INTERVIEW_ROUND_BU);
			interviewScheduleNew.setCreatedBy(interviewSchedule.getCreatedBy());
			interviewScheduleNew.setUpdatedBy(interviewSchedule.getUpdatedBy());
			interviewScheduleNew.setCreatedDateTime(LocalDateTime.now());
			interviewScheduleNew.setUpdatedDateTime(LocalDateTime.now());
			interviewScheduleRepository.save(interviewScheduleNew);
		}
		// Update status in candidate.
		if (interviewScheduleDto.getRecStatus().equals(HireProUsConstants.REC_STATUS_APPROVED_BU)) {
			interviewScheduleDto.setRecStatus(HireProUsConstants.REC_STATUS_SELECTED);
		}
		candidateService.updateRecStatus(interviewSchedule.getCandidateId(), interviewScheduleDto.getRecStatus(),
				interviewScheduleDto.getUpdatedBy());
		// If candidate is approved by BU, increment the closed openings.
		if (interviewScheduleDto.getRecStatus().equals(HireProUsConstants.REC_STATUS_SELECTED)) {
			jobRequestService.updateClosedOpening(interviewSchedule.getJrNumber());
			CandidateDto candidateDto = candidateService.getCandidateById(interviewSchedule.getCandidateId() + "");
			// Send Selected Mail.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailService.sendBUResultEmail(candidateDto, "Selected");
					} catch (Exception e) {
						logger.error("Email for Selected is not Sent.");
						logger.error(HireProUsUtil.getErrorMessage(e));
					}
				}
			}).start();
		}
		// If candidate is hold by BU.
		if (interviewScheduleDto.getRecStatus().equals(HireProUsConstants.REC_STATUS_HOLDED_BU)) {
			CandidateDto candidateDto = candidateService.getCandidateById(interviewSchedule.getCandidateId() + "");
			// Send Hold by BU Mail.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailService.sendBUResultEmail(candidateDto, "Hold");
					} catch (Exception e) {
						logger.error("Email for Hold by BU is not Sent.");
						logger.error(HireProUsUtil.getErrorMessage(e));
					}
				}
			}).start();
		}
		// If candidate is rejected by BU.
		if (interviewScheduleDto.getRecStatus().equals(HireProUsConstants.REC_STATUS_REJECTED_BU)) {
			CandidateDto candidateDto = candidateService.getCandidateById(interviewSchedule.getCandidateId() + "");
			// Send Rejected by BU Mail.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailService.sendBUResultEmail(candidateDto, "Rejected");
					} catch (Exception e) {
						logger.error("Email for Rejected by BU is not Sent.");
						logger.error(HireProUsUtil.getErrorMessage(e));
					}
				}
			}).start();
		}
		// send Mail for Interview Result
		interviewScheduleDto.setUpdatedDateTime(interviewSchedule.getUpdatedDateTime());
		interviewScheduleDto.setInterviewerId(interviewSchedule.getInterviewerId());
		interviewScheduleDto.setJrNumber(interviewSchedule.getJrNumber());
		interviewScheduleDto.setCandidateId(interviewSchedule.getCandidateId());
		interviewScheduleDto.setMode(interviewSchedule.getMode());
		interviewScheduleDto.setVenue(interviewSchedule.getVenue());
		if (interviewScheduleDto.getRound() != HireProUsConstants.INTERVIEW_ROUND_INITIAL
				&& interviewScheduleDto.getRound() != HireProUsConstants.INTERVIEW_ROUND_BU) {
			// Send Schedule Updated Mail.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailService.sendInterviewResultEmail(interviewScheduleDto);
					} catch (Exception e) {
						logger.error("Email for Interview Result is not Sent.");
						logger.error(HireProUsUtil.getErrorMessage(e));
					}
				}
			}).start();
		}
		// Send For BU Approval Email.
		if (HireProUsConstants.REC_STATUS_PASSED_HR4.equals(interviewScheduleDto.getRecStatus())) {
			CandidateDto candidateDto = candidateService.getCandidateById(interviewScheduleDto.getCandidateId() + "");
			// Send For BU Approval Mail.
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mailService.sendForBUApprovalEmail(candidateDto);
					} catch (Exception e) {
						logger.error("Email for For BU Approval is not Sent.");
						logger.error(HireProUsUtil.getErrorMessage(e));
					}
				}
			}).start();
		}
		resultDto.setId(interviewSchedule.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public void updateShortlistedResult(final InterviewScheduleDto interviewScheduleDto, final String lang)
			throws Exception {

		InterviewSchedule interviewSchedule = interviewScheduleRepository.findByJrNumberAndCandidateIdAndRound(
				interviewScheduleDto.getJrNumber(), interviewScheduleDto.getCandidateId(), 0);

		interviewSchedule.setRecStatus(interviewScheduleDto.getRecStatus());
		interviewSchedule.setResultRemarks(interviewScheduleDto.getResultRemarks());

		interviewSchedule.setUpdatedBy(interviewScheduleDto.getUpdatedBy());
		interviewSchedule.setUpdatedDateTime(LocalDateTime.now());

		interviewScheduleRepository.save(interviewSchedule);
	}

	@Override
	public InterviewScheduleDto getInterviewScheduleById(final String interviewScheduleId) throws Exception {

		InterviewSchedule interviewSchedule = interviewScheduleRepository.findById(Long.parseLong(interviewScheduleId));

		return interviewSchedule != null ? this.getInterviewScheduleDto(interviewSchedule, false) : null;
	}

	@Override
	public List<InterviewScheduleDto> getAllInterviewSchedules() throws Exception {

		List<InterviewSchedule> interviewSchedulesList = interviewScheduleRepository
				.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());

		return this.getInterviewScheduleDtoList(interviewSchedulesList);
	}

	@Override
	public List<InterviewScheduleDto> getViewHistory(String jrNumber, Long candidateId, String lang) throws Exception {

//		List<InterviewSchedule> interviewSchedulesList = interviewScheduleRepository
//				.findByJrNumberAndCandidateId(jrNumber, candidateId, HireProUsUtil.orderByCreatedDateTimeAsc());
		
		// Added on 29-12-2022 for view history not working in AI Scan
		List<InterviewSchedule> interviewSchedulesList = interviewScheduleRepository
                .findByJrNumberAndCandidateId(jrNumber, candidateId, HireProUsUtil.orderByIdAsc());

		List<InterviewScheduleDto> interviewScheduleDtoList = new ArrayList<InterviewScheduleDto>();
		int index = 0;
		boolean dropped = false;
		for (InterviewSchedule interviewSchedule : interviewSchedulesList) {

			if (interviewSchedule.getRound() > index) {
				interviewScheduleDtoList.add(new InterviewScheduleDto());
				index++;
			}

			InterviewScheduleDto interviewScheduleDto = this.getInterviewScheduleDto(interviewSchedule, true);
			if (interviewScheduleDto != null) {
				interviewScheduleDtoList.add(interviewScheduleDto);

				if (interviewScheduleDto.getStatus() == 1) {
					dropped = true;
				}
			}

			index++;
		}

		if (dropped) {
			InterviewScheduleDto interviewScheduleDto = new InterviewScheduleDto();
			interviewScheduleDto.setRecStatus(HireProUsConstants.REC_STATUS_DROPPED);
			interviewScheduleDtoList.add(interviewScheduleDto);
		}
		System.out.println(interviewScheduleDtoList);

		return interviewScheduleDtoList;
	}

	@Override
	public Long getMyInterviewsCount(Long userId) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(new Criteria().orOperator(Criteria.where("interviewerId").is(userId),
				Criteria.where("createdBy").is(userId)));

		criteriaList.add(Criteria.where("recStatus")
				.in(Arrays.asList(HireProUsConstants.REC_STATUS_SCHEDULED_R1,
						HireProUsConstants.REC_STATUS_SCHEDULED_R2, HireProUsConstants.REC_STATUS_SCHEDULED_CR3,
						HireProUsConstants.REC_STATUS_SCHEDULED_HR4)));

		List<InterviewSchedule> interviewScheduleList = null;
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			interviewScheduleList = mongoTemplate.find(searchQuery, InterviewSchedule.class);
		}

		if (interviewScheduleList == null || interviewScheduleList.isEmpty()) {
			return 0L;
		}

		Long count = 0L;
		for (InterviewSchedule interviewSchedule : interviewScheduleList) {

			CandidateDto candidateDto = candidateService
					.getCandidateShortInfoById(interviewSchedule.getCandidateId() + "");
			if (candidateDto == null || candidateDto.getRecStatus().equals(HireProUsConstants.REC_STATUS_DROPPED)) {
				continue;
			}

			count += 1;
		}

		return count;
	}

	@Override
	public List<InterviewScheduleDto> getMyInterviews(Long userId) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(new Criteria().orOperator(Criteria.where("interviewerId").is(userId),
				Criteria.where("createdBy").is(userId)));

		criteriaList.add(Criteria.where("recStatus")
				.in(Arrays.asList(HireProUsConstants.REC_STATUS_SCHEDULED_R1,
						HireProUsConstants.REC_STATUS_SCHEDULED_R2, HireProUsConstants.REC_STATUS_SCHEDULED_CR3,
						HireProUsConstants.REC_STATUS_SCHEDULED_HR4)));

		List<InterviewSchedule> interviewSchedulesList = new ArrayList<InterviewSchedule>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			interviewSchedulesList = mongoTemplate.find(searchQuery, InterviewSchedule.class);
		}

		return this.getInterviewScheduleDtoList(interviewSchedulesList);
	}

	@Override
	public List<InterviewScheduleDto> searchMyInterviews(final InterviewScheduleSearchDto interviewScheduleSearchDto,
			final Long userId) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(new Criteria().orOperator(Criteria.where("interviewerId").is(userId),
				Criteria.where("createdBy").is(userId)));

		criteriaList.add(Criteria.where("recStatus")
				.in(Arrays.asList(HireProUsConstants.REC_STATUS_SCHEDULED_R1,
						HireProUsConstants.REC_STATUS_SCHEDULED_R2, HireProUsConstants.REC_STATUS_SCHEDULED_CR3,
						HireProUsConstants.REC_STATUS_SCHEDULED_HR4)));

		if (interviewScheduleSearchDto.getJrNumber() != null && !interviewScheduleSearchDto.getJrNumber().isEmpty()) {
			criteriaList
					.add(Criteria.where("jrNumber").regex("(?i).*" + interviewScheduleSearchDto.getJrNumber() + ".*"));
		}
		if (interviewScheduleSearchDto.getInterviewerIdList() != null
				&& interviewScheduleSearchDto.getInterviewerIdList().size() > 0) {
			if (interviewScheduleSearchDto.getInterviewerIdList().get(0) != null) {
				criteriaList.add(Criteria.where("interviewerId").in(interviewScheduleSearchDto.getInterviewerIdList()));
			}
		}
		if (interviewScheduleSearchDto.getCandidateName() != null
				&& !interviewScheduleSearchDto.getCandidateName().isEmpty()) {
			List<Long> candidateIds = candidateService
					.searchCadidateIdsByName(interviewScheduleSearchDto.getCandidateName());
			if (candidateIds != null && candidateIds.size() > 0) {
				criteriaList.add(Criteria.where("candidateId").in(candidateIds));
			} else {
				return new ArrayList<InterviewScheduleDto>();
			}
		}

		List<InterviewSchedule> interviewSchedulesList = new ArrayList<InterviewSchedule>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			interviewSchedulesList = mongoTemplate.find(searchQuery, InterviewSchedule.class);
		}

		return this.getInterviewScheduleDtoList(interviewSchedulesList);
	}

	@Override
	public List<InterviewScheduleDto> getInterviewScheduledList(final int round) throws Exception {

		List<InterviewSchedule> interviewSchedulesList = new ArrayList<InterviewSchedule>();
		if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL1 == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatus(
					HireProUsConstants.REC_STATUS_SCHEDULED_R1, HireProUsUtil.orderByUpdatedDateTimeDesc());

		} else if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL2 == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatus(
					HireProUsConstants.REC_STATUS_SCHEDULED_R2, HireProUsUtil.orderByUpdatedDateTimeDesc());

		} else if (HireProUsConstants.INTERVIEW_ROUND_CUSTOMER == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatus(
					HireProUsConstants.REC_STATUS_SCHEDULED_CR3, HireProUsUtil.orderByUpdatedDateTimeDesc());

		} else if (HireProUsConstants.INTERVIEW_ROUND_HR == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatus(
					HireProUsConstants.REC_STATUS_SCHEDULED_HR4, HireProUsUtil.orderByUpdatedDateTimeDesc());

		} else if (HireProUsConstants.INTERVIEW_ROUND_BU == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatus(
					HireProUsConstants.REC_STATUS_SCHEDULED_BU, HireProUsUtil.orderByUpdatedDateTimeDesc());

		}

		return this.getInterviewScheduleDtoList(interviewSchedulesList);
	}

	@Override
	public List<InterviewScheduleDto> searchInterviewScheduledList(
			final InterviewScheduleSearchDto interviewScheduleSearchDto, final int round) throws Exception {

		String recStatus = "";
		if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL1 == round) {
			recStatus = HireProUsConstants.REC_STATUS_SCHEDULED_R1;
		} else if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL2 == round) {
			recStatus = HireProUsConstants.REC_STATUS_SCHEDULED_R2;
		} else if (HireProUsConstants.INTERVIEW_ROUND_CUSTOMER == round) {
			recStatus = HireProUsConstants.REC_STATUS_SCHEDULED_CR3;
		} else if (HireProUsConstants.INTERVIEW_ROUND_HR == round) {
			recStatus = HireProUsConstants.REC_STATUS_SCHEDULED_HR4;
		} else if (HireProUsConstants.INTERVIEW_ROUND_BU == round) {
			recStatus = HireProUsConstants.REC_STATUS_SCHEDULED_BU;
		}

		final List<Criteria> criteriaList = new ArrayList<>();
		if (interviewScheduleSearchDto.getJrNumber() != null && !interviewScheduleSearchDto.getJrNumber().isEmpty()) {
			criteriaList
					.add(Criteria.where("jrNumber").regex("(?i).*" + interviewScheduleSearchDto.getJrNumber() + ".*"));
		}
		if (interviewScheduleSearchDto.getInterviewerIdList() != null
				&& interviewScheduleSearchDto.getInterviewerIdList().size() > 0) {
			if (interviewScheduleSearchDto.getInterviewerIdList().get(0) != null) {
				criteriaList.add(Criteria.where("interviewerId").in(interviewScheduleSearchDto.getInterviewerIdList()));
			}
		}
		if (interviewScheduleSearchDto.getCandidateName() != null
				&& !interviewScheduleSearchDto.getCandidateName().isEmpty()) {
			List<Long> candidateIds = candidateService
					.searchCadidateIdsByName(interviewScheduleSearchDto.getCandidateName());
			if (candidateIds != null && candidateIds.size() > 0) {
				criteriaList.add(Criteria.where("candidateId").in(candidateIds));
			} else {
				return new ArrayList<InterviewScheduleDto>();
			}
		}
		criteriaList.add(Criteria.where("recStatus").is(recStatus));

		List<InterviewSchedule> interviewSchedulesList = new ArrayList<InterviewSchedule>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			interviewSchedulesList = mongoTemplate.find(searchQuery, InterviewSchedule.class);
		}

		return this.getInterviewScheduleDtoList(interviewSchedulesList);
	}

	@Override
	public List<InterviewScheduleDto> getForSchedule(final int round) throws Exception {

		List<InterviewSchedule> interviewSchedulesList = new ArrayList<InterviewSchedule>();
		if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL1 == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatusAndCompleted(
					HireProUsConstants.REC_STATUS_SHORTLISTED_0, 0, HireProUsUtil.orderByUpdatedDateTimeDesc());

		} else if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL2 == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatusAndCompleted(
					HireProUsConstants.REC_STATUS_PASSED_R1, 0, HireProUsUtil.orderByUpdatedDateTimeDesc());

//			List<String> jrNumbers = jobRequestService.getJRNumbersByPlacementFor("Internal");
//			if (jrNumbers.size() > 0) {
//				interviewSchedulesList.stream()
//						.filter(interviewSchedule -> jrNumbers.contains(interviewSchedule.getJrNumber()))
//						.collect(Collectors.toList());
//			}

		} else if (HireProUsConstants.INTERVIEW_ROUND_CUSTOMER == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatusAndCompleted(
					HireProUsConstants.REC_STATUS_PASSED_R2, 0, HireProUsUtil.orderByUpdatedDateTimeDesc());

//			List<InterviewSchedule> interviewSchedulesListCustomer = interviewScheduleRepository
//					.findByRecStatusAndCompleted(HireProUsConstants.REC_STATUS_PASSED_R1, 0,
//							HireProUsUtil.orderByUpdatedDateTimeDesc());
//
//			List<String> jrNumbers = jobRequestService.getJRNumbersByPlacementFor("Customer");
//			if (jrNumbers.size() > 0) {
//				interviewSchedulesListCustomer.stream()
//						.filter(interviewSchedule -> jrNumbers.contains(interviewSchedule.getJrNumber()))
//						.collect(Collectors.toList());
//			}
//
//			interviewSchedulesList.addAll(interviewSchedulesListCustomer);
//
//			Comparator<InterviewSchedule> compareByUpdatedDateTime = Comparator
//					.comparing(InterviewSchedule::getUpdatedDateTime);
//
//			interviewSchedulesList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		} else if (HireProUsConstants.INTERVIEW_ROUND_HR == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatusAndCompleted(
					HireProUsConstants.REC_STATUS_PASSED_CR3, 0, HireProUsUtil.orderByUpdatedDateTimeDesc());

		} else if (HireProUsConstants.INTERVIEW_ROUND_BU == round) {

			interviewSchedulesList = interviewScheduleRepository.findByRecStatusAndCompleted(
					HireProUsConstants.REC_STATUS_PASSED_HR4, 0, HireProUsUtil.orderByUpdatedDateTimeDesc());
		}

		return this.getInterviewScheduleDtoList(interviewSchedulesList);
	}

	@Override
	public List<InterviewScheduleDto> searchForSchedule(final InterviewScheduleSearchDto interviewScheduleSearchDto,
			final int round) throws Exception {

		String recStatus = "";
		if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL1 == round) {
			recStatus = HireProUsConstants.REC_STATUS_SHORTLISTED_0;
		} else if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL2 == round) {
			recStatus = HireProUsConstants.REC_STATUS_PASSED_R1;
		} else if (HireProUsConstants.INTERVIEW_ROUND_CUSTOMER == round) {
			recStatus = HireProUsConstants.REC_STATUS_PASSED_R2;
		} else if (HireProUsConstants.INTERVIEW_ROUND_HR == round) {
			recStatus = HireProUsConstants.REC_STATUS_PASSED_CR3;
		} else if (HireProUsConstants.INTERVIEW_ROUND_BU == round) {
			recStatus = HireProUsConstants.REC_STATUS_PASSED_HR4;
		}

		final List<Criteria> criteriaList = new ArrayList<>();
		if (interviewScheduleSearchDto.getJrNumber() != null && !interviewScheduleSearchDto.getJrNumber().isEmpty()) {
			criteriaList
					.add(Criteria.where("jrNumber").regex("(?i).*" + interviewScheduleSearchDto.getJrNumber() + ".*"));
		}
		if (interviewScheduleSearchDto.getInterviewerIdList() != null
				&& interviewScheduleSearchDto.getInterviewerIdList().size() > 0) {
			if (interviewScheduleSearchDto.getInterviewerIdList().get(0) != null) {
				criteriaList.add(Criteria.where("interviewerId").in(interviewScheduleSearchDto.getInterviewerIdList()));
			}
		}
		if (interviewScheduleSearchDto.getCandidateName() != null
				&& !interviewScheduleSearchDto.getCandidateName().isEmpty()) {
			List<Long> candidateIds = candidateService
					.searchCadidateIdsByName(interviewScheduleSearchDto.getCandidateName());
			if (candidateIds != null && candidateIds.size() > 0) {
				criteriaList.add(Criteria.where("candidateId").in(candidateIds));
			} else {
				return new ArrayList<InterviewScheduleDto>();
			}
		}

		criteriaList.add(Criteria.where("recStatus").is(recStatus));
		criteriaList.add(Criteria.where("completed").is(0));

		List<InterviewSchedule> interviewSchedulesList = new ArrayList<InterviewSchedule>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			interviewSchedulesList = mongoTemplate.find(searchQuery, InterviewSchedule.class);
		}

		return this.getInterviewScheduleDtoList(interviewSchedulesList);
	}

	@Override
	public List<InterviewScheduleDto> getAllForSchedule() throws Exception {

		List<InterviewSchedule> interviewSchedulesList = interviewScheduleRepository.findByRecStatusInAndCompleted(
				Arrays.asList(HireProUsConstants.REC_STATUS_SHORTLISTED_0, HireProUsConstants.REC_STATUS_PASSED_R1,
						HireProUsConstants.REC_STATUS_PASSED_R2, HireProUsConstants.REC_STATUS_PASSED_CR3),
				0, HireProUsUtil.orderByUpdatedDateTimeDesc());

		return this.getInterviewScheduleDtoList(interviewSchedulesList);
	}

	@Override
	public List<InterviewScheduleDto> searchAllForSchedule(final InterviewScheduleSearchDto interviewScheduleSearchDto)
			throws Exception {

		List<String> recStatusList = Arrays.asList(HireProUsConstants.REC_STATUS_SHORTLISTED_0,
				HireProUsConstants.REC_STATUS_PASSED_R1, HireProUsConstants.REC_STATUS_PASSED_R2,
				HireProUsConstants.REC_STATUS_PASSED_CR3);
		if (interviewScheduleSearchDto.getRound() != 0) {
			if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL1 == interviewScheduleSearchDto.getRound()) {
				recStatusList = Arrays.asList(HireProUsConstants.REC_STATUS_SHORTLISTED_0);
			} else if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL2 == interviewScheduleSearchDto.getRound()) {
				recStatusList = Arrays.asList(HireProUsConstants.REC_STATUS_PASSED_R1);
			} else if (HireProUsConstants.INTERVIEW_ROUND_CUSTOMER == interviewScheduleSearchDto.getRound()) {
				recStatusList = Arrays.asList(HireProUsConstants.REC_STATUS_PASSED_R2);
			} else if (HireProUsConstants.INTERVIEW_ROUND_HR == interviewScheduleSearchDto.getRound()) {
				recStatusList = Arrays.asList(HireProUsConstants.REC_STATUS_PASSED_CR3);
			} else if (HireProUsConstants.INTERVIEW_ROUND_BU == interviewScheduleSearchDto.getRound()) {
				recStatusList = Arrays.asList(HireProUsConstants.REC_STATUS_PASSED_HR4);
			}
		}

		final List<Criteria> criteriaList = new ArrayList<>();
		if (interviewScheduleSearchDto.getJrNumber() != null && !interviewScheduleSearchDto.getJrNumber().isEmpty()) {
			criteriaList
					.add(Criteria.where("jrNumber").regex("(?i).*" + interviewScheduleSearchDto.getJrNumber() + ".*"));
		}
		if (interviewScheduleSearchDto.getInterviewerIdList() != null
				&& interviewScheduleSearchDto.getInterviewerIdList().size() > 0) {
			if (interviewScheduleSearchDto.getInterviewerIdList().get(0) != null) {
				criteriaList.add(Criteria.where("interviewerId").in(interviewScheduleSearchDto.getInterviewerIdList()));
			}
		}
		if (interviewScheduleSearchDto.getCandidateName() != null
				&& !interviewScheduleSearchDto.getCandidateName().isEmpty()) {
			List<Long> candidateIds = candidateService
					.searchCadidateIdsByName(interviewScheduleSearchDto.getCandidateName());
			if (candidateIds != null && candidateIds.size() > 0) {
				criteriaList.add(Criteria.where("candidateId").in(candidateIds));
			} else {
				return new ArrayList<InterviewScheduleDto>();
			}
		}

		criteriaList.add(Criteria.where("recStatus").in(recStatusList));
		criteriaList.add(Criteria.where("completed").is(0));

		List<InterviewSchedule> interviewSchedulesList = new ArrayList<InterviewSchedule>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			interviewSchedulesList = mongoTemplate.find(searchQuery, InterviewSchedule.class);
		}

		return this.getInterviewScheduleDtoList(interviewSchedulesList);
	}

	/**
	 * @param interviewSchedulesList
	 * @return
	 * @throws Exception
	 */
	private List<InterviewScheduleDto> getInterviewScheduleDtoList(final List<InterviewSchedule> interviewSchedulesList)
			throws Exception {

		List<InterviewScheduleDto> interviewScheduleDtoList = new ArrayList<InterviewScheduleDto>();

		for (InterviewSchedule interviewSchedule : interviewSchedulesList) {
			InterviewScheduleDto interviewScheduleDto = this.getInterviewScheduleDto(interviewSchedule, false);
			if (interviewScheduleDto != null) {
				interviewScheduleDtoList.add(interviewScheduleDto);
			}
		}

		Comparator<InterviewScheduleDto> compareByUpdatedDateTime = Comparator
				.comparing(InterviewScheduleDto::getUpdatedDateTime);
		interviewScheduleDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return interviewScheduleDtoList;
	}

	/**
	 * @param interviewSchedulesList
	 * @return
	 * @throws Exception
	 */
	private InterviewScheduleDto getInterviewScheduleDto(final InterviewSchedule interviewSchedule,
			final boolean forHistory) throws Exception {

		InterviewScheduleDto interviewScheduleDto = new InterviewScheduleDto();

		interviewScheduleDto.setId(interviewSchedule.getId());
		interviewScheduleDto.setJrNumber(interviewSchedule.getJrNumber());

		interviewScheduleDto.setInterviewerId(interviewSchedule.getInterviewerId());
		interviewScheduleDto.setInterviewerName(CacheUtil.getUsersMap().get(interviewSchedule.getInterviewerId()));
		interviewScheduleDto.setCandidateId(interviewSchedule.getCandidateId());

		interviewScheduleDto.setScheduleDateTime(interviewSchedule.getScheduleDateTime());
		interviewScheduleDto.setTimeZone(interviewSchedule.getTimeZone());
		interviewScheduleDto.setDuration(interviewSchedule.getDuration());
		interviewScheduleDto.setScheduleRemarks(interviewSchedule.getScheduleRemarks());

		interviewScheduleDto.setMode(interviewSchedule.getMode());
		interviewScheduleDto.setVenue(interviewSchedule.getVenue());

		interviewScheduleDto.setRecStatus(interviewSchedule.getRecStatus());
		interviewScheduleDto.setResultRemarks(interviewSchedule.getResultRemarks());
		interviewScheduleDto.setRound(interviewSchedule.getRound());

		interviewScheduleDto.setCreatedBy(interviewSchedule.getCreatedBy());

		interviewScheduleDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(interviewSchedule.getCreatedDateTime()));

		interviewScheduleDto.setUpdatedBy(interviewSchedule.getUpdatedBy());
		interviewScheduleDto.setUpdatedByName(CacheUtil.getUsersMap().get(interviewSchedule.getUpdatedBy()));
		interviewScheduleDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(interviewSchedule.getUpdatedDateTime()));

		JobRequestDto jobRequestDto = jobRequestService.getJobRequestInfoByJRNumber(interviewSchedule.getJrNumber());
		if (jobRequestDto == null) {
			return null;
		}

		interviewScheduleDto.setJrId(jobRequestDto.getId());
		interviewScheduleDto.setCustomerName(jobRequestDto.getCustomerName());
		interviewScheduleDto.setBuId(jobRequestDto.getBuId());
		interviewScheduleDto.setBuName(jobRequestDto.getBuName());
		interviewScheduleDto.setRoleName(jobRequestDto.getRoleName());
		interviewScheduleDto.setNoOfOpenings(jobRequestDto.getNoOfOpenings());
		interviewScheduleDto.setLocation(jobRequestDto.getLocation());
		interviewScheduleDto.setEmploymentType(jobRequestDto.getEmploymentType());
		interviewScheduleDto.setPlacementFor(jobRequestDto.getPlacementFor());

		CandidateDto candidateDto = candidateService.getCandidateById(interviewSchedule.getCandidateId() + "");
		if (candidateDto == null
				|| (!forHistory && candidateDto.getRecStatus().equals(HireProUsConstants.REC_STATUS_DROPPED))) {
			return null;
		}

		if (candidateDto.getVendorId() != null) {
			if (candidateDto.getVendorId().equals(interviewScheduleDto.getCreatedBy())) {
				VendorDto vendor = vendorService.getVendorById((candidateDto.getVendorId().toString()));
				interviewScheduleDto.setCreatedByName(vendor.getVendorName());

			} else {
				interviewScheduleDto.setCreatedByName(CacheUtil.getUsersMap().get(interviewSchedule.getCreatedBy()));
			}
		} else {
			interviewScheduleDto.setCreatedByName(CacheUtil.getUsersMap().get(interviewSchedule.getCreatedBy()));
		}

		interviewScheduleDto.setCandidateName(candidateDto.getFullName());
		interviewScheduleDto.setContactNumber(candidateDto.getContactNumber());
		interviewScheduleDto.setSex(candidateDto.getSex());
		interviewScheduleDto.setExperience(candidateDto.getExperience());
		interviewScheduleDto.setCandidateType(candidateDto.getCandidateType());

		if (candidateDto.getRecStatus().equals(HireProUsConstants.REC_STATUS_DROPPED)) {
			interviewScheduleDto.setStatus(1);
		}

		return interviewScheduleDto;
	}

	@Override
	public InterviewScheduleDto deleteInterviewScheduleById(String interviewScheduleId) throws Exception {

		InterviewScheduleDto resultDto = new InterviewScheduleDto();

		InterviewSchedule interviewSchedule = interviewScheduleRepository.findById(Long.parseLong(interviewScheduleId));

		if (interviewSchedule == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "InterviewSchedule Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (interviewSchedule.getCompleted() == 1 || !interviewSchedule.getResultRemarks().isEmpty()) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto
					.setMessage(commonService.getMessage("cannot.delete", new String[] { "Interview Schedule" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		interviewScheduleRepository.deleteById(Long.parseLong(interviewScheduleId));

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public int getUserDependencyCount(Long userId) {

		return interviewScheduleRepository.countByInterviewerIdOrCreatedByOrUpdatedBy(userId, userId, userId);
	}

	@Override

	public byte[] downloadInterviewScheduleSummary(List<InterviewScheduleDto> interviewScheduleDtoList, String lang)

			throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/interviewScheduleSummary_details.xlsx")
				.getFile();

		try (Workbook resourcesMgmtWB = new XSSFWorkbook(file)) {
			Sheet sheet = resourcesMgmtWB.getSheetAt(0);

			HireProUsDefaultMethods.cleanSheet(sheet);

			if (sheet.getRow(2) != null) {
				sheet.removeRow(sheet.getRow(2));
			}

			int rowNum = 2;

			for (InterviewScheduleDto interviewScheduleDto : interviewScheduleDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);

				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(interviewScheduleDto.getJrNumber());

				dataRow.createCell(2).setCellValue(interviewScheduleDto.getBuName());

				dataRow.createCell(3).setCellValue(interviewScheduleDto.getCustomerName());

				dataRow.createCell(4).setCellValue(interviewScheduleDto.getRoleName());

				dataRow.createCell(5).setCellValue(interviewScheduleDto.getCandidateName());

				dataRow.createCell(6).setCellValue(interviewScheduleDto.getInterviewerName());

				dataRow.createCell(7).setCellValue(interviewScheduleDto.getScheduleDateTime().toLocalDate().toString());

				dataRow.createCell(8).setCellValue(interviewScheduleDto.getTimeZone());

				dataRow.createCell(9).setCellValue(interviewScheduleDto.getDuration());

				dataRow.createCell(10).setCellValue(interviewScheduleDto.getScheduleRemarks());

				dataRow.createCell(11).setCellValue(interviewScheduleDto.getMode());

				dataRow.createCell(12).setCellValue(interviewScheduleDto.getVenue());

				dataRow.createCell(13).setCellValue(this.getRoundName(interviewScheduleDto.getRound()));

				dataRow.createCell(14).setCellValue(interviewScheduleDto.getResultRemarks());

				dataRow.createCell(15).setCellValue(interviewScheduleDto.getUpdatedByName());

				dataRow.createCell(16).setCellValue(interviewScheduleDto.getUpdatedDateTime().toLocalDate().toString());

				dataRow.createCell(17).setCellValue("");

				rowNum++;

			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			resourcesMgmtWB.write(outputStream);

			resourcesMgmtWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {

			logger.error("Error during Interview Schedule Summary Details download file", ex);

			return null;

		}

	}

	private String getRoundName(int round) {
		String roundName = "";
		if (round == 1)
			return "Internal Round 1";
		if (round == 2)
			return "Internal Round 2";
		if (round == 3)
			return "Customer Round";
		if (round == 4)
			return "HR Round";

		return roundName;
	}

	private String getRecStatus(String recStatus) {
		String roundName = "";
		switch (recStatus) {
		case "04":
			roundName = "Scheduled Internal Round 1";
			break;
		case "05":
			roundName = "Passed Internal Round 1";
			break;
		case "08":
			roundName = "Scheduled Internal Round 2";
			break;
		case "09":
			roundName = "Passed Internal Round 2";
			break;
		case "12":
			roundName = "Scheduled Customer Round";
			break;
		case "13":
			roundName = "Passed Customer Round";
			break;
		case "16":
			roundName = "Scheduled HR Round";
			break;
		case "17":
			roundName = "Passed HR Round";
			break;
		}
		return roundName;

	}

	@Override
	public List<InterviewScheduleDto> searchInterviewScheduleForDownload(InterviewScheduleDto interviewScheduleDto)
			throws Exception {
		List<InterviewScheduleDto> interviewScheduleDtoList = new ArrayList<InterviewScheduleDto>();

		final List<Criteria> criteriaList = new ArrayList<>();

		if (interviewScheduleDto.getJrNumber() != null && !interviewScheduleDto.getJrNumber().isEmpty()) {
			criteriaList.add(Criteria.where("jrNumber").regex("(?i).*" + interviewScheduleDto.getJrNumber() + ".*"));
		}

		if (interviewScheduleDto.getCandidateName() != null && !interviewScheduleDto.getCandidateName().isEmpty()) {
			List<Long> candidateIds = candidateService.searchCadidateIdsByName(interviewScheduleDto.getCandidateName());
			if (candidateIds != null && candidateIds.size() > 0) {
				criteriaList.add(Criteria.where("candidateId").in(candidateIds));
			} else {
				return new ArrayList<InterviewScheduleDto>();
			}
		}

		if (interviewScheduleDto.getInterviewerName() != null && !interviewScheduleDto.getInterviewerName().isEmpty()) {
			List<Long> interviewerIds = userService.searchUsersIdsByName(interviewScheduleDto.getInterviewerName());
			if (interviewerIds != null && interviewerIds.size() > 0) {
				criteriaList.add(Criteria.where("interviewerId").in(interviewerIds));
			} else {
				return new ArrayList<InterviewScheduleDto>();
			}
		}

		if (interviewScheduleDto.getRound() == 1 || interviewScheduleDto.getRound() == 2
				|| interviewScheduleDto.getRound() == 3 || interviewScheduleDto.getRound() == 4) {
			criteriaList.add(Criteria.where("round").is(interviewScheduleDto.getRound()));
		}

		if (interviewScheduleDto.getRecStatus() != null && !interviewScheduleDto.getRecStatus().isEmpty()) {
			criteriaList.add(Criteria.where("recStatus").is(interviewScheduleDto.getInterviewerName()));
		}

		if (interviewScheduleDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(interviewScheduleDto.getFromDateTime()));
		}

		if (interviewScheduleDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(interviewScheduleDto.getToDateTime()));
		}

		List<InterviewSchedule> interviewScheduleList = new ArrayList<InterviewSchedule>();

		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			interviewScheduleList = mongoTemplate.find(searchQuery, InterviewSchedule.class);
		}

		for (InterviewSchedule interviewSchedule : interviewScheduleList) {
			InterviewScheduleDto interviewSchedule1 = this.getInterviewScheduleDto(interviewSchedule, false);
			if (interviewSchedule1 != null) {
				interviewScheduleDtoList.add(interviewSchedule1);
			}

		}

		Comparator<InterviewScheduleDto> compareByUpdatedDateTime = Comparator
				.comparing(InterviewScheduleDto::getUpdatedDateTime);
		interviewScheduleDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return interviewScheduleDtoList;

	}
}
