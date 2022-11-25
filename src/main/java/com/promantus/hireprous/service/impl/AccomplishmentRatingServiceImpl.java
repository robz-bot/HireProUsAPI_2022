package com.promantus.hireprous.service.impl;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.webresources.Cache;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.HireProUsDefaultMethods;
import com.promantus.hireprous.dto.AccomplishmentRatingDto;
import com.promantus.hireprous.dto.CandidateDto;
import com.promantus.hireprous.entity.AccomplishmentRating;
import com.promantus.hireprous.entity.Candidate;
import com.promantus.hireprous.entity.User;
import com.promantus.hireprous.repository.AccomplishmentRatingRepositroy;
import com.promantus.hireprous.repository.UserRepository;
import com.promantus.hireprous.service.AccomplishmentRatingService;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.UserService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

@Service
public class AccomplishmentRatingServiceImpl implements AccomplishmentRatingService {

	@Autowired
	CommonService commonService;
	
	@Autowired
	private BusinessUnitService businessUnitService;
	
	@Autowired
    AccomplishmentRatingRepositroy accomplishmentRatingRepository;
	
	@Autowired
	ResourceLoader resourceLoader;
		
	@Autowired
	MongoTemplate mongoTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(AccomplishmentRatingServiceImpl.class);

	@Override
	public AccomplishmentRatingDto addAccomplishmentRating(AccomplishmentRatingDto accomplishmentRatingDto,
			String lang) throws Exception 
	{

		AccomplishmentRatingDto resultDto=new AccomplishmentRatingDto();
		List<AccomplishmentRating> accomplishmentRating= accomplishmentRatingRepository.findByResourceNameOrEmployeeId(accomplishmentRatingDto.getResourceName(),accomplishmentRatingDto.getEmployeeId(),
				HireProUsUtil.orderByUpdatedDateTimeDesc());
		if(!accomplishmentRating.isEmpty()) {
		
		resultDto=getAccomplishmentRatingDto(accomplishmentRating.get(0));
	
		}
		else
		{
			resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage("Resource Not Found");
		}
 		return resultDto;
	}
	public AccomplishmentRatingDto getAccomplishmentRatingDto(AccomplishmentRating accomplishmentRating) throws Exception {

		AccomplishmentRatingDto accomplishmentRatingDto=new AccomplishmentRatingDto();
		accomplishmentRatingDto.setId(accomplishmentRating.getId());
		accomplishmentRatingDto.setResourceName(accomplishmentRating.getResourceName());
		accomplishmentRatingDto.setAchievements(accomplishmentRating.getAchievements());
		accomplishmentRatingDto.setComments(accomplishmentRating.getComments());
		accomplishmentRatingDto.setEmployeeId(accomplishmentRating.getEmployeeId());
		accomplishmentRatingDto.setCreatedBy(accomplishmentRating.getCreatedBy());
		accomplishmentRatingDto.setRating(accomplishmentRating.getRating());
		accomplishmentRatingDto.setRatingByname(accomplishmentRating.getRatingByname());
		accomplishmentRatingDto.setStatus(accomplishmentRating.getStatus());
		accomplishmentRatingDto.setCreatedDateTime(accomplishmentRating.getCreatedDateTime());
		accomplishmentRatingDto.setReviewedOn(accomplishmentRating.getReviewedOn());
		accomplishmentRatingDto.setUpdatedDateTime(accomplishmentRating.getUpdatedDateTime());
		accomplishmentRatingDto.setUpdatedBy(accomplishmentRating.getUpdatedBy());
		accomplishmentRatingDto.setBuId(accomplishmentRating.getBuId());
		accomplishmentRatingDto.setBuName(businessUnitService.getBusinessUnitNameById(accomplishmentRating.getBuId()));
		accomplishmentRatingDto.setRoleId(accomplishmentRating.getRoleId());
		accomplishmentRatingDto.setRoleName(CacheUtil.getRolesMap().get(accomplishmentRating.getRoleId()));
		return accomplishmentRatingDto;

	}
	@Override
	public AccomplishmentRatingDto updateAccomplishmentRating(AccomplishmentRatingDto accomplishmentRatingDto,
			String lang) throws Exception {
		AccomplishmentRatingDto resultDto = new AccomplishmentRatingDto();

		AccomplishmentRating accomplishmentRating = accomplishmentRatingRepository.findById(accomplishmentRatingDto.getId());
		LocalDateTime currentDate=LocalDateTime.now();
		LocalDateTime createdDate= accomplishmentRatingDto.getCreatedDateTime();
		if (accomplishmentRating == null) {
			try {
			    resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(commonService.getMessage("invalid", new String[] { "AccomplishmentRating Id" }, lang));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logger.info(resultDto.getMessage());
			return resultDto;
		}
   //     if(currentDate.getMonthValue()==createdDate.getMonthValue() && currentDate.getYear()==createdDate.getYear()) {
        	
       // if(accomplishmentRatingDto.getStatus().equalsIgnoreCase("open")) {
		//accomplishmentRating.setResourceName(accomplishmentRatingDto.getResourceName());
		//accomplishmentRating.setEmployeeId(accomplishmentRatingDto.getEmployeeId());
		//accomplishmentRating.setAchievements(accomplishmentRatingDto.getAchievements());
//		if(accomplishmentRatingDto.getBuId()==accomplishmentRating.getBuId()) {
		accomplishmentRating.setRating(accomplishmentRatingDto.getRating());
		accomplishmentRating.setComments(accomplishmentRatingDto.getComments());
		accomplishmentRating.setRatingbyId(accomplishmentRatingDto.getRatingbyId());
		accomplishmentRating.setRatingByname(accomplishmentRatingDto.getRatingByname());
		if(accomplishmentRatingDto.getRatingByname()!=null) {
			{
			accomplishmentRating.setReviewedOn(LocalDateTime.now());
			}
		}
		accomplishmentRating.setUpdatedBy(accomplishmentRatingDto.getUpdatedBy());
		accomplishmentRating.setUpdatedDateTime(LocalDateTime.now());
	    accomplishmentRating.setStatus("Closed");
		accomplishmentRatingRepository.save(accomplishmentRating);
		 resultDto.setId(accomplishmentRating.getId());
		 resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_OK);
//		}
//		else
//		{
//			 resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_OK);
//			 resultDto.setMessage("Other BusinessUnit");
//		}

		return resultDto;
	}
	
