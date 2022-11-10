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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.net.HttpHeaders;
import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.AccomplishmentRatingDto;
import com.promantus.hireprous.service.AccomplishmentRatingService;
import com.promantus.hireprous.util.HireProUsUtil;


@RestController
@RequestMapping("/api/v1")
public class AccomplishmentRatingController extends CommonController {
	private static final Logger logger = LoggerFactory.getLogger(AccomplishmentRatingController.class);
	
	@Autowired
	private AccomplishmentRatingService accomplishmentRatingService;
	

	@Value("${download.path}")
	private String downloadsPath;

	@PostMapping("/addAccomplishmentRating")
	public AccomplishmentRatingDto addAccomplishmentRating(@RequestBody AccomplishmentRatingDto accomplishmentRatingDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		
		AccomplishmentRatingDto resultDto = new AccomplishmentRatingDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			//Resource Name.
						
			if (accomplishmentRatingDto.getResourceName() == null || accomplishmentRatingDto.getResourceName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", ResourceName" : "ResourceName");
			}
			

			if (errorParam.length() > 0) {
				resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = accomplishmentRatingService.addAccomplishmentRating(accomplishmentRatingDto, lang);

		} catch (final Exception e) {

			resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	@PutMapping("/updateAccomplishmentRating")
	public AccomplishmentRatingDto updateAccomplishmentRating(@RequestBody AccomplishmentRatingDto accomplishmentRatingDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		
		AccomplishmentRatingDto resultDto = new AccomplishmentRatingDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			//Resource Name.
						
//			if (accomplishmentRatingDto.getResourceName() == null || accomplishmentRatingDto.getResourceName().isEmpty()) {
//				errorParam.append(errorParam.length() > 0 ? ", ResourceName" : "ResourceName");
//			}
			// EmployeeId
//			if (accomplishmentRatingDto.getEmployeeId() == null || accomplishmentRatingDto.getEmployeeId().isEmpty()) {
//				errorParam.append(errorParam.length() > 0 ? ", ResourceId" : "ResourceId");
//			}
			// Achievements
//			if (accomplishmentRatingDto.getAchievements() == null || accomplishmentRatingDto.getAchievements().isEmpty()) {
//				errorParam.append(errorParam.length() > 0 ? ", Achievements" : "Achievements");
//			}
			
			if (accomplishmentRatingDto.getRating() == null || accomplishmentRatingDto.getRating().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Rating" : "Rating");
			}
		

			if (errorParam.length() > 0) {
				resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = accomplishmentRatingService.updateAccomplishmentRating(accomplishmentRatingDto, lang);

		} catch (final Exception e) {

			resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}
	@PutMapping("/getResourceAccomplishmentsByRoleIds")
	public List<AccomplishmentRatingDto> getResourceAccomplishmentsByRoleIds(@RequestBody List<String> roleIds,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return accomplishmentRatingService.getResourceAccomplishmentsByRoleIds(roleIds);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<AccomplishmentRatingDto>();
	}
	@PostMapping("/searchAccomplishments")
	public List<AccomplishmentRatingDto> searchAccomplishmentRatingDto(@RequestBody AccomplishmentRatingDto accomplishmentDto, @RequestBody List<String> roleIds,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {
			return accomplishmentRatingService.searchAccomplishments(accomplishmentDto,  roleIds);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<AccomplishmentRatingDto>();
	}
	
	@PutMapping("/downloadAccomplishmentReport")
	public void downloadAccomplishmentReport(@RequestBody List<AccomplishmentRatingDto> accomplishmentDtoList,
			@RequestHeader(name = "lang", required = false) String lang, HttpServletResponse response) {

		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		try {

			File accomplishmentFile = new File(downloadsPath + "Promantus_Resource_Accomplishment.xlsx");
			FileUtils.writeByteArrayToFile(accomplishmentFile,
		    accomplishmentRatingService.downloadAccomplishReport(accomplishmentDtoList, lang));

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + accomplishmentFile.getName());
			response.setContentLength((int) accomplishmentFile.length());

			inStream = new BufferedInputStream(new FileInputStream(accomplishmentFile));
			outStream = new BufferedOutputStream(response.getOutputStream());

			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			response.flushBuffer();
			accomplishmentFile.deleteOnExit();

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
