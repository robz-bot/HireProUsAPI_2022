/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Project;

/**
 * @author Sihab.
 *
 */
public interface ProjectRepository extends MongoRepository<Project, String> {

	/**
	 * @param projectName
	 * @return
	 */
	List<Project> findByProjectNameRegex(String projectName, Sort sort);

	/**
	 * @param id
	 * @return
	 */
	Project findById(long id);

	/**
	 * @param projectName
	 * @return
	 */
	Project getProjectByProjectName(String projectName);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param projectName
	 * @return
	 */
	Project getProjectByProjectNameIgnoreCase(String projectName);

	/**
	 * @param buId
	 * @return
	 */
	int countByBusinessUnitId(Long buId);

	/**
	 * @param customerId
	 * @return
	 */
	int countByCustomerId(Long customerId);

	/**
	 * @param parseLong
	 * @param by
	 * @return
	 */
	List<Project> findByBusinessUnitId(long parseLong, Sort by);

	List<Project> findByCustomerId(long parseLong, Sort by);

	List<Project> findProjectByCustomerId(long parseLong, Sort by);


}
