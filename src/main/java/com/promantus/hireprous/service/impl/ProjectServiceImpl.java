/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.ProjectDto;
import com.promantus.hireprous.entity.Project;
import com.promantus.hireprous.repository.ProjectRepository;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.CustomerService;
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
public class ProjectServiceImpl implements ProjectService {

	private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	CustomerService customerService;

	@Autowired
	ResourceMgmtService resourceMgmtService;

	@Autowired
	OnboardService onboardService;

	@Autowired
	ProjectRepository projectRepository;

	private Boolean checkProjectName(String projectName, Long projectId, boolean addOrUpdate) throws Exception {

		Project projectCheck = projectRepository.getProjectByProjectNameIgnoreCase(projectName);
		if ((addOrUpdate && projectCheck != null)
				|| (projectCheck != null && !projectId.equals(projectCheck.getId()))) {
			return true;
		}

		return false;
	}

	@Override
	public ProjectDto addProject(final ProjectDto projectDto, final String lang) throws Exception {

		ProjectDto resultDto = new ProjectDto();
		if (this.checkProjectName(projectDto.getProjectName(), projectDto.getId(), true)) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { "Project Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		Project project = new Project();
		project.setId(commonService.nextSequenceNumber());
		project.setProjectName(projectDto.getProjectName());

		project.setBusinessUnitId(projectDto.getBusinessUnitId());
		project.setCustomerId(projectDto.getCustomerId());

		project.setCreatedBy(projectDto.getCreatedBy());
		project.setUpdatedBy(projectDto.getUpdatedBy());
		project.setCreatedDateTime(LocalDateTime.now());
		project.setUpdatedDateTime(LocalDateTime.now());
		project.setProjStatus(projectDto.getProjStatus());

		projectRepository.save(project);

		CacheUtil.getProjectsMap().put(project.getId(), project.getProjectName());
		if (CacheUtil.getProjectsByBUMap().get(project.getBusinessUnitId()) == null) {
			CacheUtil.getProjectsByBUMap().put(project.getBusinessUnitId(), project.getId() + "");
		} else {
			String projectIds = CacheUtil.getProjectsByBUMap().get(project.getBusinessUnitId());
			CacheUtil.getProjectsByBUMap().put(project.getBusinessUnitId(), projectIds + "," + project.getId());
		}

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public ProjectDto updateProject(final ProjectDto projectDto, final String lang) throws Exception {

		ProjectDto resultDto = new ProjectDto();

		Project project = projectRepository.findById(projectDto.getId());

		if (project == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Project Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (this.checkProjectName(projectDto.getProjectName(), projectDto.getId(), false)) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { "Project Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		project.setProjectName(projectDto.getProjectName());

		project.setBusinessUnitId(projectDto.getBusinessUnitId());
		project.setCustomerId(projectDto.getCustomerId());

		project.setUpdatedBy(projectDto.getUpdatedBy());
		project.setProjStatus(projectDto.getProjStatus());
		project.setUpdatedDateTime(LocalDateTime.now());

		projectRepository.save(project);

		CacheUtil.getProjectsMap().remove(project.getId());
		CacheUtil.getProjectsMap().put(project.getId(), project.getProjectName());
		if (CacheUtil.getProjectsByBUMap().get(project.getBusinessUnitId()) == null) {
			CacheUtil.getProjectsByBUMap().put(project.getBusinessUnitId(), project.getId() + "");
		} else {
			String projectIds = CacheUtil.getProjectsByBUMap().get(project.getBusinessUnitId());
			if (!projectIds.contains(project.getId() + "")) {
				CacheUtil.getProjectsByBUMap().put(project.getBusinessUnitId(), projectIds + "," + project.getId());
			}
		}

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<ProjectDto> getAllProjects() throws Exception {

		List<ProjectDto> projectDtoList = new ArrayList<ProjectDto>();

		List<Project> projectsList = projectRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Project project : projectsList) {
			projectDtoList.add(this.getProjectDto(project));
		}

		return projectDtoList;
	}

	@Override
	public String getProjectNameById(final long projectId) throws Exception {

		Project project = projectRepository.findById(projectId);

		return project != null ? project.getProjectName() : "";
	}

	@Override
	public ProjectDto getProjectById(final String projectId) throws Exception {

		Project project = projectRepository.findById(Long.parseLong(projectId));

		return project != null ? this.getProjectDto(project) : new ProjectDto();
	}

	@Override
	public List<ProjectDto> getProjectByBuId(final String buId) throws Exception {

		List<Project> projectsList = projectRepository.findByBusinessUnitId(Long.parseLong(buId),
				Sort.by(Sort.Direction.ASC, "projectName"));

		List<ProjectDto> projectDtoList = new ArrayList<ProjectDto>();
		for (Project project : projectsList) {
			projectDtoList.add(this.getProjectDto(project));
		}

		return projectDtoList;
	}

	@Override
	public Long getCustomerIdById(final Long projectId) throws Exception {

		Project project = projectRepository.findById(projectId);

		return project != null ? project.getCustomerId() : 0L;
	}

	/**
	 * @param project
	 * @return
	 * @throws Exception
	 */
	private ProjectDto getProjectDto(final Project project) throws Exception {

		ProjectDto projectDto = new ProjectDto();
		projectDto.setId(project.getId());
		projectDto.setProjectName(project.getProjectName());

		projectDto.setBusinessUnitId(project.getBusinessUnitId());
		projectDto.setBusinessUnitName(businessUnitService.getBusinessUnitNameById(project.getBusinessUnitId()));

		projectDto.setCustomerId(project.getCustomerId());
		projectDto.setCustomerName(customerService.getCustomerNameById(project.getCustomerId()));
		projectDto.setProjStatus(project.getProjStatus());

		projectDto.setCreatedBy(project.getCreatedBy());
		projectDto.setCreatedByName(CacheUtil.getUsersMap().get(project.getCreatedBy()));
		projectDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(project.getCreatedDateTime()));

		projectDto.setUpdatedBy(project.getUpdatedBy());
		projectDto.setUpdatedByName(CacheUtil.getUsersMap().get(project.getUpdatedBy()));
		projectDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(project.getUpdatedDateTime()));

		return projectDto;
	}

	@Override
	public ProjectDto deleteProjectById(final String projectId) throws Exception {

		ProjectDto resultDto = new ProjectDto();

		Project project = projectRepository.findById(Long.parseLong(projectId));

		if (project == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Project Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		boolean hasDependency = false;
		if (onboardService.getProjectDependencyCount(project.getId()) > 0) {
			hasDependency = true;
		}
		if (resourceMgmtService.getProjectDependencyCount(project.getId()) > 0) {
			hasDependency = true;
		}

		if (hasDependency) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("cannot.delete",
					new String[] { "Project is used in other functionalities. So" }, null));
			return resultDto;
		}

		projectRepository.deleteById(Long.parseLong(projectId));

		CacheUtil.getProjectsMap().remove(Long.parseLong(projectId));
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);

		return resultDto;
	}

