package com.promantus.hireprous.service;

import java.util.List;
import java.util.Map;

import com.promantus.hireprous.dto.SearchDto;
import com.promantus.hireprous.dto.TimeSheetDto;
import com.promantus.hireprous.dto.UserDto;

public interface TimeSheetService {

	TimeSheetDto updateTimeSheet(TimeSheetDto timeSheetDto, String lang) throws Exception;

	TimeSheetDto addTimeSheet(TimeSheetDto timeSheetDto, String lang) throws Exception;

	List<TimeSheetDto> getAllTimeSheets() throws Exception;

	TimeSheetDto submitTimeSheet(List<TimeSheetDto> timeSheetDtoList, String lang) throws Exception;

	TimeSheetDto getTimeSheetById(String timeSheetId) throws Exception;

	TimeSheetDto deleteTimeSheetById(String timeSheetId) throws Exception;

	List<TimeSheetDto> getTimeSheetByUserIdAndApproval(String userId, boolean isApproval) throws Exception;

	List<String> getAllEmployeeName() throws Exception;

	List<TimeSheetDto> getAllTimeSheetByManagerId(long parseLong) throws Exception;

	List<TimeSheetDto> searchTimeSheetByUserId(String userId) throws Exception;

	TimeSheetDto approveTimeSheet(TimeSheetDto timeSheetDto, String lang) throws Exception;

	List<TimeSheetDto> searchByDate(SearchDto searchDto, String lang) throws Exception;

	Map<String, Object> getTaskAndHours(Long userId) throws Exception;

	List<String> getAllDateOfBirth() throws Exception;

	List<String> getAllDateOfJoining() throws Exception;
	
}
