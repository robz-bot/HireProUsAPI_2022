
/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import com.promantus.hireprous.dto.JobRequestDto;


/**
 * @author Promantus.
 *
 */
public interface ReportJobRequestService {

	List<JobRequestDto> getJobRequestByProjectStartDateBetween(LocalDate projectStartDate,
			LocalDate projectEndDate) throws Exception;

	/**
	 * @param projectStartDate
	 * @param projectEndDate
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getJobRequestByProjectStartDateBetweenExcel(LocalDate projectStartDate,
			LocalDate projectEndDate) throws Exception;

	/**
	 * @param contacts
	 * @return
	 */
	ByteArrayInputStream exportasExel(List<JobRequestDto> contacts);

	/**
	 * @param contacts
	 * @return
	 */
	ByteArrayInputStream exportasPdf(List<JobRequestDto> contacts);

	/**
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getAllJobRequestsReport() throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> getAllJobRequestsReportCount() throws Exception;

	/**
	 * @param jobRequestStatus
	 * @return
	 * @throws Exception
	 */
	List<JobRequestDto> searchJobRequestByStatus(String jobRequestStatus) throws Exception;
}