	@Override
	public List<ProjectDto> searchProject(final String keyword) throws Exception {

		List<ProjectDto> projectDtoList = new ArrayList<ProjectDto>();

		List<Project> projectsList = projectRepository.findByProjectNameRegex("(?i).*" + keyword + ".*",
				HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Project project : projectsList) {
			projectDtoList.add(this.getProjectDto(project));
		}

		return projectDtoList;
	}

	@Override
	public int getBUDependencyCount(Long buId) {

		return projectRepository.countByBusinessUnitId(buId);
	}

	@Override
	public int getCustomerDependencyCount(Long customerId) {

		return projectRepository.countByCustomerId(customerId);
	}

	@Override
	public List<ProjectDto> getCustProjectBycustId(String custId) throws Exception {

		System.out.println("Customer Id"+ custId);
		List<Project> projectsList = projectRepository.findProjectByCustomerId(Long.parseLong(custId),
				Sort.by(Sort.Direction.ASC, "projectName"));

		List<ProjectDto> projectDtoList = new ArrayList<ProjectDto>();
		for (Project project : projectsList) {
			projectDtoList.add(this.getProjectDto(project));
			System.out.println(project);
		}

		return projectDtoList;
	}

	@Override
	public List<ProjectDto> getProjectsByCustomerId(final String customerId) throws Exception {

		List<Project> projectsList = projectRepository.findByCustomerId(Long.parseLong(customerId),
				Sort.by(Sort.Direction.ASC, "projectName"));

		List<ProjectDto> projectDtoList = new ArrayList<ProjectDto>();
		for (Project project : projectsList) {
			projectDtoList.add(this.getProjectDto(project));
		}

		return projectDtoList;
	}

	@Override
	public List<String> getAllProjectName() throws Exception {
		
		List<Project> projectsList = projectRepository.findAll();
		
		Map<Long, String> result = new HashMap<>();
		
		for (Project project : projectsList) {
			
			result.put(project.getId(), project.getProjectName());
		}
		List<Map.Entry<Long,String>> entryList = new ArrayList<>(result.entrySet());
		
		List<String> valueList = entryList.stream()
		        .map(entry -> entry.getKey() + ": " + entry.getValue())
		        .collect(Collectors.toList());
		return valueList;
 }	
}
