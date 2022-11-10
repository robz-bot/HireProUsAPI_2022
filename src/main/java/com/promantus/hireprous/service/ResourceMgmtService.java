/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.ResourceMgmtDto;

/**
 * @author Sihab.
 *
 */
public interface ResourceMgmtService {

	/**
	 * @param resourceMgmtDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	ResourceMgmtDto addResourceMgmt(final ResourceMgmtDto resourceMgmtDto, final String lang) throws Exception;

	/**
	 * @param resourceMgmtDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	ResourceMgmtDto updateResourceMgmt(final ResourceMgmtDto resourceMgmtDto, final String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<ResourceMgmtDto> getAllResourceMgmts() throws Exception;

	/**
	 * @param resourceMgmtId
	 * @return
	 * @throws Exception
	 */
	ResourceMgmtDto getResourceMgmtById(String resourceMgmtId) throws Exception;

	/**
	 * @param resourceMgmtId
	 * @return
	 * @throws Exception
	 */
	ResourceMgmtDto deleteResourceMgmtById(String resourceMgmtId) throws Exception;

	/**
	 * @param resourceMgmtDto
	 * @return
	 * @throws Exception
	 */
	List<ResourceMgmtDto> searchResourceMgmt(ResourceMgmtDto resourceMgmtDto) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<String> getAllEmployeeIds() throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<String> getAllWorkOrderNumbers() throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<ResourceMgmtDto> getBenchResources() throws Exception;

	/**
	 * @param resourceMgmtId
	 * @param updatedBy
	 * @throws Exception
	 */
	void updateProjectAllocationById(String resourceMgmtId, Long updatedBy) throws Exception;

	/**
	 * @param resourceMgmtId
	 * @param projectId
	 * @param customerId
	 * @param updatedBy
	 * @param workOrderNumber
	 * @param employeeIdByHR
	 * @param email
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	ResourceMgmtDto updateProjectAllocationById(String resourceMgmtId, Long projectId, Long customerId, Long updatedBy,
			String workOrderNumber, String employeeIdByHR, final String email, Long buId) throws Exception;

	/**
	 * @param resourceMgmtDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	ResourceMgmtDto updateResourceMgmtStatus(ResourceMgmtDto resourceMgmtDto, String lang) throws Exception;

	/**
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	List<ResourceMgmtDto> getBenchResourcesByBuId(Long buId) throws Exception;

	/**
	 * @param resourceMgmtDtoList
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	byte[] downloadResourceDetails(List<ResourceMgmtDto> resourceMgmtDtoList, String lang) throws Exception;

	/**
	 * @param projectId
	 * @return
	 */
	int getProjectDependencyCount(Long projectId);

}
