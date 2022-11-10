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
import com.promantus.hireprous.dto.VendorDto;
import com.promantus.hireprous.service.VendorService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Vendors related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class VendorController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(VendorController.class);

	@Value("${download.path}")
	private String downloadsPath;

	@Autowired
	private VendorService vendorService;

	/**
	 * @param vendorDto
	 * @return
	 */
	@PostMapping("/addVendor")
	public VendorDto addVendor(@RequestBody VendorDto vendorDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		VendorDto resultDto = new VendorDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Vendor Name.
			if (vendorDto.getVendorName() == null || vendorDto.getVendorName().isEmpty()) {
				errorParam.append("Vendor Name");
			}
			// Email.
			if (vendorDto.getEmail() == null || vendorDto.getEmail().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Email" : "Email");
			}
			// Contact Number.
			if (vendorDto.getContactNumber() == null || vendorDto.getContactNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Contact Number" : "Contact Number");
			}
			// Location.
			if (vendorDto.getLocation() == null || vendorDto.getLocation().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Location" : "Location");
			}
			// CcEmailIds.
			if (vendorDto.getCcEmailIds() == null || vendorDto.getCcEmailIds().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", CcEmailIds" : "CcEmailIds");
			}
			// VendorPriority.
			if (vendorDto.getVendorPriority() == null || vendorDto.getVendorPriority().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Vendor Priority" : "Vendor Priority");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = vendorService.addVendor(vendorDto, lang);
		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param vendorDto
	 * @return
	 */
	@PutMapping("/updateVendor")
	public VendorDto updateVendor(@RequestBody VendorDto vendorDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		VendorDto resultDto = new VendorDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Id.
			if (vendorDto.getId() == 0) {
				errorParam.append("Id");
			}
			// Vendor Id.
			if (vendorDto.getVendorId() == null || vendorDto.getVendorId().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Vendor Id" : "Vendor Id");
			}
			// Vendor Name.
			if (vendorDto.getVendorName() == null || vendorDto.getVendorName().isEmpty()) {
				errorParam.append("Vendor Name");
			}
			// Email.
			if (vendorDto.getEmail() == null || vendorDto.getEmail().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Email" : "Email");
			}
			// Contact Number.
			if (vendorDto.getContactNumber() == null || vendorDto.getContactNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Contact Number" : "Contact Number");
			}
			// Location.
			if (vendorDto.getLocation() == null || vendorDto.getLocation().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Location" : "Location");
			}
			// CcEmailIds.
			if (vendorDto.getCcEmailIds() == null || vendorDto.getCcEmailIds().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", CcEmailIds" : "CcEmailIds");
			}
			// VendorPriority.
			if (vendorDto.getVendorPriority() == null || vendorDto.getVendorPriority().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Vendor Priority" : "Vendor Priority");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = vendorService.updateVendor(vendorDto, lang);

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
	@GetMapping("/getAllVendors")
	public List<VendorDto> getAllVendors(@RequestHeader(name = "lang", required = false) String lang) {

		List<VendorDto> vendorsDtoList = new ArrayList<VendorDto>();
		try {
			vendorsDtoList = vendorService.getAllVendors();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return vendorsDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getActiveVendors")
	public List<VendorDto> getActiveVendors(@RequestHeader(name = "lang", required = false) String lang) {

		List<VendorDto> vendorsDtoList = new ArrayList<VendorDto>();
		try {
			vendorsDtoList = vendorService.getActiveVendors();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return vendorsDtoList;
	}

	/**
	 * @param vendorId
	 * @return
	 */
	@GetMapping("/getVendor/{vendorId}")
	public VendorDto getVendorById(@PathVariable String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		VendorDto vendorDto = new VendorDto();
		try {
			vendorDto = vendorService.getVendorById(vendorId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return vendorDto;
	}

	/**
	 * @param vendorId
	 * @return
	 */
	@DeleteMapping("/deleteVendorById/{vendorId}")
	public VendorDto deleteVendorById(@PathVariable String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return vendorService.deleteVendorById(vendorId);

		} catch (final Exception e) {

			VendorDto resultDto = new VendorDto();
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
	@PostMapping("/searchVendor")
	public List<VendorDto> searchVendor(@RequestBody VendorDto vendorDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return vendorService.searchVendor(vendorDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<VendorDto>();
	}

	/**
	 * @param vendorDtoList
	 * @param lang
	 * @param response
	 */
	@PutMapping("/downloadVendorDetails")
	public void downloadVendorDetails(@RequestBody List<VendorDto> vendorDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File vendorFile = new File(downloadsPath + "HireProUs_Vendor_details.xlsx");
			FileUtils.writeByteArrayToFile(vendorFile, vendorService.downloadVendorDetails(vendorDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + vendorFile.getName());
			response.setContentLength((int) vendorFile.length());

			inStream = new BufferedInputStream(new FileInputStream(vendorFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			vendorFile.deleteOnExit();

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
	@GetMapping("/getAllVendorIds")
	public List<String> getAllVendorIds(@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return vendorService.getAllVendorIds();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<String>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllVendorNames")
	public List<String> getAllVendorNames(@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return vendorService.getAllVendorNames();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<String>();
	}

	/**
	 * @param vendorDto
	 * @return
	 */
	@PostMapping("/loginVendor")
	public VendorDto loginVendor(@RequestBody(required = true) VendorDto vendorDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		VendorDto resultDto = new VendorDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Vendor Name (VendorId)
			if (vendorDto.getVendorId() == null || vendorDto.getVendorId().isEmpty()) {
				errorParam.append("Vendor Name (VendorId)");
			}
			// Password.
			if (vendorDto.getPassword() == null || vendorDto.getPassword().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Password" : "Password");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = vendorService.loginVendor(vendorDto.getVendorId(), vendorDto.getPassword(), lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param vendorDto
	 * @return
	 */
	@PutMapping("/changePasswordVendor")
	public VendorDto changePasswordVendor(@RequestBody VendorDto vendorDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		VendorDto resultDto = new VendorDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Vendor Id.
			if (vendorDto.getId() == 0) {
				errorParam.append("Vendor Id");
			}
			// Current Password.
			if (vendorDto.getPassword() == null || vendorDto.getPassword().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Current Password" : "Current Password");
			}
			// New Password.
			if (vendorDto.getNewPassword() == null || vendorDto.getNewPassword().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", New Password" : "New Password");
			}
			// Current Password AND New Password.
			if (!(vendorDto.getPassword() == null || vendorDto.getPassword().isEmpty())
					&& !(vendorDto.getNewPassword() == null || vendorDto.getNewPassword().isEmpty())
					&& vendorDto.getPassword().equalsIgnoreCase(vendorDto.getNewPassword())) {
				errorParam.append(errorParam.length() > 0 ? ", Current Password and New Password are SAME"
						: "Current Password and New Password are SAME");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = vendorService.changePasswordVendor(vendorDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param email
	 * @param lang
	 * @return
	 */
	@PutMapping("/checkVendorIdAndSendOtpVendor")
	public VendorDto checkVendorIdAndSendOtpVendor(@RequestBody String vendorId,
			@RequestHeader(name = "lang", required = false) String lang) {

		VendorDto resultDto = new VendorDto();
		try {

			// Mandatory check.
			if (vendorId == null || vendorId.isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(super.getMessage("mandatory.input.param", new String[] { "Vendor Id" }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = vendorService.checkVendorIdAndSendOtpVendor(vendorId, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param email
	 * @param lang
	 * @return
	 */
	@PutMapping("/checkOtpVendor")
	public VendorDto checkOtpVendor(@RequestBody String vendorIdAndOtp,
			@RequestHeader(name = "lang", required = false) String lang) {

		VendorDto resultDto = new VendorDto();
		try {

			// Mandatory check.
			if (vendorIdAndOtp == null || vendorIdAndOtp.split(",")[0].isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(super.getMessage("mandatory.input.param", new String[] { "Vendor Id" }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}
			if (vendorIdAndOtp == null || vendorIdAndOtp.split(",")[1] == null
					|| vendorIdAndOtp.split(",")[1].isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(super.getMessage("mandatory.input.param", new String[] { "OTP" }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			String otpFromCache = CacheUtil.getResetOtpMap().get(vendorIdAndOtp.split(",")[0]);
			if (otpFromCache != null && otpFromCache.equals(vendorIdAndOtp.split(",")[1])) {
				CacheUtil.getResetOtpMap().remove(vendorIdAndOtp.split(",")[0]);

				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
				return resultDto;

			} else {

				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("In-Valid OTP");
				return resultDto;
			}

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}
	}

	/**
	 * @param vendorDto
	 * @return
	 */
	@PutMapping("/resetPasswordVendor")
	public VendorDto resetPasswordVendor(@RequestBody VendorDto vendorDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		VendorDto resultDto = new VendorDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Vendor Id.
			if (vendorDto.getVendorId() == null || vendorDto.getVendorId().isEmpty()) {
				errorParam.append("Vendor Id");
			}
			// New Password.
			if (vendorDto.getNewPassword() == null || vendorDto.getNewPassword().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", New Password" : "New Password");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = vendorService.resetPasswordVendor(vendorDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

}
