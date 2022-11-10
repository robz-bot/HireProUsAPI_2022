/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.JobRequest;


/**
 * @author Sihab.
 *
 */
public interface JobRequestRepository extends MongoRepository<JobRequest, String> {

	/**
	 * @param jobRequestId
	 * @return
	 */
	JobRequest findById(long jobRequestId);

	/**
	 * @param parseLong
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByCustomerId(long parseLong, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param parseLong
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByBuId(long parseLong, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param parseLong
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByRoleId(long parseLong, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param string
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> findByReferenceNumberRegex(String string, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param parseLong
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByRecruiterId(long parseLong, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param year
	 * @return
	 */
	JobRequest findFirstByYearOrderByRunningNumberDesc(int year);

	/**
	 * @param year
	 * @param nextCounterAndRunningNumber
	 * @return
	 */
	JobRequest findFirstByYear(int year, Sort nextCounterAndRunningNumber);

	/**
	 * @param jrNumber
	 * @return
	 */
	JobRequest getJobRequestByReferenceNumber(String jrNumber);

	/**
	 * @param employmentFor
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByPlacementFor(String employmentFor, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param employmentFor
	 * @param jobRequestStatusInProgress
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByPlacementForAndJobReqStatus(String employmentFor, String jobRequestStatusInProgress,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param status
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByJobReqStatus(String status, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param ProjectStartDate
	 * @param ProjectEndDate
	 * @return
	 */
	List<JobRequest> findByProjectStartDateBetween(LocalDate ProjectStartDate, LocalDate ProjectEndDate);

	/**
	 * @param jobReqStatus
	 * @return
	 */
	List<JobRequest> getJobRequestByJobReqStatus(String jobReqStatus);

	/**
	 * @param jobRequestStatusInProgress
	 * @return
	 */
	Long countByJobReqStatus(String jobRequestStatusInProgress);

	/**
	 * @param jobRequestStatusInProgress
	 * @param buId
	 * @return
	 */
	Long countByJobReqStatusAndBuId(String jobRequestStatusInProgress, long buId);

	/**
	 * @param jrNumber
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> findTop10ByReferenceNumberRegex(String jrNumber, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param parseLong
	 * @param jobRequestStatusInProgress
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByBuIdAndJobReqStatus(long parseLong, String jobRequestStatusInProgress,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param jobRequestStatusInProgress
	 * @param jobRequestStatusYetToStart
	 * @param id
	 * @return
	 */
	Long countByJobReqStatusOrJobReqStatusAndBuId(String jobRequestStatusInProgress, String jobRequestStatusYetToStart,
			long id);

	/**
	 * @param jobRequestStatusInProgress
	 * @param jobRequestStatusYetToStart
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByJobReqStatusOrJobReqStatus(String jobRequestStatusInProgress,
			String jobRequestStatusYetToStart, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param parseLong
	 * @param jobRequestStatusInProgress
	 * @param jobRequestStatusYetToStart
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByBuIdAndJobReqStatusOrJobReqStatus(long parseLong, String jobRequestStatusInProgress,
			String jobRequestStatusYetToStart, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param string
	 * @param orderByCreatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> findTop5ByReferenceNumberRegex(String string, Sort orderByCreatedDateTimeDesc);

	/**
	 * @param jobRequestStatusInProgress
	 * @param jobRequestStatusYetToStart
	 * @return
	 */
	Long countByJobReqStatusOrJobReqStatus(String jobRequestStatusInProgress, String jobRequestStatusYetToStart);

	/**
	 * @param string
	 * @param asList
	 * @param orderByCreatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> findTop5ByReferenceNumberRegexAndJobReqStatusIn(String string, List<String> asList,
			Sort orderByCreatedDateTimeDesc);

	/**
	 * @param userId
	 * @param userId2
	 * @return
	 */
	Long countByRequesterIdOrRecruiterId(Long userId, Long userId2);

	/**
	 * @param userId
	 * @param userId4
	 * @param userId3
	 * @param userId2
	 * @return
	 */
	int countByRequesterIdOrRecruiterIdOrCreatedByOrUpdatedBy(Long userId, Long userId2, Long userId3, Long userId4);

	/**
	 * @param buId
	 * @param asList
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByBuIdAndJobReqStatusIn(long buId, List<String> asList,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param asList
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByJobReqStatusIn(List<String> asList, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param buId
	 * @return
	 */
	int countByBuId(Long buId);

	/**
	 * @param customerId
	 * @return
	 */
	int countByCustomerId(Long customerId);

	/**
	 * @param parseLong
	 * @param parseLong2
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByRecruiterIdOrRequesterId(long parseLong, long parseLong2,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param vendorId
	 * @param asList
	 * @return
	 */
	int countByVendorIdAndJobReqStatusIn(Long vendorId, List<String> asList);

	/**
	 * @param vendorId
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByVendorId(long vendorId, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param asList
	 * @param vendorId
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByJobReqStatusInAndVendorId(List<String> asList, long vendorId,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param parseLong
	 * @param asList
	 * @param vendorId
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> getJobRequestByBuIdAndJobReqStatusInAndVendorId(long parseLong, List<String> asList, long vendorId,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param string
	 * @param asList
	 * @param vendorId
	 * @param orderByCreatedDateTimeDesc
	 * @return
	 */
	List<JobRequest> findTop5ByReferenceNumberRegexAndJobReqStatusInAndVendorId(String string, List<String> asList,
			long vendorId, Sort orderByCreatedDateTimeDesc);

	List<JobRequest> getJobRequestByVendorPriority(String string, Sort orderByUpdatedDateTimeDesc);

	List<JobRequest> findByBuId(long id);

}
