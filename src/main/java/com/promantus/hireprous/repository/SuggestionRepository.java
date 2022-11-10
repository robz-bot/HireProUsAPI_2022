/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Suggestion;

/**
 * @author Sihab.
 *
 */
public interface SuggestionRepository extends MongoRepository<Suggestion, String> {

	/**
	 * @param suggestion
	 * @return
	 */
	List<Suggestion> findBySuggestionRegex(String suggestion, Sort sort);

	/**
	 * @param suggestionId
	 * @return
	 */
	Suggestion findById(long suggestionId);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param string
	 * @param orderByCodeNumberDesc
	 */
	Suggestion findFirstByCodePrefix(String string, Sort orderByCodeNumberDesc);

	/**
	 * @param sugStatus
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Suggestion> findBySugStatus(String sugStatus, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param type
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Suggestion> findByType(String type, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param sugStatus
	 * @param type
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Suggestion> findBySugStatusAndType(String sugStatus, String type, Sort orderByUpdatedDateTimeDesc);

}