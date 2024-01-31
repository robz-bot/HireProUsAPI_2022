package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.HolidayDto;
import com.promantus.hireprous.dto.LeaveTrackerDto;

public interface LeaveTrackerService {

	LeaveTrackerDto addLeave(LeaveTrackerDto leaveTrackerDto) throws Exception;

	List<LeaveTrackerDto> getAllLeave() throws Exception;

	LeaveTrackerDto getAllLeaveByMonth(String month) throws Exception;

	List<LeaveTrackerDto> getAllLeaveByYear(String year) throws Exception;

	LeaveTrackerDto updateLeave(LeaveTrackerDto leaveTrackerDto) throws Exception;

	LeaveTrackerDto deleteLeaveById(String LeaveId) throws Exception;
}
