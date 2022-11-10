/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.CustomerDto;
import com.promantus.hireprous.dto.CustomerRecRoleDto;
import com.promantus.hireprous.dto.RecruitmentRoleDto;

/**
 * @author Sihab.
 *
 */
public interface CustomerRecRoleMappingService {

	/**
	 * @return
	 * @throws Exception
	 */
	List<CustomerRecRoleDto> getAllCustomersWithRecRoles() throws Exception;

	/**
	 * @param customerId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CustomerDto getRecRolesForUpdate(String customerId, String lang) throws Exception;

	/**
	 * @param customerId
	 * @param userId
	 * @param recRoleIds
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CustomerDto updateCustomerRecRoleMapping(String customerId, String userId, List<String> recRoleIds, String lang)
			throws Exception;

	/**
	 * @param customerId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<RecruitmentRoleDto> getRecRolesByCustomerId(String customerId, String lang) throws Exception;

	/**
	 * @param customerId
	 * @param userId
	 * @param recRoleId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CustomerDto mapExistingRecRoleWithCustomer(String customerId, String userId, String recRoleId, String lang)
			throws Exception;

	/**
	 * @param customerId
	 * @param userId
	 * @param recRoleName
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CustomerDto mapNewRecRoleWithCustomer(String customerId, String userId, String recRoleName, String lang)
			throws Exception;

	/**
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	List<CustomerRecRoleDto> getRecRolesByCustomerIdForFilter(String customerId) throws Exception;

}
