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
import com.promantus.hireprous.dto.SuggestionDto;
import com.promantus.hireprous.dto.SuggestionSearchDto;
import com.promantus.hireprous.service.SuggestionService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Suggestions related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class SuggestionController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(SuggestionController.class);

	@Autowired
	private SuggestionService suggestionService;

	/**
	 * @param suggestionDto
	 * @return
	 */
	@PostMapping("/addSuggestion")
	public SuggestionDto addSuggestion(@RequestBody SuggestionDto suggestionDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		SuggestionDto resultDto = new SuggestionDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Suggestion.
			if (suggestionDto.getSuggestion() == null || suggestionDto.getSuggestion().isEmpty()) {
				errorParam.append("Suggestion / Issue details");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = suggestionService.addSuggestion(suggestionDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param suggestionDto
	 * @return
	 */
	@PutMapping("/updateSuggestion")
	public SuggestionDto updateSuggestion(@RequestBody SuggestionDto suggestionDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		SuggestionDto resultDto = new SuggestionDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Suggestion Id.
			if (suggestionDto.getId() == 0) {
				errorParam.append("Suggestion Id");
			}
			// Suggestion / Issue Type
			if (suggestionDto.getType() == null || suggestionDto.getType().isEmpty()) {
				errorParam.append("Suggestion / Issue Type");
			}
			// Suggestion / Issue details.
			if (suggestionDto.getSuggestion() == null || suggestionDto.getSuggestion().isEmpty()) {
				errorParam.append("Suggestion / Issue details");
			}
			// Reply.
			if (suggestionDto.getReply() == null || suggestionDto.getReply().isEmpty()) {
				errorParam.append("Reply");
			}
			// SugStatus.
			if (suggestionDto.getSugStatus() == null || suggestionDto.getSugStatus().isEmpty()) {
				errorParam.append("SugStatus");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = suggestionService.updateSuggestion(suggestionDto, lang);
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
	@GetMapping("/getAllSuggestions")
	public List<SuggestionDto> getAllSuggestions(@RequestHeader(name = "lang", required = false) String lang) {

		List<SuggestionDto> suggestionsDtoList = new ArrayList<SuggestionDto>();
		try {
			suggestionsDtoList = suggestionService.getAllSuggestions();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return suggestionsDtoList;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllSuggestionIds")
	public List<String> getAllSuggestionIds(@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return suggestionService.getAllSuggestionIds();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<String>();
	}

	/**
	 * @param suggestionId
	 * @return
	 */
	@GetMapping("/getSuggestion/{suggestionId}")
	public SuggestionDto getSuggestionById(@PathVariable String suggestionId,
			@RequestHeader(name = "lang", required = false) String lang) {

		SuggestionDto suggestionDto = new SuggestionDto();
		try {
			suggestionDto = suggestionService.getSuggestionById(suggestionId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return suggestionDto;
	}

	/**
	 * @param suggestionId
	 * @return
	 */
	@DeleteMapping("/deleteSuggestionById/{suggestionId}")
	public Boolean deleteSuggestionById(@PathVariable String suggestionId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			suggestionService.deleteSuggestionById(suggestionId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param key
	 * @return
	 */
	@PutMapping("/searchSuggestion")
	public List<SuggestionDto> searchSuggestion(@RequestBody SuggestionSearchDto suggestionSearchDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return suggestionService.searchSuggestion(suggestionSearchDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<SuggestionDto>();
	}
}
