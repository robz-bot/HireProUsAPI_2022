/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Onboard;

/**
 * @author Sihab.
 *
 */
public interface OnboardRepository extends MongoRepository<Onboard, String> {

	/**
	 * @param onboardId
	 * @return
	 */
	Onboard findById(long onboardId);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param jrNumber
	 * @param candidateId
	 * @return
	 */
	Object getOnboardByJrNumberAndCandidateId(String jrNumber, Long candidateId);

	/**
	 * @param year
	 * @param orderByRunningNumberDesc
	 * @return
	 */
	Onboard findFirstByYear(int year, Sort orderByRunningNumberDesc);

	/**
	 * @param jrNumber
	 * @param candidateId
	 * @param employeeIdByHR
	 * @param workOrderNumber
	 * @return
	 */
	Object getOnboardByJrNumberAndCandidateIdAndEmployeeIdByHRAndWorkOrderNumber(String jrNumber, Long candidateId,
			String employeeIdByHR, String workOrderNumber);

	/**
	 * @param projectId
	 * @return
	 */
	int countByProjectId(Long projectId);

}
