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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.SuggestionDto;
import com.promantus.hireprous.dto.SuggestionSearchDto;
import com.promantus.hireprous.entity.Suggestion;
import com.promantus.hireprous.repository.SuggestionRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.MailService;
import com.promantus.hireprous.service.SuggestionService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class SuggestionServiceImpl implements SuggestionService {

	private static final Logger logger = LoggerFactory.getLogger(SuggestionServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	MailService mailService;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	SuggestionRepository suggestionRepository;

	@Override
	public SuggestionDto addSuggestion(final SuggestionDto suggestionDto, String lang) throws Exception {

		SuggestionDto resultDto = new SuggestionDto();

		final Suggestion suggestion = new Suggestion();
		suggestion.setId(commonService.nextSequenceNumber());

		suggestionDto.setCode(this.setCode(suggestion, suggestionDto.getType()));

		suggestion.setType(suggestionDto.getType());
		suggestion.setSuggestion(suggestionDto.getSuggestion());
		suggestion.setSugStatus(HireProUsConstants.SUGGESTION_STATUS_OPEN);

		suggestion.setCreatedBy(suggestionDto.getCreatedBy());
		suggestion.setUpdatedBy(suggestionDto.getUpdatedBy());

		suggestion.setCreatedDateTime(LocalDateTime.now());
		suggestion.setUpdatedDateTime(LocalDateTime.now());

		suggestionRepository.save(suggestion);

		suggestionDto.setCreatedDateTime(suggestion.getCreatedDateTime());
		// Send New Suggestion Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendNewSuggestionEmail(suggestionDto);
				} catch (Exception e) {

					logger.error("New Suggestion Email is not Sent");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setCode(suggestionDto.getCode());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	/**
	 * @param suggestion
	 * @param type
	 * @return
	 */
	private String setCode(Suggestion suggestion, String type) {

		String codePrefix = "";
		long codeNumber = 0;
		if (type.equals("Suggestion")) {
			codePrefix = "SG";
		} else {
			codePrefix = "IS";
		}

		Suggestion suggestionForCode = suggestionRepository.findFirstByCodePrefix(codePrefix,
				HireProUsUtil.orderByCodeNumberDesc());

		String code = "";
		if (suggestionForCode == null) {
			codeNumber = codeNumber + 1;
		} else {
			codeNumber = suggestionForCode.getCodeNumber() + 1;
		}

		code = codePrefix + String.format("%0" + HireProUsConstants.SUGGESTION_MAX_DIGIT_CODE_NUMBER + "d", codeNumber);

		suggestion.setCodePrefix(codePrefix);
		suggestion.setCodeNumber(codeNumber);

		return code;
	}

	@Override
	public SuggestionDto updateSuggestion(final SuggestionDto suggestionDto, String lang) throws Exception {

		SuggestionDto resultDto = new SuggestionDto();

		Suggestion suggestion = suggestionRepository.findById(suggestionDto.getId());

		if (suggestion == null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Suggestion Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		suggestion.setType(suggestionDto.getType());
		suggestion.setReply(suggestionDto.getReply());
		suggestion.setSugStatus(suggestionDto.getSugStatus());

		suggestion.setUpdatedBy(suggestionDto.getUpdatedBy());
		suggestion.setUpdatedDateTime(LocalDateTime.now());

		suggestionRepository.save(suggestion);

		suggestionDto.setCreatedBy(suggestion.getCreatedBy());
		suggestionDto.setCreatedDateTime(suggestion.getCreatedDateTime());
		suggestionDto.setUpdatedDateTime(suggestion.getUpdatedDateTime());
		// Send Update Suggestion Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendSuggestionUpdatedEmail(suggestionDto);
				} catch (Exception e) {

					logger.error("Suggestion Updated Email is not Sent");
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<SuggestionDto> getAllSuggestions() throws Exception {

		List<SuggestionDto> suggestionDtoList = new ArrayList<SuggestionDto>();

		List<Suggestion> suggestionsList = suggestionRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Suggestion suggestion : suggestionsList) {
			suggestionDtoList.add(this.getSuggestionDto(suggestion));
		}

		return suggestionDtoList;
	}

	@Override
	public List<String> getAllSuggestionIds() throws Exception {

		List<String> suggestionIdList = new ArrayList<String>();

		List<Suggestion> suggestionsList = suggestionRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (Suggestion suggestion : suggestionsList) {
			suggestionIdList.add(suggestion.getCodePrefix() + String.format(
					"%0" + HireProUsConstants.SUGGESTION_MAX_DIGIT_CODE_NUMBER + "d", suggestion.getCodeNumber()));
		}

		return suggestionIdList;
	}

	@Override
	public SuggestionDto getSuggestionById(final String suggestionId) throws Exception {

		Suggestion suggestion = suggestionRepository.findById(Long.parseLong(suggestionId));

		return suggestion != null ? this.getSuggestionDto(suggestion) : new SuggestionDto();
	}

	/**
	 * @param suggestion
	 * @return
	 * @throws Exception
	 */
	private SuggestionDto getSuggestionDto(final Suggestion suggestion) throws Exception {

		SuggestionDto suggestionDto = new SuggestionDto();

		suggestionDto.setId(suggestion.getId());
		suggestionDto.setCode(suggestion.getCodePrefix() + String
				.format("%0" + HireProUsConstants.SUGGESTION_MAX_DIGIT_CODE_NUMBER + "d", suggestion.getCodeNumber()));
		suggestionDto.setType(suggestion.getType());
		suggestionDto.setSuggestion(suggestion.getSuggestion());
		suggestionDto.setReply(suggestion.getReply());
		suggestionDto.setSugStatus(suggestion.getSugStatus());

		suggestionDto.setCreatedBy(suggestion.getCreatedBy());
		suggestionDto.setCreatedByName(CacheUtil.getUsersMap().get(suggestion.getCreatedBy()));
		suggestionDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(suggestion.getCreatedDateTime()));

		suggestionDto.setUpdatedBy(suggestion.getUpdatedBy());
		suggestionDto.setUpdatedByName(CacheUtil.getUsersMap().get(suggestion.getUpdatedBy()));
		suggestionDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(suggestion.getUpdatedDateTime()));

		return suggestionDto;
	}

	@Override
	public void deleteSuggestionById(final String suggestionId) throws Exception {

		suggestionRepository.deleteById(Long.parseLong(suggestionId));
	}

	@Override
	public List<SuggestionDto> searchSuggestion(final SuggestionSearchDto suggestionSearchDto) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();

		if (suggestionSearchDto.getType() != null && !suggestionSearchDto.getType().isEmpty()) {
			criteriaList.add(Criteria.where("type").is(suggestionSearchDto.getType()));
		}
		if (suggestionSearchDto.getSugStatus() != null && !suggestionSearchDto.getSugStatus().isEmpty()) {
			criteriaList.add(Criteria.where("sugStatus").is(suggestionSearchDto.getSugStatus()));
		}
		if (suggestionSearchDto.getCode() != null && !suggestionSearchDto.getCode().isEmpty()) {
			criteriaList.add(Criteria.where("codePrefix").is(suggestionSearchDto.getCode().substring(0, 2)));
			criteriaList
					.add(Criteria.where("codeNumber").is(Long.parseLong(suggestionSearchDto.getCode().substring(2))));
		}

		List<Suggestion> suggestionsList = new ArrayList<Suggestion>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			suggestionsList = mongoTemplate.find(searchQuery, Suggestion.class);
		}

		List<SuggestionDto> suggestionDtoList = new ArrayList<SuggestionDto>();
		for (Suggestion suggestion : suggestionsList) {
			suggestionDtoList.add(this.getSuggestionDto(suggestion));
		}

		return suggestionDtoList;
	}
}
