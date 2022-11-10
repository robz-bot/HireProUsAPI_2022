/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.CustomerDto;
import com.promantus.hireprous.dto.CustomerRecRoleDto;
import com.promantus.hireprous.dto.RecruitmentRoleDto;
import com.promantus.hireprous.entity.Customer;
import com.promantus.hireprous.repository.CustomerRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.CustomerRecRoleMappingService;
import com.promantus.hireprous.service.RecruitmentRoleService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class CustomerRecRoleMappingServiceImpl implements CustomerRecRoleMappingService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRecRoleMappingServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	private RecruitmentRoleService recruitmentRoleService;

	@Autowired
	CustomerRepository customerRepository;

	@Override
	public List<CustomerRecRoleDto> getAllCustomersWithRecRoles() throws Exception {

		List<CustomerRecRoleDto> customerRecRoleDtoList = new ArrayList<CustomerRecRoleDto>();

		List<Customer> customersList = customerRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Customer customer : customersList) {

			CustomerRecRoleDto customerRecRoleDto = new CustomerRecRoleDto();

			customerRecRoleDto.setCustomerId(customer.getId());
			customerRecRoleDto.setCustomerName(
					customer.getCustomerName() + ", " + customer.getLocation() + ", " + customer.getRegion());

			List<String> recRoleStringList = !(customer.getRecRoleIds() == null || customer.getRecRoleIds().isEmpty())
					? Arrays.asList(customer.getRecRoleIds().split(","))
					: new ArrayList<String>();

			String recRoleNamesList = "";
			if (recRoleStringList != null && recRoleStringList.size() > 0) {
				recRoleNamesList = recruitmentRoleService.getRecruitmentRoleNameByIds(
						recRoleStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
			}

			customerRecRoleDto.setRecRoleList(recRoleNamesList);

			customerRecRoleDtoList.add(customerRecRoleDto);
		}

		return customerRecRoleDtoList;
	}

	@Override
	public List<CustomerRecRoleDto> getRecRolesByCustomerIdForFilter(String customerId) throws Exception {

		Customer customer = customerRepository.findById(Long.parseLong(customerId));

		if (customer == null) {
			logger.info(commonService.getMessage("invalid", new String[] { "Customer Id" }, null));

			return new ArrayList<CustomerRecRoleDto>();
		}

		CustomerRecRoleDto customerRecRoleDto = new CustomerRecRoleDto();

		customerRecRoleDto.setCustomerId(customer.getId());
		customerRecRoleDto.setCustomerName(
				customer.getCustomerName() + ", " + customer.getLocation() + ", " + customer.getRegion());

		List<String> recRoleStringList = !(customer.getRecRoleIds() == null || customer.getRecRoleIds().isEmpty())
				? Arrays.asList(customer.getRecRoleIds().split(","))
				: new ArrayList<String>();

		String recRoleNamesList = "";
		if (recRoleStringList != null && recRoleStringList.size() > 0) {
			recRoleNamesList = recruitmentRoleService.getRecruitmentRoleNameByIds(
					recRoleStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
		}

		customerRecRoleDto.setRecRoleList(recRoleNamesList);

		List<CustomerRecRoleDto> customerRecRoleDtoList = new ArrayList<CustomerRecRoleDto>();
		customerRecRoleDtoList.add(customerRecRoleDto);
		return customerRecRoleDtoList;
	}

	@Override
	public CustomerDto getRecRolesForUpdate(final String customerId, final String lang) throws Exception {

		CustomerDto resultDto = new CustomerDto();

		Customer customer = customerRepository.findById(Long.parseLong(customerId));

		if (customer == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Customer Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		resultDto.setId(customer.getId());
		resultDto.setCustomerName(customer.getCustomerName());

		List<String> recRoleIdList = !(customer.getRecRoleIds() == null || customer.getRecRoleIds().isEmpty())
				? Arrays.asList(customer.getRecRoleIds().split(","))
				: new ArrayList<String>();

		resultDto.setRecRoleIds(customer.getRecRoleIds());

		List<RecruitmentRoleDto> recRoleDtoList = recruitmentRoleService.getAllRecruitmentRoles();
		for (RecruitmentRoleDto recRoleDto : recRoleDtoList) {

			recRoleDto.setSelected("0");
			if (recRoleIdList.contains(recRoleDto.getId() + "")) {
				recRoleDto.setSelected("1");
			}
		}
		resultDto.setRecRoleDtoList(recRoleDtoList);

		return resultDto;
	}

	@Override
	public CustomerDto updateCustomerRecRoleMapping(final String customerId, final String userId,
			final List<String> recRoleIds, final String lang) throws Exception {

		CustomerDto resultDto = new CustomerDto();

		Customer customer = customerRepository.findById(Long.parseLong(customerId));

		if (customer == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Customer Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		// Rec. Role Id
		StringBuilder recRoleIdList = new StringBuilder("");
		for (String recRoleId : recRoleIds) {

			if (recRoleIdList.length() > 0) {
				recRoleIdList.append(",");
			}
			recRoleIdList.append(recRoleId);
		}
		customer.setRecRoleIds(recRoleIdList.toString());

		customer.setUpdatedBy(Long.parseLong(userId));
		customer.setUpdatedDateTime(LocalDateTime.now());

		customerRepository.save(customer);

		CacheUtil.getRecRolesByCustomerMap().remove(customer.getId());
		CacheUtil.getRecRolesByCustomerMap().put(customer.getId(), customer.getRecRoleIds());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<RecruitmentRoleDto> getRecRolesByCustomerId(final String customerId, final String lang)
			throws Exception {

		CustomerDto resultDto = new CustomerDto();

		Customer customer = customerRepository.findById(Long.parseLong(customerId));

		if (customer == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Customer Id" }, lang));

			logger.info(resultDto.getMessage());
			// return resultDto;
		}

		resultDto.setId(customer.getId());
		resultDto.setCustomerName(customer.getCustomerName());

		List<String> recRoleIdList = !(customer.getRecRoleIds() == null || customer.getRecRoleIds().isEmpty())
				? Arrays.asList(customer.getRecRoleIds().split(","))
				: new ArrayList<String>();

		resultDto.setRecRoleIdList(recRoleIdList);

		if (recRoleIdList != null && recRoleIdList.size() > 0) {
			return recruitmentRoleService
					.getRecruitmentRolesByIds(recRoleIdList.stream().map(Long::parseLong).collect(Collectors.toList()));
		} else {
			return new ArrayList<RecruitmentRoleDto>();
		}
	}

	@Override
	public CustomerDto mapExistingRecRoleWithCustomer(final String customerId, final String userId,
			final String recRoleId, final String lang) throws Exception {

		CustomerDto resultDto = new CustomerDto();

		Customer customer = customerRepository.findById(Long.parseLong(customerId));

		if (customer == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Customer Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (customer.getRecRoleIds() == null || customer.getRecRoleIds().isEmpty()) {
			customer.setRecRoleIds(recRoleId);
		} else {
			List<String> recRoleIds = Arrays.asList(customer.getRecRoleIds().split(","));
			if (!recRoleIds.contains(recRoleId)) {
				customer.setRecRoleIds(customer.getRecRoleIds() + "," + recRoleId);
			}
		}

		customer.setUpdatedBy(Long.parseLong(userId));
		customer.setUpdatedDateTime(LocalDateTime.now());

		customerRepository.save(customer);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public CustomerDto mapNewRecRoleWithCustomer(final String customerId, final String userId, final String recRoleName,
			final String lang) throws Exception {

		CustomerDto resultDto = new CustomerDto();

		Customer customer = customerRepository.findById(Long.parseLong(customerId));

		if (customer == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Customer Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		RecruitmentRoleDto recRoleDto = new RecruitmentRoleDto();
		recRoleDto.setRecruitmentRoleName(recRoleName);
		recRoleDto.setCreatedBy(Long.parseLong(userId));
		recRoleDto.setUpdatedBy(Long.parseLong(userId));
		recRoleDto = recruitmentRoleService.addRecruitmentRole(recRoleDto, lang);

		if (customer.getRecRoleIds() == null || customer.getRecRoleIds().isEmpty()) {
			customer.setRecRoleIds(recRoleDto.getId() + "");
		} else {
			List<String> recRoleIds = Arrays.asList(customer.getRecRoleIds().split(","));
			if (!recRoleIds.contains(recRoleDto.getId() + "")) {
				customer.setRecRoleIds(customer.getRecRoleIds() + "," + recRoleDto.getId());
			}
		}

		customer.setUpdatedBy(Long.parseLong(userId));
		customer.setUpdatedDateTime(LocalDateTime.now());

		customerRepository.save(customer);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}
}
