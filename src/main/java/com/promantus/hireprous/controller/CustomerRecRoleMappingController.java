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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.CustomerDto;
import com.promantus.hireprous.dto.CustomerRecRoleDto;
import com.promantus.hireprous.dto.RecruitmentRoleDto;
import com.promantus.hireprous.service.CustomerRecRoleMappingService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Customer and Recruitment Role Mapping related
 * APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class CustomerRecRoleMappingController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRecRoleMappingController.class);

	@Autowired
	private CustomerRecRoleMappingService customerRecRoleMappingService;

	/**
	 * @return
	 */
	@GetMapping("/getAllCustomersWithRecRoles")
	public List<CustomerRecRoleDto> getAllCustomersWithRecRoles(
			@RequestHeader(name = "lang", required = false) String lang) {

		List<CustomerRecRoleDto> customerRecRoleDtoList = new ArrayList<CustomerRecRoleDto>();
		try {
			customerRecRoleDtoList = customerRecRoleMappingService.getAllCustomersWithRecRoles();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return customerRecRoleDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getRecRolesByCustomerIdForFilter/{customerId}")
	public List<CustomerRecRoleDto> getRecRolesByCustomerIdForFilter(@PathVariable String customerId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return customerRecRoleMappingService.getRecRolesByCustomerIdForFilter(customerId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CustomerRecRoleDto>();
	}

	/**
	 * @param roleId
	 * @return
	 */
	@GetMapping("/getRecRolesForUpdate/{customerId}")
	public CustomerDto getRecRolesForUpdate(@PathVariable String customerId,
			@RequestHeader(name = "lang", required = false) String lang) {

		CustomerDto resultDto = new CustomerDto();
		try {

			resultDto = customerRecRoleMappingService.getRecRolesForUpdate(customerId, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param roleId
	 * @return
	 */
	@GetMapping("/getRecRolesByCustomerId/{customerId}")
	public List<RecruitmentRoleDto> getRecRolesByCustomerId(@PathVariable String customerId,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<RecruitmentRoleDto> resultDto = new ArrayList<RecruitmentRoleDto>();
		try {

			return customerRecRoleMappingService.getRecRolesByCustomerId(customerId, lang);

		} catch (final Exception e) {

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}
	}

	/**
	 * @param customerId
	 * @param userId
	 * @param recRoleIds
	 * @param lang
	 * @return
	 */
	@PutMapping("/updateCustomerRecRoleMapping/{customerId}/{userId}")
	public CustomerDto updateCustomerRecRoleMapping(@PathVariable String customerId, @PathVariable String userId,
			@RequestBody List<String> recRoleIds, @RequestHeader(name = "lang", required = false) String lang) {

		CustomerDto resultDto = new CustomerDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Customer Id.
			if (customerId == null || customerId.isEmpty() || customerId.equals("0")) {
				errorParam.append("Customer Id");
			}
			// User Id.
			if (userId == null || userId.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", User Id" : "User Id");
			}
			// Rec Role List.
			if (recRoleIds == null || recRoleIds.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Rec Role List" : "Rec Role List");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = customerRecRoleMappingService.updateCustomerRecRoleMapping(customerId, userId, recRoleIds,
					lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param customerId
	 * @param userId
	 * @param recRoleId
	 * @param lang
	 * @return
	 */
	@PutMapping("/mapExistingRecRoleWithCustomer/{customerId}/{userId}/{recRoleId}")
	public CustomerDto mapExistingRecRoleWithCustomer(@PathVariable String customerId, @PathVariable String userId,
			@PathVariable String recRoleId, @RequestHeader(name = "lang", required = false) String lang) {

		CustomerDto resultDto = new CustomerDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Customer Id.
			if (customerId == null || customerId.isEmpty() || customerId.equals("0")) {
				errorParam.append("Customer Id");
			}
			// User Id.
			if (userId == null || userId.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", User Id" : "User Id");
			}
			// Rec Role List.
			if (recRoleId == null || recRoleId.isEmpty() || recRoleId.equals("0")) {
				errorParam.append(errorParam.length() > 0 ? ", Rec Role Id" : "Rec Role Id");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = customerRecRoleMappingService.mapExistingRecRoleWithCustomer(customerId, userId, recRoleId,
					lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param customerId
	 * @param userId
	 * @param recRoleName
	 * @param lang
	 * @return
	 */
	@PutMapping("/mapNewRecRoleWithCustomer/{customerId}/{userId}/{recRoleName}")
	public CustomerDto mapNewRecRoleWithCustomer(@PathVariable String customerId, @PathVariable String userId,
			@PathVariable String recRoleName, @RequestHeader(name = "lang", required = false) String lang) {

		CustomerDto resultDto = new CustomerDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Customer Id.
			if (customerId == null || customerId.isEmpty() || customerId.equals("0")) {
				errorParam.append("Customer Id");
			}
			// User Id.
			if (userId == null || userId.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", User Id" : "User Id");
			}
			// Rec Role Name.
			if (recRoleName == null || recRoleName.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Rec Role Name" : "Rec Role Name");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = customerRecRoleMappingService.mapNewRecRoleWithCustomer(customerId, userId, recRoleName, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}
}
