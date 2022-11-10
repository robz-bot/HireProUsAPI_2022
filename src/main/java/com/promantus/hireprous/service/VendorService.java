/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.VendorDto;

/**
 * @author Sihab.
 *
 */
public interface VendorService {

	/**
	 * @param vendorDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	VendorDto addVendor(final VendorDto vendorDto, final String lang) throws Exception;

	/**
	 * @param vendorDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	VendorDto updateVendor(final VendorDto vendorDto, final String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<VendorDto> getAllVendors() throws Exception;

	/**
	 * @param vendorId
	 * @return
	 * @throws Exception
	 */
	VendorDto getVendorById(String vendorId) throws Exception;

	/**
	 * @param vendorId
	 * @return
	 * @throws Exception
	 */
	VendorDto deleteVendorById(String vendorId) throws Exception;

	/**
	 * @param vendorDto
	 * @return
	 * @throws Exception
	 */
	List<VendorDto> searchVendor(VendorDto vendorDto) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<String> getAllVendorIds() throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<String> getAllVendorNames() throws Exception;

	/**
	 * @param vendorDtoList
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	byte[] downloadVendorDetails(List<VendorDto> vendorDtoList, String lang) throws Exception;

	/**
	 * @param vendorId
	 * @param password
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	VendorDto loginVendor(String vendorId, String password, String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<VendorDto> getActiveVendors() throws Exception;

	/**
	 * @param vendorDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	VendorDto changePasswordVendor(VendorDto vendorDto, String lang) throws Exception;

	/**
	 * @param vendorId
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	VendorDto checkVendorIdAndSendOtpVendor(String vendorId, String lang) throws Exception;

	/**
	 * @param vendorDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	VendorDto resetPasswordVendor(VendorDto vendorDto, String lang) throws Exception;

}
