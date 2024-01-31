package com.promantus.hireprous.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.dto.LeaveTrackerDto;
import com.promantus.hireprous.entity.LeaveTracker;
import com.promantus.hireprous.entity.User;
import com.promantus.hireprous.repository.LeaveTrackerRepository;
import com.promantus.hireprous.repository.UserRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.LeaveTrackerService;

@Service
public class LeaveTrackerServiceImpl implements LeaveTrackerService{
	

	@Autowired
	LeaveTrackerRepository leaveTrackerRepository;
	
	@Autowired
	CommonService commonService;

	@Autowired
	UserRepository userRepository;
	
	@Override
	public LeaveTrackerDto addLeave(LeaveTrackerDto leaveTrackerDto) throws Exception {
		
		LeaveTrackerDto resultDto = new LeaveTrackerDto();
		
		LeaveTracker leaveTracker = new LeaveTracker();
		
//		User user = userRepository.findById(leaveTrackerDto.getUserId());
		
		if(leaveTrackerRepository.findByLeaveType(leaveTrackerDto.getLeaveType())==null)
		{
			leaveTracker.setId(commonService.nextSequenceNumber());
			leaveTracker.setUserId(leaveTrackerDto.getUserId());
			leaveTracker.setFromDate(leaveTrackerDto.getFromDate());  //year-month-dATE
			leaveTracker.setToDate(leaveTrackerDto.getToDate());
			leaveTracker.setYear(leaveTrackerDto.getLeaveDate().split("-")[0].trim().toString());
			leaveTracker.setMonth(leaveTrackerDto.getLeaveDate().split("-")[1].trim().toString());
			leaveTracker.setLeaveType(leaveTrackerDto.getLeaveType());
			leaveTracker.setReason(leaveTrackerDto.getReason());
			leaveTracker.setApprovedBy(leaveTrackerDto.getApprovedBy());
			leaveTracker.setCreatedBy(leaveTrackerDto.getCreatedBy());
			leaveTracker.setCreatedOn(leaveTrackerDto.getCreatedOn());
			leaveTracker.setUpdatedBy(leaveTrackerDto.getUpdatedBy());
			leaveTracker.setUpdatedOn(leaveTrackerDto.getUpdatedOn());
		}
		
		leaveTrackerRepository.save(leaveTracker);
		resultDto.setMessage("Leave Added Successfully");
		return resultDto;
	}

	@Override
	public List<LeaveTrackerDto> getAllLeave() {
		List<LeaveTracker> leaveTrackerList = leaveTrackerRepository.findAll();

		List<LeaveTrackerDto> LeaveTrackerDtoList = new ArrayList<LeaveTrackerDto>();
		for (LeaveTracker LeaveTracker : leaveTrackerList) {
			LeaveTrackerDtoList.add(this.getLeaveTrackerDto(LeaveTracker));
		}

		return LeaveTrackerDtoList;
	}

	private LeaveTrackerDto getLeaveTrackerDto(LeaveTracker leaveTracker) {
		
		LeaveTrackerDto leaveTrackerDto=new LeaveTrackerDto();
		
		leaveTrackerDto.setId(leaveTracker.getId());
		User user = userRepository.findById(leaveTracker.getUserId());
		if(user != null) {
			leaveTrackerDto.setUserName(user.getFirstName()+" "+user.getLastName());
		}	
		leaveTrackerDto.setFromDate(leaveTracker.getFromDate());
		leaveTrackerDto.setToDate(leaveTracker.getToDate());
		leaveTrackerDto.setYear(leaveTracker.getLeaveDate().split("-")[0].trim().toString());
		leaveTrackerDto.setMonth(leaveTracker.getLeaveDate().split("-")[1].trim().toString());
		leaveTrackerDto.setLeaveType(leaveTracker.getLeaveType());
		leaveTrackerDto.setReason(leaveTracker.getReason());
		leaveTrackerDto.setApprovedBy(leaveTracker.getApprovedBy());
		leaveTrackerDto.setCreatedBy(leaveTracker.getCreatedBy());
		leaveTrackerDto.setCreatedOn(leaveTracker.getCreatedOn());
		leaveTrackerDto.setUpdatedBy(leaveTracker.getUpdatedBy());
		leaveTrackerDto.setUpdatedOn(leaveTracker.getUpdatedOn());
		return leaveTrackerDto;
	}

	@Override
	public LeaveTrackerDto getAllLeaveByMonth(String month) {
		LeaveTracker LeaveType = leaveTrackerRepository.findByMonth(month);
		LeaveTrackerDto leaveTrackerDto = new LeaveTrackerDto();
		if (LeaveType == null) {
			leaveTrackerDto.setStatus(1);
		}

		return LeaveType != null ? this.getLeaveTrackerDto(LeaveType) : leaveTrackerDto;
	}

	@Override
	public List<LeaveTrackerDto> getAllLeaveByYear(String year) {
		List<LeaveTracker> leaveTrackers = leaveTrackerRepository.findAllByYear(year);
		List<LeaveTrackerDto> leaveTrackerDto = new ArrayList <LeaveTrackerDto>();
		for (LeaveTracker leaveTracker : leaveTrackers) {
			leaveTrackerDto.add(this.getLeaveTrackerDto(leaveTracker));
		}
		
		return  leaveTrackerDto;
	}

	@Override
	public LeaveTrackerDto updateLeave(LeaveTrackerDto leaveTrackerDto) {
		LeaveTrackerDto resultDto = new LeaveTrackerDto();
		System.out.println(leaveTrackerDto.getId());
		LeaveTracker leaveTracker = leaveTrackerRepository.findById(leaveTrackerDto.getId()).orElse(null);

		if (leaveTracker == null) {

			resultDto.setMessage("Leave does not exist");
			return resultDto;
		}

		leaveTracker.setUserId(leaveTrackerDto.getUserId());
		leaveTracker.setFromDate(leaveTrackerDto.getFromDate());  //year-month-dATE
		leaveTracker.setToDate(leaveTrackerDto.getToDate());
		leaveTracker.setYear(leaveTrackerDto.getLeaveDate().split("-")[0].trim().toString());
		leaveTracker.setMonth(leaveTrackerDto.getLeaveDate().split("-")[1].trim().toString());
		leaveTracker.setLeaveType(leaveTrackerDto.getLeaveType());
		leaveTracker.setReason(leaveTrackerDto.getReason());
		leaveTracker.setApprovedBy(leaveTrackerDto.getApprovedBy());
		leaveTracker.setYear(leaveTrackerDto.getYear());
	    leaveTracker.setMonth(leaveTrackerDto.getMonth());
		leaveTracker.setCreatedBy(leaveTrackerDto.getCreatedBy());
		leaveTracker.setCreatedOn(leaveTrackerDto.getCreatedOn());
		leaveTracker.setUpdatedBy(leaveTrackerDto.getUpdatedBy());
		leaveTracker.setUpdatedOn(leaveTrackerDto.getUpdatedOn());

		leaveTrackerRepository.save(leaveTracker);
		resultDto.setMessage("Leave Updated Successfully");
		return resultDto;
	}

	@Override
	public LeaveTrackerDto deleteLeaveById(String Id) throws Exception {
		LeaveTrackerDto resultDto = new LeaveTrackerDto();
		LeaveTracker leaveType = leaveTrackerRepository.findById(Long.parseLong(Id)).orElse(null);
		if (leaveType == null) {
			resultDto.setMessage("data does not exist");
			return resultDto;
		}
		leaveTrackerRepository.delete(leaveType);
		resultDto.setMessage("Leave Deleted Successfully");
		return resultDto;
	}
}

	
