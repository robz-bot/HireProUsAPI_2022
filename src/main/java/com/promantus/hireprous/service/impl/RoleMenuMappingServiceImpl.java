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
import com.promantus.hireprous.dto.MainMenuDto;
import com.promantus.hireprous.dto.MenuDto;
import com.promantus.hireprous.dto.RoleDto;
import com.promantus.hireprous.dto.SubMenuDto;
import com.promantus.hireprous.entity.Role;
import com.promantus.hireprous.repository.RoleRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.MenuService;
import com.promantus.hireprous.service.RoleMenuMappingService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class RoleMenuMappingServiceImpl implements RoleMenuMappingService {

	private static final Logger logger = LoggerFactory.getLogger(RoleMenuMappingServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	MenuService menuService;

	@Autowired
	RoleRepository roleRepository;

	@Override
	public RoleDto addRoleMenuMapping(final String roleId, final String userId, final List<String> mainMenuIds,
			final List<String> subMenuIds, final String lang) throws Exception {

		RoleDto resultDto = new RoleDto();

		Role role = roleRepository.findById(Long.parseLong(roleId));

		if (role == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Role Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		// Main Menu.
		StringBuilder mainMenuIdList = new StringBuilder("");
		for (String mainMenuId : mainMenuIds) {

			if (mainMenuIdList.length() > 0) {
				mainMenuIdList.append(",");
			}
			mainMenuIdList.append(mainMenuId);
		}
		role.setMenuIds(mainMenuIdList.toString());

		// Sub Menu.
		if (subMenuIds != null && subMenuIds.size() > 0) {
			StringBuilder subMenuIdList = new StringBuilder("");
			for (String subMenuId : subMenuIds) {

				if (subMenuIdList.length() > 0) {
					subMenuIdList.append(",");
				}
				subMenuIdList.append(subMenuId);
			}
			role.setSubMenuIds(subMenuIdList.toString());
		}

		role.setUpdatedBy(Long.parseLong(userId));
		role.setUpdatedDateTime(LocalDateTime.now());

		roleRepository.save(role);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public RoleDto updateRoleWithMenus(final String roleId, final String userId, final List<String> mainMenuIds,
			final List<String> subMenuIds, final String lang) throws Exception {

		RoleDto resultDto = new RoleDto();

		Role role = roleRepository.findById(Long.parseLong(roleId));

		if (role == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Role Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		// Main Menu
		StringBuilder mainMenuIdList = new StringBuilder("");
		for (String mainMenuId : mainMenuIds) {

			if (mainMenuIdList.length() > 0) {
				mainMenuIdList.append(",");
			}
			mainMenuIdList.append(mainMenuId);
		}
		role.setMenuIds(mainMenuIdList.toString());

		// Sub Menu
		role.setSubMenuIds(null);
		if (subMenuIds != null && subMenuIds.size() > 0) {
			StringBuilder subMenuIdList = new StringBuilder("");
			for (String subMenuId : subMenuIds) {

				if (subMenuIdList.length() > 0) {
					subMenuIdList.append(",");
				}
				subMenuIdList.append(subMenuId);
			}
			role.setSubMenuIds(subMenuIdList.toString());
		}

		role.setUpdatedBy(Long.parseLong(userId));
		role.setUpdatedDateTime(LocalDateTime.now());

		roleRepository.save(role);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public RoleDto getMenusByRoleId(final String roleId, final String lang) throws Exception {

		RoleDto resultDto = new RoleDto();

		Role role = roleRepository.findById(Long.parseLong(roleId));

		if (role == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Role Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		resultDto.setId(role.getId());
		resultDto.setRoleName(role.getRoleName());

		List<String> menuList = !(role.getMenuIds() == null || role.getMenuIds().isEmpty())
				? Arrays.asList(role.getMenuIds().split(","))
				: new ArrayList<String>();

		List<MenuDto> mainMenuDtoList = menuService.getAllMainMenuList();
		for (MenuDto mainMenuDto : mainMenuDtoList) {

			mainMenuDto.setSelected("0");
			if (menuList.contains(mainMenuDto.getMainMenuId() + "")) {
				mainMenuDto.setSelected("1");
			}
		}
		resultDto.setMenuDtoList(mainMenuDtoList);

		List<String> subMenuList = !(role.getSubMenuIds() == null || role.getSubMenuIds().isEmpty())
				? Arrays.asList(role.getSubMenuIds().split(","))
				: new ArrayList<String>();

		List<MenuDto> subMenuDtoList = menuService.getAllSubMenuList();
		for (MenuDto mainMenuDto : subMenuDtoList) {

			mainMenuDto.setSelected("0");
			if (subMenuList.contains(mainMenuDto.getId() + "")) {
				mainMenuDto.setSelected("1");
			}
		}
		resultDto.setSubMenuDtoList(subMenuDtoList);

		return resultDto;
	}

	@Override
	public List<RoleDto> getMenuSubMenuList(String lang) throws Exception {

		List<Role> roleList = roleRepository.findAll();

		if (roleList == null || roleList.size() == 0) {
			return new ArrayList<RoleDto>();
		}

		List<RoleDto> resultDtoList = new ArrayList<RoleDto>();
		for (Role role : roleList) {

			List<String> menuStringList = !(role.getMenuIds() == null || role.getMenuIds().isEmpty())
					? Arrays.asList(role.getMenuIds().split(","))
					: new ArrayList<String>();

			List<String> subMenuStringList = !(role.getSubMenuIds() == null || role.getSubMenuIds().isEmpty())
					? Arrays.asList(role.getSubMenuIds().split(","))
					: new ArrayList<String>();

			String mainMenuNamesList = "";
			if (menuStringList != null && menuStringList.size() > 0) {
				mainMenuNamesList = menuService.getMenuNamesByMenuIds(
						menuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
			}

			String subMenuNamesList = "";
			if (subMenuStringList != null && subMenuStringList.size() > 0) {
				subMenuNamesList = menuService.getMenuNamesByMenuIds(
						subMenuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
			}

//			List<MenuDto> mainMenuDtoList = new ArrayList<MenuDto>();
//			if (menuStringList != null && menuStringList.size() > 0) {
//				mainMenuDtoList = menuService.getAllMainMenuListByIds(
//						menuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
//			}
//
//			List<MenuDto> subMenuDtoList = new ArrayList<MenuDto>();
//			if (subMenuStringList != null && subMenuStringList.size() > 0) {
//				subMenuDtoList = menuService.getAllSubMenuListByIds(
//						subMenuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
//			}

			RoleDto roleDto = new RoleDto();

			roleDto.setId(role.getId());
			roleDto.setRoleName(role.getRoleName());

			roleDto.setMainMenuNames(mainMenuNamesList);
			roleDto.setSubMenuNames(subMenuNamesList);

			roleDto.setCreatedBy(role.getCreatedBy());
			roleDto.setCreatedByName(CacheUtil.getUsersMap().get(role.getCreatedBy()));
			roleDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(role.getCreatedDateTime()));

			roleDto.setUpdatedBy(role.getUpdatedBy());
			roleDto.setUpdatedByName(CacheUtil.getUsersMap().get(role.getUpdatedBy()));
			roleDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(role.getUpdatedDateTime()));

			resultDtoList.add(roleDto);
		}

		return resultDtoList;
	}

	@Override
	public List<RoleDto> getMenuSubMenuListByRoleId(String roleId, String lang) throws Exception {

		Role role = roleRepository.findById(Long.parseLong(roleId));

		if (role == null) {
			return new ArrayList<RoleDto>();
		}

		List<String> menuStringList = !(role.getMenuIds() == null || role.getMenuIds().isEmpty())
				? Arrays.asList(role.getMenuIds().split(","))
				: new ArrayList<String>();

		List<String> subMenuStringList = !(role.getSubMenuIds() == null || role.getSubMenuIds().isEmpty())
				? Arrays.asList(role.getSubMenuIds().split(","))
				: new ArrayList<String>();

		String mainMenuNamesList = "";
		if (menuStringList != null && menuStringList.size() > 0) {
			mainMenuNamesList = menuService
					.getMenuNamesByMenuIds(menuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
		}

		String subMenuNamesList = "";
		if (subMenuStringList != null && subMenuStringList.size() > 0) {
			subMenuNamesList = menuService.getMenuNamesByMenuIds(
					subMenuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
		}

		RoleDto roleDto = new RoleDto();

		roleDto.setId(role.getId());
		roleDto.setRoleName(role.getRoleName());

		roleDto.setMainMenuNames(mainMenuNamesList);
		roleDto.setSubMenuNames(subMenuNamesList);

		roleDto.setCreatedBy(role.getCreatedBy());
		roleDto.setCreatedByName(CacheUtil.getUsersMap().get(role.getCreatedBy()));
		roleDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(role.getCreatedDateTime()));

		roleDto.setUpdatedBy(role.getUpdatedBy());
		roleDto.setUpdatedByName(CacheUtil.getUsersMap().get(role.getUpdatedBy()));
		roleDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(role.getUpdatedDateTime()));

		List<RoleDto> resultDtoList = new ArrayList<RoleDto>();
		resultDtoList.add(roleDto);

		return resultDtoList;
	}

	@Override
	public String getMainMenusForLogin(final Role role) throws Exception {

		List<String> menuStringList = !(role.getMenuIds() == null || role.getMenuIds().isEmpty())
				? Arrays.asList(role.getMenuIds().split(","))
				: new ArrayList<String>();

		List<MenuDto> mainMenuDtoList = new ArrayList<MenuDto>();
		if (menuStringList != null && menuStringList.size() > 0) {
			mainMenuDtoList = menuService
					.getAllMainMenuListByIds(menuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
		}

		StringBuilder mainMenus = new StringBuilder();
		for (MenuDto menuDto : mainMenuDtoList) {
			if (mainMenus.length() > 0) {
				mainMenus.append(", ");
			}
			mainMenus.append(menuDto.getMainMenuName());
		}

		return mainMenus.toString();
	}

	@Override
	public String getSubMenusForLogin(final Role role) throws Exception {

		List<String> subMenuStringList = !(role.getSubMenuIds() == null || role.getSubMenuIds().isEmpty())
				? Arrays.asList(role.getSubMenuIds().split(","))
				: new ArrayList<String>();

		List<MenuDto> subMenuList = new ArrayList<MenuDto>();
		if (subMenuStringList != null && subMenuStringList.size() > 0) {
			subMenuList = menuService.getAllSubMenuListByIdsForLogin(
					subMenuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
		}

		StringBuilder subMenus = new StringBuilder();
		for (MenuDto menuDto : subMenuList) {
			if (subMenus.length() > 0) {
				subMenus.append(", ");
			}
			subMenus.append(menuDto.getMenuName());
		}

		return subMenus.toString();
	}

	@Override
	public List<MainMenuDto> getMenusForLogin(long roleId) throws Exception {

		Role role = roleRepository.findById(roleId);

		if (role == null) {
			logger.info(commonService.getMessage("invalid", new String[] { "Role Id" }, null));
			return null;
		}

		List<String> menuStringList = !(role.getMenuIds() == null || role.getMenuIds().isEmpty())
				? Arrays.asList(role.getMenuIds().split(","))
				: new ArrayList<String>();

		List<String> subMenuStringList = !(role.getSubMenuIds() == null || role.getSubMenuIds().isEmpty())
				? Arrays.asList(role.getSubMenuIds().split(","))
				: new ArrayList<String>();

		List<MenuDto> mainMenuDtoList = new ArrayList<MenuDto>();
		if (menuStringList != null && menuStringList.size() > 0) {
			mainMenuDtoList = menuService
					.getAllMainMenuListByIds(menuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
		}

		List<MenuDto> subMenuList = new ArrayList<MenuDto>();
		if (subMenuStringList != null && subMenuStringList.size() > 0) {
			subMenuList = menuService.getAllSubMenuListByIds(
					subMenuStringList.stream().map(Long::parseLong).collect(Collectors.toList()));
		}

		List<MainMenuDto> resultDtoList = new ArrayList<MainMenuDto>();
		for (MenuDto menuDto : mainMenuDtoList) {

			MainMenuDto mainMenuDto = new MainMenuDto();
			mainMenuDto.setMenuId(menuDto.getMainMenuId());
			mainMenuDto.setMenuName(menuDto.getMainMenuName());
			mainMenuDto.setPageLink(menuDto.getPageLink());

			List<SubMenuDto> subMenuDtoList = new ArrayList<SubMenuDto>();
			for (MenuDto sMenuDto : subMenuList) {
				if (menuDto.getMainMenuId().equals(sMenuDto.getMainMenuId())) {

					SubMenuDto subMenuDto = new SubMenuDto();
					subMenuDto.setMenuId(sMenuDto.getId());
					subMenuDto.setMenuName(sMenuDto.getMenuName());
					subMenuDto.setPageLink(sMenuDto.getPageLink());

					subMenuDtoList.add(subMenuDto);
				}
			}
			mainMenuDto.setSubMenuList(subMenuDtoList);

			resultDtoList.add(mainMenuDto);
		}

		return resultDtoList;
	}

	@Override
	public List<MainMenuDto> getMenusForLoginSa() throws Exception {

		List<MenuDto> mainMenuDtoList = menuService.getAllMainMenuList();
		List<MenuDto> subMenuList = menuService.getAllSubMenuList();

		List<MainMenuDto> resultDtoList = new ArrayList<MainMenuDto>();
		for (MenuDto menuDto : mainMenuDtoList) {

			MainMenuDto mainMenuDto = new MainMenuDto();
			mainMenuDto.setMenuId(menuDto.getMainMenuId());
			mainMenuDto.setMenuName(menuDto.getMainMenuName());
			mainMenuDto.setPageLink(menuDto.getPageLink());

			List<SubMenuDto> subMenuDtoList = new ArrayList<SubMenuDto>();
			for (MenuDto sMenuDto : subMenuList) {
				if (menuDto.getMainMenuId().equals(sMenuDto.getMainMenuId())) {

					SubMenuDto subMenuDto = new SubMenuDto();
					subMenuDto.setMenuId(sMenuDto.getId());
					subMenuDto.setMenuName(sMenuDto.getMenuName());
					subMenuDto.setPageLink(sMenuDto.getPageLink());

					subMenuDtoList.add(subMenuDto);
				}
			}
			mainMenuDto.setSubMenuList(subMenuDtoList);

			resultDtoList.add(mainMenuDto);
		}

		return resultDtoList;
	}
}
