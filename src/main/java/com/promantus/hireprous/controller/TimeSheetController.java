/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;



import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.SearchDto;
import com.promantus.hireprous.dto.TimeSheetDto;
import com.promantus.hireprous.service.TimeSheetService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle Projects related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class TimeSheetController extends CommonController {
	
	private static final Logger logger = LoggerFactory.getLogger(TimeSheetController.class);

	@Autowired
	private TimeSheetService timeSheetService;

	@PostMapping("/addTimesheet")
	public TimeSheetDto addTimesheet(@RequestBody TimeSheetDto timeSheetDto,
			@RequestHeader(name = "lang", required = false) String lang) {
		
		TimeSheetDto resultDto = new TimeSheetDto();
		try {
			StringBuilder errorParam = new StringBuilder();
			
			// Time Sheet Date
			if (timeSheetDto.getDate() == null || timeSheetDto.getDate().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("Date is required");
				return resultDto;
			}
			// Project Id.
			if (timeSheetDto.getProjectId() == 0 ) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("project name is invalid");
				return resultDto;
			}
			// User Id.
			if (timeSheetDto.getUserId() == 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("project name is invalid");
				return resultDto;
			}
			// Manager Id.
			if (timeSheetDto.getManagerId() == 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("managerId is required");
				return resultDto;
			}
			// Task.
			if (timeSheetDto.getTask() == null || timeSheetDto.getTask().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("Task is invalid");
				return resultDto;
			}
			// Start Time.
			if (timeSheetDto.getStartTime() == null || timeSheetDto.getStartTime().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("startTime is required");
				return resultDto;
			}
			// End Time.
			if (timeSheetDto.getEndTime() == null || timeSheetDto.getEndTime().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("endTime is required");
				return resultDto;
			}
			// Calculated Hours.
			if (timeSheetDto.getCalHrs() == null || timeSheetDto.getCalHrs().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("calculated hours is required");
				return resultDto;
			}
			// Description.
			if (timeSheetDto.getDescription() == null || timeSheetDto.getDescription().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("Description is required");
				return resultDto;
			}
		
			resultDto = timeSheetService.addTimeSheet(timeSheetDto, lang);
		} catch (Exception e) {
			
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}
		
		return resultDto;
	}
	/**
	 * @param projectDto
	 * @return
	 */
	@PutMapping("/updateTimeSheet")
	public TimeSheetDto updateTimeSheet(@RequestBody TimeSheetDto timeSheetDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		TimeSheetDto resultDto = new TimeSheetDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// TimeSheet Id.
			if (timeSheetDto.getId() == 0) {
				errorParam.append("TimeSheet Id is required");
			}
			// Time Sheet Date
			if (timeSheetDto.getDate() == null || timeSheetDto.getDate().isEmpty()) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("Date is required");
				return resultDto;
			}
				// Project Id.
				if (timeSheetDto.getProjectId() == 0 ) {
					resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
					resultDto.setMessage("project name is invalid");
					return resultDto;
				}
				// User Id.
				if (timeSheetDto.getUserId() == 0) {
					resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
					resultDto.setMessage("project name is invalid");
					return resultDto;
				}
				// Manager Id.
				if (timeSheetDto.getManagerId() == 0) {
					resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
					resultDto.setMessage("managerId is required");
					return resultDto;
				}
				// Task.
				if (timeSheetDto.getTask() == null || timeSheetDto.getTask().isEmpty()) {
					resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
					resultDto.setMessage("Task is invalid");
					return resultDto;
				}
				// Start Time.
				if (timeSheetDto.getStartTime() == null || timeSheetDto.getStartTime().isEmpty()) {
					resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
					resultDto.setMessage("startTime is required");
					return resultDto;
				}
				// End Time.
				if (timeSheetDto.getEndTime() == null || timeSheetDto.getEndTime().isEmpty()) {
					resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
					resultDto.setMessage("endTime is required");
					return resultDto;
				}
				// Calculated Hours.
				if (timeSheetDto.getCalHrs() == null || timeSheetDto.getCalHrs().isEmpty()) {
					resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
					resultDto.setMessage("calculated hours is required");
					return resultDto;
				}
				// Description.
				if (timeSheetDto.getDescription() == null || timeSheetDto.getDescription().isEmpty()) {
					resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
					resultDto.setMessage("Description is required");
					return resultDto;
				}

			resultDto = timeSheetService.updateTimeSheet(timeSheetDto, lang);
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
			resultDto.setMessage("TimeSheet Updated Successfully");

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		return resultDto;
	}

	@PutMapping("/submitTimeSheet")
	public TimeSheetDto submitTimeSheet(@RequestBody List<TimeSheetDto> timeSheetDtoList,
			@RequestHeader(name = "lang", required = false) String lang) {

		TimeSheetDto resultDto = new TimeSheetDto();
		
		try {
			
			resultDto = timeSheetService.submitTimeSheet(timeSheetDtoList , lang);
			return resultDto;
		} catch (Exception e) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}
	}
	
	@GetMapping("/getAllTimeSheets")
	public List<TimeSheetDto> getAllTimeSheets(@RequestHeader(name = "lang", required = false) String lang) {

		List<TimeSheetDto> timeSheetsDtoList = new ArrayList<TimeSheetDto>();
		try {
			timeSheetsDtoList = timeSheetService.getAllTimeSheets();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return timeSheetsDtoList;
	}
	
	@GetMapping("/getTimeSheet/{timeSheetId}")
	public TimeSheetDto getTimeSheetById(@PathVariable String timeSheetId,
			@RequestHeader(name = "lang", required = false) String lang) {

		TimeSheetDto timeSheetDto = new TimeSheetDto();
		try {
			
			timeSheetDto = timeSheetService.getTimeSheetById(timeSheetId);
			
		} catch (final Exception e) {
			
			logger.error(HireProUsUtil.getErrorMessage(e));
		}
		return timeSheetDto;
	}
	
	@GetMapping("/getTimeSheetByUserIdAndApproval/{userId}/{isApproval}")
	public List<TimeSheetDto> getTimeSheetByUserIdAndApproval(@PathVariable String userId,@PathVariable boolean isApproval,
			@RequestHeader(name = "lang", required = false) String lang) {

		List<TimeSheetDto> timeSheetDto = new ArrayList<TimeSheetDto>();
		try {
			
			timeSheetDto = timeSheetService.getTimeSheetByUserIdAndApproval(userId,isApproval);
			
		} catch (final Exception e) {
			
			logger.error(HireProUsUtil.getErrorMessage(e));
		}
		return timeSheetDto;
	}
	
	@DeleteMapping("/deleteTimeSheetById/{timeSheetId}")
	public Boolean deleteTimeSheetById(@PathVariable String timeSheetId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			timeSheetService.deleteTimeSheetById(timeSheetId);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return true;
	}
	@GetMapping("/getAllEmplyeeName")
	public List<String> getAllEmployeeName(@RequestHeader(name = "lang", required = false) String lang) {

//		Map<String, Long> employeeList = new HashMap<>();
		try {
			return timeSheetService.getAllEmployeeName();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}
		return null;

//		return employeeList;
	}
	@GetMapping("/getAllTimeSheetByManagerId/{managerId}")
	public List<TimeSheetDto> getAllTimeSheetByManagerId (@PathVariable String managerId,
				@RequestHeader(name = "lang", required = false) String lang){
		
		List<TimeSheetDto> timeSheetDto = new ArrayList<TimeSheetDto>();
		try {
			
			timeSheetDto = timeSheetService.getAllTimeSheetByManagerId(Long.parseLong(managerId));
			
		} catch (final Exception e) {
			
			logger.error(HireProUsUtil.getErrorMessage(e));
		}
		return timeSheetDto;
	}
	@GetMapping("/searchTimeSheetByUserId/{userId}")
	public List<TimeSheetDto> searchTimeSheetByUserId (@PathVariable String userId,
			@RequestHeader(name = "lang", required = false) String lang){
		List<TimeSheetDto> timeSheetDto = new ArrayList<>();
		try {
			
			timeSheetDto = timeSheetService.searchTimeSheetByUserId(userId);
			
		} catch (final Exception e) {
		
		logger.error(HireProUsUtil.getErrorMessage(e));
		
		}
		return timeSheetDto;
	}


	@PutMapping("/approveTimeSheet")
	public TimeSheetDto approveTimeSheet(@RequestBody TimeSheetDto timeSheetDto,
	            @RequestHeader(name = "lang", required = false) String lang) {

	    TimeSheetDto resultDto = new TimeSheetDto();
	    try {
	    
	        resultDto = timeSheetService.approveTimeSheet(timeSheetDto , lang);

	        return resultDto;

	    } catch (Exception e) {

	        resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
	        resultDto.setMessage(e.getMessage());
	        logger.error(HireProUsUtil.getErrorMessage(e));

	        return resultDto;
	    }
	}
	
	@PutMapping("/searchByDate")
	public List<TimeSheetDto> searchByDate (@RequestBody SearchDto searchDto,
			@RequestHeader(name = "lang", required = false) String lang) {
		
		List<TimeSheetDto> resultDto = new ArrayList<>();
		try {
		    
	        resultDto = timeSheetService.searchByDate(searchDto,lang);

	        return resultDto;

	    } catch (Exception e) {

//	        resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
//	        resultDto.setMessage(e.getMessage());
	        logger.error(HireProUsUtil.getErrorMessage(e));

	        return resultDto;
	    }
	}
	
}