	@Override
	public List<AccomplishmentRatingDto> searchAccomplishments(AccomplishmentRatingDto accomplishmentDto, List<String> roleIds) throws Exception {
			
			List<AccomplishmentRatingDto> accomplishmentDtoList = new ArrayList<AccomplishmentRatingDto>();
			List<AccomplishmentRating> accomplishmentList = new ArrayList<AccomplishmentRating>();
		    			
			final List<Criteria> criteriaList = new ArrayList<>();
			
			if (accomplishmentDto.getBuId() != null) {
				criteriaList.add(Criteria.where("buId").is(accomplishmentDto.getBuId()));
			}
			if (accomplishmentDto.getRefId()!= 0) {
				criteriaList.add(Criteria.where("refId").is(accomplishmentDto.getRefId()));
			}
			if (accomplishmentDto.getBuName() != null && !accomplishmentDto.getBuName().isEmpty()) {
				criteriaList.add(Criteria.where("buName").regex("(?i).*" + accomplishmentDto.getBuName() + ".*"));
			}
			if (accomplishmentDto.getResourceName() != null && !accomplishmentDto.getResourceName().isEmpty()) {
				criteriaList.add(Criteria.where("resourceName").regex("(?i).*" + accomplishmentDto.getResourceName()));
			}
			if (accomplishmentDto.getStatus() != null && !accomplishmentDto.getStatus().isEmpty()) {
				criteriaList.add(Criteria.where("status").is(accomplishmentDto.getStatus()));
			}
			if (accomplishmentDto.getRating() != null && !accomplishmentDto.getRating().isEmpty()) {
				criteriaList.add(Criteria.where("rating").is(accomplishmentDto.getRating()));
			}
			if (roleIds!= null && !roleIds.isEmpty()) {
				criteriaList.add(Criteria.where("roleId").in(roleIds));
			}
//			if (accomplishmentDto.getYear() != 0) {
////				criteriaList.add(Criteria.where("createdDateTime").regex("(?i).*" + accomplishmentDto.getYear() + ".*"));
//				List<AccomplishmentRating > accomplishmentlst = accomplishmentRatingRepository.findAll();
//				for(AccomplishmentRating accomplishment : accomplishmentlst) {
//					if((accomplishment.getCreatedDateTime().getYear() == accomplishmentDto.getYear())) {
//						criteriaList.add(Criteria.where("year").is(accomplishmentDto.getYear()));
//						//accomplishmentList.add(accomplishment);
//					}
//				}
//			}
			
			if (!criteriaList.isEmpty()) {
				Query searchQuery = new Query();
				searchQuery
						.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
				accomplishmentList = mongoTemplate.find(searchQuery, AccomplishmentRating.class);
			}

			for (AccomplishmentRating accomplishment : accomplishmentList) {
				accomplishmentDtoList.add(this.getAccomplishmentRatingDto(accomplishment));
			}

			Comparator<AccomplishmentRatingDto> compareByUpdatedDateTime = Comparator
					.comparing(AccomplishmentRatingDto::getUpdatedDateTime);
			accomplishmentDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

			return 	accomplishmentDtoList;
		}
	@Override
	public byte[] downloadAccomplishReport(List<AccomplishmentRatingDto> accomplishmentRatingDtoList, String lang) throws IOException {
		
			File file = resourceLoader.getResource("classpath:excel-templates/Accomplishment_Report.xlsx").getFile();
			try (Workbook resourceAccomplishmentWB = new XSSFWorkbook(file)) {

				System.out.println("Size"+accomplishmentRatingDtoList.size());
				Sheet sheet = resourceAccomplishmentWB.getSheetAt(0);
				
				HireProUsDefaultMethods.cleanSheet(sheet);
				int rowNum = 2;
				for (AccomplishmentRatingDto accomplishmentRatingDto : accomplishmentRatingDtoList) {

					Row dataRow = sheet.createRow(rowNum);

					Cell slNo = dataRow.createCell(0);
					slNo.setCellValue(rowNum - 1);

//					dataRow.createCell(1).setCellValue(accomplishmentRatingDto.getId());
					dataRow.createCell(1).setCellValue(accomplishmentRatingDto.getResourceName());
					dataRow.createCell(2).setCellValue(accomplishmentRatingDto.getBuName());
					dataRow.createCell(3).setCellValue(accomplishmentRatingDto.getAchievements());
					dataRow.createCell(4).setCellValue(accomplishmentRatingDto.getStatus());
					dataRow.createCell(5).setCellValue(accomplishmentRatingDto.getRating());
					dataRow.createCell(6).setCellValue(accomplishmentRatingDto.getRatingByname());
					dataRow.createCell(7).setCellValue(accomplishmentRatingDto.getReviewedOn()==null? "-": accomplishmentRatingDto.getReviewedOn().toLocalDate().toString());
					dataRow.createCell(8).setCellValue("");

					
					rowNum++;
				}

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				resourceAccomplishmentWB.write(outputStream);

				resourceAccomplishmentWB.close();

				return outputStream.toByteArray();

			} catch (Exception ex) {
				logger.error("Error during Customer Details download file", ex);
				return null;
			}
      
	}
	@Override
	public List<AccomplishmentRatingDto> getResourceAccomplishmentsByRoleIds(final List<String> roleIds) throws Exception {
		
		List<AccomplishmentRatingDto> accomplishmentDtoList = new ArrayList<AccomplishmentRatingDto>();
		List<AccomplishmentRating> accomplishmentList = new ArrayList<AccomplishmentRating>();
		

		accomplishmentList = accomplishmentRatingRepository.findAll();
		
		for (AccomplishmentRating accomplishmentRating : accomplishmentList) {
			if(Long.parseLong(roleIds.get(0))== accomplishmentRating.getRoleId() ||
					Long.parseLong(roleIds.get(1))== accomplishmentRating.getRoleId()||
					Long.parseLong(roleIds.get(2))== accomplishmentRating.getRoleId()||
					Long.parseLong(roleIds.get(3))== accomplishmentRating.getRoleId()){
				accomplishmentDtoList.add(this.getAccomplishmentRatingDto(accomplishmentRating));
			}
			
		}

		Comparator<AccomplishmentRatingDto> compareByUpdatedDateTime = Comparator
				.comparing(AccomplishmentRatingDto::getUpdatedDateTime);
		accomplishmentDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return 	accomplishmentDtoList;
	}

}
