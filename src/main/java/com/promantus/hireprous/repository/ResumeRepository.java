/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Resume;

/**
 * @author Sihab.
 *
 */
public interface ResumeRepository extends MongoRepository<Resume, String> {

	/**
	 * @param resumeName
	 * @return
	 */
	Resume getResumeByResumeName(String resumeName);

	/**
	 * @param resumeName
	 */
	void deleteByResumeName(String resumeName);

}
