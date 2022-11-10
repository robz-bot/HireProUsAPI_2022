/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.InterviewScheduleDto;
import com.promantus.hireprous.dto.InterviewScheduleSearchDto;

/**
 * @author Sihab.
 *
 */
public interface InterviewScheduleService {

	/**
	 * @param interviewScheduleDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	InterviewScheduleDto addInterviewSchedule(final InterviewScheduleDto interviewScheduleDto, String lang)
			throws Exception;

	/**
	 * @param interviewScheduleDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	InterviewScheduleDto updateInterviewSchedule(final InterviewScheduleDto interviewScheduleDto, final String lang)
			throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> getAllInterviewSchedules() throws Exception;

	/**
	 * @param interviewScheduleId
	 * @return
	 * @throws Exception
	 */
	InterviewScheduleDto getInterviewScheduleById(String interviewScheduleId) throws Exception;

	/**
	 * @param interviewScheduleId
	 * @return
	 * @throws Exception
	 */
	InterviewScheduleDto deleteInterviewScheduleById(String interviewScheduleId) throws Exception;

	/**
	 * @param interviewScheduleDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	InterviewScheduleDto updateResult(InterviewScheduleDto interviewScheduleDto, String lang) throws Exception;

	/**
	 * @param round
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> getInterviewScheduledList(final int round) throws Exception;

	/**
	 * @param round
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> getForSchedule(int round) throws Exception;

	/**
	 * @param interviewScheduleDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	void updateShortlistedResult(InterviewScheduleDto interviewScheduleDto, String lang) throws Exception;

	/**
	 * @param jrNumber
	 * @param candidateId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> getViewHistory(String jrNumber, Long candidateId, String lang) throws Exception;

	/**
	 * @param userId
	 * @return
	 */
	int getUserDependencyCount(Long userId);

	/**
	 * @param interviewScheduleSearchDto
	 * @param round
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> searchInterviewScheduledList(InterviewScheduleSearchDto interviewScheduleSearchDto,
			int round) throws Exception;

	/**
	 * @param interviewScheduleSearchDto
	 * @param round
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> searchForSchedule(InterviewScheduleSearchDto interviewScheduleSearchDto, int round)
			throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> getAllForSchedule() throws Exception;

	/**
	 * @param interviewScheduleSearchDto
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> searchAllForSchedule(InterviewScheduleSearchDto interviewScheduleSearchDto)
			throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> getMyInterviews(Long userId) throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Long getMyInterviewsCount(Long userId) throws Exception;

	/**
	 * @param interviewScheduleSearchDto
	 * @param parseLong
	 * @return
	 * @throws Exception
	 */
	List<InterviewScheduleDto> searchMyInterviews(InterviewScheduleSearchDto interviewScheduleSearchDto, Long parseLong)
			throws Exception;

	/**
	 * 
	 * @param interviewScheduleDtoList
	 * 
	 * @param lang
	 * 
	 * @return
	 * 
	 * @throws Exception
	 * 
	 */

	byte[] downloadInterviewScheduleSummary(List<InterviewScheduleDto> interviewScheduleDtoList, String lang)
			throws Exception;

	/**
	 * 
	 * @param interviewScheduleDto
	 * 
	 * @return
	 * 
	 * @throws Exception
	 * 
	 */

	List<InterviewScheduleDto> searchInterviewScheduleForDownload(InterviewScheduleDto interviewScheduleDto)
			throws Exception;
}
