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
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.MenuDto;
import com.promantus.hireprous.service.MenuService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Menus related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class MenuController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private MenuService menuService;

	/**
	 * @param menuDto
	 * @return
	 */
	@PostMapping("/addMenu")
	public MenuDto addMenu(@RequestBody MenuDto menuDto, @RequestHeader(name = "lang", required = false) String lang) {

		MenuDto resultDto = new MenuDto();
		try {

			if (menuDto.getMenuName() == null || menuDto.getMenuName().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(super.getMessage("mandatory.input.param", new String[] { "Menu Name" }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}
//			if (menuDto.getPageLink() == null || menuDto.getPageLink().isEmpty()) {
//				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
//				resultDto.setMessage(super.getMessage("mandatory.input.param", new String[] { "Page Link" }, lang));
//
//				logger.info(resultDto.getMessage());
//				return resultDto;
//			}

			resultDto = menuService.addMenu(menuDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param menuName
	 * @param lang
	 * @return
	 */
	@PostMapping("/checkMenuName")
	public Boolean checkMenuName(@RequestBody String menuName,
			@RequestHeader(name = "lang", required = false) String lang) {
		try {
			return menuService.checkMenuName(menuName);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param menuDto
	 * @return
	 */
	@PutMapping("/updateMenu")
	public MenuDto updateMenu(@RequestBody MenuDto menuDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		MenuDto resultDto = new MenuDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Menu Id.
			if (menuDto.getId() == 0) {
				errorParam.append("Menu Id");
			}
			// Menu Name.
			if (menuDto.getMenuName() == null || menuDto.getMenuName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Menu Name" : "Menu Name");
			}
			// Page Link
//			if (menuDto.getPageLink() == null || menuDto.getPageLink().isEmpty()) {
//				errorParam.append(errorParam.length() > 0 ? ", Page Link" : "Page Link");
//			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = menuService.updateMenu(menuDto, lang);

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
	@GetMapping("/getAllMenus")
	public List<MenuDto> getAllMenus(@RequestHeader(name = "lang", required = false) String lang) {

		List<MenuDto> menusDtoList = new ArrayList<MenuDto>();
		try {
			menusDtoList = menuService.getAllMenus();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return menusDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllMainMenuList")
	public List<MenuDto> getAllMainMenuList(@RequestHeader(name = "lang", required = false) String lang) {

		List<MenuDto> menusDtoList = new ArrayList<MenuDto>();
		try {
			menusDtoList = menuService.getAllMainMenuList();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return menusDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllSubMenuList")
	public List<MenuDto> getAllSubMenuList(@RequestHeader(name = "lang", required = false) String lang) {

		List<MenuDto> menusDtoList = new ArrayList<MenuDto>();
		try {
			menusDtoList = menuService.getAllSubMenuList();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return menusDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getSubMenuList/{menuId}")
	public List<MenuDto> getAllSubMenuList(@PathVariable String menuId,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<MenuDto> menusDtoList = new ArrayList<MenuDto>();
		try {
			menusDtoList = menuService.getSubMenuList(menuId, lang);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return menusDtoList;
	}

	/**
	 * @param menuId
	 * @return
	 */
	@GetMapping("/getMenu/{menuId}")
	public MenuDto getMenuById(@PathVariable String menuId,
			@RequestHeader(name = "lang", required = false) String lang) {

		MenuDto menuDto = new MenuDto();
		try {
			menuDto = menuService.getMenuById(menuId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return menuDto;
	}

	/**
	 * @param menuId
	 * @return
	 */
	@DeleteMapping("/deleteMenuById/{menuId}")
	public Boolean deleteMenuById(@PathVariable String menuId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			menuService.deleteMenuById(menuId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchMenu/{key}")
	public List<MenuDto> searchMenu(@PathVariable String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<MenuDto> menusDtoList = new ArrayList<MenuDto>();
		try {
			menusDtoList = menuService.searchMenu(key);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return menusDtoList;
	}
}
