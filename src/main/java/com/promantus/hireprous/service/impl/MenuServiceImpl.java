/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.MenuDto;
import com.promantus.hireprous.entity.Menu;
import com.promantus.hireprous.repository.MenuRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.MenuService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class MenuServiceImpl implements MenuService {

	private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	MenuRepository menuRepository;

	@Override
	public Boolean checkMenuName(String menuName) {

		if (menuRepository.getMenuByMenuNameIgnoreCase(menuName) != null) {
			return true;
		}

		return false;
	}

	@Override
	public MenuDto addMenu(final MenuDto menuDto, String lang) throws Exception {

		MenuDto resultDto = new MenuDto();

		if (this.checkMenuName(menuDto.getMenuName())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { "Menu Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		Menu menu = new Menu();
		menu.setId(commonService.nextSequenceNumber());
		menu.setMenuName(menuDto.getMenuName());
		// menu.setPageLink(menuDto.getPageLink());
		menu.setMainMenuId(menuDto.getMainMenuId());

		menu.setCreatedBy(menuDto.getCreatedBy());
		menu.setUpdatedBy(menuDto.getUpdatedBy());
		menu.setCreatedDateTime(LocalDateTime.now());
		menu.setUpdatedDateTime(LocalDateTime.now());
		menuRepository.save(menu);

		resultDto.setId(menu.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public MenuDto updateMenu(final MenuDto menuDto, String lang) throws Exception {

		MenuDto resultDto = new MenuDto();

		Menu menu = menuRepository.findById(menuDto.getId());

		if (menu == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Menu Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		Menu menuByName = menuRepository.getMenuByMenuName(menuDto.getMenuName());
		if (menuByName != null && !menu.getId().equals(menuByName.getId())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { "Menu Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		menu.setMenuName(menuDto.getMenuName());
		// menu.setPageLink(menuDto.getPageLink());
		menu.setMainMenuId(menuDto.getMainMenuId());

		menu.setUpdatedBy(menuDto.getUpdatedBy());
		menu.setUpdatedDateTime(LocalDateTime.now());

		menuRepository.save(menu);

		resultDto.setId(menu.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public String getMenuNameById(final long menuId) throws Exception {

		Menu menu = menuRepository.findById(menuId);

		return menu != null ? menu.getMenuName() : "";
	}

	@Override
	public List<MenuDto> getAllMenus() throws Exception {

		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

		List<Menu> menusList = menuRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Menu menu : menusList) {
			menuDtoList.add(this.getMenuDto(menu, false));
		}

//		Comparator<MenuDto> sortByMainMenuAndSubMenu = Comparator.comparing(MenuDto::getMainMenuName)
//				.thenComparing(Comparator.comparing(MenuDto::getMenuName));
//
//		return menuDtoList.stream().sorted(sortByMainMenuAndSubMenu).collect(Collectors.toList());
		return menuDtoList;
	}

	@Override
	public List<MenuDto> getAllMainMenuList() throws Exception {

		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

		List<Menu> menusList = menuRepository.findByMainMenuId(0L, Sort.by(Sort.Direction.ASC, "menuName"));
		for (Menu menu : menusList) {
			menuDtoList.add(this.getMainMenuDto(menu));
		}

		return menuDtoList;
	}

	@Override
	public List<MenuDto> getAllMainMenuListByIds(List<Long> menuIds) throws Exception {

		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

		List<Menu> menusList = menuRepository.findByIdInAndMainMenuId(menuIds, 0L,
				Sort.by(Sort.Direction.ASC, "menuName"));
		for (Menu menu : menusList) {
			menuDtoList.add(this.getMainMenuDto(menu));
		}

		return menuDtoList;
	}

	@Override
	public List<MenuDto> getAllSubMenuList() throws Exception {

		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

		List<Menu> menusList = menuRepository.findByMainMenuIdNot(0L, Sort.by(Sort.Direction.ASC, "menuName"));
		for (Menu menu : menusList) {
			menuDtoList.add(this.getSubMenuDto(menu));
		}

		return menuDtoList;
	}

	@Override
	public List<MenuDto> getAllSubMenuListByIds(List<Long> subMenuIds) throws Exception {

		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

		List<Menu> menusList = menuRepository.findByIdInAndMainMenuIdNot(subMenuIds, 0L,
				Sort.by(Sort.Direction.ASC, "menuName"));
		for (Menu menu : menusList) {
			menuDtoList.add(this.getSubMenuDto(menu));
		}

		return menuDtoList;
	}

	@Override
	public List<MenuDto> getAllSubMenuListByIdsForLogin(List<Long> subMenuIds) throws Exception {

		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

		List<Menu> menusList = menuRepository.findByIdInAndMainMenuIdNot(subMenuIds, 0L,
				Sort.by(Sort.Direction.ASC, "menuName"));
		for (Menu menu : menusList) {
			menuDtoList.add(this.getSubMenuDtoForLogin(menu));
		}

		return menuDtoList;
	}

	@Override
	public List<MenuDto> getSubMenuList(String menuId, String lang) throws Exception {

		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

		List<Menu> menusList = menuRepository.findByMainMenuId(Long.parseLong(menuId),
				Sort.by(Sort.Direction.ASC, "menuName"));
		for (Menu menu : menusList) {
			menuDtoList.add(this.getMenuDto(menu, false));
		}

		return menuDtoList;
	}

	@Override
	public List<MenuDto> getAllMenus(final boolean needShort) throws Exception {

		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

		List<Menu> menusList = menuRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Menu menu : menusList) {
			menuDtoList.add(this.getMenuDto(menu, needShort));
		}

		return menuDtoList;
	}

	@Override
	public MenuDto getMenuById(final String menuId) throws Exception {

		Menu menu = menuRepository.findById(Long.parseLong(menuId));

		return menu != null ? this.getMenuDto(menu, false) : new MenuDto();
	}

	@Override
	public String getMenuNamesByMenuIds(final List<Long> menuIds) throws Exception {

		List<Menu> menuList = menuRepository.getMenuByIdIn(menuIds, HireProUsUtil.orderByUpdatedDateTimeDesc());

		StringBuilder menuListSb = new StringBuilder("");
		for (Menu menu : menuList) {
			if (menuListSb.length() > 0) {
				menuListSb.append(", ");
			}
			menuListSb.append(menu.getMenuName());
		}

		return menuListSb.toString();
	}

	/**
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	private MenuDto getMenuDto(final Menu menu, final boolean needShort) throws Exception {

		MenuDto menuDto = new MenuDto();

		menuDto.setId(menu.getId());
		menuDto.setMenuName(menu.getMenuName());

		if (menu.getMainMenuId().equals(0L)) {
			menuDto.setMainMenuId(menu.getId());
			menuDto.setMainMenuName(menu.getMenuName());
			menuDto.setMenuName("-");
		} else {
			menuDto.setMainMenuId(menu.getMainMenuId());
			menuDto.setMainMenuName(menu.getMainMenuId() != null ? this.getMenuNameById(menu.getMainMenuId()) : "");
		}

		menuDto.setPageLink(menu.getPageLink());

		if (!needShort) {
			menuDto.setCreatedBy(menu.getCreatedBy());
			menuDto.setCreatedByName(CacheUtil.getUsersMap().get(menu.getCreatedBy()));
			menuDto.setCreatedDateTime(menu.getCreatedDateTime());

			menuDto.setUpdatedBy(menu.getUpdatedBy());
			menuDto.setUpdatedByName(CacheUtil.getUsersMap().get(menu.getUpdatedBy()));
			menuDto.setUpdatedDateTime(menu.getUpdatedDateTime());
		}

		return menuDto;
	}

	/**
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	private MenuDto getMainMenuDto(final Menu menu) throws Exception {

		MenuDto menuDto = new MenuDto();

		menuDto.setMainMenuId(menu.getId());
		menuDto.setMainMenuName(menu.getMenuName());
		menuDto.setMenuName("-");
		menuDto.setPageLink(menu.getPageLink());

		return menuDto;
	}

	/**
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	private MenuDto getSubMenuDto(final Menu menu) throws Exception {

		MenuDto menuDto = new MenuDto();

		menuDto.setId(menu.getId());
		menuDto.setMenuName(
				menu.getMainMenuId() != null ? this.getMenuNameById(menu.getMainMenuId()) + " - " + menu.getMenuName()
						: "" + " - " + menu.getMenuName());
		menuDto.setMainMenuId(menu.getMainMenuId());
		menuDto.setMainMenuName(menu.getMainMenuId() != null ? this.getMenuNameById(menu.getMainMenuId()) : "");

		menuDto.setPageLink(menu.getPageLink());

		return menuDto;
	}

	/**
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	private MenuDto getSubMenuDtoForLogin(final Menu menu) throws Exception {

		MenuDto menuDto = new MenuDto();

		menuDto.setId(menu.getId());
		menuDto.setMenuName(menu.getMenuName());
		menuDto.setMainMenuId(menu.getMainMenuId());
		menuDto.setMainMenuName(menu.getMainMenuId() != null ? this.getMenuNameById(menu.getMainMenuId()) : "");
		menuDto.setPageLink(menu.getPageLink());

		return menuDto;
	}

	@Override
	public void deleteMenuById(final String menuId) throws Exception {

		menuRepository.deleteById(Long.parseLong(menuId));
	}

	@Override
	public List<MenuDto> searchMenu(final String keyword) throws Exception {

		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();

		List<Menu> menusList = menuRepository.findByMenuNameRegex("(?i).*" + keyword + ".*",
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<Menu> totalMenusList = new ArrayList<Menu>();
		for (Menu menu : menusList) {

			totalMenusList.add(menu);

			List<Menu> menusListById = null;
			if (menu.getMainMenuId().equals(0L)) {
				menusListById = menuRepository.findByMainMenuId(menu.getId(),
						HireProUsUtil.orderByUpdatedDateTimeDesc());
			}

			if (menusListById != null && menusListById.size() > 0) {
				totalMenusList.addAll(menusListById);
			}
		}

		for (Menu menu : totalMenusList) {
			menuDtoList.add(this.getMenuDto(menu, false));
		}

		return menuDtoList;
	}
}
