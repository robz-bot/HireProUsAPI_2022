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
import com.promantus.hireprous.dto.BusinessUnitDto;
import com.promantus.hireprous.dto.InterviewPanelDto;
import com.promantus.hireprous.entity.BusinessUnit;
import com.promantus.hireprous.repository.BusinessUnitRepository;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.EmailConfigurationService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.service.ProjectService;
import com.promantus.hireprous.service.UserService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {

	private static final Logger logger = LoggerFactory.getLogger(BusinessUnitServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	UserService userService;

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	ProjectService projectService;

	@Autowired
	EmailConfigurationService emailConfService;

	@Autowired
	BusinessUnitRepository businessUnitRepository;

	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public BusinessUnitDto addBusinessUnit(final BusinessUnitDto businessUnitDto, String lang) throws Exception {

		BusinessUnitDto resultDto = new BusinessUnitDto();
		if (this.checkBUName(businessUnitDto.getBusinessUnitName(), businessUnitDto.getManagerId())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto
					.setMessage(commonService.getMessage("already.exists", new String[] { "BusinessUnit Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		BusinessUnit businessUnit = new BusinessUnit();
		businessUnit.setId(commonService.nextSequenceNumber());
		businessUnit.setBusinessUnitName(businessUnitDto.getBusinessUnitName());
		businessUnit.setBuShortName(businessUnitDto.getBuShortName());
		businessUnit.setManagerId(businessUnitDto.getManagerId());
		businessUnit.setCreatedBy(businessUnitDto.getCreatedBy());
		businessUnit.setUpdatedBy(businessUnitDto.getUpdatedBy());

		businessUnit.setCreatedDateTime(LocalDateTime.now());
		businessUnit.setUpdatedDateTime(LocalDateTime.now());

		businessUnitRepository.save(businessUnit);

		CacheUtil.getBusMap().put(businessUnit.getId(), businessUnit.getBusinessUnitName());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public BusinessUnitDto updateBusinessUnit(final BusinessUnitDto businessUnitDto, String lang) throws Exception {

		BusinessUnitDto resultDto = new BusinessUnitDto();

		BusinessUnit businessUnit = businessUnitRepository.findById(businessUnitDto.getId());

		if (businessUnit == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "BusinessUnit Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (this.checkBUName(businessUnitDto.getBusinessUnitName(), businessUnitDto.getManagerId())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto
					.setMessage(commonService.getMessage("already.exists", new String[] { "BusinessUnit Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		businessUnit.setBusinessUnitName(businessUnitDto.getBusinessUnitName());
		businessUnit.setBuShortName(businessUnitDto.getBuShortName());
		businessUnit.setManagerId(businessUnitDto.getManagerId());
		businessUnit.setUpdatedBy(businessUnitDto.getUpdatedBy());
		businessUnit.setUpdatedDateTime(LocalDateTime.now());

		businessUnitRepository.save(businessUnit);

		CacheUtil.getBusMap().remove(businessUnit.getId());
		CacheUtil.getBusMap().put(businessUnit.getId(), businessUnit.getBusinessUnitName());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public Boolean checkBUName(String buName, long managerId) throws Exception {

//		if (businessUnitRepository.getBusinessUnitByBusinessUnitNameAndManagerIdIgnoreCase(buName,managerId) != null) {
//		Bug fixed - Unable to add and edit in Business unit - added on 05/11/2022
		if (businessUnitRepository.getBusinessUnitByBusinessUnitNameAndManagerId(buName,managerId) != null) {
			return true;
		}

		return false;
	}

	@Override
	public List<BusinessUnitDto> getAllBusinessUnits() throws Exception {

		List<BusinessUnitDto> businessUnitDtoList = new ArrayList<BusinessUnitDto>();

		List<BusinessUnit> businessUnitsList = businessUnitRepository
				.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (BusinessUnit businessUnit : businessUnitsList) {
			businessUnitDtoList.add(this.getBusinessUnitDto(businessUnit));
		}

		return businessUnitDtoList;
	}

	@Override
	public List<InterviewPanelDto> getAllBUsWithPanel() throws Exception {

		List<InterviewPanelDto> interviewPanelDtoList = new ArrayList<InterviewPanelDto>();

		List<BusinessUnit> businessUnitsList = businessUnitRepository
				.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (BusinessUnit businessUnit : businessUnitsList) {

			InterviewPanelDto interviewPanelDto = new InterviewPanelDto();

			interviewPanelDto.setBusinessUnitId(businessUnit.getId());
			interviewPanelDto.setBusinessUnitName(businessUnit.getBusinessUnitName());

			interviewPanelDto
					.setInterviewPanelList(userService.getPanelUsersStringByBusinessUnitId(businessUnit.getId() + ""));

			interviewPanelDtoList.add(interviewPanelDto);
		}

		return interviewPanelDtoList;
	}

	@Override
	public List<InterviewPanelDto> getPanelsByBuId(final String buId, final String lang) throws Exception {

		List<InterviewPanelDto> interviewPanelDtoList = new ArrayList<InterviewPanelDto>();

		BusinessUnit businessUnit = businessUnitRepository.findById(Long.parseLong(buId));

		if (businessUnit != null) {
			InterviewPanelDto interviewPanelDto = new InterviewPanelDto();

			interviewPanelDto.setBusinessUnitId(businessUnit.getId());
			interviewPanelDto.setBusinessUnitName(businessUnit.getBusinessUnitName());

			interviewPanelDto
					.setInterviewPanelList(userService.getPanelUsersStringByBusinessUnitId(businessUnit.getId() + ""));

			interviewPanelDtoList.add(interviewPanelDto);
		}

		return interviewPanelDtoList;
	}

	@Override
	public BusinessUnitDto getBusinessUnitById(final String businessUnitId) throws Exception {

		BusinessUnit businessUnit = businessUnitRepository.findById(Long.parseLong(businessUnitId));

		return businessUnit != null ? this.getBusinessUnitDto(businessUnit) : new BusinessUnitDto();
	}

	@Override
	public String getBusinessUnitNameById(final long businessUnitId) throws Exception {

		BusinessUnit businessUnit = businessUnitRepository.findById(businessUnitId);

		return businessUnit != null ? businessUnit.getBusinessUnitName() : "";
	}

	/**
	 * @param businessUnit
	 * @return
	 * @throws Exception
	 */
	private BusinessUnitDto getBusinessUnitDto(final BusinessUnit businessUnit) throws Exception {

		BusinessUnitDto businessUnitDto = new BusinessUnitDto();

		businessUnitDto.setId(businessUnit.getId());
		businessUnitDto.setBusinessUnitName(businessUnit.getBusinessUnitName());
		businessUnitDto.setBuShortName(businessUnit.getBuShortName());

		businessUnitDto.setCreatedBy(businessUnit.getCreatedBy());
		businessUnitDto.setCreatedByName(CacheUtil.getUsersMap().get(businessUnit.getCreatedBy()));
		businessUnitDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(businessUnit.getCreatedDateTime()));
		businessUnitDto.setManagerId(businessUnit.getManagerId());
		businessUnitDto.setManagerName(CacheUtil.getUsersMap().get(businessUnit.getManagerId()));

		businessUnitDto.setUpdatedBy(businessUnit.getUpdatedBy());
		businessUnitDto.setUpdatedByName(CacheUtil.getUsersMap().get(businessUnit.getUpdatedBy()));
		businessUnitDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(businessUnit.getUpdatedDateTime()));

		return businessUnitDto;
	}

	@Override
	public BusinessUnitDto deleteBusinessUnitById(final String businessUnitId, final String lang) throws Exception {

		BusinessUnitDto resultDto = new BusinessUnitDto();

		BusinessUnit businessUnit = businessUnitRepository.findById(Long.parseLong(businessUnitId));

		if (businessUnit == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "BusinessUnit Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		boolean hasDependency = false;
		if (userService.getBUDependencyCount(businessUnit.getId()) > 0) {
			hasDependency = true;
		}
		if (jobRequestService.getBUDependencyCount(businessUnit.getId()) > 0) {
			hasDependency = true;
		}
		if (projectService.getBUDependencyCount(businessUnit.getId()) > 0) {
			hasDependency = true;
		}
		if (emailConfService.getBUDependencyCount(businessUnit.getId()) > 0) {
			hasDependency = true;
		}

		if (hasDependency) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("cannot.delete",
					new String[] { "Business Unit is used in other functionalities. So" }, null));
		} else {
			businessUnitRepository.deleteById(Long.parseLong(businessUnitId));
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);

			CacheUtil.getBusMap().remove(businessUnit.getId());
		}

		return resultDto;
	}

	@Override
	public List<BusinessUnitDto> searchBusinessUnit(final String keyword) throws Exception {

		List<BusinessUnitDto> businessUnitDtoList = new ArrayList<BusinessUnitDto>();

		List<BusinessUnit> businessUnitsList = businessUnitRepository
				.findByBusinessUnitNameRegex("(?i).*" + keyword + ".*", HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (BusinessUnit businessUnit : businessUnitsList) {
			businessUnitDtoList.add(this.getBusinessUnitDto(businessUnit));
		}

		return businessUnitDtoList;
	}
	
	@Override
	public List<BusinessUnitDto> searchBusinessUnitForDownload(BusinessUnitDto businessUnitDto) throws Exception {
	
		List<BusinessUnitDto> businessUnitDtoList = new ArrayList<BusinessUnitDto>();

		final List<Criteria> criteriaList = new ArrayList<>();
		
		if (businessUnitDto.getBusinessUnitName() != null && !businessUnitDto.getBusinessUnitName().isEmpty()) {
			criteriaList.add(Criteria.where("businessUnitName").regex("(?i).*" + businessUnitDto.getBusinessUnitName() + ".*"));
		}
		if (businessUnitDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(businessUnitDto.getFromDateTime()));
		}
		if (businessUnitDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(businessUnitDto.getToDateTime()));
		}

		List<BusinessUnit> businessUnitList = new ArrayList<BusinessUnit>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			businessUnitList = mongoTemplate.find(searchQuery, BusinessUnit.class);
		}

		for (BusinessUnit businessUnit : businessUnitList) {
			businessUnitDtoList.add(this.getBusinessUnitDto(businessUnit));
		}

		Comparator<BusinessUnitDto> compareByUpdatedDateTime = Comparator
				.comparing(BusinessUnitDto::getUpdatedDateTime);
		businessUnitDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return businessUnitDtoList;
	}

	@Override
	public byte[] downloadBusinessUnitDetails(List<BusinessUnitDto> businessUnitDtoList, String lang) throws Exception {
		
		File file = resourceLoader.getResource("classpath:excel-templates/businessUnit_details.xlsx").getFile();
		try (Workbook businessUnitDetailsWB = new XSSFWorkbook(file)) {

			Sheet sheet = businessUnitDetailsWB.getSheetAt(0);
			HireProUsDefaultMethods.cleanSheet(sheet);
			int rowNum = 2;
			for (BusinessUnitDto businessUnitDto : businessUnitDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(businessUnitDto.getBusinessUnitName());
				dataRow.createCell(2).setCellValue(businessUnitDto.getCreatedByName());
				dataRow.createCell(3).setCellValue(businessUnitDto.getCreatedDateTime().toLocalDate().toString());
				dataRow.createCell(4).setCellValue(businessUnitDto.getUpdatedByName());
				dataRow.createCell(5).setCellValue(businessUnitDto.getUpdatedDateTime().toLocalDate().toString());
				dataRow.createCell(6).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			businessUnitDetailsWB.write(outputStream);

			businessUnitDetailsWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during Business Unit Details download file", ex);
			return null;
		}
	}

}
