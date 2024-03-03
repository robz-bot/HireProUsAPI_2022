/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.PDF;
import com.promantus.hireprous.entity.Resume;

/**
 * @author Sihab.
 *
 */
public interface PDFRepository extends MongoRepository<PDF, String> {

	PDF findByPdfName(String resumeName);

	void deleteByPdfName(String resumeName);
	

}
