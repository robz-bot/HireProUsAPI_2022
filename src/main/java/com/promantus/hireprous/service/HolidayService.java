package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.HolidayDto;

public interface HolidayService {

	List<HolidayDto> getAllHoliday();

	HolidayDto addHoliday(HolidayDto holidayDto, String lang) throws Exception;

	HolidayDto updateHoliday(HolidayDto holidayDto, String lang) throws Exception;

	HolidayDto deleteHolidayById(String holidayId) throws Exception;

	List<HolidayDto> getHolidayByYear(String year) throws Exception;

	HolidayDto getHolidayByMonth(String month) throws Exception;

}
