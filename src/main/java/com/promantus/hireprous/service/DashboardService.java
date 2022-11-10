/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.BUsCountDto;
import com.promantus.hireprous.dto.JobRequestAgingCountDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.JobRequestStagesCountDto;
import com.promantus.hireprous.dto.WidgetDto;

/**
 * @author Sihab.
 *
 */
public interface DashboardService {

	/**
	 * @param userId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<WidgetDto> getWidgetData(String userId, String lang) throws Exception;

	/**
	 * @param lang
	 * @return
	 */
	List<BUsCountDto> getBUsAndJobRequestCount(String lang);

	/**
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getLatestJobRequests(String lang) throws Exception;

	/**
	 * @param lang
	 * @return
	 */
	JobRequestStagesCountDto getAllJobRequestStagesCount(String lang);

	/**
	 * @param buId
	 * @param lang
	 * @return
	 */
	JobRequestStagesCountDto getAllJobRequestStagesCountByBuId(String buId, String lang);

	/**
	 * @param lang
	 * @return
	 */
	JobRequestAgingCountDto getJobRequestAgingCount(String lang);

	/**
	 * @param buId
	 * @param lan
	 * @return
	 */
	JobRequestAgingCountDto getJobRequestAgingCountByBuId(String buId, String lan);

	/**
	 * @param vendorId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<WidgetDto> getWidgetDataForVendor(String vendorId, String lang) throws Exception;

	/**
	 * @param vendorId
	 * @param lang
	 * @return
	 */
	List<BUsCountDto> getBUsAndJobRequestCountForVendor(String vendorId, String lang);

	/**
	 * @param vendorId
	 * @param lang
	 * @return
	 */
	JobRequestAgingCountDto getJobRequestAgingCountForVendor(String vendorId, String lang);

	/**
	 * @param buId
	 * @param vendorId
	 * @param lan
	 * @return
	 */
	JobRequestAgingCountDto getJobRequestAgingCountByBuIdForVendor(String buId, String vendorId, String lan);

	/**
	 * @param vendorId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getLatestJobRequestsForVendor(String vendorId, String lang) throws Exception;

}
