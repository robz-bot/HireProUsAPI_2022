/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.entity.BusinessUnit;
import com.promantus.hireprous.entity.Candidate;
import com.promantus.hireprous.entity.Counter;
import com.promantus.hireprous.entity.Customer;
import com.promantus.hireprous.entity.Project;
import com.promantus.hireprous.entity.RecruitmentRole;
import com.promantus.hireprous.entity.Role;
import com.promantus.hireprous.entity.User;
import com.promantus.hireprous.entity.Vendor;
import com.promantus.hireprous.repository.BusinessUnitRepository;
import com.promantus.hireprous.repository.CandidateRepository;
import com.promantus.hireprous.repository.CounterRepository;
import com.promantus.hireprous.repository.CustomerRepository;
import com.promantus.hireprous.repository.ImageRepository;
import com.promantus.hireprous.repository.InterviewScheduleRepository;
import com.promantus.hireprous.repository.JobRequestRepository;
import com.promantus.hireprous.repository.OnboardRepository;
import com.promantus.hireprous.repository.ProjectRepository;
import com.promantus.hireprous.repository.RecruitmentRoleRepository;
import com.promantus.hireprous.repository.ResourceMgmtRepository;
import com.promantus.hireprous.repository.ResumeRepository;
import com.promantus.hireprous.repository.RoleRepository;
import com.promantus.hireprous.repository.UserRepository;
import com.promantus.hireprous.repository.VendorRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.util.CacheUtil;

/**
 * Service class for commonly used features.
 * 
 * @author Sihab.
 *
 */
@Service
public class CommonServiceImpl implements CommonService {

	private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CounterRepository counterRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BusinessUnitRepository businessUnitRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private RecruitmentRoleRepository recruitmentRoleRepository;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	InterviewScheduleRepository interviewScheduleRepository;

	@Autowired
	JobRequestRepository jobRequestRepository;

	@Autowired
	OnboardRepository onboardRepository;

	@Autowired
	ResumeRepository resumeRepository;

	@Autowired
	ResourceMgmtRepository resourceMgmtRepository;

	@Autowired
	VendorRepository vendorRepository;

	@Override
	public long nextSequenceNumber() throws Exception {

		List<Counter> counterList = counterRepository.findAll();
		Counter counter = new Counter();
		long seqNumber = 1;

		if (counterList.size() > 0) {
			counter = counterList.get(0);
			seqNumber = counter.getSequenceNumber() + 1;
			counter.setSequenceNumber(seqNumber);
		} else {
			counter.setSequenceNumber(1);
		}

		counterRepository.save(counter);

		return seqNumber;
	}

	@Override
	public String getMessage(String messageKey, String[] params, String language) throws Exception {

		Locale locale = language == null || language.isEmpty() || !language.contains("_") ? Locale.getDefault()
				: new Locale(language.split("_")[0], language.split("_")[1]);

		if (params == null || params.length == 0) {
			return messageSource.getMessage(messageKey, null, locale);
		} else {
			return messageSource.getMessage(messageKey, params, locale);
		}
	}

	@Override
	public void truncateData() throws Exception {

		candidateRepository.deleteAll();
		imageRepository.deleteAll();
		interviewScheduleRepository.deleteAll();
		jobRequestRepository.deleteAll();
		onboardRepository.deleteAll();
		resumeRepository.deleteAll();
		resourceMgmtRepository.deleteAll();
	}

	@PostConstruct
	private void loadCacheData() {
		logger.info("*****  Cache Data Loading... *****");

		// Users.
		List<User> usersList = userRepository.findAll();
		CacheUtil.getUsersMap().put(0L, "Super Admin");
		for (User user : usersList) {
			CacheUtil.getUsersMap().put(user.getId(), user.getFirstName() + " " + user.getLastName());
			CacheUtil.getUsersEmailMap().put(user.getId(), user.getEmail());
		}

		// BusinessUnits.
		List<BusinessUnit> businessUnitsList = businessUnitRepository.findAll();
		for (BusinessUnit businessUnit : businessUnitsList) {
			CacheUtil.getBusMap().put(businessUnit.getId(), businessUnit.getBusinessUnitName());
		}

		// Roles.
		List<Role> rolesList = roleRepository.findAll();
		for (Role role : rolesList) {
			CacheUtil.getRolesMap().put(role.getId(), role.getRoleName());
		}

		// Customers.
		List<Customer> customersList = customerRepository.findAll();
		for (Customer customer : customersList) {
			CacheUtil.getCustomersMap().put(customer.getId(),
					customer.getCustomerName() + ", " + customer.getLocation() + ", " + customer.getRegion());

			CacheUtil.getRecRolesByCustomerMap().put(customer.getId(), customer.getRecRoleIds());
		}

		// Projects.
		List<Project> projectsList = projectRepository.findAll();
		for (Project project : projectsList) {

			CacheUtil.getProjectsMap().put(project.getId(), project.getProjectName());

			if (CacheUtil.getProjectsByBUMap().get(project.getBusinessUnitId()) == null) {
				CacheUtil.getProjectsByBUMap().put(project.getBusinessUnitId(), project.getId() + "");
			} else {
				String projectIds = CacheUtil.getProjectsByBUMap().get(project.getBusinessUnitId());
				CacheUtil.getProjectsByBUMap().put(project.getBusinessUnitId(), projectIds + "," + project.getId());
			}
		}

		// RecruitmentRoles.
		List<RecruitmentRole> recRolesList = recruitmentRoleRepository.findAll();
		for (RecruitmentRole recRole : recRolesList) {
			CacheUtil.getRecRolesMap().put(recRole.getId(), recRole.getRecruitmentRoleName());
		}

		// Candidates.
		List<Candidate> candidatesList = candidateRepository.findAll();
		for (Candidate candidate : candidatesList) {
			CacheUtil.getCandidatesMap().put(candidate.getId(),
					candidate.getFirstName() + " " + candidate.getLastName());
		}

		// Vendors.
		List<Vendor> vendorsList = vendorRepository.findAll();
		for (Vendor vendor : vendorsList) {
			CacheUtil.getVendorsMap().put(vendor.getId(), vendor.getVendorName());
			CacheUtil.getVendorsEmailMap().put(vendor.getId(), vendor.getEmail());
			CacheUtil.getVendorsCCEmailMap().put(vendor.getId(), vendor.getCcEmailIds());
		}

		logger.info("*****  Cache Data Loaded Successfully... *****");
	}
}
