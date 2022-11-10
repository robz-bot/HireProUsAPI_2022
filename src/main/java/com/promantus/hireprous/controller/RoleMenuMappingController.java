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
import com.promantus.hireprous.dto.RoleDto;
import com.promantus.hireprous.service.RoleMenuMappingService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Role and Menu Mapping related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class RoleMenuMappingController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(RoleMenuMappingController.class);

	@Autowired
	private RoleMenuMappingService roleMenuMappingService;

	/**
	 * @param roleDto
	 * @return
	 */
	@PutMapping("/addRoleMenuMapping/{roleId}/{userId}")
	public RoleDto addRoleMenuMapping(@PathVariable String roleId, @PathVariable String userId,
			@RequestBody RoleDto roleDto, @RequestHeader(name = "lang", required = false) String lang) {

		RoleDto resultDto = new RoleDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Role Id.
			if (roleId == null || roleId.isEmpty() || roleId.equals("0")) {
				errorParam.append("Role Id");
			}
			// User Id.
			if (userId == null || userId.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", User Id" : "User Id");
			}
			// Main Menu List.
			if (roleDto.getMainMenuIds() == null || roleDto.getMainMenuIds().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Main Menu List" : "Main Menu List");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = roleMenuMappingService.addRoleMenuMapping(roleId, userId, roleDto.getMainMenuIds(),
					roleDto.getSubMenuIds(), lang);

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
	@GetMapping("/getMenuSubMenuList")
	public List<RoleDto> getMenuSubMenuList(@RequestHeader(name = "lang", required = false) String lang) {

		List<RoleDto> resultDto = new ArrayList<RoleDto>();
		try {

			resultDto = roleMenuMappingService.getMenuSubMenuList(lang);

		} catch (final Exception e) {

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param roleId
	 * @return
	 */
	@GetMapping("/getMenuSubMenuListByRoleId/{roleId}")
	public List<RoleDto> getMenuSubMenuListByRoleId(@PathVariable String roleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<RoleDto> resultDto = new ArrayList<RoleDto>();
		try {

			resultDto = roleMenuMappingService.getMenuSubMenuListByRoleId(roleId, lang);

		} catch (final Exception e) {

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param roleId
	 * @return
	 */
	@GetMapping("/getMenusForUpdate/{roleId}")
	public RoleDto getMenusByRoleId(@PathVariable String roleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		RoleDto resultDto = new RoleDto();
		try {

			resultDto = roleMenuMappingService.getMenusByRoleId(roleId, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param roleDto
	 * @return
	 */
	@PutMapping("/updateRoleMenuMapping/{roleId}/{userId}")
	public RoleDto updateRoleMenuMapping(@PathVariable String roleId, @PathVariable String userId,
			@RequestBody RoleDto roleDto, @RequestHeader(name = "lang", required = false) String lang) {

		RoleDto resultDto = new RoleDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Role Id.
			if (roleId == null || roleId.isEmpty() || roleId.equals("0")) {
				errorParam.append("Role Id");
			}
			// User Id.
			if (userId == null || userId.isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", User Id" : "User Id");
			}
			// Main Menu List.
			if (roleDto.getMainMenuIds() == null || roleDto.getMainMenuIds().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Main Menu List" : "Main Menu List");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = roleMenuMappingService.updateRoleWithMenus(roleId, userId, roleDto.getMainMenuIds(),
					roleDto.getSubMenuIds(), lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}
}
