/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.InterviewSchedule;

/**
 * @author Sihab.
 *
 */
public interface InterviewScheduleRepository extends MongoRepository<InterviewSchedule, String> {

	/**
	 * @param interviewScheduleId
	 * @return
	 */
	InterviewSchedule findById(long interviewScheduleId);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param interviewerId
	 * @param scheduleDateTime
	 * @return
	 */
	InterviewSchedule findByInterviewerIdAndScheduleDateTime(Long interviewerId, LocalDateTime scheduleDateTime);

	/**
	 * @param recStatus
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<InterviewSchedule> findByRecStatus(String recStatus, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param jrNumber
	 * @param candidateId
	 * @param i
	 * @return
	 */
	InterviewSchedule findByJrNumberAndCandidateIdAndRound(String jrNumber, Long candidateId, int i);

	/**
	 * @param recStatusShortlisted0
	 * @param i
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<InterviewSchedule> findByRecStatusAndCompleted(String recStatusShortlisted0, int i,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param interviewerId
	 * @param scheduleDateTime
	 * @param round
	 * @return
	 */
	InterviewSchedule findByInterviewerIdAndScheduleDateTimeAndRound(Long interviewerId, LocalDateTime scheduleDateTime,
			int round);

	/**
	 * @param jrNumber
	 * @param candidateId
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<InterviewSchedule> findByJrNumberAndCandidateId(String jrNumber, Long candidateId,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param jrNumberList
	 * @return
	 */
	List<InterviewSchedule> findByJrNumberIn(List<String> jrNumberList);

	/**
	 * @param userId
	 * @param userId2
	 * @return
	 */
	Long countByInterviewerIdOrCreatedBy(Long userId, Long userId2);

	/**
	 * @param userId
	 * @param userId3
	 * @param userId2
	 * @return
	 */
	int countByInterviewerIdOrCreatedByOrUpdatedBy(Long userId, Long userId2, Long userId3);

	/**
	 * @param recStatusList
	 * @param completed
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<InterviewSchedule> findByRecStatusInAndCompleted(List<String> recStatusList, int completed,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param interviewerId
	 * @return
	 */
	List<InterviewSchedule> findByInterviewerIdAndResultRemarksNull(Long interviewerId);

	InterviewSchedule findByJrNumberAndCandidateId(String jrNumber, Long id);

}
