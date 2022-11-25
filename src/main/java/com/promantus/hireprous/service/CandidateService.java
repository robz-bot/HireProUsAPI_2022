/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.CandidateDto;
import com.promantus.hireprous.dto.CandidateStatusDto;
import com.promantus.hireprous.dto.CandidatesCountDto;
import com.promantus.hireprous.dto.EvaluateResumeDto;

/**
 * @author Sihab.
 *
 */
public interface CandidateService {

	/**
	 * @param candidateDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CandidateDto addCandidate(final CandidateDto candidateDto, String lang) throws Exception;

	/**
	 * @param candidateDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CandidateDto updateCandidate(final CandidateDto candidateDto, final String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> getAllCandidates() throws Exception;

	/**
	 * @param candidateId
	 * @return
	 * @throws Exception
	 */
	CandidateDto getCandidateById(String candidateId) throws Exception;

	/**
	 * @param candidateId
	 * @return
	 * @throws Exception
	 */
	CandidateDto deleteCandidateById(String candidateId) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> searchCandidate(String keyword) throws Exception;

	/**
	 * @param jrNumber
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> getCandidatesByJRNum(String jrNumber, String lang) throws Exception;

	/**
	 * @param recStatus
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> getCandidatesByRecStatus(String recStatus, String lang) throws Exception;

	/**
	 * @param candidateDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CandidateDto updateRecStatus(final CandidateDto candidateDto, String lang) throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getCandidateNameById(long userId) throws Exception;

	/**
	 * @param jrNumber
	 * @param recStatus
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> getCandidatesByJRNumAndRecStatus(String jrNumber, String recStatus, String lang)
			throws Exception;

	/**
	 * @param recStatusList
	 * @param vendorId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> getCandidatesByRecStatusList(List<String> recStatusList, String vendorId, String lang)
			throws Exception;

	/**
	 * @param jrNumber
	 * @param recStatusList
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> getCandidatesByJRNumAndRecStatusList(String jrNumber, List<String> recStatusList, String lang)
			throws Exception;

	/**
	 * @param candidateId
	 * @return
	 * @throws Exception
	 */
	CandidateDto getCandidateShortInfoById(String candidateId) throws Exception;

	/**
	 * @param candidateId
	 * @param recStatus
	 * @param updatedBy
	 * @return
	 * @throws Exception
	 */
	CandidateDto updateRecStatus(Long candidateId, String recStatus, Long updatedBy) throws Exception;

	List<CandidateDto> getCandidatesByRecStatusList(List<String> passedCandidateList);

	List<CandidateDto> getCandidatesByJRNumAndCandidateId(String jrNum, String candidateId, String lang);

	List<CandidateStatusDto> getCandidatesStatusByJRNum(String jrNumber, String lang) throws Exception;

	List<CandidateStatusDto> getAllCandidateStages(String lang) throws Exception;

	List<CandidateStatusDto> getAllCandidateStagesByBU(String buId, String lang) throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getCandidateEmailById(long userId) throws Exception;

	/**
	 * @param name
	 * @param jrNumber
	 * @param recStatusList
	 * @param vendorId
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> searchCandidateByRecStatusList(String name, String jrNumber, List<String> recStatusList,
			String vendorId) throws Exception;

	/**
	 * @param vendorId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CandidatesCountDto getCandidatesCount(String vendorId, String lang) throws Exception;

	/**
	 * @param candidateName
	 * @return
	 * @throws Exception
	 */
	List<Long> searchCadidateIdsByName(String candidateName) throws Exception;

	/**
	 * @param candidateDtoList
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	byte[] downloadSelectedCandidateDetails(List<CandidateDto> candidateDtoList, String lang) throws Exception;

	/**
	 * @param candidateDto
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> searchCandidateForDownload(CandidateDto candidateDto) throws Exception;

	/**
	 * @param candidateDto
	 * @return
	 * @throws Exception
	 */
	List<CandidateDto> searchRejectedCandidateForDownload(CandidateDto candidateDto) throws Exception;

	/**
	 * @param candidateDtoList
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	byte[] downloadRejectedCandidateDetails(List<CandidateDto> candidateDtoList, String lang) throws Exception;

	CandidatesCountDto getCandidatesCountVendorVsJr(String vendorId, String jrNumber, String lang) throws Exception;

	byte[] downloadCandidateDetails(List<CandidateDto> candidateDtoList, String lang) throws Exception;

	List<CandidateDto> searchAllCandidateForDownload(CandidateDto candidateDto) throws Exception;

	// Added on 1/5/2022
	CandidateDto rejectToUploadedStatus(CandidateDto candidateDto, String jrNumber, String lang);EvaluateResumeDto getEvaluateResume(String jrNumber, String candidateId);

	

}
