/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.CustomerDto;
import com.promantus.hireprous.service.CustomerService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Customers related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class CustomerController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	@Value("${download.path}")
	private String downloadsPath;

	/**
	 * @param customerDto
	 * @return
	 */
	@PostMapping("/addCustomer")
	public CustomerDto addCustomer(@RequestBody CustomerDto customerDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		CustomerDto resultDto = new CustomerDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Customer Name.
			if (customerDto.getCustomerName() == null || customerDto.getCustomerName().isEmpty()) {
				errorParam.append("Customer Name");
			}
			// Location.
			if (customerDto.getLocation() == null || customerDto.getLocation().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Location" : "Location");
			}
			// Region.
			if (customerDto.getRegion() == null || customerDto.getRegion().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Region" : "Region");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = customerService.addCustomer(customerDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @param customerName
	 * @param lang
	 * @return
	 */
	@PostMapping("/checkCustomer")
	public Boolean checkCustomer(@RequestBody CustomerDto customerDto,
			@RequestHeader(name = "lang", required = false) String lang) {
		try {
			return customerService.checkCustomer(customerDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}

	/**
	 * @param customerDto
	 * @return
	 */
	@PutMapping("/updateCustomer")
	public CustomerDto updateCustomer(@RequestBody CustomerDto customerDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		CustomerDto resultDto = new CustomerDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Customer Id.
			if (customerDto.getId() == 0) {
				errorParam.append("Customer Id");
			}
			// Customer Name.
			if (customerDto.getCustomerName() == null || customerDto.getCustomerName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Customer Name" : "Customer Name");
			}
			// Location.
			if (customerDto.getLocation() == null || customerDto.getLocation().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Location" : "Location");
			}
			// Region.
			if (customerDto.getRegion() == null || customerDto.getRegion().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Region" : "Region");
			}

			if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = customerService.updateCustomer(customerDto, lang);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * @return
	 */
	@GetMapping("/getAllCustomers")
	public List<CustomerDto> getAllCustomers(@RequestHeader(name = "lang", required = false) String lang) {

		List<CustomerDto> customersDtoList = new ArrayList<CustomerDto>();
		try {
			customersDtoList = customerService.getAllCustomers();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return customersDtoList;
	}

	/**
	 * @param customerId
	 * @return
	 */
	@GetMapping("/getCustomer/{customerId}")
	public CustomerDto getCustomerById(@PathVariable String customerId,
			@RequestHeader(name = "lang", required = false) String lang) {

		CustomerDto customerDto = new CustomerDto();
		try {
			customerDto = customerService.getCustomerById(customerId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return customerDto;
	}

	/**
	 * @param customerId
	 * @return
	 */
	@DeleteMapping("/deleteCustomerById/{customerId}")
	public CustomerDto deleteCustomerById(@PathVariable String customerId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return customerService.deleteCustomerById(customerId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new CustomerDto();
	}

	/**
	 * @param key
	 * @return
	 */
	@GetMapping("/searchCustomer")
	public List<CustomerDto> searchCustomer(@RequestParam String key,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<CustomerDto> customersDtoList = new ArrayList<CustomerDto>();
		try {
			customersDtoList = customerService.searchCustomer(key);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return customersDtoList;
	}

	@PostMapping("/searchCustomerForDownload")
	public List<CustomerDto> searchCustomerForDownload(@RequestBody CustomerDto customerDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return customerService.searchCustomerForDownload(customerDto);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<CustomerDto>();
	}

	@PutMapping("/downloadCustomerDetails")
	public void downloadCustomerDetails(@RequestBody List<CustomerDto> customerDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File customerFile = new File(downloadsPath + "HireProUs_Customer_details.xlsx");
			FileUtils.writeByteArrayToFile(customerFile,
					customerService.downloadCustomerDetails(customerDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + customerFile.getName());
			response.setContentLength((int) customerFile.length());

			inStream = new BufferedInputStream(new FileInputStream(customerFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			customerFile.deleteOnExit();

		} catch (final Exception e) {

			logger.error(HireProUsUtil.getErrorMessage(e));

		} finally {
			try {
				if (outStream != null) {
					outStream.flush();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				logger.error(HireProUsUtil.getErrorMessage(e));
			}
		}
	}

}
