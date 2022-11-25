/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.ResourceMgmtDto;
import com.promantus.hireprous.service.ResourceMgmtService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle ResourceMgmts related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ResourceMgmtController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(ResourceMgmtController.class);

	@Value("${download.path}")
	private String downloadsPath;

	@Autowired
	private ResourceMgmtService resourceMgmtService;

	/**
	 * @param resourceMgmtDto
	 * @return
	 */
	@PostMapping("/addResourceMgmt")
	public ResourceMgmtDto addResourceMgmt(@RequestBody ResourceMgmtDto resourceMgmtDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		ResourceMgmtDto resultDto = new ResourceMgmtDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Employee Id.
			if (resourceMgmtDto.getEmployeeId() == null || resourceMgmtDto.getEmployeeId().isEmpty()) {
				errorParam.append("Employee Id");
			}
			// Work Order Number.
			if (resourceMgmtDto.getWorkOrderNumber() == null || resourceMgmtDto.getWorkOrderNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Work Order Number" : "Work Order Number");
			}
			// First Name.
			if (resourceMgmtDto.getFirstName() == null || resourceMgmtDto.getFirstName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", First Name" : "First Name");
			}
			// Last Name.
			if (resourceMgmtDto.getLastName() == null || resourceMgmtDto.getLastName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Last Name" : "Last Name");
			}
			// Contact Number.
			if (resourceMgmtDto.getContactNumber() == null || resourceMgmtDto.getContactNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Contact Number" : "Contact Number");
			}
			// Email.
			if (resourceMgmtDto.getEmail() == null || resourceMgmtDto.getEmail().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Email" : "Email");
			}
			// Gender.
			if (resourceMgmtDto.getSex() == null || resourceMgmtDto.getSex().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Gender" : "Gender");
			}
			// SkillSet.
			if (resourceMgmtDto.getSkillSet() == null || resourceMgmtDto.getSkillSet().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", SkillSet" : "SkillSet");
			}
			// BusinessUnit Id.
			if (resourceMgmtDto.getBuId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit" : "BusinessUnit");
			}
			// Designation.
			if (resourceMgmtDto.getDesignation() == null || resourceMgmtDto.getDesignation().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Designation" : "Designation");
			}
			// Employment Type.
			if (resourceMgmtDto.getEmploymentType() == null || resourceMgmtDto.getEmploymentType().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Employment Type" : "Employment Type");
			}
			// Experience.
			if (resourceMgmtDto.getExperience() == null || resourceMgmtDto.getExperience().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Experience" : "Experience");
			}
			// Project Allocation
			if (resourceMgmtDto.getProjectAllocation() == null || resourceMgmtDto.getProjectAllocation().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Project Allocation" : "Project Allocation");
			} else if (resourceMgmtDto.getProjectAllocation().equals("Yes")) {
				// Customer Id.
				if (resourceMgmtDto.getCustomerId() == 0) {
					errorParam.append(errorParam.length() > 0 ? ", Customer" : "Customer");
				}
				// Project Id.
				if (resourceMgmtDto.getProjectId() == 0) {
					errorParam.append(errorParam.length() > 0 ? ", Project" : "Project");
				}
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = resourceMgmtService.addResourceMgmt(resourceMgmtDto, lang);
		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param resourceMgmtDto
	 * @return
	 */
	@PutMapping("/updateResourceMgmt")
	public ResourceMgmtDto updateResourceMgmt(@RequestBody ResourceMgmtDto resourceMgmtDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		ResourceMgmtDto resultDto = new ResourceMgmtDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// ResourceMgmt Id.
			if (resourceMgmtDto.getId() == 0) {
				errorParam.append("ResourceMgmt Id");
			}
			// Employee Id.
			if (resourceMgmtDto.getEmployeeId() == null || resourceMgmtDto.getEmployeeId().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Employee Id" : "Employee Id");
			}
			// Work Order Number.
			if (resourceMgmtDto.getWorkOrderNumber() == null || resourceMgmtDto.getWorkOrderNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Work Order Number" : "Work Order Number");
			}
			// First Name.
			if (resourceMgmtDto.getFirstName() == null || resourceMgmtDto.getFirstName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", First Name" : "First Name");
			}
			// Last Name.
			if (resourceMgmtDto.getLastName() == null || resourceMgmtDto.getLastName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Last Name" : "Last Name");
			}
			// Contact Number.
			if (resourceMgmtDto.getContactNumber() == null || resourceMgmtDto.getContactNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Contact Number" : "Contact Number");
			}
			// Email.
			if (resourceMgmtDto.getEmail() == null || resourceMgmtDto.getEmail().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Email" : "Email");
			}
			// Gender.
			if (resourceMgmtDto.getSex() == null || resourceMgmtDto.getSex().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Gender" : "Gender");
			}
			// SkillSet.
			if (resourceMgmtDto.getSkillSet() == null || resourceMgmtDto.getSkillSet().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", SkillSet" : "SkillSet");
			}
			// BusinessUnit Id.
			if (resourceMgmtDto.getBuId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit" : "BusinessUnit");
			}
			// Designation.
			if (resourceMgmtDto.getDesignation() == null || resourceMgmtDto.getDesignation().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Designation" : "Designation");
			}
			// Employment Type.
			if (resourceMgmtDto.getEmploymentType() == null || resourceMgmtDto.getEmploymentType().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Employment Type" : "Employment Type");
			}
			// Experience.
			if (resourceMgmtDto.getExperience() == null || resourceMgmtDto.getExperience().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Experience" : "Experience");
			}
			// Project Allocation
			if (resourceMgmtDto.getProjectAllocation() == null || resourceMgmtDto.getProjectAllocation().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Project Allocation" : "Project Allocation");
			} else if (resourceMgmtDto.getProjectAllocation().equals("Yes")) {
				// Project Id.
				if (resourceMgmtDto.getProjectId() == 0) {
					errorParam.append(errorParam.length() > 0 ? ", Project" : "Project");
				}
				// Customer Id.
				if (resourceMgmtDto.getCustomerId() == 0) {
					errorParam.append(errorParam.length() > 0 ? ", Customer" : "Customer");
				}
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = resourceMgmtService.updateResourceMgmt(resourceMgmtDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param resourceMgmtDto
	 * @return
	 */
	@PutMapping("/updateResourceMgmtStatus")
	public ResourceMgmtDto updateResourceMgmtStatus(@RequestBody ResourceMgmtDto resourceMgmtDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		ResourceMgmtDto resultDto = new ResourceMgmtDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// ResourceMgmt Id.
			if (resourceMgmtDto.getId() == 0) {
				errorParam.append("ResourceMgmt Id");
			}
			// Resource Status.
			if (resourceMgmtDto.getResourceStatus() == null || resourceMgmtDto.getResourceStatus().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Resource Status" : "Resource Status");
			}
			// Remarks.
			if (resourceMgmtDto.getRemarks() == null || resourceMgmtDto.getRemarks().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Remarks" : "Remarks");
			}
			// Effective Date.
			if (resourceMgmtDto.getEffectiveDate() == null) {
				errorParam.append(errorParam.length() > 0 ? ", Effective Date" : "Effective Date");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = resourceMgmtService.updateResourceMgmtStatus(resourceMgmtDto, lang);

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
	@GetMapping("/getAllResourceMgmts")
	public List<ResourceMgmtDto> getAllResourceMgmts(@RequestHeader(name = "lang", required = false) String lang) {

		List<ResourceMgmtDto> resourceMgmtsDtoList = new ArrayList<ResourceMgmtDto>();
		try {
			resourceMgmtsDtoList = resourceMgmtService.getAllResourceMgmts();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resourceMgmtsDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getBenchResources")
	public List<ResourceMgmtDto> getBenchResources(@RequestHeader(name = "lang", required = false) String lang) {

		List<ResourceMgmtDto> resourceMgmtsDtoList = new ArrayList<ResourceMgmtDto>();
		try {
			resourceMgmtsDtoList = resourceMgmtService.getBenchResources();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resourceMgmtsDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getBenchResourcesByBuId/{buId}")
	public List<ResourceMgmtDto> getBenchResourcesByBuId(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<ResourceMgmtDto> resourceMgmtsDtoList = new ArrayList<ResourceMgmtDto>();
		try {
			resourceMgmtsDtoList = resourceMgmtService.getBenchResourcesByBuId(Long.parseLong(buId));
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resourceMgmtsDtoList;
	}

	/**
	 * @param resourceMgmtId
	 * @return
	 */
	@GetMapping("/getResourceMgmt/{resourceMgmtId}")
	public ResourceMgmtDto getResourceMgmtById(@PathVariable String resourceMgmtId,
			@RequestHeader(name = "lang", required = false) String lang) {

		ResourceMgmtDto resourceMgmtDto = new ResourceMgmtDto();
		try {
			resourceMgmtDto = resourceMgmtService.getResourceMgmtById(resourceMgmtId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resourceMgmtDto;
	}

	/**
	 * @param resourceMgmtId
	 * @return
	 */
	@DeleteMapping("/deleteResourceMgmtById/{resourceMgmtId}")
	public ResourceMgmtDto deleteResourceMgmtById(@PathVariable String resourceMgmtId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return resourceMgmtService.deleteResourceMgmtById(resourceMgmtId);

		} catch (final Exception e) {

			ResourceMgmtDto resultDto = new ResourceMgmtDto();
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(HireProUsUtil.getErrorMessage(e));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
	}

	/**
	 * @param key
	 * @return
	 */
	@PostMapping("/searchResourceMgmt")
	public List<ResourceMgmtDto> searchResourceMgmt(@RequestBody ResourceMgmtDto resourceMgmtDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return resourceMgmtService.searchResourceMgmt(resourceMgmtDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<ResourceMgmtDto>();
	}

	/**
	 * @param resourceMgmtDtoList
	 * @param lang
	 * @param response
	 */
	@PutMapping("/downloadResourceDetails")
	public void downloadResourceDetails(@RequestBody List<ResourceMgmtDto> resourceMgmtDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File resourceFile = new File(downloadsPath + "HireProUs_Resources_details.xlsx");
			FileUtils.writeByteArrayToFile(resourceFile,
					resourceMgmtService.downloadResourceDetails(resourceMgmtDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resourceFile.getName());
			response.setContentLength((int) resourceFile.length());

			inStream = new BufferedInputStream(new FileInputStream(resourceFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			resourceFile.deleteOnExit();

		} catch (final Exception e) {

			logger.error(HireProUsUtil.getErrorMessage(e));

		} finally {
			try {
				if (outStream != null) {
					outStream.flush();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				logger.error(HireProUsUtil.getErrorMessage(e));
			}
		}
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllEmployeeIds")
	public List<String> getAllEmployeeIds(@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return resourceMgmtService.getAllEmployeeIds();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<String>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllWorkOrderNumbers")
	public List<String> getAllWorkOrderNumbers(@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return resourceMgmtService.getAllWorkOrderNumbers();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<String>();
	}
}
