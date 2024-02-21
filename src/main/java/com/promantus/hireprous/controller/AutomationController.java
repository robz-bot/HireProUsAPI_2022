/**
 * 
 */
package com.promantus.hireprous.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.entity.BusinessUnit;
import com.promantus.hireprous.entity.Customer;
import com.promantus.hireprous.entity.JobRequest;
import com.promantus.hireprous.entity.RecruitmentRole;
import com.promantus.hireprous.repository.BusinessUnitRepository;
import com.promantus.hireprous.repository.CustomerRepository;
import com.promantus.hireprous.repository.JobRequestRepository;
import com.promantus.hireprous.repository.RecruitmentRoleRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Promantus
 *
 */
@RestController
@RequestMapping("/api/v1")
public class AutomationController {

	@Autowired
	CommonService commonService;

	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Autowired
	private BusinessUnitRepository businessUnitRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RecruitmentRoleRepository recruitmentRoleRepository;

	@GetMapping("/addDummyJobRequests")
	public String addDummyJobRequests() throws Exception {

		BusinessUnit bu = businessUnitRepository.findAll().get(0);
		Customer customer = customerRepository.findAll().get(0);
		RecruitmentRole recRole = recruitmentRoleRepository.findAll().get(0);

		JobRequest jobReq = new JobRequest();

		jobReq.setId(commonService.nextSequenceNumber());

		this.setJobReferenceNumber(jobReq);

		jobReq.setBuId(bu.getId());
		jobReq.setCustomerId(customer.getId());
		jobReq.setRoleId(customer.getRecRoleIds().contains(",") ? Long.parseLong(customer.getRecRoleIds().split(",")[0])
				: Long.parseLong(customer.getRecRoleIds()));
		jobReq.setCustomerId(customer.getId());
		jobReq.setCustomerId(customer.getId());
		jobReq.setNoOfOpenings(1);
		jobReq.setPayRange("As Per Standard");
		jobReq.setPayFrequency("Monthly");
		jobReq.setCurrency("INR");
		jobReq.setEmploymentType("Full Time");
		jobReq.setPlacementFor("Internal");
		jobReq.setProjectStartDate(LocalDate.now());
		jobReq.setJobDescription(
				"Exp: 3 to 5\nQualification : Any degree\n\n-Extremely well-organized, able to priorities tasks, multitask, and pay close attention to details. \n-Extremely enthusiastic about working with coworkers and clients. \n-Excellent verbal and writing communication abilities, as well as a passion for presentations. \n-Extremely flexible and driven to fulfil deadlines. \n-Discover new opportunities for business growth.");
		jobReq.setMandatorySkills(" User Acceptance Testing");
		jobReq.setJobReqStatus(HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START);

		jobReq.setCreatedBy(0L);
		jobReq.setUpdatedBy(0L);
		jobReq.setCreatedDateTime(LocalDateTime.now());
		jobReq.setUpdatedDateTime(LocalDateTime.now());

		jobRequestRepository.save(jobReq);

		return "1 Dummy JR added";
	}

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
}
