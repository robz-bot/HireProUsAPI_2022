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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.BusinessUnitDto;
import com.promantus.hireprous.dto.InterviewPanelDto;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle BusinessUnits related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class BusinessUnitController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(BusinessUnitController.class);

	@Value("${super.admin.password}")
	private String saPassword;

	@Value("${clean.data}")
	private String dataClean;

	@Value("${server.address}")
	private String serverAddress;

	@Value("${download.path}")
	private String downloadsPath;

	@Autowired
	private BusinessUnitService businessUnitService;

	@Autowired
	private CommonService commonService;

	/**
	 * @param businessUnitDto
	 * @return
	 */
	@PostMapping("/addBusinessUnit")
	public BusinessUnitDto addBusinessUnit(@RequestBody BusinessUnitDto businessUnitDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		BusinessUnitDto resultDto = new BusinessUnitDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// BusinessUnit Name.
			if (businessUnitDto.getBusinessUnitName() == null || businessUnitDto.getBusinessUnitName().isEmpty()) {
				errorParam.append("Business Unit Name");
			}
			
			if (businessUnitDto.getManagerId() == 0) {
				errorParam.append("Manager Name");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = businessUnitService.addBusinessUnit(businessUnitDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param buName
	 * @param lang
	 * @return
	 */
	
	@PostMapping("/checkBUName")
	public Boolean checkBUName(@RequestBody String buName, @RequestBody String buShortName, @RequestBody long managerId,
			@RequestHeader(name = "lang", required = false) String lang) {
		try {
			return businessUnitService.checkBUName(buName,managerId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param businessUnitDto
	 * @return
	 */
	@PutMapping("/updateBusinessUnit")
	public BusinessUnitDto updateBusinessUnit(@RequestBody BusinessUnitDto businessUnitDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		BusinessUnitDto resultDto = new BusinessUnitDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// BusinessUnit Id.
			if (businessUnitDto.getId() == 0) {
				errorParam.append("BusinessUnit Id");
			}
			
			if (businessUnitDto.getManagerId() == 0) {
				errorParam.append("Manager Name");
			}
			// BusinessUnit Name.
			if (businessUnitDto.getBusinessUnitName() == null || businessUnitDto.getBusinessUnitName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit Name" : "BusinessUnit Name");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = businessUnitService.updateBusinessUnit(businessUnitDto, lang);
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
	@GetMapping("/getAllBusinessUnits")
	public List<BusinessUnitDto> getAllBusinessUnits(@RequestHeader(name = "lang", required = false) String lang) {

		List<BusinessUnitDto> businessUnitsDtoList = new ArrayList<BusinessUnitDto>();
		try {
			businessUnitsDtoList = businessUnitService.getAllBusinessUnits();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return businessUnitsDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllBUsWithPanel")
	public List<InterviewPanelDto> getAllBUsWithPanel(@RequestHeader(name = "lang", required = false) String lang) {

		List<InterviewPanelDto> interviewPanelsDtoList = new ArrayList<InterviewPanelDto>();
		try {
			interviewPanelsDtoList = businessUnitService.getAllBUsWithPanel();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return interviewPanelsDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getPanelsByBuId/{buId}")
	public List<InterviewPanelDto> getPanelsByBuId(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return businessUnitService.getPanelsByBuId(buId, lang);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<InterviewPanelDto>();
	}

	/**
	 * @param businessUnitId
	 * @return
	 */
	@GetMapping("/getBusinessUnit/{businessUnitId}")
	public BusinessUnitDto getBusinessUnitById(@PathVariable String businessUnitId,
			@RequestHeader(name = "lang", required = false) String lang) {

		BusinessUnitDto businessUnitDto = new BusinessUnitDto();
		try {
			businessUnitDto = businessUnitService.getBusinessUnitById(businessUnitId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return businessUnitDto;
	}

	/**
	 * @param businessUnitId
	 * @return
	 */
	@DeleteMapping("/deleteBusinessUnitById/{businessUnitId}")
	public BusinessUnitDto deleteBusinessUnitById(@PathVariable String businessUnitId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return businessUnitService.deleteBusinessUnitById(businessUnitId, lang);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new BusinessUnitDto();
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchBusinessUnit")
	public List<BusinessUnitDto> searchBusinessUnit(@RequestParam String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<BusinessUnitDto> businessUnitsDtoList = new ArrayList<BusinessUnitDto>();
		try {
			businessUnitsDtoList = businessUnitService.searchBusinessUnit(key);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return businessUnitsDtoList;
	}

	/**
	 * @param businessUnitDto
	 * @param lang
	 * @return
	 */
	@PostMapping("/searchBusinessUnitForDownload")
	public List<BusinessUnitDto> searchBusinessUnitForDownload(@RequestBody BusinessUnitDto businessUnitDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return businessUnitService.searchBusinessUnitForDownload(businessUnitDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<BusinessUnitDto>();
	}

	/**
	 * @param businessUnitDtoList
	 * @param lang
	 * @param response
	 */
	@PutMapping("/downloadBusinessUnitDetails")
	public void downloadBusinessUnitDetails(@RequestBody List<BusinessUnitDto> businessUnitDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File businessUnitFile = new File(downloadsPath + "HireProUs_BusinessUnit_details.xlsx");
			FileUtils.writeByteArrayToFile(businessUnitFile,
					businessUnitService.downloadBusinessUnitDetails(businessUnitDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + businessUnitFile.getName());
			response.setContentLength((int) businessUnitFile.length());

			inStream = new BufferedInputStream(new FileInputStream(businessUnitFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			businessUnitFile.deleteOnExit();

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
	 * @param passCode
	 * @param lang
	 * @return
	 */
	@PostMapping("/truncateData")
	public BusinessUnitDto truncateData(@RequestBody String passCode,
			@RequestHeader(name = "lang", required = false) String lang) {

		BusinessUnitDto resultDto = new BusinessUnitDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Pass Code.
			if (passCode == null || passCode.isEmpty()) {
				errorParam.append("Pass Code");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						this.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			if ("0".equals(dataClean) || serverAddress.equals("52.73.21.79")) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(this.getMessage("invalid", new String[] { "Pass Code" }, lang));

				logger.info("*****" + resultDto.getMessage());
				return resultDto;
			}
			if (!passCode.equals(HireProUsUtil.decrypt(saPassword))) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(this.getMessage("invalid", new String[] { "Pass Code" }, lang));

				logger.info("*****" + resultDto.getMessage());
				return resultDto;
			}

			commonService.truncateData();

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			return resultDto;
		}

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}
}
