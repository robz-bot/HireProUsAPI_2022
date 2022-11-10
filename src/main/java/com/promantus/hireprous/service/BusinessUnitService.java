/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.BusinessUnitDto;
import com.promantus.hireprous.dto.InterviewPanelDto;

/**
 * @author Sihab.
 *
 */
public interface BusinessUnitService {

	/**
	 * @param businessUnitDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	BusinessUnitDto addBusinessUnit(final BusinessUnitDto businessUnitDto, String lang) throws Exception;

	/**
	 * @param businessUnitDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	BusinessUnitDto updateBusinessUnit(final BusinessUnitDto businessUnitDto, String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<BusinessUnitDto> getAllBusinessUnits() throws Exception;

	/**
	 * @param businessUnitId
	 * @return
	 * @throws Exception
	 */
	BusinessUnitDto getBusinessUnitById(String businessUnitId) throws Exception;

	/**
	 * @param businessUnitId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	BusinessUnitDto deleteBusinessUnitById(String businessUnitId, String lang) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<BusinessUnitDto> searchBusinessUnit(String keyword) throws Exception;

	/**
	 * @param buId
	 * @return
	 * @throws Exception
	 */
	String getBusinessUnitNameById(long buId) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<InterviewPanelDto> getAllBUsWithPanel() throws Exception;

	/**
	 * @param buName
	 * @return
	 * @throws Exception
	 */
	Boolean checkBUName(String buName,  long managerId) throws Exception;

	/**
	 * @param buId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	List<InterviewPanelDto> getPanelsByBuId(String buId, String lang) throws Exception;
	
	/**
	* @param businessUnitDtoList
	* @param lang
	* @return
	* @throws Exception
	*/
	byte[] downloadBusinessUnitDetails(List<BusinessUnitDto> businessUnitDtoList, String lang) throws Exception;;



	/**
	* @param businessUnitDto
	* @return
	* @throws Exception
	*/
	List<BusinessUnitDto> searchBusinessUnitForDownload(BusinessUnitDto businessUnitDto) throws Exception;

}
