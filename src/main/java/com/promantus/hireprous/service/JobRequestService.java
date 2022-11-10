/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.JobRequestSearchDto;

/**
 * @author Sihab.
 *
 */
public interface JobRequestService {

	/**
	 * @param jobRequestDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	JobRequestDto addJobRequest(final JobRequestDto jobRequestDto, String lang) throws Exception;

	/**
	 * @param jobRequestDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	JobRequestDto updateJobRequest(final JobRequestDto jobRequestDto, final String lang) throws Exception;

	/**
	 * @param jobRequestId
	 * @return
	 * @throws Exception
	 */
	JobRequestDto getJobRequestById(String jobRequestId) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getAllJobRequests() throws Exception;

	/**
	 * @param customerId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getJobRequestsByCustomerId(String customerId, String lang) throws Exception;

	/**
	 * @param buId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getJobRequestsByBuId(String buId, String lang) throws Exception;

	/**
	 * @param roleId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getJobRequestsByRoleId(String roleId, String lang) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> searchJobRequest(String keyword) throws Exception;

	/**
	 * @param jobRequestId
	 * @return
	 * @throws Exception
	 */
	JobRequestDto deleteJobRequestById(String jobRequestId) throws Exception;

	/**
	 * @param jobRequestId
	 * @param status
	 * @param userId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	JobRequestDto updateJRStatus(Long jobRequestId, String status, Long userId, String lang) throws Exception;

	/**
	 * @param jrNumber
	 * @param status
	 * @param userId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	JobRequestDto updateJRStatusByJRNumber(String jrNumber, String status, Long userId, String lang) throws Exception;

	/**
	 * @param recId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getJobRequestsByRecruiterId(String recId, String lang) throws Exception;

	/**
	 * @param jobRequestId
	 * @param recruiterId
	 * @param userId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	JobRequestDto updateRecruiter(String jobRequestId, String recruiterId, String userId, String lang) throws Exception;

	/**
	 * @param jrNumber
	 * @return
	 * @throws Exception
	 */
	JobRequestDto getJobRequestByJRNumber(String jrNumber) throws Exception;

	/**
	 * @param jrNumber
	 * @return
	 * @throws Exception
	 */
	JobRequestDto getJobRequestInfoByJRNumber(String jrNumber) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getAllJobRequestNumbers() throws Exception;

	/**
	 * @param jrNumber
	 * @throws Exception
	 */
	void updateClosedOpening(String jrNumber) throws Exception;

	/**
	 * @param employmentFor
	 * @return
	 * @throws Exception
	 */
	List<String> getJRNumbersByPlacementFor(String employmentFor) throws Exception;

	/**
	 * @param status
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getJobRequestsByStatus(String status, String lang) throws Exception;

	/**
	 * @param jobRequestSearchDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> searchJobRequest(JobRequestSearchDto jobRequestSearchDto, String lang) throws Exception;

	/**
	 * @param buId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<String> getActiveJobRequestNumbersByBuId(String buId, String lang) throws Exception;

	/**
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getLatestJobRequests(String lang) throws Exception;

	/**
	 * @param jrNumber
	 * @return
	 * @throws Exception
	 */
	String getRecruiterEmailByJRNumber(String jrNumber) throws Exception;

	/**
	 * @param jrNumber
	 * @return
	 * @throws Exception
	 */
	Long getBuIdByJRNumber(String jrNumber) throws Exception;

	/**
	 * @param userId
	 * @return
	 */
	int getUserDependencyCount(Long userId);

	/**
	 * @param buId
	 * @return
	 */
	int getBUDependencyCount(Long buId);

	/**
	 * @param customerId
	 * @return
	 */
	int getCustomerDependencyCount(Long customerId);

	/**
	 * @param userId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getMyJobRequests(Long userId, String lang) throws Exception;

	/**
	 * @param jobRequestSearchDto
	 * @param userId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> searchMyJobRequests(JobRequestSearchDto jobRequestSearchDto, Long userId, String lang)
			throws Exception;

	/**
	 * @param userId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	Long getMyJobRequestsCount(Long userId, String lang) throws Exception;

	/**
	 * @param jobRequestId
	 * @param vendorId
	 * @param userId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	JobRequestDto updateVendor(String jobRequestId, String vendorId, String userId, String lang) throws Exception;

	/**
	 * @param vendorId
	 * @return
	 */
	int getVendorDependencyCount(Long vendorId);

	/**
	 * @param vendorId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getJobRequestsByVendorId(String vendorId, String lang) throws Exception;

	/**
	 * @param vendorId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	Long getMyJobRequestsCountForVendor(Long vendorId, String lang) throws Exception;

	/**
	 * @param vendorId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getLatestJobRequestsForVendor(String vendorId, String lang) throws Exception;
	
	/**
	* @param jobRequestDtoList
	* @param lang
	* @return
	* @throws Exception
	*/
	byte[] downloadJobRequestDetails(List<JobRequestDto> jobRequestDtoList, String lang) throws Exception;



	/**
	* @param jobRequestSearchDto
	* @param lang
	* @return
	* @throws Exception
	*/
	List<JobRequestDto> searchJobRequestForDownload(JobRequestSearchDto jobRequestSearchDto, String lang)
	throws Exception;

	List<JobRequestDto> getJobRequestsByVendorPriority(String vendorPriority, String lang) throws Exception;

}
