/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.ProjectDto;

/**
 * @author Sihab.
 *
 */
public interface ProjectService {

	/**
	 * @param projectDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	ProjectDto addProject(final ProjectDto projectDto, final String lang) throws Exception;

	/**
	 * @param projectDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	ProjectDto updateProject(final ProjectDto projectDto, final String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<ProjectDto> getAllProjects() throws Exception;

	/**
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	ProjectDto getProjectById(String projectId) throws Exception;

	/**
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	ProjectDto deleteProjectById(String projectId) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<ProjectDto> searchProject(String keyword) throws Exception;

	/**
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	String getProjectNameById(long projectId) throws Exception;

	/**
	 * @param buId
	 * @return
	 */
	int getBUDependencyCount(Long buId);

	/**
	 * @param customerId
	 * @return
	 */
	int getCustomerDependencyCount(Long customerId);

	/**
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	Long getCustomerIdById(Long projectId) throws Exception;

	/**
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	List<ProjectDto> getProjectByBuId(String buId) throws Exception;

	List<ProjectDto> getCustProjectBycustId(String custId) throws Exception;

}
