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
import com.promantus.hireprous.dto.BusinessUnitDto;
import com.promantus.hireprous.dto.HolidayDto;
import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.service.HolidayService;
import com.promantus.hireprous.util.HireProUsUtil;

@RestController
@RequestMapping("/api/v1")
public class HolidayController extends CommonController{
	
	private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);
	
   @Autowired
   private HolidayService holidayService;
   
	/**
	 * 
	 * @param HolidayDto
	 * @return
	 */  
   
   @PostMapping("/addHoliday")
   public HolidayDto addHoliday(@RequestBody(required = true) HolidayDto holidayDto,
			@RequestHeader(name = "lang", required = false) String lang) {
	   
      HolidayDto resultDto = new HolidayDto();
      try {
       StringBuilder errorParam = new StringBuilder();
       
       if (holidayDto.getHolidayDate ()== null || holidayDto.getHolidayDate().isEmpty()) {
    	  errorParam.append("Holiday Date Required");
      }
      
       if(holidayDto.getHolidayName() == null || holidayDto.getHolidayName().isEmpty()) {
    	   errorParam.append("Holiday Name Required");
       }
       
       if(holidayDto.getBranch() == null || holidayDto.getBranch().isEmpty()) {
    	   errorParam.append("Branch Required");
       }
       
       if (errorParam.length() > 0) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(
					super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
       
		resultDto = holidayService.addHoliday(holidayDto,lang);
        
   }catch(final Exception e){
	   
	   resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
	   resultDto.setMessage(e.getMessage());
	   
	   logger.error(HireProUsUtil.getErrorMessage(e));
	   return resultDto;
   }
      return resultDto;
   }
   
   
	@GetMapping("/getAllHoliday")
	public List<HolidayDto> getAllHoliday(@RequestHeader(name = "lang", required = false) String lang) {

		List<HolidayDto> holidayDtoList = new ArrayList<HolidayDto>();
		try {
			holidayDtoList = holidayService.getAllHoliday();
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return holidayDtoList;
	}
	
	@GetMapping("/getAllByYear/{year}")
	public List<HolidayDto> getHolidayByYear(@PathVariable String year,
			@RequestHeader(name = "lang", required = false) String lang) throws Exception {

	
			return holidayService.getHolidayByYear(year);
		

	
	}
	
	@GetMapping("/getAllByMonth/{month}")
	public HolidayDto getHolidayByMonth(@PathVariable String month,
			@RequestHeader(name = "lang", required = false) String lang) {

		HolidayDto HolidayDto = new HolidayDto();
		try {
			HolidayDto = holidayService.getHolidayByMonth(month);
		} catch (final Exception e) {

		}

		return HolidayDto;
	}

	
	@PutMapping("/updateHoliday")
	   public HolidayDto holiday(@RequestBody HolidayDto holidayDto,
				@RequestHeader(name = "lang", required = false) String lang) {
		   
	      HolidayDto resultDto = new HolidayDto();
	      try {
	       StringBuilder errorParam = new StringBuilder();
	       
	       if (holidayDto.getHolidayDate ()== null || holidayDto.getHolidayDate().isEmpty()) {
	    	  errorParam.append("Holiday Date Required");
	      }
	      
	       if(holidayDto.getHolidayName() == null || holidayDto.getHolidayName().isEmpty()) {
	    	   errorParam.append("Holiday Name Required");
	       }
	       
	       if(holidayDto.getBranch() == null || holidayDto.getBranch().isEmpty()) {
	    	   errorParam.append("Branch Required");
	       }
	       
	       if (errorParam.length() > 0) {
				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}
	       
	        resultDto = holidayService.updateHoliday(holidayDto,lang);
	        
	   }catch(final Exception e){
		   
		   resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
		   resultDto.setMessage(e.getMessage());
		   
		   logger.error(HireProUsUtil.getErrorMessage(e));
		   return resultDto;
	   }
	      return resultDto;
	   }
	
	@DeleteMapping("/deleteHolidayById/{id}")
	public HolidayDto deleteHolidayById(@PathVariable String id,
			@RequestHeader(name = "lang", required = false) String lang) {

		HolidayDto resultDto = new HolidayDto();
		try {

			return holidayService.deleteHolidayById(id);

		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));

			return resultDto;
		}
	}
	

}
