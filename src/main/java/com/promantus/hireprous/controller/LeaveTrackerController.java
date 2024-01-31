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
import com.promantus.hireprous.dto.HolidayDto;
import com.promantus.hireprous.dto.LeaveTrackerDto;
import com.promantus.hireprous.service.LeaveTrackerService;
import com.promantus.hireprous.util.HireProUsUtil;

@RestController
@RequestMapping("/api/v1")
public class LeaveTrackerController extends CommonController{
 
	private static final Logger logger = LoggerFactory.getLogger(LeaveTrackerController.class);
	

	@Autowired
	private LeaveTrackerService leaveTrackerService;
	
	/**
	 * 
	 * @param LeaveTrackerDto
	 * @return
	 */  
	
	@PostMapping("/addLeave")
	public LeaveTrackerDto addLeave(@RequestBody(required = true) LeaveTrackerDto leaveTrackerDto) {
    
		LeaveTrackerDto resultDto = new LeaveTrackerDto();
		try {
			
	      StringBuilder errorParam = new StringBuilder();
		     	 
	      if (leaveTrackerDto.getFromDate() == null || leaveTrackerDto.getFromDate().isEmpty()) {
	    	  errorParam.append("From Date Required");
	      }
	      
	      if (leaveTrackerDto.getToDate() == null || leaveTrackerDto.getToDate().isEmpty()) {
	    	  errorParam.append("To Date Required");
	      }
	      
	      if (leaveTrackerDto.getLeaveType() == null || leaveTrackerDto.getLeaveType().isEmpty()) {
	    	  errorParam.append("Leave Type Required");
	      }
	      if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				
				logger.info(resultDto.getMessage());
				return resultDto;
			}
	      resultDto = leaveTrackerService.addLeave(leaveTrackerDto);
	       
		}catch(final Exception e) {
			 resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			   resultDto.setMessage(e.getMessage());
			   
			   logger.error(HireProUsUtil.getErrorMessage(e));
			   return resultDto;
		   }
		      return resultDto;
	
	}
	
	@GetMapping("/getAllLeave")
	public List<LeaveTrackerDto> getAllLeave(@RequestHeader(name = "lang", required = false) String lang) {

		List<LeaveTrackerDto> leaveTrackerDtoList = new ArrayList<LeaveTrackerDto>();
		try {
			leaveTrackerDtoList = leaveTrackerService.getAllLeave();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return leaveTrackerDtoList;
	}
	
	@GetMapping("/getAllLeaveByMonth/{month}")
	public LeaveTrackerDto getAllLeaveByMonth(@PathVariable String month,
			@RequestHeader(name = "lang", required = false) String lang) {

		LeaveTrackerDto LeaveTrackerDto = new LeaveTrackerDto();
		try {
			LeaveTrackerDto = leaveTrackerService.getAllLeaveByMonth(month);
		} catch (final Exception e) {

		}

		return LeaveTrackerDto;
	}
	
	@GetMapping("/getAllLeaveByYear/{year}")
	public List<LeaveTrackerDto> getAllLeaveByYear(@PathVariable String year,
			@RequestHeader(name = "lang", required = false) String lang) throws Exception {

	
			return leaveTrackerService.getAllLeaveByYear(year);
	
	}
	
	@PutMapping("/updateLeave")
	   public LeaveTrackerDto updateLeave(@RequestBody LeaveTrackerDto leaveTrackerDto,
				@RequestHeader(name = "lang", required = false) String lang) {
		   
	      LeaveTrackerDto resultDto = new LeaveTrackerDto();
	      try {
	       StringBuilder errorParam = new StringBuilder();
	       
	       if (leaveTrackerDto.getFromDate() == null || leaveTrackerDto.getFromDate().isEmpty()) {
		    	  errorParam.append("From Date Required");
		      }
		      
		      if (leaveTrackerDto.getToDate() == null || leaveTrackerDto.getToDate().isEmpty()) {
		    	  errorParam.append("To Date Required");
		      }
		      
		      if (leaveTrackerDto.getLeaveType() == null || leaveTrackerDto.getLeaveType().isEmpty()) {
		    	  errorParam.append("Leave Type Required");
		      }
		      if (errorParam.length() > 0) {
					resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
					
					logger.info(resultDto.getMessage());
					return resultDto;
				}
		      resultDto = leaveTrackerService.updateLeave(leaveTrackerDto);
		       
			}catch(final Exception e) {
				 resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				   resultDto.setMessage(e.getMessage());
				   
				   logger.error(HireProUsUtil.getErrorMessage(e));
				   return resultDto;
			   }
			      return resultDto;
	}

	@DeleteMapping("/deleteLeaveById/{id}")
	public LeaveTrackerDto deleteLeaveById(@PathVariable String id,
			@RequestHeader(name = "lang", required = false) String lang) {


		LeaveTrackerDto resultDto = new LeaveTrackerDto();
		try {

			return leaveTrackerService.deleteLeaveById(id);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));

			return resultDto;
		}
	}
}
