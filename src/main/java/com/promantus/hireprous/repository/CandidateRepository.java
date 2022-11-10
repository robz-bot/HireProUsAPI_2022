/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Candidate;

/**
 * @author Sihab.
 *
 */
public interface CandidateRepository extends MongoRepository<Candidate, String> {

	/**
	 * @param candidateId
	 * @return
	 */
	Candidate findById(long candidateId);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param firstName
	 * @param lastName
	 * @param sort
	 * @return
	 */
	List<Candidate> findByFirstNameRegexOrLastNameRegex(String firstName, String lastName, Sort sort);

	/**
	 * @param jrNumber
	 * @param email
	 * @param contactNumber
	 * @return
	 */
	Candidate findByJrNumberAndEmailAndContactNumber(String jrNumber, String email, String contactNumber);

	/**
	 * @param jrNumber
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByJrNumber(String jrNumber, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param recStatus
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByRecStatus(String recStatus, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param jrNumber
	 * @param recStatus
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByJrNumberAndRecStatus(String jrNumber, String recStatus, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param recStatusList
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByRecStatusIn(List<String> recStatusList, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param jrNumber
	 * @param recStatusList
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByJrNumberAndRecStatusIn(String jrNumber, List<String> recStatusList,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param string
	 * @param string2
	 * @param recStatusList
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByFirstNameRegexOrLastNameRegexAndRecStatusIn(String string, String string2,
			List<String> recStatusList, Sort orderByUpdatedDateTimeDesc);

	List<Candidate> findByJrNumberAndId(String jrNumber, long candidateId, Sort orderByUpdatedDateTimeDesc);

	List<Candidate> findByCreatedDateTimeBetween(LocalDateTime now, LocalDateTime now2,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param jrNumbers
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByJrNumberIn(List<String> jrNumbers, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param recStatusOnboarded
	 * @return
	 */
	Long countByRecStatus(String recStatusOnboarded);

	/**
	 * @param jrNumber
	 * @param email
	 * @param contactNumber
	 * @return
	 */
	Candidate findByJrNumberAndEmailIgnoreCaseAndContactNumber(String jrNumber, String email, String contactNumber);

	/**
	 * @param candidateIds
	 * @param recStatusList
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByIdInAndRecStatusIn(List<Long> candidateIds, List<String> recStatusList,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param jrNumber
	 * @param email
	 * @return
	 */
	Candidate findByJrNumberAndEmailIgnoreCase(String jrNumber, String email);

	/**
	 * @param jrNumber
	 * @param contactNumber
	 * @return
	 */
	Candidate findByJrNumberAndContactNumber(String jrNumber, String contactNumber);

	/**
	 * @param recStatusList
	 * @param vendorId
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByRecStatusInAndVendorId(List<String> recStatusList, long vendorId,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param vendorId
	 * @return
	 */
	List<Candidate> findByVendorId(long vendorId);

	/**
	 * @param recStatusSelected
	 * @param vendorId
	 * @return
	 */
	Long countByRecStatusAndVendorId(String recStatusSelected, long vendorId);

	/**
	 * @param jrNumbers
	 * @param vendorId
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Candidate> findByJrNumberInAndVendorId(List<String> jrNumbers, Long vendorId, Sort orderByUpdatedDateTimeDesc);

	List<Candidate> findByJrNumberAndVendorId(List<String> jrNumbers, Long vendorId, Sort orderByUpdatedDateTimeDesc);

	Candidate findByEmailIgnoreCase(String email);

	Candidate findByContactNumber(String contactNumber);

	List<Candidate> findByJrNumberAndVendorId(String jrNumber, long parseLong);

	List<Candidate> findByVendorId(long parseLong, Sort orderByUpdatedDateTimeDesc);

	List<Candidate> findByJrNumberInAndVendorId(List<String> jrNumber, long parseLong);

//	List<Candidate> findByJrNumberInAndVendorId(List<String> jrNumber2, long parseLong);



}
