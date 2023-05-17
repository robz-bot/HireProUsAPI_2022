package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.dto.HolidayDto;
import com.promantus.hireprous.entity.Holiday;
import com.promantus.hireprous.repository.HolidayRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.HolidayService;

@Service
public class HolidayServiceImpl implements HolidayService{
	
	@Autowired
	HolidayRepository holidayRepository;
	
	@Autowired
	CommonService commonService;
	
	@Override
	public HolidayDto addHoliday (HolidayDto holidayDto, String lang) throws Exception{
		
		HolidayDto resultDto = new HolidayDto();
		Holiday holiday = new Holiday();
		if(holidayRepository.findByHolidayName(holidayDto.getHolidayName())==null)
		{
			holiday.setId(commonService.nextSequenceNumber());
		    holiday.setHolidayDate(holidayDto.getHolidayDate());  //year-month-dATE
		    holiday.setHolidayName(holidayDto.getHolidayName());
		    holiday.setBranch(holidayDto.getBranch());
		    holiday.setYear(holidayDto.getHolidayDate().split("-")[0].trim().toString());
		    holiday.setMonth(holidayDto.getHolidayDate().split("-")[1].trim().toString());
		    holiday.setCreatedBy(holidayDto.getCreatedBy());
		    holiday.setCreatedOn(holidayDto.getCreatedOn());
		    holiday.setUpdatedBy(holidayDto.getUpdatedBy());
		    holiday.setUpdatedOn(holidayDto.getUpdatedOn());
		}
		holidayRepository.save(holiday);
		resultDto.setMessage("Holiday Added Successfully");
		return resultDto;
	}
	
	@Override
	public List<HolidayDto> getAllHoliday() {
		List<Holiday> HolidaysList = holidayRepository.findAll();

		List<HolidayDto> HolidayDtoList = new ArrayList<HolidayDto>();
		for (Holiday Holiday : HolidaysList) {
			HolidayDtoList.add(this.getHolidayDto(Holiday));
		}

		return HolidayDtoList;
	}

	private HolidayDto getHolidayDto(Holiday Holiday) {
		HolidayDto HolidayDto=new HolidayDto();
		HolidayDto.setId(Holiday.getId());
		HolidayDto.setHolidayDate(Holiday.getHolidayDate());
		HolidayDto.setHolidayName(Holiday.getHolidayName());
		HolidayDto.setBranch(Holiday.getBranch());
		HolidayDto.setMonth(Holiday.getMonth());
		HolidayDto.setYear(Holiday.getYear());
		HolidayDto.setCreatedBy(Holiday.getCreatedBy());
		HolidayDto.setCreatedOn(Holiday.getCreatedOn());
		HolidayDto.setUpdatedBy(Holiday.getUpdatedBy());
		HolidayDto.setUpdatedOn(Holiday.getUpdatedOn());
		return HolidayDto;	
		}
	
	
	@Override
	public List<HolidayDto> getHolidayByYear(String year) throws Exception {
		
		List<Holiday> holidays = holidayRepository.findAllByYear(year);
		List<HolidayDto> holidayDto = new ArrayList <HolidayDto>();
		for (Holiday holiday : holidays) {
			holidayDto.add(this.getHolidayDto(holiday));
		}
		
		return  holidayDto;
	}
	
	
	@Override
	public HolidayDto getHolidayByMonth(String month) throws Exception {

		
		Holiday holidayName = holidayRepository.findByMonth(month);
		HolidayDto holidayDto = new HolidayDto();
		if (holidayName == null) {
			holidayDto.setStatus(1);
		}

		return holidayName != null ? this.getHolidayDto(holidayName) : holidayDto;
	}
	
	@Override
	public HolidayDto updateHoliday(HolidayDto holidayDto, String lang)  throws Exception {

		HolidayDto resultDto = new HolidayDto();
		System.out.println(holidayDto.getId());
		Holiday holiday = holidayRepository.findById(holidayDto.getId()).orElse(null);

		if (holiday == null) {

			resultDto.setMessage("Holiday does not exist");
			return resultDto;
		}

		holiday.setHolidayDate(holidayDto.getHolidayDate());
		holiday.setHolidayName(holidayDto.getHolidayName());
		holiday.setBranch(holidayDto.getBranch());
		holiday.setYear(holidayDto.getYear());
	    holiday.setMonth(holidayDto.getMonth());
		holiday.setCreatedBy(holidayDto.getCreatedBy());
		holiday.setCreatedOn(holidayDto.getCreatedOn());
		holiday.setUpdatedBy(holidayDto.getUpdatedBy());
		holiday.setUpdatedOn(holidayDto.getUpdatedOn());

		holidayRepository.save(holiday);
		resultDto.setMessage("Holiday Updated Successfully");
		return resultDto;

	}
	
	@Override
	public HolidayDto deleteHolidayById(String Id) throws Exception{
		HolidayDto resultDto = new HolidayDto();
		Holiday holidayName = holidayRepository.findById(Long.parseLong(Id)).orElse(null);
		if (holidayName == null) {

			resultDto.setMessage("data does not exist");
			return resultDto;
		}

		holidayRepository.delete(holidayName);
		resultDto.setMessage("Holiday Deleted Successfully");
		return resultDto;
	}

}
