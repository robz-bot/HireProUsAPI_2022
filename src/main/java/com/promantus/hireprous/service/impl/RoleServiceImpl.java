/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.RoleDto;
import com.promantus.hireprous.entity.Role;
import com.promantus.hireprous.repository.RoleRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.MenuService;
import com.promantus.hireprous.service.RoleService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class RoleServiceImpl implements RoleService {

	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	MenuService menuService;

	@Autowired
	RoleRepository roleRepository;

	@Override
	public Boolean checkRoleName(String roleName) {

		if (roleRepository.getRoleByRoleNameIgnoreCase(roleName) != null) {
			return true;
		}

		return false;
	}

	@Override
	public RoleDto addRole(final RoleDto roleDto, String lang) throws Exception {

		RoleDto resultDto = new RoleDto();

		if (this.checkRoleName(roleDto.getRoleName())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { "Role Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		Role role = new Role();
		role.setId(commonService.nextSequenceNumber());
		role.setRoleName(roleDto.getRoleName());
		role.setMenuIds("");
		role.setCreatedBy(roleDto.getCreatedBy());
		role.setUpdatedBy(roleDto.getUpdatedBy());
		role.setCreatedDateTime(LocalDateTime.now());
		role.setUpdatedDateTime(LocalDateTime.now());
		roleRepository.save(role);

		CacheUtil.getRolesMap().put(role.getId(), role.getRoleName());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public RoleDto updateRole(final RoleDto roleDto, String lang) throws Exception {

		RoleDto resultDto = new RoleDto();

		Role role = roleRepository.findById(roleDto.getId());

		if (role == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Role Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (this.checkRoleName(roleDto.getRoleName())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { "Role Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		role.setRoleName(roleDto.getRoleName());
		role.setUpdatedBy(roleDto.getUpdatedBy());
		role.setUpdatedDateTime(LocalDateTime.now());

		roleRepository.save(role);

		CacheUtil.getRolesMap().remove(role.getId());
		CacheUtil.getRolesMap().put(role.getId(), role.getRoleName());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public String getRoleNameById(final long roleId) throws Exception {

		Role role = roleRepository.findById(roleId);

		return role != null ? role.getRoleName() : "";
	}

	@Override
	public List<RoleDto> getAllRoles() throws Exception {

		List<RoleDto> roleDtoList = new ArrayList<RoleDto>();

		List<Role> rolesList = roleRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Role role : rolesList) {
			roleDtoList.add(this.getRoleDto(role));
		}

		return roleDtoList;
	}

	@Override
	public RoleDto getRoleById(final String roleId) throws Exception {

		Role role = roleRepository.findById(Long.parseLong(roleId));

		return role != null ? this.getRoleDto(role) : new RoleDto();
	}

	@Override
	public Role getRoleById(final long roleId) throws Exception {

		return roleRepository.findById(roleId);
	}

	/**
	 * @param role
	 * @return
	 * @throws Exception
	 */
	private RoleDto getRoleDto(final Role role) throws Exception {

		RoleDto roleDto = new RoleDto();

		roleDto.setId(role.getId());
		roleDto.setRoleName(role.getRoleName());

		roleDto.setCreatedBy(role.getCreatedBy());
		roleDto.setCreatedByName(CacheUtil.getUsersMap().get(role.getCreatedBy()));
		roleDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(role.getCreatedDateTime()));

		roleDto.setUpdatedBy(role.getUpdatedBy());
		roleDto.setUpdatedByName(CacheUtil.getUsersMap().get(role.getUpdatedBy()));
		roleDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(role.getUpdatedDateTime()));

		return roleDto;
	}

	@Override
	public void deleteRoleById(final String roleId) throws Exception {

		roleRepository.deleteById(Long.parseLong(roleId));

		CacheUtil.getRolesMap().remove(Long.parseLong(roleId));
	}

	@Override
	public List<RoleDto> searchRole(final String keyword) throws Exception {

		List<RoleDto> roleDtoList = new ArrayList<RoleDto>();

		List<Role> rolesList = roleRepository.findByRoleNameRegex("(?i).*" + keyword + ".*",
				HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Role role : rolesList) {
			roleDtoList.add(this.getRoleDto(role));
		}

		return roleDtoList;
	}

	@Override
	public String getMainMenusStringByRoleId(final String roleId, final String lang) throws Exception {

		Role role = roleRepository.findById(Long.parseLong(roleId));

		if (role == null) {
			logger.error(commonService.getMessage("invalid", new String[] { "Role Id" }, lang));
		}

		if (role.getMenuIds() != null && !role.getMenuIds().isEmpty()) {
			return menuService.getMenuNamesByMenuIds(
					Arrays.stream(role.getMenuIds().split(",")).map(Long::valueOf).collect(Collectors.toList()));
		}

		return "";
	}

	@Override
	public Long getRoleIdByRoleName(String roleName) {

		Role role = roleRepository.getRoleByRoleName(roleName);

		return role != null ? role.getId() : 0;
	}

	@Override
	public List<Long> getRoleIdsByRoleNames(List<String> roleNames) {

		List<Role> rolesList = roleRepository.findByRoleNameIn(roleNames);

		List<Long> roleIds = new ArrayList<Long>();
		for (Role role : rolesList) {
			roleIds.add(role.getId());
		}

		return roleIds;
	}
}
