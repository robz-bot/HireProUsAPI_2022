/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.CalendarRequest;
import com.promantus.hireprous.dto.CandidateDto;
import com.promantus.hireprous.dto.EmailConfDto;
import com.promantus.hireprous.dto.InterviewScheduleDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.OnboardDto;
import com.promantus.hireprous.dto.SuggestionDto;
import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.dto.VendorDto;
import com.promantus.hireprous.entity.Candidate;
import com.promantus.hireprous.entity.InterviewSchedule;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CandidateService;
import com.promantus.hireprous.service.CustomerService;
import com.promantus.hireprous.service.EmailConfigurationService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.service.MailService;
import com.promantus.hireprous.service.RecruitmentRoleService;
import com.promantus.hireprous.service.RoleService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Service class for commonly used features.
 * 
 * @author Sihab.
 *
 */
@Service
@Configuration
@PropertySource(value = { "classpath:email.properties" })
public class MailServiceImpl implements MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Value("${ui.server.url}")
	private String uiServerUrl;

	@Value("${spring.mail.username}")
	private String adminEmail;

	@Value("${bcc.email.ids}")
	private String bccEmailIds;

	private final String adminEmailName = "HireProUs Admin";

	@Autowired
	private Environment env;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	CandidateService candidateService;

	@Autowired
	CustomerService customerService;

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	RoleService roleService;

	@Autowired
	RecruitmentRoleService recRoleService;

	@Autowired
	EmailConfigurationService emailConfigurationService;

	@Override
	public void sendRegisteredEmail(final UserDto userDto, final String password) throws Exception {

		String content = env.getProperty("registration.content");

		content = content.replace("[FullName]", userDto.getFirstName() + " " + userDto.getLastName());
		content = content.replace("[UserName]", userDto.getEmail());
		content = content.replace("[Password]", password);
		content = content.replace("[ContactNumber]", userDto.getContactNumber());
		content = content.replace("[Role]", roleService.getRoleNameById(userDto.getRoleId()));
		content = content.replace("[Designation]", userDto.getDesignation());
		content = content.replace("[BU]", businessUnitService.getBusinessUnitNameById(userDto.getBusinessUnitId()));
		content = content.replace("[Location]", userDto.getLocation() != null ? userDto.getLocation() : "-");
		content = content.replace("[SkillSet]", userDto.getSkillSet() != null ? userDto.getSkillSet() : "-");
		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
		helper.setTo(new String[] { userDto.getEmail() });

		helper.setBcc(bccEmailIds.split(","));

		helper.setSubject(env.getProperty("registration.subject"));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendProfileUpdateEmail(final UserDto userDto, final String password) throws Exception {

		String content = env.getProperty("profile.update.content");

		content = content.replace("[FullName]", userDto.getFirstName() + " " + userDto.getLastName());
		content = content.replace("[UserName]", userDto.getEmail());
		content = content.replace("[Password]", password);
		content = content.replace("[ContactNumber]", userDto.getContactNumber());
		content = content.replace("[Role]", roleService.getRoleNameById(userDto.getRoleId()));
		content = content.replace("[Designation]", userDto.getDesignation());
		content = content.replace("[BU]", businessUnitService.getBusinessUnitNameById(userDto.getBusinessUnitId()));
		content = content.replace("[Location]", userDto.getLocation() != null ? userDto.getLocation() : "-");
		content = content.replace("[SkillSet]", userDto.getSkillSet() != null ? userDto.getSkillSet() : "-");
		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
		helper.setTo(new String[] { userDto.getEmail() });

		helper.setBcc(bccEmailIds.split(","));

		helper.setSubject(env.getProperty("profile.update.subject"));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendPasswordUpdateEmail(final String toEmail, final String fullName, final String password,
			final String vendorId) throws Exception {

		String content = env.getProperty("change.password.content");

		content = content.replace("[FullName]", fullName);
		content = content.replace("[UserName]", vendorId != null && !vendorId.isEmpty() ? vendorId : toEmail);
		content = content.replace("[Password]", password);
		content = content.replace("[url]",
				vendorId != null && !vendorId.isEmpty() ? uiServerUrl + "vendorLogin" : uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
		helper.setTo(new String[] { toEmail });

		helper.setBcc(bccEmailIds.split(","));

		helper.setText(content, true);
		helper.setSubject(env.getProperty("change.password.subject"));

		this.sendMail(message, helper);
	}

	@Override
	public void sendOTPEmail(final String toEmail, final String fullName, final String otp) throws Exception {

		String content = env.getProperty("reset.password.otp.content");

		content = content.replace("[FullName]", fullName);
		content = content.replace("[OTP]", otp);
		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
		helper.setTo(new String[] { toEmail });

		helper.setBcc(bccEmailIds.split(","));

		helper.setText(content, true);
		helper.setSubject(env.getProperty("reset.password.otp.subject"));

		this.sendMail(message, helper);
	}

	@Override
	public void sendNewJobRequestEmail(final JobRequestDto jobRequestDto) throws Exception {

		String content = env.getProperty("job.request.new.content");

		content = content.replace("[CreatedBy]", CacheUtil.getUsersMap().get(jobRequestDto.getCreatedBy()));
		content = content
				.replace("[CreatedOn]",
						HireProUsUtil.formatDateTime(HireProUsUtil
								.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(jobRequestDto.getCreatedDateTime())))
								+ " IST");

		content = content.replace("[JobRequestNumber]", jobRequestDto.getReferenceNumber());
		content = content.replace("[Customer]",
				customerService.getCustomerNameInDetailById(jobRequestDto.getCustomerId()));
		content = content.replace("[RecruitmentRole]",
				recRoleService.getRecruitmentRoleNameById(jobRequestDto.getRoleId()));
		content = content.replace("[BusinessUnit]",
				businessUnitService.getBusinessUnitNameById(jobRequestDto.getBuId()));
		content = content.replace("[NoOfOpenings]", jobRequestDto.getNoOfOpenings() + "");
		content = content.replace("[PlacementFor]", jobRequestDto.getPlacementFor());
		content = content.replace("[EmploymentType]", jobRequestDto.getEmploymentType());
		content = content.replace("[ContractDuration]",
				jobRequestDto.getContractDuration() != null ? jobRequestDto.getContractDuration() : "-");
		content = content.replace("[ProjectStartDate]", jobRequestDto.getProjectStartDate() + "");
		content = content.replace("[RemoteWorkingOption]",
				jobRequestDto.getRemoteOption() != null ? jobRequestDto.getRemoteOption() : "-");
		content = content.replace("[Recruiter]",
				jobRequestDto.getRecruiterId() != null ? CacheUtil.getUsersMap().get(jobRequestDto.getRecruiterId())
						: "-");
		content = content.replace("[Vendor]",
				jobRequestDto.getVendorId() != null ? CacheUtil.getVendorsMap().get(jobRequestDto.getVendorId()) : "-");

		content = content.replace("[JobDescription]", jobRequestDto.getJobDescription());
		content = content.replace("[MandatorySkills]", jobRequestDto.getMandatorySkills());
		content = content.replace("[OptionalSkills]",
				jobRequestDto.getOptionalSkills() != null ? jobRequestDto.getOptionalSkills() : "-");

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_JOB_REQUEST_CREATE, jobRequestDto.getBuId());

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		if (jobRequestDto.getRecruiterId() != null) {
			helper.addTo(CacheUtil.getUsersEmailMap().get(jobRequestDto.getRecruiterId()));
		}
		if (jobRequestDto.getVendorId() != null) {
			helper.addTo(CacheUtil.getVendorsEmailMap().get(jobRequestDto.getVendorId()));
			String[] ccArray = CacheUtil.getVendorsCCEmailMap().get(jobRequestDto.getVendorId()).split(",");
			for (String cc : ccArray) {
				helper.addCc(cc);
			}
		}

		this.addBcc(helper);

		helper.setSubject(
				env.getProperty("job.request.new.subject").replace("[jrNumber]", jobRequestDto.getReferenceNumber()));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendJobRequestUpdateEmail(final JobRequestDto jobRequestDto) throws Exception {

		String content = env.getProperty("job.request.update.content");

		content = content.replace("[UpdatedBy]", CacheUtil.getUsersMap().get(jobRequestDto.getUpdatedBy()));
		content = content
				.replace("[UpdatedOn]",
						HireProUsUtil.formatDateTime(HireProUsUtil
								.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(jobRequestDto.getUpdatedDateTime())))
								+ " IST");

		content = content.replace("[JobRequestNumber]", jobRequestDto.getReferenceNumber());
		content = content.replace("[Customer]",
				customerService.getCustomerNameInDetailById(jobRequestDto.getCustomerId()));
		content = content.replace("[RecruitmentRole]",
				recRoleService.getRecruitmentRoleNameById(jobRequestDto.getRoleId()));
		content = content.replace("[BusinessUnit]",
				businessUnitService.getBusinessUnitNameById(jobRequestDto.getBuId()));
		content = content.replace("[Status]", jobRequestDto.getJobReqStatus());
		content = content.replace("[NoOfOpenings]", jobRequestDto.getNoOfOpenings() + "");
		content = content.replace("[PlacementFor]", jobRequestDto.getPlacementFor());
		content = content.replace("[EmploymentType]", jobRequestDto.getEmploymentType());
		content = content.replace("[ContractDuration]",
				jobRequestDto.getContractDuration() != null ? jobRequestDto.getContractDuration() : "-");
		content = content.replace("[ProjectStartDate]", jobRequestDto.getProjectStartDate() + "");
		content = content.replace("[RemoteWorkingOption]",
				jobRequestDto.getRemoteOption() != null ? jobRequestDto.getRemoteOption() : "-");
		content = content.replace("[Recruiter]",
				jobRequestDto.getRecruiterId() != null ? CacheUtil.getUsersMap().get(jobRequestDto.getRecruiterId())
						: "-");
		content = content.replace("[Vendor]",
				jobRequestDto.getVendorId() != null ? CacheUtil.getVendorsMap().get(jobRequestDto.getVendorId()) : "-");

		content = content.replace("[JobDescription]", jobRequestDto.getJobDescription());
		content = content.replace("[MandatorySkills]", jobRequestDto.getMandatorySkills());
		content = content.replace("[OptionalSkills]",
				jobRequestDto.getOptionalSkills() != null ? jobRequestDto.getOptionalSkills() : "-");

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_JOB_REQUEST_CREATE, jobRequestDto.getBuId());

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		if (jobRequestDto.getRecruiterId() != null) {
			helper.addTo(CacheUtil.getUsersEmailMap().get(jobRequestDto.getRecruiterId()));
		}
		if (jobRequestDto.getVendorId() != null) {
			helper.addTo(CacheUtil.getVendorsEmailMap().get(jobRequestDto.getVendorId()));
			String[] ccArray = CacheUtil.getVendorsCCEmailMap().get(jobRequestDto.getVendorId()).split(",");
			for (String cc : ccArray) {
				helper.addCc(cc);
			}
		}

		this.addBcc(helper);

		helper.setSubject(env.getProperty("job.request.update.subject").replace("[jrNumber]",
				jobRequestDto.getReferenceNumber()));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendJobRequestStatusUpdatedEmail(final JobRequestDto jobRequestDto) throws Exception {

		String content = env.getProperty("job.request.status.content");

		content = content.replace("[Status1]", jobRequestDto.getJobReqStatus());
		content = content.replace("[UpdatedBy]", CacheUtil.getUsersMap().get(jobRequestDto.getUpdatedBy()));
		content = content
				.replace("[UpdatedOn]",
						HireProUsUtil.formatDateTime(HireProUsUtil
								.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(jobRequestDto.getUpdatedDateTime())))
								+ " IST");

		content = content.replace("[JobRequestNumber]", jobRequestDto.getReferenceNumber());
		content = content.replace("[Customer]",
				customerService.getCustomerNameInDetailById(jobRequestDto.getCustomerId()));
		content = content.replace("[RecruitmentRole]",
				recRoleService.getRecruitmentRoleNameById(jobRequestDto.getRoleId()));
		content = content.replace("[BusinessUnit]",
				businessUnitService.getBusinessUnitNameById(jobRequestDto.getBuId()));
		content = content.replace("[Status2]", jobRequestDto.getJobReqStatus());
		content = content.replace("[NoOfOpenings]",
				jobRequestDto.getClosedOpenings() + " / " + jobRequestDto.getNoOfOpenings());
		content = content.replace("[PlacementFor]", jobRequestDto.getPlacementFor());
		content = content.replace("[EmploymentType]", jobRequestDto.getEmploymentType());
		content = content.replace("[ContractDuration]",
				jobRequestDto.getContractDuration() != null ? jobRequestDto.getContractDuration() : "-");
		content = content.replace("[ProjectStartDate]", jobRequestDto.getProjectStartDate() + "");
		content = content.replace("[RemoteWorkingOption]",
				jobRequestDto.getRemoteOption() != null ? jobRequestDto.getRemoteOption() : "-");
		content = content.replace("[Recruiter]",
				jobRequestDto.getRecruiterId() != null ? CacheUtil.getUsersMap().get(jobRequestDto.getRecruiterId())
						: "-");
		content = content.replace("[Vendor]",
				jobRequestDto.getVendorId() != null ? CacheUtil.getVendorsMap().get(jobRequestDto.getVendorId()) : "-");

		content = content.replace("[JobDescription]", jobRequestDto.getJobDescription());
		content = content.replace("[MandatorySkills]", jobRequestDto.getMandatorySkills());
		content = content.replace("[OptionalSkills]",
				jobRequestDto.getOptionalSkills() != null ? jobRequestDto.getOptionalSkills() : "-");

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_JOB_REQUEST_UPDATE, jobRequestDto.getBuId());

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		if (jobRequestDto.getRecruiterId() != null) {
			helper.addTo(CacheUtil.getUsersEmailMap().get(jobRequestDto.getRecruiterId()));
		}
		if (jobRequestDto.getVendorId() != null) {
			helper.addTo(CacheUtil.getVendorsEmailMap().get(jobRequestDto.getVendorId()));
			String[] ccArray = CacheUtil.getVendorsCCEmailMap().get(jobRequestDto.getVendorId()).split(",");
			for (String cc : ccArray) {
				helper.addCc(cc);
			}
		}

		this.addBcc(helper);

		String subject = env.getProperty("job.request.status.subject");
		subject = subject.replace("[jrNumber]", jobRequestDto.getReferenceNumber());
		subject = subject.replace("[Status3]", jobRequestDto.getJobReqStatus());
		helper.setSubject(subject);

		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendNewCandidateUploaded(final Candidate candidate) throws Exception {

		String content = env.getProperty("candidate.added.content");

		content = content.replace("[CreatedBy]", CacheUtil.getUsersMap().get(candidate.getCreatedBy()));
		content = content.replace("[CreatedOn]",
				HireProUsUtil.formatDateTime(
						HireProUsUtil.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(candidate.getCreatedDateTime())))
						+ " IST");

		content = content.replace("[CandidateName]", candidate.getFirstName() + " " + candidate.getLastName());
		content = content.replace("[ContactNumber]", candidate.getContactNumber());
		content = content.replace("[Email]", candidate.getEmail());
		content = content.replace("[Gender]", candidate.getSex());
		content = content.replace("[CandidateType]", candidate.getCandidateType());
		content = content.replace("[YearsOfExperience]", candidate.getExperience());
		content = content.replace("[CurrentCompanyName]", candidate.getCurrentCompany());
		content = content.replace("[SkillSet]", candidate.getSkillSet());

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_CANDIDATE_UPLOAD,
				jobRequestService.getBuIdByJRNumber(candidate.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(candidate.getJrNumber());
		if (recruiterEmail != null) {
			helper.addTo(recruiterEmail);
		}

		this.addBcc(helper);

		helper.setSubject(env.getProperty("candidate.added.subject").replace("[jrNumber]", candidate.getJrNumber()));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendCandidateUpdated(final Candidate candidate) throws Exception {

		String content = env.getProperty("candidate.updated.content");

		content = content.replace("[UpdatedBy]", CacheUtil.getUsersMap().get(candidate.getUpdatedBy()));
		content = content.replace("[UpdatedOn]",
				HireProUsUtil.formatDateTime(
						HireProUsUtil.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(candidate.getUpdatedDateTime())))
						+ " IST");

		content = content.replace("[CandidateName]", candidate.getFirstName() + " " + candidate.getLastName());
		content = content.replace("[ContactNumber]", candidate.getContactNumber());
		content = content.replace("[Email]", candidate.getEmail());
		content = content.replace("[Gender]", candidate.getSex());
		content = content.replace("[CandidateType]", candidate.getCandidateType());
		content = content.replace("[YearsOfExperience]", candidate.getExperience());
		content = content.replace("[CurrentCompanyName]", candidate.getCurrentCompany());
		content = content.replace("[SkillSet]", candidate.getSkillSet());

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_CANDIDATE_UPLOAD,
				jobRequestService.getBuIdByJRNumber(candidate.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(candidate.getJrNumber());
		if (recruiterEmail != null) {
			helper.addTo(recruiterEmail);
		}

		this.addBcc(helper);

		helper.setSubject(env.getProperty("candidate.updated.subject").replace("[jrNumber]", candidate.getJrNumber()));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendResumeShortlistedEmail(final Candidate candidate, final String remarks) throws Exception {

		String content = env.getProperty("resume.shortlisted.content");

		content = content.replace("[UpdatedBy]", CacheUtil.getUsersMap().get(candidate.getUpdatedBy()));
		content = content.replace("[UpdatedOn]",
				HireProUsUtil.formatDateTime(
						HireProUsUtil.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(candidate.getUpdatedDateTime())))
						+ " IST");

		content = content.replace("[JobRequestNumber]", candidate.getJrNumber());
		content = content.replace("[CandidateName]", candidate.getFirstName() + " " + candidate.getLastName());
		content = content.replace("[ContactNumber]", candidate.getContactNumber());
		content = content.replace("[Email]", candidate.getEmail());

		String status = "Shortlisted";
		if (HireProUsConstants.REC_STATUS_HOLDED_0.equals(candidate.getRecStatus())) {
			status = "Hold";
		} else if (HireProUsConstants.REC_STATUS_REJECTED_0.equals(candidate.getRecStatus())) {
			status = "Rejected";
		}
		content = content.replace("[Status]", status);
		content = content.replace("[Remarks]", remarks);

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_CANDIDATE_UPLOAD,
				jobRequestService.getBuIdByJRNumber(candidate.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(candidate.getJrNumber());
		if (recruiterEmail != null) {
			helper.addTo(recruiterEmail);
		}

		this.addBcc(helper);

		helper.setSubject(env.getProperty("resume.shortlisted.subject").replace("[jrNumber]", candidate.getJrNumber()));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	/**
	 * Interview Scheduled.
	 */
	@Override
	public void sendInterviewScheduledEmail(final InterviewScheduleDto interviewScheduleDto,
			final InterviewSchedule interviewSchedule) throws Exception {

		String content = env.getProperty("interview.scheduled.content");

		content = content.replace("[ScheduledBy]", CacheUtil.getUsersMap().get(interviewScheduleDto.getCreatedBy()));

		String interviewRound = "";
		String purpose = HireProUsConstants.MAIL_PURPOSE_INTERNAL_ROUND1_SCHEDULED;
		if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL1 == interviewScheduleDto.getRound()) {
			interviewRound = "Technical Round 1";
		} else if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL2 == interviewScheduleDto.getRound()) {
			interviewRound = "Technical Round 2";
			purpose = HireProUsConstants.MAIL_PURPOSE_INTERNAL_ROUND2_SCHEDULED;
		} else if (HireProUsConstants.INTERVIEW_ROUND_CUSTOMER == interviewScheduleDto.getRound()) {
			interviewRound = "Customer Round";
			purpose = HireProUsConstants.MAIL_PURPOSE_CUSTOMER_ROUND_SCHEDULED;
		} else if (HireProUsConstants.INTERVIEW_ROUND_HR == interviewScheduleDto.getRound()) {
			interviewRound = "HR Round";
			purpose = HireProUsConstants.MAIL_PURPOSE_HR_ROUND_SCHEDULED;
		}

		String candidateName = candidateService.getCandidateNameById(interviewScheduleDto.getCandidateId());
		content = content.replace("[FullName]", candidateName);
		content = content.replace("[InterviewRound]", interviewRound);
		content = content.replace("[jrNumber]", interviewScheduleDto.getJrNumber());
		content = content.replace("[InterviewerName]",
				CacheUtil.getUsersMap().get(interviewScheduleDto.getInterviewerId()));
		content = content.replace("[DateandTime]", HireProUsUtil.formatDateTime(interviewSchedule.getScheduleDateTime())
				+ ",   Time Zone : " + interviewSchedule.getTimeZone());
		content = content.replace("[Duration]", interviewScheduleDto.getDuration());
		content = content.replace("[Mode]", interviewScheduleDto.getMode());
		content = content.replace("[Venue]",
				interviewScheduleDto.getVenue() != null ? interviewScheduleDto.getVenue() : "-");

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(purpose,
				jobRequestService.getBuIdByJRNumber(interviewScheduleDto.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String toEmails = "";
		String candidateEmail = candidateService.getCandidateEmailById(interviewScheduleDto.getCandidateId());
		if (candidateEmail != null && !candidateEmail.isEmpty()) {
			helper.addTo(candidateEmail);
			toEmails = candidateEmail;
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(interviewScheduleDto.getJrNumber());
		if (recruiterEmail != null && !recruiterEmail.isEmpty()) {
			helper.addTo(recruiterEmail);
			if (toEmails.length() > 0) {
				toEmails = toEmails + "," + recruiterEmail;
			} else {
				toEmails = recruiterEmail;
			}
		}

		String interviewerEmail = CacheUtil.getUsersEmailMap().get(interviewScheduleDto.getInterviewerId());
		if (interviewerEmail != null && !interviewerEmail.isEmpty()) {
			helper.addTo(interviewerEmail);
			if (toEmails.length() > 0) {
				toEmails = toEmails + "," + interviewerEmail;
			} else {
				toEmails = interviewerEmail;
			}
		}

		if (toEmails != null && toEmails.trim().isEmpty()) {
			toEmails = adminEmail;
		}

		this.addBcc(helper);

		String subject = env.getProperty("interview.scheduled.subject");
		subject = subject.replace("[InterviewRound]", interviewRound);
		subject = subject.replace("[FullName]", candidateName);
		helper.setSubject(subject);

		helper.setText(content, true);

		this.sendMail(message, helper);

		String hourMinute = HireProUsUtil.getHourMinute(interviewScheduleDto.getDuration());
		// Send Calendar Invite.
		this.sendCalendarInvite(adminEmail,
				new CalendarRequest.Builder().withSubject("Invite : " + subject)
						.withBody("Interview Schedule Invite!!!").withToEmail(toEmails)
						.withMeetingStartTime(interviewSchedule.getScheduleDateTime())
						.withMeetingEndTime(interviewSchedule.getScheduleDateTime()
								.plusHours(Long.parseLong(hourMinute.split(",")[0]))
								.plusMinutes(Long.parseLong(hourMinute.split(",")[1])))
						.build());
	}

	/**
	 * Interview Schedule Updated.
	 */
	@Override
	public void sendInterviewScheduleUpdatedEmail(final InterviewScheduleDto interviewScheduleDto,
			final InterviewSchedule interviewSchedule) throws Exception {

		String content = env.getProperty("interview.rescheduled.content");

		content = content.replace("[ScheduledBy]", CacheUtil.getUsersMap().get(interviewScheduleDto.getUpdatedBy()));

		String interviewRound = "";
		String purpose = HireProUsConstants.MAIL_PURPOSE_INTERNAL_ROUND1_SCHEDULED;
		if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL1 == interviewScheduleDto.getRound()) {
			interviewRound = "Technical Round 1";
		} else if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL2 == interviewScheduleDto.getRound()) {
			interviewRound = "Technical Round 2";
			purpose = HireProUsConstants.MAIL_PURPOSE_INTERNAL_ROUND2_SCHEDULED;
		} else if (HireProUsConstants.INTERVIEW_ROUND_CUSTOMER == interviewScheduleDto.getRound()) {
			interviewRound = "Customer Round";
			purpose = HireProUsConstants.MAIL_PURPOSE_CUSTOMER_ROUND_SCHEDULED;
		} else if (HireProUsConstants.INTERVIEW_ROUND_HR == interviewScheduleDto.getRound()) {
			interviewRound = "HR Round";
			purpose = HireProUsConstants.MAIL_PURPOSE_HR_ROUND_SCHEDULED;
		}

		String candidateName = candidateService.getCandidateNameById(interviewScheduleDto.getCandidateId());
		content = content.replace("[FullName]", candidateName);
		content = content.replace("[InterviewRound]", interviewRound);
		content = content.replace("[jrNumber]", interviewScheduleDto.getJrNumber());
		content = content.replace("[InterviewerName]",
				CacheUtil.getUsersMap().get(interviewScheduleDto.getInterviewerId()));
		content = content.replace("[DateandTime]", HireProUsUtil.formatDateTime(interviewSchedule.getScheduleDateTime())
				+ ",   Time Zone : " + interviewSchedule.getTimeZone());
		content = content.replace("[Duration]", interviewScheduleDto.getDuration());
		content = content.replace("[Mode]", interviewScheduleDto.getMode());
		content = content.replace("[Venue]",
				interviewScheduleDto.getVenue() != null ? interviewScheduleDto.getVenue() : "-");

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(purpose,
				jobRequestService.getBuIdByJRNumber(interviewScheduleDto.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String toEmails = "";
		String candidateEmail = candidateService.getCandidateEmailById(interviewScheduleDto.getCandidateId());
		if (candidateEmail != null && !candidateEmail.isEmpty()) {
			helper.addTo(candidateEmail);
			toEmails = candidateEmail;
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(interviewScheduleDto.getJrNumber());
		if (recruiterEmail != null && !recruiterEmail.isEmpty()) {
			helper.addTo(recruiterEmail);
			if (toEmails.length() > 0) {
				toEmails = toEmails + "," + recruiterEmail;
			} else {
				toEmails = recruiterEmail;
			}
		}

		String interviewerEmail = CacheUtil.getUsersEmailMap().get(interviewScheduleDto.getInterviewerId());
		if (interviewerEmail != null && !interviewerEmail.isEmpty()) {
			helper.addTo(interviewerEmail);
			if (toEmails.length() > 0) {
				toEmails = toEmails + "," + interviewerEmail;
			} else {
				toEmails = interviewerEmail;
			}
		}

		if (toEmails != null && toEmails.trim().isEmpty()) {
			toEmails = adminEmail;
		}

		this.addBcc(helper);

		String subject = env.getProperty("interview.rescheduled.subject");
		subject = subject.replace("[InterviewRound]", interviewRound);
		subject = subject.replace("[FullName]", candidateName);
		helper.setSubject(subject);

		helper.setText(content, true);

		this.sendMail(message, helper);

		String hourMinute = HireProUsUtil.getHourMinute(interviewScheduleDto.getDuration());
		// Send Calendar Invite.
		this.sendCalendarInvite(adminEmail,
				new CalendarRequest.Builder().withSubject("Invite : " + subject)
						.withBody("Interview Re-Schedule Invite!!!").withToEmail(toEmails)
						.withMeetingStartTime(interviewSchedule.getScheduleDateTime())
						.withMeetingEndTime(interviewSchedule.getScheduleDateTime()
								.plusHours(Long.parseLong(hourMinute.split(",")[0]))
								.plusMinutes(Long.parseLong(hourMinute.split(",")[1])))
						.build());
	}

	/**
	 * Interview Result.
	 */
	@Override
	public void sendInterviewResultEmail(final InterviewScheduleDto interviewScheduleDto) throws Exception {

		String content = env.getProperty("interview.results.content");

		content = content.replace("[UpdatedBy]", CacheUtil.getUsersMap().get(interviewScheduleDto.getInterviewerId()));
		content = content.replace("[UpdatedOn]",
				HireProUsUtil.formatDateTime(HireProUsUtil
						.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(interviewScheduleDto.getUpdatedDateTime())))
						+ " IST");

		String interviewRound = "";
		String purpose = HireProUsConstants.MAIL_PURPOSE_INTERNAL_ROUND1_RESULT;
		if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL1 == interviewScheduleDto.getRound()) {
			interviewRound = "Technical Round 1";
		} else if (HireProUsConstants.INTERVIEW_ROUND_INTERNAL2 == interviewScheduleDto.getRound()) {
			interviewRound = "Technical Round 2";
			purpose = HireProUsConstants.MAIL_PURPOSE_INTERNAL_ROUND2_RESULT;
		} else if (HireProUsConstants.INTERVIEW_ROUND_CUSTOMER == interviewScheduleDto.getRound()) {
			interviewRound = "Customer Round";
			purpose = HireProUsConstants.MAIL_PURPOSE_CUSTOMER_ROUND_RESULT;
		} else if (HireProUsConstants.INTERVIEW_ROUND_HR == interviewScheduleDto.getRound()) {
			interviewRound = "HR Round";
			purpose = HireProUsConstants.MAIL_PURPOSE_HR_ROUND_RESULT;
		}

		String candidateName = candidateService.getCandidateNameById(interviewScheduleDto.getCandidateId());
		content = content.replace("[FullName]", candidateName);
		content = content.replace("[InterviewRound]", interviewRound);
		content = content.replace("[jrNumber]", interviewScheduleDto.getJrNumber());
		content = content.replace("[Mode]", interviewScheduleDto.getMode());
		content = content.replace("[Venue]",
				interviewScheduleDto.getVenue() != null ? interviewScheduleDto.getVenue() : "-");

		content = content.replace("[Result]", this.getResult(interviewScheduleDto.getRecStatus()));
		content = content.replace("[Remarks]", interviewScheduleDto.getResultRemarks());

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(purpose,
				jobRequestService.getBuIdByJRNumber(interviewScheduleDto.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(interviewScheduleDto.getJrNumber());
		if (recruiterEmail != null) {
			helper.addTo(recruiterEmail);
		}

		this.addBcc(helper);

		String subject = env.getProperty("interview.results.subject");
		subject = subject.replace("[InterviewRound]", interviewRound);
		subject = subject.replace("[FullName]", candidateName);
		helper.setSubject(subject);

		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	/**
	 * @param recStatus
	 * @return
	 */
	private String getResult(String recStatus) {

		String result = "-";
		if (HireProUsConstants.REC_STATUS_PASSED_R1.equals(recStatus)
				|| HireProUsConstants.REC_STATUS_PASSED_R2.equals(recStatus)
				|| HireProUsConstants.REC_STATUS_PASSED_CR3.equals(recStatus)
				|| HireProUsConstants.REC_STATUS_PASSED_HR4.equals(recStatus)) {
			result = "Selected";
		} else if (HireProUsConstants.REC_STATUS_HOLDED_R1.equals(recStatus)
				|| HireProUsConstants.REC_STATUS_HOLDED_R2.equals(recStatus)
				|| HireProUsConstants.REC_STATUS_HOLDED_CR3.equals(recStatus)
				|| HireProUsConstants.REC_STATUS_HOLDED_HR4.equals(recStatus)) {
			result = "Hold";
		} else if (HireProUsConstants.REC_STATUS_REJECTED_R1.equals(recStatus)
				|| HireProUsConstants.REC_STATUS_REJECTED_R2.equals(recStatus)
				|| HireProUsConstants.REC_STATUS_REJECTED_CR3.equals(recStatus)
				|| HireProUsConstants.REC_STATUS_REJECTED_HR4.equals(recStatus)) {
			result = "Rejected";
		}

		return result;
	}

	@Override
	public void sendForBUApprovalEmail(final CandidateDto candidateDto) throws Exception {

		String content = env.getProperty("bu.approval.content");

		content = content.replace("[jrNumber]", candidateDto.getJrNumber());
		content = content.replace("[CandidateName]", candidateDto.getFirstName() + " " + candidateDto.getLastName());
		content = content.replace("[ContactNumber]", candidateDto.getContactNumber());
		content = content.replace("[Email]", candidateDto.getEmail());
		content = content.replace("[Gender]", candidateDto.getSex());
		content = content.replace("[CandidateType]", candidateDto.getCandidateType());
		content = content.replace("[YearsOfExperience]", candidateDto.getExperience());

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_FOR_BU_APPROVAL,
				jobRequestService.getBuIdByJRNumber(candidateDto.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(candidateDto.getJrNumber());
		if (recruiterEmail != null) {
			helper.addTo(recruiterEmail);
		}

		this.addBcc(helper);

		helper.setSubject(env.getProperty("bu.approval.subject").replace("[CandidateName]",
				candidateDto.getFirstName() + " " + candidateDto.getLastName()));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendBUResultEmail(final CandidateDto candidateDto, final String status) throws Exception {

		String content = env.getProperty("bu.status.content");

		content = content.replace("[Status1]", status);
		content = content.replace("[jrNumber]", candidateDto.getJrNumber());
		content = content.replace("[CandidateName]", candidateDto.getFirstName() + " " + candidateDto.getLastName());
		content = content.replace("[Status2]", status);
		content = content.replace("[ContactNumber]", candidateDto.getContactNumber());
		content = content.replace("[Email]", candidateDto.getEmail());
		content = content.replace("[Gender]", candidateDto.getSex());
		content = content.replace("[CandidateType]", candidateDto.getCandidateType());
		content = content.replace("[YearsOfExperience]", candidateDto.getExperience());

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_SELECTED,
				jobRequestService.getBuIdByJRNumber(candidateDto.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(candidateDto.getJrNumber());
		if (recruiterEmail != null) {
			helper.addTo(recruiterEmail);
		}

		this.addBcc(helper);

		String subject = env.getProperty("bu.status.subject");
		subject = subject.replace("[Status]", status);
		subject = subject.replace("[jrNumber]", candidateDto.getJrNumber());
		helper.setSubject(subject);

		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendOnBoardedEmail(final CandidateDto candidateDto, final OnboardDto onboardDto) throws Exception {

		String content = env.getProperty("onboard.candidate.content");

		content = content.replace("[jrNumber]", candidateDto.getJrNumber());
		content = content.replace("[CandidateName]", candidateDto.getFirstName() + " " + candidateDto.getLastName());

		content = content.replace("[EmployeeId]",
				onboardDto.getEmployeeId() != null ? onboardDto.getEmployeeId() : "-");
		content = content.replace("[WorkOrderNumber]",
				onboardDto.getWorkOrderNumber() != null ? onboardDto.getWorkOrderNumber() : "-");
		content = content.replace("[JoiningDate]", onboardDto.getJoiningDate() + "");
		content = content.replace("[DocsVerified]", onboardDto.getDocsVerified() == 0 ? "No" : "Yes");
		content = content.replace("[Joined]", onboardDto.getJoined() == 0 ? "No" : "Yes");

		content = content.replace("[ContactNumber]", candidateDto.getContactNumber());
		content = content.replace("[Email]", candidateDto.getEmail());
		content = content.replace("[Gender]", candidateDto.getSex());
		content = content.replace("[CandidateType]", candidateDto.getCandidateType());
		content = content.replace("[YearsOfExperience]", candidateDto.getExperience());

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_ON_BOARDED,
				jobRequestService.getBuIdByJRNumber(candidateDto.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(candidateDto.getJrNumber());
		if (recruiterEmail != null) {
			helper.addTo(recruiterEmail);
		}

		this.addBcc(helper);

		String subject = env.getProperty("onboard.candidate.subject");
		subject = subject.replace("[CandidateName]", candidateDto.getFirstName() + " " + candidateDto.getLastName());
		subject = subject.replace("[jrNumber]", candidateDto.getJrNumber());
		helper.setSubject(subject);

		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendOnBoardUpdatedEmail(final CandidateDto candidateDto, final OnboardDto onboardDto) throws Exception {

		String content = env.getProperty("onboard.candidate.content");

		content = content.replace("[jrNumber]", candidateDto.getJrNumber());
		content = content.replace("[CandidateName]", candidateDto.getFirstName() + " " + candidateDto.getLastName());

		content = content.replace("[EmployeeId]",
				onboardDto.getEmployeeId() != null ? onboardDto.getEmployeeId() : "-");
		content = content.replace("[WorkOrderNumber]",
				onboardDto.getWorkOrderNumber() != null ? onboardDto.getWorkOrderNumber() : "-");
		content = content.replace("[JoiningDate]", onboardDto.getJoiningDate() + "");
		content = content.replace("[DocsVerified]", onboardDto.getDocsVerified() == 0 ? "No" : "Yes");
		content = content.replace("[Joined]", onboardDto.getJoined() == 0 ? "No" : "Yes");

		content = content.replace("[ContactNumber]", candidateDto.getContactNumber());
		content = content.replace("[Email]", candidateDto.getEmail());
		content = content.replace("[Gender]", candidateDto.getSex());
		content = content.replace("[CandidateType]", candidateDto.getCandidateType());
		content = content.replace("[YearsOfExperience]", candidateDto.getExperience());

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		EmailConfDto emailConfDto = emailConfigurationService.getEmailConfByPurposeAndBuId(
				HireProUsConstants.MAIL_PURPOSE_ON_BOARDED,
				jobRequestService.getBuIdByJRNumber(candidateDto.getJrNumber()));

		if (emailConfDto != null) {
			if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
				helper.setTo(emailConfDto.getTo().split(","));
			}
			if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
				helper.setCc(emailConfDto.getCc().split(","));
			}
			if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
				helper.setBcc(emailConfDto.getBcc().split(","));
			}
		}

		String recruiterEmail = jobRequestService.getRecruiterEmailByJRNumber(candidateDto.getJrNumber());
		if (recruiterEmail != null) {
			helper.addTo(recruiterEmail);
		}

		this.addBcc(helper);

		String subject = env.getProperty("onboard.update.subject");
		subject = subject.replace("[CandidateName]", candidateDto.getFirstName() + " " + candidateDto.getLastName());
		subject = subject.replace("[jrNumber]", candidateDto.getJrNumber());
		helper.setSubject(subject);

		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendNewSuggestionEmail(final SuggestionDto suggestionDto) throws Exception {

		String content = env.getProperty("suggestion.new.content");

		content = content.replace("[CreatedBy]", CacheUtil.getUsersMap().get(suggestionDto.getCreatedBy()));
		content = content
				.replace("[CreatedOn]",
						HireProUsUtil.formatDateTime(HireProUsUtil
								.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(suggestionDto.getCreatedDateTime())))
								+ " IST");

		content = content.replace("[Suggestion/Issue]", suggestionDto.getType());
		content = content.replace("[Code]", suggestionDto.getCode());
		content = content.replace("[SuggestionType]", suggestionDto.getType());
		content = content.replace("[Details]", suggestionDto.getSuggestion());

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		helper.setTo(adminEmail);
		helper.setCc(CacheUtil.getUsersEmailMap().get(suggestionDto.getCreatedBy()));

		this.addBcc(helper);

		String subject = env.getProperty("suggestion.new.subject");
		subject = subject.replace("[Suggestion/Issue]", suggestionDto.getType());
		subject = subject.replace("[Code]", suggestionDto.getCode());
		helper.setSubject(subject);

		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendSuggestionUpdatedEmail(final SuggestionDto suggestionDto) throws Exception {

		String content = env.getProperty("suggestion.update.content");

		content = content.replace("[FullName]", CacheUtil.getUsersMap().get(suggestionDto.getCreatedBy()));

		content = content.replace("[UpdatedBy]", CacheUtil.getUsersMap().get(suggestionDto.getUpdatedBy()));
		content = content
				.replace("[UpdatedOn]",
						HireProUsUtil.formatDateTime(HireProUsUtil
								.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(suggestionDto.getUpdatedDateTime())))
								+ " IST");

		content = content.replace("[Suggestion/Issue]", suggestionDto.getType());
		content = content.replace("[Code]", suggestionDto.getCode());
		content = content.replace("[SuggestionType]", suggestionDto.getType());
		content = content.replace("[Details]", suggestionDto.getSuggestion());
		content = content
				.replace("[AddedOn]",
						HireProUsUtil.formatDateTime(HireProUsUtil
								.getISTLocalDateTime(HireProUsUtil.getGMTDateTime(suggestionDto.getCreatedDateTime())))
								+ " IST");
		content = content.replace("[AdminComment]", suggestionDto.getReply());
		content = content.replace("[Status]", suggestionDto.getSugStatus());

		content = content.replace("[url]", uiServerUrl);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

		helper.setTo(CacheUtil.getUsersEmailMap().get(suggestionDto.getCreatedBy()));
//		helper.setCc(adminEmail);

		this.addBcc(helper);

		String subject = env.getProperty("suggestion.update.subject");
		subject = subject.replace("[Suggestion/Issue]", suggestionDto.getType());
		subject = subject.replace("[Code]", suggestionDto.getCode());
		helper.setSubject(subject);

		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	/**
	 * @param helper
	 * @throws MessagingException
	 */
	private void addBcc(final MimeMessageHelper helper) throws MessagingException {

		String[] bccEmailIdsArray = bccEmailIds.split(",");

		if (bccEmailIdsArray == null || bccEmailIdsArray.length <= 0) {
			return;
		}

		for (String bccEmailId : bccEmailIdsArray) {
			helper.addBcc(bccEmailId);
		}
	}

	@Override
	public void sendVendorCreatedEmail(final VendorDto vendorDto, final String password) throws Exception {

		String content = env.getProperty("vendor.create.content");

		content = content.replace("[VendorName]", vendorDto.getVendorName());
		content = content.replace("[UserName]", vendorDto.getVendorId());
		content = content.replace("[Password]", password);
		content = content.replace("[ContactNumber]", vendorDto.getContactNumber());
		content = content.replace("[Email]", vendorDto.getEmail());
		content = content.replace("[Location]", vendorDto.getLocation() != null ? vendorDto.getLocation() : "-");
		content = content.replace("[Address]", vendorDto.getAddress() != null ? vendorDto.getAddress() : "-");
		content = content.replace("[CcEmailIds]", vendorDto.getCcEmailIds());
		content = content.replace("[url]", uiServerUrl + "vendorLogin");

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
		helper.setTo(new String[] { vendorDto.getEmail() });

		helper.setBcc(bccEmailIds.split(","));
		helper.setCc(vendorDto.getCcEmailIds().split(","));

		helper.setSubject(env.getProperty("vendor.create.subject"));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	@Override
	public void sendVendorUpdatedEmail(final VendorDto vendorDto, final String password) throws Exception {

		String content = env.getProperty("vendor.update.content");

		content = content.replace("[VendorName]", vendorDto.getVendorName());
		content = content.replace("[UserName]", vendorDto.getVendorId());
		content = content.replace("[Password]", password);
		content = content.replace("[ContactNumber]", vendorDto.getContactNumber());
		content = content.replace("[Email]", vendorDto.getEmail());
		content = content.replace("[Location]", vendorDto.getLocation() != null ? vendorDto.getLocation() : "-");
		content = content.replace("[Address]", vendorDto.getAddress() != null ? vendorDto.getAddress() : "-");
		content = content.replace("[CcEmailIds]", vendorDto.getCcEmailIds());
		content = content.replace("[url]", uiServerUrl + "vendorLogin");

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
		helper.setTo(new String[] { vendorDto.getEmail() });

		helper.setBcc(bccEmailIds.split(","));
		helper.setCc(vendorDto.getCcEmailIds().split(","));

		helper.setSubject(env.getProperty("vendor.update.subject"));
		helper.setText(content, true);

		this.sendMail(message, helper);
	}

	/**
	 * @param message
	 * @param helper
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private void sendMail(MimeMessage message, MimeMessageHelper helper)
			throws MessagingException, UnsupportedEncodingException {

		helper.setFrom(adminEmail, adminEmailName);
		mailSender.send(message);
	}

	@Override
	public void sendInviteMail() throws Exception {

//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//		mailSender.setUsername("hirepro.v1@gmail.com");
//		mailSender.setPassword("hirepro@123");
//		Properties properties = new Properties();
//		properties.put("mail.smtp.auth", "true");
//		properties.put("mail.smtp.starttls.enable", "true");
//		properties.put("mail.smtp.host", "smtp-mail.outlook.com");
//		properties.put("mail.smtp.port", "587");
//		mailSender.setJavaMailProperties(properties);

		this.sendCalendarInvite(adminEmail, new CalendarRequest.Builder().withSubject("HireProUs Test Meeting Invite.")
				.withBody("This is a test event.")
//	                    .withToEmail("sihabudeen@promantus.com,robinrajesh@promantus.com")
				.withToEmail("sihabudeen@promantus.com").withMeetingStartTime(LocalDateTime.now().plusMinutes(30))
				.withMeetingEndTime(LocalDateTime.now().plusHours(1)).build());
	}

	/**
	 * @param fromEmail
	 * @param calendarRequest
	 * @throws Exception
	 */
	public void sendCalendarInvite(String fromEmail, CalendarRequest calendarRequest) throws Exception {

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.addHeaderLine("method=REQUEST");
		mimeMessage.addHeaderLine("charset=UTF-8");
		mimeMessage.addHeaderLine("component=VEVENT");
		mimeMessage.setFrom(new InternetAddress(fromEmail));
		mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(calendarRequest.getToEmail()));
		mimeMessage.setSubject(calendarRequest.getSubject());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
		StringBuilder builder = new StringBuilder();
//		builder.append("BEGIN:VCALENDAR\n");
//		builder.append("METHOD:REQUEST\n");
//		builder.append("PRODID:Microsoft Exchange Server 2010\n");
//		builder.append("VERSION:2.0\n");
//		builder.append("BEGIN:VTIMEZONE\n");
//		builder.append("TZID:Asia/Kolkata\n");
//		builder.append("END:VTIMEZONE\n");
//		builder.append("BEGIN:VEVENT\n");
//		builder.append("ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:");
//		builder.append(calendarRequest.getToEmail());
//		builder.append("\n");
//		builder.append("ORGANIZER;CN=");
//		builder.append("HireProUs Admin");
//		builder.append(":MAILTO:");
//		builder.append(calendarRequest.getToEmail());
//		builder.append("\n");
//		builder.append("DESCRIPTION;LANGUAGE=en-US:");
//		builder.append(calendarRequest.getBody());
//		builder.append("\n");
//		builder.append("UID:");
//		builder.append(calendarRequest.getUid());
//		builder.append("\n");
//		builder.append("SUMMARY;LANGUAGE=en-US:");
//		builder.append(calendarRequest.getSubject());
//		builder.append("\n");
//		builder.append("DTSTART:");
//		builder.append(formatter.format(calendarRequest.getMeetingStartTime()).replace(" ", "T"));
//		builder.append("\n");
//		builder.append("DTEND:");
//		builder.append(formatter.format(calendarRequest.getMeetingEndTime()).replace(" ", "T"));
//		builder.append("\n");
//		builder.append("CLASS:PUBLIC\n");
//		builder.append("PRIORITY:5\n");
//		builder.append("DTSTAMP:20200922T105302Z\n");
//		builder.append("TRANSP:OPAQUE\n");
//		builder.append("STATUS:CONFIRMED\n");
//		builder.append("SEQUENCE:$sequenceNumber\n");
//		builder.append("LOCATION;LANGUAGE=en-US:Microsoft Teams Meeting;Teams Meeting\n");
//		builder.append("BEGIN:VALARM\n");
//		builder.append("DESCRIPTION:REMINDER\n");
//		builder.append("TRIGGER;RELATED=START:-PT15M\n");
//		builder.append("ACTION:DISPLAY\n");
//		builder.append("END:VALARM\n");
//		builder.append("END:VEVENT\n");
//		builder.append("END:VCALENDAR");

		builder.append("BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n" + "PRODID:Microsoft Exchange Server 2010\n"
				+ "VERSION:2.0\n" + "BEGIN:VTIMEZONE\n" + "TZID:Asia/Kolkata\n" + "END:VTIMEZONE\n" + "BEGIN:VEVENT\n"
				+ "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + calendarRequest.getToEmail() + "\n"
				+ "ORGANIZER;CN=Foo:MAILTO:" + fromEmail + "\n" + "DESCRIPTION;LANGUAGE=en-US:"
				+ calendarRequest.getBody() + "\n" + "UID:" + calendarRequest.getUid() + "\n"
				+ "SUMMARY;LANGUAGE=en-US:" + calendarRequest.getSubject() + "\n" + "DTSTART:"
				+ formatter.format(calendarRequest.getMeetingStartTime()).replace(" ", "T") + "\n" + "DTEND:"
				+ formatter.format(calendarRequest.getMeetingEndTime()).replace(" ", "T") + "\n" + "CLASS:PUBLIC\n"
				+ "PRIORITY:5\n" + "DTSTAMP:20200922T105302Z\n" + "TRANSP:OPAQUE\n" + "STATUS:CONFIRMED\n"
				+ "SEQUENCE:$sequenceNumber\n" + "LOCATION;LANGUAGE=en-US:Microsoft Teams Meeting\n" + "BEGIN:VALARM\n"
				+ "DESCRIPTION:REMINDER\n" + "TRIGGER;RELATED=START:-PT15M\n" + "ACTION:DISPLAY\n" + "END:VALARM\n"
				+ "END:VEVENT\n" + "END:VCALENDAR");

		MimeBodyPart messageBodyPart = new MimeBodyPart();

		messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
		messageBodyPart.setHeader("Content-ID", "calendar_message");
		messageBodyPart.setDataHandler(new DataHandler(
				new ByteArrayDataSource(builder.toString(), "text/calendar;method=REQUEST;name=\"invite.ics\"")));

		MimeMultipart multipart = new MimeMultipart();

		multipart.addBodyPart(messageBodyPart);

		mimeMessage.setContent(multipart);

//		logger.info("Calendar Invite Start");
//		logger.info(builder.toString());
//		logger.info("Calendar Invite End");

		mailSender.send(mimeMessage);
		logger.info("Calendar invite sent");

	}

	@Override
	public void sendEmailWithAttachment() throws MessagingException, IOException {

		MimeMessage msg = mailSender.createMimeMessage();

		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);

		helper.setTo(
				new String[] { "nmd.shihabudeen@gmail.com", "sihabudeen@promantus.com", "robinrajesh@promantus.com" });

		helper.setSubject("HirePro - Testing from Spring Boot");

		// default = text/plain
		// helper.setText("Check attachment for image!");

		// true = text/html
		helper.setText("<h1>Check attachment for image!</h1>", true);

		// hard coded a file path
		// FileSystemResource file = new FileSystemResource(new
		// File("path/android.png"));

		// helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

		mailSender.send(msg);
	}

//	@Override
//	public Event sendTeamsMeeting() {
//
////		final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
////				.clientId("7d851628-6d68-4871-a290-d7deb39f5ad5").clientSecret("bbbc4ff3-4312-404e-98bb-5045c0292b6b")
////				.tenantId("fc31dc55-73e2-4797-9644-70cf4b4fd11c").build();
//
//		final UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
//		        .clientId("7d851628-6d68-4871-a290-d7deb39f5ad5")
//		        .username("sihabudeen@promantus.com")
//		        .password("bismi@123")
//		        .build();
//		
//		String[] scopes = { "https://outlook.office.com/mail.read", "https://outlook.office.com/calendars.read", "https://teams.microsoft.com/_#/discover" };
//
//		final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(
//				Arrays.asList("User.Read"), usernamePasswordCredential);
//
//		GraphServiceClient graphClient = GraphServiceClient.builder()
//				.authenticationProvider(tokenCredentialAuthProvider).buildClient();
//
//		LinkedList<Option> requestOptions = new LinkedList<Option>();
//		requestOptions.add(new HeaderOption("Prefer", "outlook.timezone=\"Indian Standard Time\""));
//
//		Event event = new Event();
//		event.subject = "Interview schedule";
//		ItemBody body = new ItemBody();
//		body.contentType = BodyType.HTML;
//		body.content = "Interview scheduled on the below details.";
//		event.body = body;
//		DateTimeTimeZone start = new DateTimeTimeZone();
//		start.dateTime = "2021-07-11T18:00:00";
//		start.timeZone = "Indian Standard Time";
//		event.start = start;
//		DateTimeTimeZone end = new DateTimeTimeZone();
//		end.dateTime = "2021-07-11T19:00:00";
//		end.timeZone = "Indian Standard Time";
//		event.end = end;
//		Location location = new Location();
//		location.displayName = "Teams";
//		event.location = location;
//		LinkedList<Attendee> attendeesList = new LinkedList<Attendee>();
//		Attendee attendees = new Attendee();
//		EmailAddress emailAddress = new EmailAddress();
//		emailAddress.address = "sihabudeen@promantus.com";
//		emailAddress.name = "Mohamed Sihabudeen";
//		attendees.emailAddress = emailAddress;
//		attendees.type = AttendeeType.REQUIRED;
//		attendeesList.add(attendees);
//		event.attendees = attendeesList;
//		event.allowNewTimeProposals = true;
//		event.isOnlineMeeting = true;
//		event.onlineMeetingProvider = OnlineMeetingProviderType.TEAMS_FOR_BUSINESS;
//
////		graphClient.me().events().buildRequest(requestOptions).post(event);
//		
//		EventCollectionPage eventCollectionPage = graphClient.me().events().buildRequest().get();
//		Event event2 = eventCollectionPage.getCurrentPage().get(0);
//		System.out.println("ZDasdasda   -  "+event2.organizer.emailAddress.address);
//		return null;

//	}
}
