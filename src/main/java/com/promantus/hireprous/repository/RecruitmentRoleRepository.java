/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.RecruitmentRole;

/**
 * @author Sihab.
 *
 */
public interface RecruitmentRoleRepository extends MongoRepository<RecruitmentRole, String> {

	/**
	 * @param recruitmentRoleName
	 * @return
	 */
	List<RecruitmentRole> findByRecruitmentRoleNameRegex(String recruitmentRoleName, Sort sort);

	/**
	 * @param recruitmentRoleId
	 * @return
	 */
	RecruitmentRole findById(long recruitmentRoleId);

	/**
	 * @param recRoleName
	 * @return
	 */
	RecruitmentRole getRecruitmentRoleByRecruitmentRoleName(String recRoleName);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param recruitmentRoleIds
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<RecruitmentRole> findByIdIn(List<Long> recruitmentRoleIds, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param recRoleName
	 * @return
	 */
	RecruitmentRole getRecruitmentRoleByRecruitmentRoleNameIgnoreCase(String recRoleName);

}
