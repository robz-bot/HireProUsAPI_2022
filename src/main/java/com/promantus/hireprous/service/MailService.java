/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.io.IOException;

import javax.mail.MessagingException;

import com.promantus.hireprous.dto.CandidateDto;
import com.promantus.hireprous.dto.InterviewScheduleDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.OnboardDto;
import com.promantus.hireprous.dto.SuggestionDto;
import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.dto.VendorDto;
import com.promantus.hireprous.entity.Candidate;
import com.promantus.hireprous.entity.InterviewSchedule;

/**
 * @author Sihab.
 *
 */
public interface MailService {

	/**
	 * @throws MessagingException
	 * @throws IOException
	 */
	void sendEmailWithAttachment() throws MessagingException, IOException;

	/**
	 * @throws Exception
	 */
	void sendInviteMail() throws Exception;

	/**
	 * @param userDto
	 * @param toEmail
	 * @throws MessagingException
	 * @throws IOException
	 * @throws Exception
	 */
	void sendRegisteredEmail(UserDto userDto, String toEmail) throws MessagingException, IOException, Exception;

	/**
	 * @param userDto
	 * @param password
	 * @throws Exception
	 */
	void sendProfileUpdateEmail(UserDto userDto, final String password) throws Exception;

	/**
	 * @param toEmail
	 * @param fullName
	 * @param password
	 * @param vendorId
	 * @throws Exception
	 */
	void sendPasswordUpdateEmail(final String toEmail, final String fullName, final String password,
			final String vendorId) throws Exception;

	/**
	 * @param jobRequestDto
	 * @throws Exception
	 */
	void sendNewJobRequestEmail(JobRequestDto jobRequestDto) throws Exception;

	/**
	 * @param jobRequestDto
	 * @throws Exception
	 */
	void sendJobRequestUpdateEmail(JobRequestDto jobRequestDto) throws Exception;

	/**
	 * @param candidate
	 * @throws Exception
	 */
	void sendNewCandidateUploaded(Candidate candidate) throws Exception;

	/**
	 * @param candidate
	 * @throws Exception
	 */
	void sendCandidateUpdated(Candidate candidate) throws Exception;

	/**
	 * @param candidate
	 * @param remarks
	 * @throws Exception
	 */
	void sendResumeShortlistedEmail(Candidate candidate, String remarks) throws Exception;

	/**
	 * @param interviewScheduleDto
	 * @param interviewSchedule
	 * @throws Exception
	 */
	void sendInterviewScheduledEmail(InterviewScheduleDto interviewScheduleDto, InterviewSchedule interviewSchedule)
			throws Exception;

	/**
	 * @param interviewScheduleDto
	 * @param interviewSchedule
	 * @throws Exception
	 */
	void sendInterviewScheduleUpdatedEmail(InterviewScheduleDto interviewScheduleDto,
			InterviewSchedule interviewSchedule) throws Exception;

	/**
	 * @param interviewScheduleDto
	 * @throws Exception
	 */
	void sendInterviewResultEmail(InterviewScheduleDto interviewScheduleDto) throws Exception;

	/**
	 * @param candidateDto
	 * @throws Exception
	 */
	void sendForBUApprovalEmail(CandidateDto candidateDto) throws Exception;

	/**
	 * @param candidateDto
	 * @param status
	 * @throws Exception
	 */
	void sendBUResultEmail(CandidateDto candidateDto, String status) throws Exception;

	/**
	 * @param candidateDto
	 * @param onboardDto
	 * @throws Exception
	 */
	void sendOnBoardedEmail(CandidateDto candidateDto, OnboardDto onboardDto) throws Exception;

	/**
	 * @param candidateDto
	 * @param onboardDto
	 * @throws Exception
	 */
	void sendOnBoardUpdatedEmail(CandidateDto candidateDto, OnboardDto onboardDto) throws Exception;

	/**
	 * @param jobRequestDto
	 * @throws Exception
	 */
	void sendJobRequestStatusUpdatedEmail(JobRequestDto jobRequestDto) throws Exception;

	/**
	 * @param suggestionDto
	 * @throws Exception
	 */
	void sendNewSuggestionEmail(SuggestionDto suggestionDto) throws Exception;

	/**
	 * @param suggestionDto
	 * @throws Exception
	 */
	void sendSuggestionUpdatedEmail(SuggestionDto suggestionDto) throws Exception;

	/**
	 * @param toEmail
	 * @param fullName
	 * @param otp
	 * @throws Exception
	 */
	void sendOTPEmail(String toEmail, String fullName, final String otp) throws Exception;

	/**
	 * @param vendorDto
	 * @param password
	 * @throws Exception
	 */
	void sendVendorCreatedEmail(VendorDto vendorDto, String password) throws Exception;

	/**
	 * @param vendorDto
	 * @param password
	 * @throws Exception
	 */
	void sendVendorUpdatedEmail(VendorDto vendorDto, String password) throws Exception;

}
