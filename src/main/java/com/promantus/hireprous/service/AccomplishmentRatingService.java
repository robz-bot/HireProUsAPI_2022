package com.promantus.hireprous.service;

import java.io.IOException;
import java.util.List;

import com.promantus.hireprous.dto.AccomplishmentRatingDto;

public interface AccomplishmentRatingService {

	AccomplishmentRatingDto addAccomplishmentRating(AccomplishmentRatingDto accomplishmentRatingDto, String lang) throws Exception;

	AccomplishmentRatingDto updateAccomplishmentRating(AccomplishmentRatingDto accomplishmentRatingDto, String lang) throws Exception;

	byte[] downloadAccomplishReport(List<AccomplishmentRatingDto> accomplishmentDtoList, String lang) throws IOException;

	List<AccomplishmentRatingDto> searchAccomplishments(AccomplishmentRatingDto accomplishmentDto, List<String> roleIds) throws Exception;

	List<AccomplishmentRatingDto> getResourceAccomplishmentsByRoleIds(List<String> roleIds) throws Exception;

}
