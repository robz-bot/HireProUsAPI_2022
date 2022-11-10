/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.SuggestionDto;
import com.promantus.hireprous.dto.SuggestionSearchDto;

/**
 * @author Sihab.
 *
 */
public interface SuggestionService {

	/**
	 * @param suggestionDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	SuggestionDto addSuggestion(final SuggestionDto suggestionDto, String lang) throws Exception;

	/**
	 * @param suggestionDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	SuggestionDto updateSuggestion(final SuggestionDto suggestionDto, String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<SuggestionDto> getAllSuggestions() throws Exception;

	/**
	 * @param suggestionId
	 * @return
	 * @throws Exception
	 */
	SuggestionDto getSuggestionById(String suggestionId) throws Exception;

	/**
	 * @param suggestionId
	 * @throws Exception
	 */
	void deleteSuggestionById(String suggestionId) throws Exception;

	/**
	 * @param suggestionSearchDto
	 * @return
	 * @throws Exception
	 */
	List<SuggestionDto> searchSuggestion(SuggestionSearchDto suggestionSearchDto) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<String> getAllSuggestionIds() throws Exception;

}
