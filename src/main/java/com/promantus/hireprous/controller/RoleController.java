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
import com.promantus.hireprous.dto.RoleDto;
import com.promantus.hireprous.service.RoleService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Roles related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class RoleController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	/**
	 * @param roleDto
	 * @return
	 */
	@PostMapping("/addRole")
	public RoleDto addRole(@RequestBody RoleDto roleDto, @RequestHeader(name = "lang", required = false) String lang) {

		RoleDto resultDto = new RoleDto();
		try {

			if (roleDto.getRoleName() == null || roleDto.getRoleName().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(super.getMessage("mandatory.input.param", new String[] { "Role Name" }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = roleService.addRole(roleDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param roleName
	 * @param lang
	 * @return
	 */
	@PostMapping("/checkRoleName")
	public Boolean checkRoleName(@RequestBody String roleName,
			@RequestHeader(name = "lang", required = false) String lang) {
		try {
			return roleService.checkRoleName(roleName);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param roleDto
	 * @return
	 */
	@PutMapping("/updateRole")
	public RoleDto updateRole(@RequestBody RoleDto roleDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		RoleDto resultDto = new RoleDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Role Id.
			if (roleDto.getId() == 0) {
				errorParam.append("Role Id");
			}
			// Role Name.
			if (roleDto.getRoleName() == null || roleDto.getRoleName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Role Name" : "Role Name");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = roleService.updateRole(roleDto, lang);

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
	@GetMapping("/getAllRoles")
	public List<RoleDto> getAllRoles(@RequestHeader(name = "lang", required = false) String lang) {

		List<RoleDto> rolesDtoList = new ArrayList<RoleDto>();
		try {
			rolesDtoList = roleService.getAllRoles();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return rolesDtoList;
	}

	/**
	 * @param roleId
	 * @return
	 */
	@GetMapping("/getRole/{roleId}")
	public RoleDto getRoleById(@PathVariable String roleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		RoleDto roleDto = new RoleDto();
		try {
			roleDto = roleService.getRoleById(roleId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return roleDto;
	}

	/**
	 * @param roleId
	 * @return
	 */
	@DeleteMapping("/deleteRoleById/{roleId}")
	public Boolean deleteRoleById(@PathVariable String roleId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			roleService.deleteRoleById(roleId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchRole")
	public List<RoleDto> searchRole(@RequestParam String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<RoleDto> rolesDtoList = new ArrayList<RoleDto>();
		try {
			if (key != null) {
				rolesDtoList = roleService.searchRole(key);
			}
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return rolesDtoList;
	}
}
