/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.HireProUsDefaultMethods;
import com.promantus.hireprous.dto.ResourceMgmtDto;
import com.promantus.hireprous.entity.ResourceMgmt;
import com.promantus.hireprous.repository.ResourceMgmtRepository;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.CustomerService;
import com.promantus.hireprous.service.ProjectService;
import com.promantus.hireprous.service.ResourceMgmtService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class ResourceMgmtServiceImpl implements ResourceMgmtService {

	private static final Logger logger = LoggerFactory.getLogger(ResourceMgmtServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	ProjectService projectService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	CustomerService customerService;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	ResourceMgmtRepository resourceMgmtRepository;

	@Override
	public ResourceMgmtDto addResourceMgmt(final ResourceMgmtDto resourceMgmtDto, final String lang) throws Exception {

		ResourceMgmtDto resultDto = new ResourceMgmtDto();

		String errorMessage = this.checkDuplicate(resourceMgmtDto, true);
		if (errorMessage != null && !errorMessage.isEmpty()) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { errorMessage }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		ResourceMgmt resourceMgmt = new ResourceMgmt();
		resourceMgmt.setId(commonService.nextSequenceNumber());
		resourceMgmt.setEmployeeId(resourceMgmtDto.getEmployeeId());
		resourceMgmt.setEmployeeIdByHR(resourceMgmtDto.getEmployeeIdByHR());
		resourceMgmt.setWorkOrderNumber(resourceMgmtDto.getWorkOrderNumber());

		resourceMgmt.setFirstName(resourceMgmtDto.getFirstName());
		resourceMgmt.setLastName(resourceMgmtDto.getLastName());
		resourceMgmt.setEmail(resourceMgmtDto.getEmail());
		resourceMgmt.setContactNumber(resourceMgmtDto.getContactNumber());
		resourceMgmt.setSex(resourceMgmtDto.getSex());
		resourceMgmt.setDesignation(resourceMgmtDto.getDesignation());
		resourceMgmt.setSkillSet(resourceMgmtDto.getSkillSet());

		resourceMgmt.setProjectAllocation(resourceMgmtDto.getProjectAllocation());
		resourceMgmt.setEmploymentType(resourceMgmtDto.getEmploymentType());
		resourceMgmt.setExperience(resourceMgmtDto.getExperience());

		resourceMgmt.setCustomerId(resourceMgmtDto.getCustomerId());
		resourceMgmt.setProjectId(resourceMgmtDto.getProjectId());
		resourceMgmt.setBuId(resourceMgmtDto.getBuId());

		resourceMgmt.setResourceStatus(HireProUsConstants.RESOURCE_STATUS_ACTIVE);
		resourceMgmt.setRemarks("Added");
		resourceMgmt.setEffectiveDate(LocalDate.now());

		resourceMgmt.setCreatedBy(resourceMgmtDto.getCreatedBy());
		resourceMgmt.setUpdatedBy(resourceMgmtDto.getUpdatedBy());
		resourceMgmt.setCreatedDateTime(LocalDateTime.now());
		resourceMgmt.setUpdatedDateTime(LocalDateTime.now());

		resourceMgmtRepository.save(resourceMgmt);

		resultDto.setId(resourceMgmt.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public ResourceMgmtDto updateResourceMgmt(final ResourceMgmtDto resourceMgmtDto, final String lang)
			throws Exception {

		ResourceMgmtDto resultDto = new ResourceMgmtDto();

		ResourceMgmt resourceMgmt = resourceMgmtRepository.findById(resourceMgmtDto.getId());

		if (resourceMgmt == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "ResourceMgmt Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		
		String errorMessage = this.checkDuplicate(resourceMgmtDto, false);
		if (errorMessage != null && !errorMessage.isEmpty()) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { errorMessage }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		resourceMgmt.setEmployeeId(resourceMgmtDto.getEmployeeId());
		resourceMgmt.setEmployeeIdByHR(resourceMgmtDto.getEmployeeIdByHR());
		resourceMgmt.setWorkOrderNumber(resourceMgmtDto.getWorkOrderNumber());

		resourceMgmt.setFirstName(resourceMgmtDto.getFirstName());
		resourceMgmt.setLastName(resourceMgmtDto.getLastName());
		resourceMgmt.setEmail(resourceMgmtDto.getEmail());
		resourceMgmt.setContactNumber(resourceMgmtDto.getContactNumber());
		resourceMgmt.setSex(resourceMgmtDto.getSex());
		resourceMgmt.setDesignation(resourceMgmtDto.getDesignation());
		resourceMgmt.setSkillSet(resourceMgmtDto.getSkillSet());

		resourceMgmt.setProjectAllocation(resourceMgmtDto.getProjectAllocation());
		resourceMgmt.setEmploymentType(resourceMgmtDto.getEmploymentType());
		resourceMgmt.setExperience(resourceMgmtDto.getExperience());

		resourceMgmt.setProjectId(resourceMgmtDto.getProjectId());
		resourceMgmt.setBuId(resourceMgmtDto.getBuId());
		resourceMgmt.setCustomerId(resourceMgmtDto.getCustomerId());

		resourceMgmt.setUpdatedBy(resourceMgmtDto.getUpdatedBy());
		resourceMgmt.setUpdatedDateTime(LocalDateTime.now());

		resourceMgmtRepository.save(resourceMgmt);

		resultDto.setId(resourceMgmt.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public ResourceMgmtDto updateResourceMgmtStatus(final ResourceMgmtDto resourceMgmtDto, final String lang)
			throws Exception {

		ResourceMgmtDto resultDto = new ResourceMgmtDto();

		ResourceMgmt resourceMgmt = resourceMgmtRepository.findById(resourceMgmtDto.getId());

		if (resourceMgmt == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "ResourceMgmt Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		resourceMgmt.setResourceStatus(resourceMgmtDto.getResourceStatus());
		resourceMgmt.setRemarks(resourceMgmtDto.getRemarks());
		resourceMgmt.setEffectiveDate(resourceMgmtDto.getEffectiveDate());

		resourceMgmt.setUpdatedBy(resourceMgmtDto.getUpdatedBy());
		resourceMgmt.setUpdatedDateTime(LocalDateTime.now());

		resourceMgmtRepository.save(resourceMgmt);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	/**
	 * @param resourceMgmtDto
	 * @param isAdd
	 * @return
	 * @throws Exception
	 */
	private String checkDuplicate(final ResourceMgmtDto resourceMgmtDto, boolean isAdd) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(new Criteria().orOperator(Criteria.where("employeeId").is(resourceMgmtDto.getEmployeeId()),
				Criteria.where("employeeIdByHR").regex("(?i).*" + resourceMgmtDto.getEmployeeIdByHR() + ".*"),
//				Criteria.where("workOrderNumber").is(resourceMgmtDto.getWorkOrderNumber()),
				Criteria.where("email").regex("(?i).*" + resourceMgmtDto.getEmail() + ".*")));
//				Criteria.where("contactNumber").is(resourceMgmtDto.getContactNumber())));

		Query searchQuery = new Query();
		searchQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
		List<ResourceMgmt> resourceMgmtListCheck = mongoTemplate.find(searchQuery, ResourceMgmt.class);

		String errorMessage = "";
		if ((isAdd && resourceMgmtListCheck != null && resourceMgmtListCheck.size() > 0)
				|| (resourceMgmtListCheck != null && resourceMgmtListCheck.size() > 0
						&& !resourceMgmtDto.getId().equals(resourceMgmtListCheck.get(0).getId()))) {

			if (resourceMgmtDto.getEmployeeId().equalsIgnoreCase(resourceMgmtListCheck.get(0).getEmployeeId())) {
				errorMessage = "Employee Id";
			} else if (resourceMgmtDto.getEmployeeIdByHR()
					.equalsIgnoreCase(resourceMgmtListCheck.get(0).getEmployeeIdByHR())) {
				errorMessage = "EmployeeId By HR";
//			} else if (resourceMgmtDto.getWorkOrderNumber() != null && !resourceMgmtDto.getWorkOrderNumber().isEmpty()
//					&& resourceMgmtDto.getWorkOrderNumber().equals(resourceMgmtListCheck.get(0).getWorkOrderNumber())) {
//				errorMessage = "WorkOrderNumber";
			} else if (resourceMgmtDto.getEmail().equalsIgnoreCase(resourceMgmtListCheck.get(0).getEmail())) {
				errorMessage = "Email";
			} 
//			else if (resourceMgmtDto.getContactNumber().equals(resourceMgmtListCheck.get(0).getContactNumber())) {
//				errorMessage = "Contact Number";
//			}
		}

		return errorMessage;
	}

	@Override
	public List<ResourceMgmtDto> getAllResourceMgmts() throws Exception {

		List<ResourceMgmt> resourceMgmtsList = resourceMgmtRepository
				.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<ResourceMgmtDto> resourceMgmtDtoList = new ArrayList<ResourceMgmtDto>();
		for (ResourceMgmt resourceMgmt : resourceMgmtsList) {
			resourceMgmtDtoList.add(this.getResourceMgmtDto(resourceMgmt));
		}

		return resourceMgmtDtoList;
	}

	@Override
	public List<ResourceMgmtDto> getBenchResources() throws Exception {

		List<ResourceMgmt> resourceMgmtsList = resourceMgmtRepository.findByProjectAllocationAndResourceStatus("No",
				HireProUsConstants.RESOURCE_STATUS_ACTIVE, HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<ResourceMgmtDto> resourceMgmtDtoList = new ArrayList<ResourceMgmtDto>();
		for (ResourceMgmt resourceMgmt : resourceMgmtsList) {
			resourceMgmtDtoList.add(this.getResourceMgmtDto(resourceMgmt));
		}

		return resourceMgmtDtoList;
	}

	@Override
	public List<ResourceMgmtDto> getBenchResourcesByBuId(Long buId) throws Exception {
		List<ResourceMgmt> resourceMgmtsList = resourceMgmtRepository.findByProjectAllocationAndBuIdAndResourceStatus("No", buId,HireProUsConstants.RESOURCE_STATUS_ACTIVE,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<ResourceMgmtDto> resourceMgmtDtoList = new ArrayList<ResourceMgmtDto>();
		for (ResourceMgmt resourceMgmt : resourceMgmtsList) {
			resourceMgmtDtoList.add(this.getResourceMgmtDto(resourceMgmt));
		}

		return resourceMgmtDtoList;
	}

	@Override
	public ResourceMgmtDto getResourceMgmtById(final String resourceMgmtId) throws Exception {

		ResourceMgmt resourceMgmt = resourceMgmtRepository.findById(Long.parseLong(resourceMgmtId));

		return resourceMgmt != null ? this.getResourceMgmtDto(resourceMgmt) : new ResourceMgmtDto();
	}

	@Override
	public void updateProjectAllocationById(final String resourceMgmtId, final Long updatedBy) throws Exception {

		ResourceMgmt resourceMgmt = resourceMgmtRepository.findById(Long.parseLong(resourceMgmtId));

		if (resourceMgmt != null) {
			resourceMgmt.setProjectAllocation("Middle");
			resourceMgmt.setUpdatedBy(updatedBy);
			resourceMgmt.setUpdatedDateTime(LocalDateTime.now());

			resourceMgmtRepository.save(resourceMgmt);
		}
	}

	@Override
	public ResourceMgmtDto updateProjectAllocationById(final String resourceMgmtId, final Long projectId,
			final Long customerId, final Long updatedBy, final String workOrderNumber, final String employeeIdByHR,
			final String email, Long buId) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(new Criteria().orOperator(Criteria.where("employeeIdByHR").is(employeeIdByHR),
//				Criteria.where("workOrderNumber").is(resourceMgmtDto.getWorkOrderNumber()),
				Criteria.where("email").regex("(?i).*" + email + ".*")));

		Query searchQuery = new Query();
		searchQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
		List<ResourceMgmt> resourceMgmtListCheck = mongoTemplate.find(searchQuery, ResourceMgmt.class);

		String errorMessage = "";
		if (resourceMgmtListCheck != null && resourceMgmtListCheck.size() > 0) {

			if (employeeIdByHR.equals(resourceMgmtListCheck.get(0).getEmployeeIdByHR())) {
				errorMessage = "EmployeeId By HR";
//			} else if (resourceMgmtDto.getWorkOrderNumber() != null && !resourceMgmtDto.getWorkOrderNumber().isEmpty()
//					&& resourceMgmtDto.getWorkOrderNumber().equals(resourceMgmtListCheck.get(0).getWorkOrderNumber())) {
//				errorMessage = "WorkOrderNumber";
			} else if (email.equals(resourceMgmtListCheck.get(0).getEmail())) {
				errorMessage = "Email";
//			} else if (resourceMgmtDto.getContactNumber().equals(resourceMgmtListCheck.get(0).getContactNumber())) {
//				errorMessage = "Contact Number";
			}
		}

		ResourceMgmtDto resultDto = new ResourceMgmtDto();

		ResourceMgmt resourceMgmt = resourceMgmtRepository.findById(Long.parseLong(resourceMgmtId));

		if (resourceMgmt == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "ResourceMgmt Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (errorMessage != null && !errorMessage.isEmpty()
				&& !resourceMgmt.getId().equals(resourceMgmtListCheck.get(0).getId())) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { errorMessage }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (resourceMgmt != null) {
			resourceMgmt.setProjectAllocation("No");
			if (projectId != null && !projectId.equals(0L)) {
				resourceMgmt.setProjectAllocation("Yes");
				resourceMgmt.setProjectId(projectId);
			}
			resourceMgmt.setCustomerId(customerId);
			resourceMgmt.setWorkOrderNumber(workOrderNumber);
			resourceMgmt.setEmployeeIdByHR(employeeIdByHR);
			resourceMgmt.setEmail(email);

			if (buId != null && !buId.equals(0L)) {
				resourceMgmt.setBuId(buId);
			}

			resourceMgmt.setUpdatedBy(updatedBy);
			resourceMgmt.setUpdatedDateTime(LocalDateTime.now());

			resourceMgmtRepository.save(resourceMgmt);
		}

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<String> getAllEmployeeIds() throws Exception {

		List<ResourceMgmt> resourceMgmtsList = resourceMgmtRepository
				.findAll(Sort.by(Sort.Direction.ASC, "employeeId"));

		List<String> employeeIds = new ArrayList<String>();
		for (ResourceMgmt resourceMgmt : resourceMgmtsList) {
			if (resourceMgmt.getEmployeeId() != null && !resourceMgmt.getEmployeeId().isEmpty()) {
				employeeIds.add(resourceMgmt.getEmployeeId());
			}
		}

		return employeeIds;
	}

	@Override
	public List<String> getAllWorkOrderNumbers() throws Exception {

		List<ResourceMgmt> resourceMgmtsList = resourceMgmtRepository
				.findAll(Sort.by(Sort.Direction.ASC, "workOrderNumber"));

		List<String> workOrderNumbers = new ArrayList<String>();
		for (ResourceMgmt resourceMgmt : resourceMgmtsList) {
			if (resourceMgmt.getWorkOrderNumber() != null && !resourceMgmt.getWorkOrderNumber().isEmpty()) {
				workOrderNumbers.add(resourceMgmt.getWorkOrderNumber());
			}
		}

		return workOrderNumbers;
	}

	/**
	 * @param resourceMgmt
	 * @return
	 * @throws Exception
	 */
	private ResourceMgmtDto getResourceMgmtDto(final ResourceMgmt resourceMgmt) throws Exception {

		ResourceMgmtDto resourceMgmtDto = new ResourceMgmtDto();
		resourceMgmtDto.setId(resourceMgmt.getId());
		resourceMgmtDto.setEmployeeId(resourceMgmt.getEmployeeId());
		resourceMgmtDto.setEmployeeIdByHR(resourceMgmt.getEmployeeIdByHR());
		resourceMgmtDto.setWorkOrderNumber(resourceMgmt.getWorkOrderNumber());

		resourceMgmtDto.setFirstName(resourceMgmt.getFirstName());
		resourceMgmtDto.setLastName(resourceMgmt.getLastName());
		resourceMgmtDto.setFullName(resourceMgmt.getFirstName() + " " + resourceMgmt.getLastName());
		resourceMgmtDto.setEmail(resourceMgmt.getEmail());
		resourceMgmtDto.setContactNumber(resourceMgmt.getContactNumber());
		resourceMgmtDto.setSex(resourceMgmt.getSex());
		resourceMgmtDto.setDesignation(resourceMgmt.getDesignation());
		resourceMgmtDto.setSkillSet(resourceMgmt.getSkillSet());

		resourceMgmtDto.setProjectAllocation(resourceMgmt.getProjectAllocation());
		resourceMgmtDto.setEmploymentType(resourceMgmt.getEmploymentType());
		resourceMgmtDto.setExperience(resourceMgmt.getExperience());

		resourceMgmtDto.setResourceStatus(resourceMgmt.getResourceStatus());
		resourceMgmtDto.setRemarks(resourceMgmt.getRemarks());
		resourceMgmtDto.setEffectiveDate(resourceMgmt.getEffectiveDate());

		resourceMgmtDto.setBuId(resourceMgmt.getBuId());
		resourceMgmtDto.setBuName(businessUnitService.getBusinessUnitNameById(resourceMgmt.getBuId()));

		if ("Yes".equals(resourceMgmt.getProjectAllocation())) {
			resourceMgmtDto.setProjectId(resourceMgmt.getProjectId());
			resourceMgmtDto.setProjectName(projectService.getProjectNameById(resourceMgmt.getProjectId()));

			resourceMgmtDto.setCustomerId(resourceMgmt.getCustomerId());
			resourceMgmtDto.setCustomerName(customerService.getCustomerNameById(resourceMgmt.getCustomerId()));
		}

		resourceMgmtDto.setCreatedBy(resourceMgmt.getCreatedBy());
		resourceMgmtDto.setCreatedByName(CacheUtil.getUsersMap().get(resourceMgmt.getCreatedBy()));
		resourceMgmtDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(resourceMgmt.getCreatedDateTime()));

		resourceMgmtDto.setUpdatedBy(resourceMgmt.getUpdatedBy());
		resourceMgmtDto.setUpdatedByName(CacheUtil.getUsersMap().get(resourceMgmt.getUpdatedBy()));
		resourceMgmtDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(resourceMgmt.getUpdatedDateTime()));

		return resourceMgmtDto;
	}

	@Override
	public ResourceMgmtDto deleteResourceMgmtById(final String resourceMgmtId) throws Exception {

		resourceMgmtRepository.deleteById(Long.parseLong(resourceMgmtId));

		return new ResourceMgmtDto();
	}

	@Override
	public List<ResourceMgmtDto> searchResourceMgmt(final ResourceMgmtDto resourceMgmtDto) throws Exception {

		List<ResourceMgmtDto> resourceMgmtDtoList = new ArrayList<ResourceMgmtDto>();

		final List<Criteria> criteriaList = new ArrayList<>();
		if (resourceMgmtDto.getFullName() != null && !resourceMgmtDto.getFullName().isEmpty()) {

			AggregationOperation project = Aggregation.project(ResourceMgmt.class)
					.andExpression("concat(firstName,' ', lastName)").as("fullName");
			AggregationOperation match = Aggregation
					.match(Criteria.where("fullName").regex("(?i).*" + resourceMgmtDto.getFullName() + ".*"));
			Aggregation aggregation = Aggregation.newAggregation(project, match);
			List<ResourceMgmt> resourceMgmtList = mongoTemplate
					.aggregate(aggregation, ResourceMgmt.class, ResourceMgmt.class).getMappedResults();

			List<Long> resourceMgmtIds = new ArrayList<Long>();
			for (ResourceMgmt resourceMgmt : resourceMgmtList) {
				resourceMgmtIds.add(resourceMgmt.getId());
			}

			if (resourceMgmtIds.size() > 0) {
				criteriaList.add(Criteria.where("id").in(resourceMgmtIds));
			} else {
				return new ArrayList<ResourceMgmtDto>();
			}
		}
		if (resourceMgmtDto.getEmail() != null && !resourceMgmtDto.getEmail().isEmpty()) {
			criteriaList.add(Criteria.where("email").regex("(?i).*" + resourceMgmtDto.getEmail() + ".*"));
		}
		if (resourceMgmtDto.getEmployeeId() != null && !resourceMgmtDto.getEmployeeId().isEmpty()) {
			criteriaList.add(Criteria.where("employeeId").is(resourceMgmtDto.getEmployeeId()));
		}
		if (resourceMgmtDto.getBuId() != null && !resourceMgmtDto.getBuId().equals(0L)) {
			criteriaList.add(Criteria.where("buId").is(resourceMgmtDto.getBuId()));
		}
		if (resourceMgmtDto.getEmployeeIdByHR() != null && !resourceMgmtDto.getEmployeeIdByHR().isEmpty()) {
			criteriaList.add(Criteria.where("employeeIdByHR").is(resourceMgmtDto.getEmployeeIdByHR()));
		}
		if (resourceMgmtDto.getWorkOrderNumber() != null && !resourceMgmtDto.getWorkOrderNumber().isEmpty()) {
			criteriaList.add(Criteria.where("workOrderNumber").is(resourceMgmtDto.getWorkOrderNumber()));
		}
		if (resourceMgmtDto.getResourceStatus() != null && !resourceMgmtDto.getResourceStatus().isEmpty()) {
			criteriaList.add(Criteria.where("resourceStatus").is(resourceMgmtDto.getResourceStatus()));
		}
		if (resourceMgmtDto.getProjectAllocation() != null && !resourceMgmtDto.getProjectAllocation().isEmpty()) {
			criteriaList.add(Criteria.where("projectAllocation").is(resourceMgmtDto.getProjectAllocation()));
		}
		if (resourceMgmtDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(resourceMgmtDto.getFromDateTime()));
		}
		if (resourceMgmtDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(resourceMgmtDto.getToDateTime()));
		}

		List<ResourceMgmt> resourceMgmtsList = new ArrayList<ResourceMgmt>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			resourceMgmtsList = mongoTemplate.find(searchQuery, ResourceMgmt.class);
		}

		for (ResourceMgmt resourceMgmt : resourceMgmtsList) {
			resourceMgmtDtoList.add(this.getResourceMgmtDto(resourceMgmt));
		}

		Comparator<ResourceMgmtDto> compareByUpdatedDateTime = Comparator
				.comparing(ResourceMgmtDto::getUpdatedDateTime);
		resourceMgmtDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return resourceMgmtDtoList;
	}

	@Override
	public byte[] downloadResourceDetails(final List<ResourceMgmtDto> resourceMgmtDtoList, final String lang)
			throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/resource_mgmt.xlsx").getFile();
		try (Workbook resourcesMgmtWB = new XSSFWorkbook(file)) {

			Sheet sheet = resourcesMgmtWB.getSheetAt(0);
			HireProUsDefaultMethods.cleanSheet(sheet);
			int rowNum = 2;
			for (ResourceMgmtDto resourceMgmtDto : resourceMgmtDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(resourceMgmtDto.getEmployeeId());
				dataRow.createCell(2).setCellValue(resourceMgmtDto.getEmployeeIdByHR());
				dataRow.createCell(3).setCellValue(resourceMgmtDto.getWorkOrderNumber());
				dataRow.createCell(4).setCellValue(resourceMgmtDto.getFullName());
				dataRow.createCell(5).setCellValue(resourceMgmtDto.getSex().equals("Male") ? "Male" : "Female");
				dataRow.createCell(6).setCellValue(resourceMgmtDto.getEmail());
				dataRow.createCell(7).setCellValue(resourceMgmtDto.getContactNumber());
				dataRow.createCell(8).setCellValue(resourceMgmtDto.getDesignation());
				dataRow.createCell(9).setCellValue(resourceMgmtDto.getExperience());
				dataRow.createCell(10).setCellValue(resourceMgmtDto.getResourceStatus());
				dataRow.createCell(11).setCellValue(resourceMgmtDto.getProjectAllocation());
				dataRow.createCell(12).setCellValue(resourceMgmtDto.getBuName());
				dataRow.createCell(13).setCellValue(resourceMgmtDto.getProjectName());
				dataRow.createCell(14).setCellValue(resourceMgmtDto.getCustomerName());
				dataRow.createCell(15).setCellValue(resourceMgmtDto.getRemarks());
				dataRow.createCell(16).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			resourcesMgmtWB.write(outputStream);

			resourcesMgmtWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during Resource Management download file", ex);
			return null;
		}
	}

	@Override
	public int getProjectDependencyCount(Long projectId) {

		return resourceMgmtRepository.countByProjectId(projectId);
	}
}