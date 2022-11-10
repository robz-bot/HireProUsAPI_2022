/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Menu;

/**
 * @author Sihab.
 *
 */
public interface MenuRepository extends MongoRepository<Menu, String> {

	/**
	 * @param menuName
	 * @return
	 */
	List<Menu> findByMenuNameRegex(String menuName, Sort sort);

	/**
	 * @param menuId
	 * @return
	 */
	Menu findById(long menuId);

	/**
	 * @param menuName
	 * @return
	 */
	Menu getMenuByMenuName(String menuName);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param menuIds
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Menu> getMenuByIdIn(List<Long> menuIds, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param mainMenuId
	 * @param byMenuName
	 * @return
	 */
	List<Menu> findByMainMenuId(long mainMenuId, Sort byMenuName);

	/**
	 * @param mainMenuId
	 * @param byMenuName
	 * @return
	 */
	List<Menu> findByMainMenuIdNot(long mainMenuId, Sort byMenuName);

	/**
	 * @param menuIds
	 * @param mainMenuId
	 * @param byMenuName
	 * @return
	 */
	List<Menu> findByIdInAndMainMenuId(List<Long> menuIds, long mainMenuId, Sort byMenuName);

	/**
	 * @param menuIds
	 * @param mainMenuId
	 * @param byMenuName
	 * @return
	 */
	List<Menu> findByIdInAndMainMenuIdNot(List<Long> menuIds, long mainMenuId, Sort byMenuName);

	/**
	 * @param menuName
	 * @return
	 */
	Menu getMenuByMenuNameIgnoreCase(String menuName);

}
