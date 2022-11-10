/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.HireProUsDefaultMethods;
import com.promantus.hireprous.dto.CustomerDto;
import com.promantus.hireprous.entity.Customer;
import com.promantus.hireprous.repository.CustomerRepository;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.CustomerService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.service.ProjectService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	ProjectService projectService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public Boolean checkCustomer(CustomerDto customerDto) throws Exception {

		Customer customer = customerRepository
				.getCustomerByCustomerNameIgnoreCaseAndLocationIgnoreCaseAndRegionIgnoreCase(
						customerDto.getCustomerName(), customerDto.getLocation(), customerDto.getRegion());

		if (customer != null) {
			return true;
		}

		return false;
	}

	@Override
	public CustomerDto addCustomer(final CustomerDto customerDto, final String lang) throws Exception {

		CustomerDto resultDto = new CustomerDto();
		if (this.checkCustomer(customerDto)) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists",
					new String[] { "Customer Name, Location and Region" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		Customer customer = new Customer();
		customer.setId(commonService.nextSequenceNumber());
		customer.setCustomerName(customerDto.getCustomerName());
		customer.setRecRoleIds("");
		customer.setLocation(customerDto.getLocation());
		customer.setRegion(customerDto.getRegion());

		customer.setCreatedBy(customerDto.getCreatedBy());
		customer.setUpdatedBy(customerDto.getUpdatedBy());
		customer.setCreatedDateTime(LocalDateTime.now());
		customer.setUpdatedDateTime(LocalDateTime.now());

		customerRepository.save(customer);

		CacheUtil.getCustomersMap().put(customer.getId(),
				customer.getCustomerName() + ", " + customer.getLocation() + ", " + customer.getRegion());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public CustomerDto updateCustomer(final CustomerDto customerDto, final String lang) throws Exception {

		Customer customer = customerRepository.findById(customerDto.getId());

		CustomerDto resultDto = new CustomerDto();
		if (customer == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Customer Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (this.checkCustomer(customerDto)) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists",
					new String[] { "Customer Name, Location and Region" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		customer.setCustomerName(customerDto.getCustomerName());
		customer.setLocation(customerDto.getLocation());
		customer.setRegion(customerDto.getRegion());

		customer.setUpdatedBy(customerDto.getUpdatedBy());
		customer.setUpdatedDateTime(LocalDateTime.now());

		customerRepository.save(customer);

		CacheUtil.getCustomersMap().remove(customer.getId());
		CacheUtil.getCustomersMap().put(customer.getId(),
				customer.getCustomerName() + ", " + customer.getLocation() + ", " + customer.getRegion());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<CustomerDto> getAllCustomers() throws Exception {

		List<CustomerDto> customerDtoList = new ArrayList<CustomerDto>();

		List<Customer> customersList = customerRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Customer customer : customersList) {
			customerDtoList.add(this.getCustomerDto(customer));
		}

		return customerDtoList;
	}

	@Override
	public CustomerDto getCustomerById(final String customerId) throws Exception {

		Customer customer = customerRepository.findById(Long.parseLong(customerId));

		return customer != null ? this.getCustomerDto(customer) : new CustomerDto();
	}

	@Override
	public String getCustomerNameById(final long customerId) throws Exception {

		Customer customer = customerRepository.findById(customerId);

		return customer != null ? customer.getCustomerName() : "";
	}

	@Override
	public String getCustomerNameInDetailById(final long customerId) throws Exception {

		Customer customer = customerRepository.findById(customerId);

		return customer != null
				? customer.getCustomerName() + ", " + customer.getLocation() + ", " + customer.getRegion()
				: "";
	}

	/**
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	private CustomerDto getCustomerDto(final Customer customer) throws Exception {

		CustomerDto customerDto = new CustomerDto();
		customerDto.setId(customer.getId());
		customerDto.setCustomerName(customer.getCustomerName());
		customerDto.setLocation(customer.getLocation());
		customerDto.setRegion(customer.getRegion());

		customerDto.setCreatedBy(customer.getCreatedBy());
		customerDto.setCreatedByName(CacheUtil.getUsersMap().get(customer.getCreatedBy()));
		customerDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(customer.getCreatedDateTime()));

		customerDto.setUpdatedBy(customer.getUpdatedBy());
		customerDto.setUpdatedByName(CacheUtil.getUsersMap().get(customer.getUpdatedBy()));
		customerDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(customer.getUpdatedDateTime()));

		return customerDto;
	}

	@Override
	public CustomerDto deleteCustomerById(final String customerId) throws Exception {

		CustomerDto resultDto = new CustomerDto();

		Customer customer = customerRepository.findById(Long.parseLong(customerId));

		if (customer == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Customer Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		boolean hasDependency = false;
		if (jobRequestService.getCustomerDependencyCount(customer.getId()) > 0) {
			hasDependency = true;
		}
		if (projectService.getCustomerDependencyCount(customer.getId()) > 0) {
			hasDependency = true;
		}
//		if (customer.getRecRoleIds() != null && !customer.getRecRoleIds().isEmpty()) {
//			hasDependency = true;
//		}

		if (hasDependency) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("cannot.delete",
					new String[] { "Customer is used in other functionalities. So" }, null));
			return resultDto;
		}

		customerRepository.deleteById(Long.parseLong(customerId));
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);

		CacheUtil.getCustomersMap().remove(customer.getId());
		CacheUtil.getRecRolesByCustomerMap().remove(customer.getId());

		return resultDto;
	}

	@Override
	public List<CustomerDto> searchCustomer(final String keyword) throws Exception {

		List<CustomerDto> customerDtoList = new ArrayList<CustomerDto>();

		List<Customer> customersList = customerRepository.findByCustomerNameRegex("(?i).*" + keyword + ".*",
				HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Customer customer : customersList) {
			customerDtoList.add(this.getCustomerDto(customer));
		}

		return customerDtoList;
	}

	@Override
	public List<CustomerDto> searchCustomerForDownload(final CustomerDto customerDto) throws Exception {

		List<CustomerDto> customerDtoList = new ArrayList<CustomerDto>();

		final List<Criteria> criteriaList = new ArrayList<>();

		if (customerDto.getCustomerName() != null && !customerDto.getCustomerName().isEmpty()) {
			criteriaList.add(Criteria.where("customerName").is(customerDto.getCustomerName()));
		}
		if (customerDto.getLocation() != null && !customerDto.getLocation().isEmpty()) {
			criteriaList.add(Criteria.where("location").regex("(?i).*" + customerDto.getLocation() + ".*"));
		}
		if (customerDto.getRegion() != null && !customerDto.getRegion().isEmpty()) {
			criteriaList.add(Criteria.where("region").regex("(?i).*" + customerDto.getRegion() + ".*"));
		}
		if (customerDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(customerDto.getFromDateTime()));
		}
		if (customerDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(customerDto.getToDateTime()));
		}

		List<Customer> customerList = new ArrayList<Customer>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			customerList = mongoTemplate.find(searchQuery, Customer.class);
		}

		for (Customer customer : customerList) {
			customerDtoList.add(this.getCustomerDto(customer));
		}

		Comparator<CustomerDto> compareByUpdatedDateTime = Comparator.comparing(CustomerDto::getUpdatedDateTime);
		customerDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return customerDtoList;
	}

	@Override
	public byte[] downloadCustomerDetails(List<CustomerDto> customerDtoList, String lang) throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/customer_details.xlsx").getFile();
		try (Workbook customerDetailsWB = new XSSFWorkbook(file)) {

			
			Sheet sheet = customerDetailsWB.getSheetAt(0);
			
			HireProUsDefaultMethods.cleanSheet(sheet);
			
			int rowNum = 2;
			for (CustomerDto customerDto : customerDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

//				dataRow.createCell(1).setCellValue(customerDto.getId());
				dataRow.createCell(1).setCellValue(customerDto.getCustomerName());
				dataRow.createCell(2).setCellValue(customerDto.getLocation());
				dataRow.createCell(3).setCellValue(customerDto.getRegion());
				dataRow.createCell(4).setCellValue(customerDto.getCreatedByName());
				dataRow.createCell(5).setCellValue(customerDto.getCreatedDateTime().toLocalDate().toString());
				dataRow.createCell(6).setCellValue(customerDto.getUpdatedByName());
				dataRow.createCell(7).setCellValue(customerDto.getUpdatedDateTime().toLocalDate().toString());
				dataRow.createCell(8).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			customerDetailsWB.write(outputStream);

			customerDetailsWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during Customer Details download file", ex);
			return null;
		}
	}
}
