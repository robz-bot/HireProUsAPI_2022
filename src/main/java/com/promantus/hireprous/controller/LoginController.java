/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.service.LoginService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle User login related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class LoginController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Value("${super.admin.username}")
	private String saUserName;

	@Autowired
	private LoginService loginService;

	/**
	 * @param userDto
	 * @return
	 */
	@PostMapping("/login")
	public UserDto login(@RequestBody(required = true) UserDto userDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		UserDto resultDto = new UserDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// User Name (EmailConfDto).
			if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
				errorParam.append("User Name");
			}
			// Password.
			if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Password" : "Password");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			if (userDto.getEmail().equals(HireProUsUtil.decrypt(saUserName))) {
				resultDto = loginService.loginsa(userDto.getEmail(), userDto.getPassword(), lang);
			} else {
				resultDto = loginService.login(userDto.getEmail(), userDto.getPassword(), lang);
			}
		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param userDto
	 * @return
	 */
	@PutMapping("/updateProfile")
	public UserDto updateProfile(@RequestBody UserDto userDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		UserDto resultDto = new UserDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// User Id.
			if (userDto.getId() == 0) {
				errorParam.append("User Id");
			}
			// User Name. (EmailConfDto)
			if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", User Name (EmailConfDto)" : "User Name (EmailConfDto)");
			}
			// First Name.
			if (userDto.getFirstName() == null || userDto.getFirstName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", First Name" : "First Name");
			}
			// Last Name.
			if (userDto.getLastName() == null || userDto.getLastName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Last Name" : "Last Name");
			}
			// Gender.
			if (userDto.getSex() == null || !(userDto.getSex().equals("0") || userDto.getSex().equals("1"))) {
				errorParam.append(errorParam.length() > 0 ? ", Gender" : "Gender");
			}
			// BusninessUnit Id.
			if (userDto.getBusinessUnitId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", BusinessUnit Id" : "BusinessUnit Id");
			}
			// Role Id.
			if (userDto.getRoleId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Role Id" : "Role Id");
			}
			// Designation.
			if (userDto.getDesignation() == null || userDto.getDesignation().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Designation" : "Designation");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = loginService.updateProfile(userDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param userDto
	 * @return
	 */
	@PutMapping("/changePassword")
	public UserDto changePassword(@RequestBody UserDto userDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		UserDto resultDto = new UserDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// User Id.
			if (userDto.getId() == 0) {
				errorParam.append("User Id");
			}
			// Current Password.
			if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Current Password" : "Current Password");
			}
			// New Password.
			if (userDto.getNewPassword() == null || userDto.getNewPassword().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", New Password" : "New Password");
			}
			// Current Password AND New Password.
			if (!(userDto.getPassword() == null || userDto.getPassword().isEmpty())
					&& !(userDto.getNewPassword() == null || userDto.getNewPassword().isEmpty())
					&& userDto.getPassword().equalsIgnoreCase(userDto.getNewPassword())) {
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

			resultDto = loginService.changePassword(userDto, lang);

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
	@PutMapping("/checkEmailAndSendOtp")
	public UserDto checkEmailAndSendOtp(@RequestBody String email,
			@RequestHeader(name = "lang", required = false) String lang) {

		UserDto resultDto = new UserDto();
		try {

			// Mandatory check.
			if (email == null || email.isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(super.getMessage("mandatory.input.param", new String[] { "Email" }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = loginService.checkEmailAndSendOtp(email, lang);

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
	@PutMapping("/checkOtp")
	public UserDto checkOtp(@RequestBody String emailAndOtp,
			@RequestHeader(name = "lang", required = false) String lang) {

		UserDto resultDto = new UserDto();
		try {

			// Mandatory check.
			if (emailAndOtp == null || emailAndOtp.split(",")[0].isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(super.getMessage("mandatory.input.param", new String[] { "Email" }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}
			if (emailAndOtp == null || emailAndOtp.split(",")[1] == null || emailAndOtp.split(",")[1].isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(super.getMessage("mandatory.input.param", new String[] { "OTP" }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			String otpFromCache = CacheUtil.getResetOtpMap().get(emailAndOtp.split(",")[0]);
			if (otpFromCache != null && otpFromCache.equals(emailAndOtp.split(",")[1])) {
				CacheUtil.getResetOtpMap().remove(emailAndOtp.split(",")[0]);

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
	 * @param userDto
	 * @return
	 */
	@PutMapping("/resetPassword")
	public UserDto resetPassword(@RequestBody UserDto userDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		UserDto resultDto = new UserDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Email.
			if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
				errorParam.append("Email");
			}
			// New Password.
			if (userDto.getNewPassword() == null || userDto.getNewPassword().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", New Password" : "New Password");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = loginService.resetPassword(userDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}
}
