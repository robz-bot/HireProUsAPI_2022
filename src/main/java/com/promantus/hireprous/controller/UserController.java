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
import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.dto.UserSearchDto;
import com.promantus.hireprous.service.UserService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Users related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class UserController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * @param userDto
	 * @return
	 */
	@PostMapping("/addUser")
	public UserDto addUser(@RequestBody UserDto userDto, @RequestHeader(name = "lang", required = false) String lang) {

		UserDto resultDto = new UserDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// User Name. (EmailConfDto)
			if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
				errorParam.append("User Name (EmailConfDto)");
			}
			// First Name.
			if (userDto.getFirstName() == null || userDto.getFirstName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", First Name" : "First Name");
			}
			// Last Name.
			if (userDto.getLastName() == null || userDto.getLastName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Last Name" : "Last Name");
			}
			// Contact Number.
			if (userDto.getContactNumber() == null || userDto.getContactNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Contact Number" : "Contact Number");
			}
			if (userDto.getManagerId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Manager Id" : "Manager Id");
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

			resultDto = userService.addUser(userDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @param userName
	 * @param lang
	 * @return
	 */
	@PostMapping("/checkUserName")
	public Boolean checkUserName(@RequestBody String userName,
			@RequestHeader(name = "lang", required = false) String lang) {
		try {
			return userService.checkUserName(userName);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param userDto
	 * @return
	 */
	@PutMapping("/updateUser")
	public UserDto updateUser(@RequestBody UserDto userDto,
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
			// Contact Number.
			if (userDto.getContactNumber() == null || userDto.getContactNumber().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Contact Number" : "Contact Number");
				
			}
			if (userDto.getManagerId() == 0) {
				errorParam.append(errorParam.length() > 0 ? ", Manager Id" : "Manager Id");
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

			resultDto = userService.updateUser(userDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllUsers")
	public List<UserDto> getAllUsers(@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return userService.getAllUsers();

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<UserDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllUsersByBUId/{buId}")
	public List<UserDto> getAllUsersByBU(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return userService.getAllUsersByBU(buId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<UserDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getInterviewersByBuId/{buId}")
	public List<UserDto> getInterviewersByBuId(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return userService.getInterviewersByBuId(buId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<UserDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllUsersByRoleId/{roleId}")
	public List<UserDto> getAllUsersByRole(@PathVariable String roleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return userService.getAllUsersByRole(roleId, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<UserDto>();
	}

	/**
	 * @return
	 */
	@GetMapping("/getRecruiters")
	public List<UserDto> getRecruiters(@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return userService.getRecruiters(lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<UserDto>();
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchUser")
	public List<UserDto> searchUser(@RequestParam String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return userService.searchUser(key);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<UserDto>();
	}

	/**
	 * @param key
	 * @return
	 */
	@PostMapping("/searchUser")
	public List<UserDto> searchUser(@RequestBody UserSearchDto userSearchDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return userService.searchUser(userSearchDto, lang);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<UserDto>();
	}

	/**
	 * @param userId
	 * @return
	 */
	@GetMapping("/getUser/{userId}")
	public UserDto getUserById(@PathVariable String userId,
			@RequestHeader(name = "lang", required = false) String lang) {

		UserDto userDto = new UserDto();
		try {
			userDto = userService.getUserById(userId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return userDto;
	}

	/**
	 * @param userId
	 * @return
	 */
	@DeleteMapping("/deleteUserById/{userId}")
	public UserDto deleteUserById(@PathVariable String userId,
			@RequestHeader(name = "lang", required = false) String lang) {

		UserDto resultDto = new UserDto();
		try {

			return userService.deleteUserById(userId);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));

			return resultDto;
		}
	}

	@GetMapping("/getUsersByBUId/{buId}")
	public List<UserDto> getUsersByBUId(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) throws Exception {

		try {

			return userService.getUsersByBusinessUnitId(buId);

		} catch (final Exception e) {

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<UserDto>();
	}

	/**
	 * @param businessUnitDto
	 * @return
	 */
	@PutMapping("/updatePanelUsers/{buId}")
	public UserDto updatePanelUsers(@PathVariable String buId, @RequestBody List<String> userIds,
			@RequestHeader(name = "lang", required = false) String lang) {

		UserDto resultDto = new UserDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// BusinessUnit Id.
			if (buId == null || buId.isEmpty() || buId.equals("0")) {
				errorParam.append("BusinessUnit Id");
			}
			// Interview Panel List.
			if (userIds == null || userIds.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Interview Panel List" : "Interview Panel List");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = userService.updatePanelUsers(buId, userIds, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}
}
