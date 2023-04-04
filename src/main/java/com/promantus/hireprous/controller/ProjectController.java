/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.ProjectDto;
import com.promantus.hireprous.service.ProjectService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Projects related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ProjectController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectService projectService;

	/**
	 * @param projectDto
	 * @return
	 */
	@PostMapping("/addProject")
	public ProjectDto addProject(@RequestBody ProjectDto projectDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		ProjectDto resultDto = new ProjectDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Project Name.
			if (projectDto.getProjectName() == null || projectDto.getProjectName().isEmpty()) {
				errorParam.append("Project Name");
			}
			// BusinessUnit Id.
			if (projectDto.getBusinessUnitId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit" : "BusinessUnit");
			}
			// Customer Id.
			if (projectDto.getCustomerId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Customer" : "Customer");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = projectService.addProject(projectDto, lang);
		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param projectDto
	 * @return
	 */
	@PutMapping("/updateProject")
	public ProjectDto updateProject(@RequestBody ProjectDto projectDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		ProjectDto resultDto = new ProjectDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Project Id.
			if (projectDto.getId() == 0) {
				errorParam.append("Project Id");
			}
			// Project Name.
			if (projectDto.getProjectName() == null || projectDto.getProjectName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Project Name" : "Project Name");
			}
			// BusinessUnit Id.
			if (projectDto.getBusinessUnitId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit" : "BusinessUnit");
			}
			// Customer Id.
			if (projectDto.getCustomerId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Customer" : "Customer");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = projectService.updateProject(projectDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllProjects")
	public List<ProjectDto> getAllProjects(@RequestHeader(name = "lang", required = false) String lang) {

		List<ProjectDto> projectsDtoList = new ArrayList<ProjectDto>();
		try {
			projectsDtoList = projectService.getAllProjects();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return projectsDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getProjectsByBuId/{buId}")
	public List<ProjectDto> getProjectsByBuId(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return projectService.getProjectByBuId(buId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<ProjectDto>();
	}

	/**
	 * @param projectId
	 * @return
	 */
	@GetMapping("/getProject/{projectId}")
	public ProjectDto getProjectById(@PathVariable String projectId,
			@RequestHeader(name = "lang", required = false) String lang) {

		ProjectDto projectDto = new ProjectDto();
		try {
			projectDto = projectService.getProjectById(projectId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return projectDto;
	}
	/**
	 * @param projectId
	 * @return
	 */
	@GetMapping("/getCustomerProjects/{custId}")
	public List<ProjectDto> getCustProjectById(@PathVariable String custId,
			@RequestHeader(name = "lang", required = false) String lang) {
		List<ProjectDto> projectsDtoList = new ArrayList<ProjectDto>();
		try {
			projectsDtoList = projectService.getCustProjectBycustId(custId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return projectsDtoList;
	
	}


	
	/**
	 * @param projectId
	 * @return
	 */
	@DeleteMapping("/deleteProjectById/{projectId}")
	public Boolean deleteProjectById(@PathVariable String projectId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			projectService.deleteProjectById(projectId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchProject")
	public List<ProjectDto> searchProject(@RequestParam String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<ProjectDto> projectsDtoList = new ArrayList<ProjectDto>();
		try {
			projectsDtoList = projectService.searchProject(key);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return projectsDtoList;
	}
	@GetMapping("/getProjectsByCustomerId/{custometId}")
	public List<ProjectDto> getProjectsByCustomerId(@PathVariable String custometId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return projectService.getProjectsByCustomerId(custometId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<ProjectDto>();
	}
	
	@GetMapping("/getAllProjectName")
	public List<String> getAllProjectName(@RequestHeader(name = "lang", required = false) String lang) 
	{
		try {
			return projectService.getAllProjectName();
			
	  } catch (final Exception e) {
		  
		logger.error(HireProUsUtil.getErrorMessage(e));
		
		}
		
		return null;
		}
}
