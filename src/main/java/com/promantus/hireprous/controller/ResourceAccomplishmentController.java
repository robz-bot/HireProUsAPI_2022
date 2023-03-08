package com.promantus.hireprous.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.promantus.hireprous.dto.ResourceAccomplishmentDto;
import com.promantus.hireprous.service.ResourceAccomplishmentService;
import com.promantus.hireprous.util.HireProUsUtil;

@RestController
@RequestMapping("/api/v1")
public class ResourceAccomplishmentController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(JobRequestController.class);

	@Autowired
	private ResourceAccomplishmentService resourceAccomplishmentService;

	@Value("${download.path}")
	private String downloadsPath;

	@PostMapping("/addResourceAccomplishment")
	public ResourceAccomplishmentDto addResourceAccomplishment(
			@RequestBody ResourceAccomplishmentDto resourceAccomplishmentDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		ResourceAccomplishmentDto resultDto = new ResourceAccomplishmentDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Resource Name.

			if (resourceAccomplishmentDto.getResourceName() == null
					|| resourceAccomplishmentDto.getResourceName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", ResourceName" : "ResourceName");
			}
			// EmployeeId
			if (resourceAccomplishmentDto.getBuId() == null) {
				errorParam.append(errorParam.length() > 0 ? ", BUId" : "BUId");
			}
			// Achievements
			if (resourceAccomplishmentDto.getAchievements() == null
					|| resourceAccomplishmentDto.getAchievements().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Achievements" : "Achievements");
			}

			if (errorParam.length() > 0) {
				resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = resourceAccomplishmentService.addResourceAccomplishment(resourceAccomplishmentDto, lang);

		} catch (final Exception e) {

			resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	@PutMapping("/updateResourceAccomplishment")
	public ResourceAccomplishmentDto updateResourceAccomplishment(
			@RequestBody ResourceAccomplishmentDto resourceAccomplishmentDto,
			@RequestHeader(name = "lang", required = false) String lang) {

		ResourceAccomplishmentDto resultDto = new ResourceAccomplishmentDto();
		try {

			// Mandatory check.
			StringBuilder errorParam = new StringBuilder();
			// Resource Name.

			if (resourceAccomplishmentDto.getResourceName() == null
					|| resourceAccomplishmentDto.getResourceName().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", ResourceName" : "ResourceName");
			}
			// EmployeeId
			if (resourceAccomplishmentDto.getBuId() == null) {
				errorParam.append(errorParam.length() > 0 ? ", BUId" : "BUId");
			}
			// Achievements
			if (resourceAccomplishmentDto.getAchievements() == null
					|| resourceAccomplishmentDto.getAchievements().isEmpty()) {
				errorParam.append(errorParam.length() > 0 ? ", Achievements" : "Achievements");
			}

			if (errorParam.length() > 0) {
				resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage(
						super.getMessage("mandatory.input.param", new String[] { errorParam.toString() }, lang));

				logger.info(resultDto.getMessage());
				return resultDto;
			}

			resultDto = resourceAccomplishmentService.updateResourceAccomplishment(resourceAccomplishmentDto, lang);

		} catch (final Exception e) {

			resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	@DeleteMapping("/deleteAccomplishmentById/{Id}")
	public ResourceAccomplishmentDto deleteById(@PathVariable String Id,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return resourceAccomplishmentService.deleteResourceAccomplishmentById(Long.parseLong(Id));

		} catch (final Exception e) {

			ResourceAccomplishmentDto resultDto = new ResourceAccomplishmentDto();
			resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(HireProUsUtil.getErrorMessage(e));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
	}

	@GetMapping("/getAllResourceAccomplishment")
	public List<ResourceAccomplishmentDto> getAllResourceAccomplishment(
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return resourceAccomplishmentService.getAllResourceAccomplishment();

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<ResourceAccomplishmentDto>();
	}

	@GetMapping("/getResourceAccomplishmentByBuId/{buId}")
	public List<ResourceAccomplishmentDto> getResourceAccomplishmentByBuId(@PathVariable String buId,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return resourceAccomplishmentService.getResourceAccomplishmentByBuId(Long.parseLong(buId));

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<ResourceAccomplishmentDto>();
	}
	
	@GetMapping("/getResourceAccomplishmentByBuIdYear/{buId}/{year}")
	public List<ResourceAccomplishmentDto> getResourceAccomplishmentByBuIdYear(@PathVariable String buId,@PathVariable int year,
			@RequestHeader(name = "lang", required = false) String lang) {

		try {

			return resourceAccomplishmentService.getResourceAccomplishmentByBuIdYear(Long.parseLong(buId),year);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return new ArrayList<ResourceAccomplishmentDto>();
	}

	@GetMapping("/getResourceAccomplishment/{id}")
	public ResourceAccomplishmentDto getResourceAccomplishmentById(@PathVariable String id,
			@RequestHeader(name = "lang", required = false) String lang) {
		ResourceAccomplishmentDto resultDto = new ResourceAccomplishmentDto();

		try {

			resultDto = resourceAccomplishmentService.getResourceAccomplishmentById(Long.parseLong(id));

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	@GetMapping("/getResourceAccomplishmentByName/{name}")
	public List<ResourceAccomplishmentDto> getResourceAccomplishmentByName(@PathVariable String name,
			@RequestHeader(name = "lang", required = false) String lang) {
		List<ResourceAccomplishmentDto> resultDto = new ArrayList<ResourceAccomplishmentDto>();
		System.out.println(name);
		try {

			resultDto = resourceAccomplishmentService.getResourceAccomplishmentByName(name);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

	@GetMapping("/checkEntryValidity/{name}/{year}")
	public ResourceAccomplishmentDto checkEntryValidity(@PathVariable String name, @PathVariable String year,
			@RequestHeader(name = "lang", required = false) String lang) {
		ResourceAccomplishmentDto resultDto = new ResourceAccomplishmentDto();
		System.out.println(year);
		try {

			resultDto = resourceAccomplishmentService.checkEntryValidity(name, year);

		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
		}

		return resultDto;
	}

}
