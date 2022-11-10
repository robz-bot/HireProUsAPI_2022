package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.ResourceAccomplishmentDto;
import com.promantus.hireprous.entity.BusinessUnit;
import com.promantus.hireprous.entity.ResourceAccomplishment;
import com.promantus.hireprous.entity.Role;
import com.promantus.hireprous.entity.User;
import com.promantus.hireprous.repository.BusinessUnitRepository;
import com.promantus.hireprous.repository.ResourceAccomplishmentRepository;
import com.promantus.hireprous.repository.RoleRepository;
import com.promantus.hireprous.repository.UserRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.ResourceAccomplishmentService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

@Service
public class ResourceAccomplishmentImpl implements ResourceAccomplishmentService {

	@Autowired
	CommonService commonService;

	@Autowired
	ResourceAccomplishmentRepository resourceAccomplishmentRepository;

	@Autowired
	BusinessUnitRepository businessUnitRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(ResourceAccomplishmentImpl.class);

	@Override
	public ResourceAccomplishmentDto addResourceAccomplishment(ResourceAccomplishmentDto resourceAccomplishmentDto,
			String lang) throws Exception {

		ResourceAccomplishmentDto resultDto = new ResourceAccomplishmentDto();
		LocalDateTime currentDT = LocalDateTime.now();
		List<ResourceAccomplishment> resourceAccomplishmentList = new ArrayList<ResourceAccomplishment>();
		resourceAccomplishmentList = resourceAccomplishmentRepository.findByResourceName(
				resourceAccomplishmentDto.getResourceName(), HireProUsUtil.orderByUpdatedDateTimeDesc());

		if (resourceAccomplishmentList.size() > 0) {
			ResourceAccomplishment resourceAccomplishment = resourceAccomplishmentList.get(0);
			LocalDateTime updatedDT = resourceAccomplishment.getUpdatedDateTime();

			if (currentDT.getMonthValue() > updatedDT.getMonthValue() && currentDT.getYear() >= updatedDT.getYear()) {
				addNewEntry(resourceAccomplishmentDto);
				resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_OK);
				resultDto.setMessage("Entry Added Successfully");

			} else {
				resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				resultDto.setMessage("Entry already exist for this month");
			}
		}

		else {
			addNewEntry(resourceAccomplishmentDto);
			resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_OK);
			resultDto.setMessage("Entry added");

		}

		return resultDto;
	}

	@SuppressWarnings("unused")
	@Override
	public ResourceAccomplishmentDto updateResourceAccomplishment(ResourceAccomplishmentDto resourceAccomplishmentDto,
			String lang) throws Exception {

		ResourceAccomplishmentDto resultDto = new ResourceAccomplishmentDto();

		ResourceAccomplishment resourceAccomplishment = resourceAccomplishmentRepository
				.findById(resourceAccomplishmentDto.getId());
		LocalDateTime currentDate = LocalDateTime.now();
		LocalDateTime createdDate = resourceAccomplishmentDto.getCreatedDateTime();
		System.out.println(currentDate.getMonthValue() + " " + currentDate.getYear());
		System.out.println(createdDate.getMonthValue() + " " + createdDate.getYear());

		// resourceAccomplishment.setId(commonService.nextSequenceNumber());
		if (resourceAccomplishment == null) {

			resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(
					commonService.getMessage("invalid", new String[] { "ResourceAccomplishment Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if ( currentDate.getYear() == createdDate.getYear()) {

			if (resourceAccomplishmentDto.getStatus().equalsIgnoreCase("open")) {
				resourceAccomplishment.setResourceName(resourceAccomplishmentDto.getResourceName());
				// resourceAccomplishment.setEmployeeId(resourceAccomplishmentDto.getEmployeeId());
				resourceAccomplishment.setAchievements(resourceAccomplishmentDto.getAchievements());
				resourceAccomplishment.setBuId(resourceAccomplishmentDto.getBuId());
				resourceAccomplishment.setUpdatedBy(resourceAccomplishmentDto.getUpdatedBy());
				resourceAccomplishment.setUpdatedDateTime(LocalDateTime.now());

				resourceAccomplishmentRepository.save(resourceAccomplishment);
				resultDto.setId(resourceAccomplishment.getId());
				resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_OK);
			} else {
				resultDto.setMessage("Entry Closed");
			}
		} else {
			resultDto.setId(resourceAccomplishment.getId());
			resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage("Out of date");

		}

		return resultDto;

	}

	public void addNewEntry(ResourceAccomplishmentDto resourceAccomplishmentDto) throws Exception

	{

		ResourceAccomplishment resourceAccomplishment = new ResourceAccomplishment();

		resourceAccomplishment.setId(commonService.nextSequenceNumber());
		resourceAccomplishment.setResourceName(resourceAccomplishmentDto.getResourceName());
		resourceAccomplishment.setEmployeeId(resourceAccomplishmentDto.getEmployeeId());
		resourceAccomplishment.setAchievements(resourceAccomplishmentDto.getAchievements());
		resourceAccomplishment.setBuId(resourceAccomplishmentDto.getBuId());
		resourceAccomplishment.setRefId(resourceAccomplishmentDto.getRefId());
		resourceAccomplishment.setStatus("Open");
		resourceAccomplishment.setRating("Yet To Review");
		resourceAccomplishment.setRatingByname("Yet To Review");
		resourceAccomplishment.setCreatedBy(resourceAccomplishmentDto.getCreatedBy());
		resourceAccomplishment.setCreatedDateTime(LocalDateTime.now());
		resourceAccomplishment.setUpdatedBy(resourceAccomplishmentDto.getUpdatedBy());
		resourceAccomplishment.setUpdatedDateTime(LocalDateTime.now());
		resourceAccomplishment.setRoleId(resourceAccomplishmentDto.getRoleId());

		resourceAccomplishmentRepository.save(resourceAccomplishment);

	}

	@Override
	public ResourceAccomplishmentDto deleteResourceAccomplishmentById(
			ResourceAccomplishmentDto resourceAccomplishmentDto, String lang) throws Exception {

		resourceAccomplishmentRepository.deleteById(resourceAccomplishmentDto.getId());
		resourceAccomplishmentDto.setMessage("Record Deleted");
		return resourceAccomplishmentDto;
	}

	@Override
	public List<ResourceAccomplishmentDto> getAllResourceAccomplishment() throws Exception {

		List<ResourceAccomplishmentDto> resourceAccomplishmentDtoList = new ArrayList<ResourceAccomplishmentDto>();

		List<ResourceAccomplishment> resourceAccomplishmentList = new ArrayList<ResourceAccomplishment>();
		resourceAccomplishmentList = resourceAccomplishmentRepository.findAll();
		for (ResourceAccomplishment resourceAccomplishment : resourceAccomplishmentList) {
			resourceAccomplishmentDtoList.add(this.getResourceAccomplishmentDto(resourceAccomplishment));
		}

		return resourceAccomplishmentDtoList;
	}
	@Override
	public List<ResourceAccomplishmentDto> getResourceAccomplishmentByBuId(long buId) throws Exception {

		List<ResourceAccomplishmentDto> resourceAccomplishmentDtoList = new ArrayList<ResourceAccomplishmentDto>();

		List<ResourceAccomplishment> resourceAccomplishmentList = new ArrayList<ResourceAccomplishment>();
		resourceAccomplishmentList = resourceAccomplishmentRepository.findByBuId(buId);
		for (ResourceAccomplishment resourceAccomplishment : resourceAccomplishmentList) {
			resourceAccomplishmentDtoList.add(this.getResourceAccomplishmentDto(resourceAccomplishment));
		}

		return resourceAccomplishmentDtoList;
	}

	private ResourceAccomplishmentDto getResourceAccomplishmentDto(ResourceAccomplishment resourceAccomplishment) {

		ResourceAccomplishmentDto resourceAccomplishmentDto = new ResourceAccomplishmentDto();

		resourceAccomplishmentDto.setId(resourceAccomplishment.getId());
		resourceAccomplishmentDto.setResourceName(resourceAccomplishment.getResourceName());
		resourceAccomplishmentDto.setAchievements(resourceAccomplishment.getAchievements());
		resourceAccomplishmentDto.setEmployeeId(resourceAccomplishment.getEmployeeId());
		resourceAccomplishmentDto.setRoleId(resourceAccomplishment.getRoleId());
		resourceAccomplishmentDto.setRoleName(CacheUtil.getRolesMap().get(resourceAccomplishment.getRoleId()));
		resourceAccomplishmentDto.setCreatedBy(resourceAccomplishment.getCreatedBy());
		resourceAccomplishmentDto.setRating(resourceAccomplishment.getRating());
		if (resourceAccomplishment.getRatingByname() != null) {
			resourceAccomplishmentDto.setRatingByname(resourceAccomplishment.getRatingByname());
		} else {
			resourceAccomplishmentDto.setRatingByname("Yet To Review");

		}
		resourceAccomplishmentDto.setReviewedOn(resourceAccomplishment.getReviewedOn());
		resourceAccomplishmentDto.setRatingbyId(resourceAccomplishment.getRatingbyId());
		resourceAccomplishmentDto.setStatus(resourceAccomplishment.getStatus());
		resourceAccomplishmentDto.setCreatedDateTime(resourceAccomplishment.getCreatedDateTime());
		resourceAccomplishmentDto.setUpdatedDateTime(resourceAccomplishment.getUpdatedDateTime());
		resourceAccomplishmentDto.setUpdatedBy(resourceAccomplishment.getUpdatedBy());
		resourceAccomplishmentDto.setComments(resourceAccomplishment.getComments());

		User createdUser = userRepository.findById(resourceAccomplishment.getCreatedBy());
		resourceAccomplishmentDto.setCreatedByName(createdUser.getFirstName() + " " + createdUser.getLastName());
		resourceAccomplishmentDto.setDesignation(createdUser.getDesignation());

		User updatedUser = userRepository.findById(resourceAccomplishment.getUpdatedBy());
		resourceAccomplishmentDto.setUpdatedByName(updatedUser.getFirstName() + " " + updatedUser.getLastName());
		resourceAccomplishmentDto.setDesignation(updatedUser.getDesignation());

		resourceAccomplishmentDto.setBuId(resourceAccomplishment.getBuId());
		BusinessUnit bu = businessUnitRepository.findById(resourceAccomplishment.getBuId());
		resourceAccomplishmentDto.setBuName(bu.getBusinessUnitName());

//		resourceAccomplishmentDto.setRoleId(resourceAccomplishment.getRoleId());
//		Role role =roleRepository.findById(resourceAccomplishment.getRoleId());
//		resourceAccomplishmentDto.setRoleName(role.getRoleName());

		return resourceAccomplishmentDto;
	}

	@Override
	public ResourceAccomplishmentDto getResourceAccomplishmentById(Long id) throws Exception {
		ResourceAccomplishmentDto resourceAccomplishmentDto = new ResourceAccomplishmentDto();
		ResourceAccomplishment resourceAccomplishment = resourceAccomplishmentRepository.findById(id);
		if (resourceAccomplishment != null) {
			resourceAccomplishmentDto = getResourceAccomplishmentDto(resourceAccomplishment);
		} else {
			resourceAccomplishmentDto.setMessage("Entry not Found");
		}
		return resourceAccomplishmentDto;
	}

	@Override
	public ResourceAccomplishmentDto deleteResourceAccomplishmentById(Long id) throws Exception {
		resourceAccomplishmentRepository.deleteById(id);
		return null;
	}

	@Override
	public List<ResourceAccomplishmentDto> getResourceAccomplishmentByName(String name) {
		List<ResourceAccomplishmentDto> resourceAccomplishmentDto = new ArrayList<ResourceAccomplishmentDto>();
		List<ResourceAccomplishment> resourceAccomplishment = new ArrayList<ResourceAccomplishment>();
		resourceAccomplishment = resourceAccomplishmentRepository.findByResourceName(name);
		if (resourceAccomplishment != null) {
			// System.out.println(resourceAccomplishment.getResourceName());
			for (ResourceAccomplishment resource : resourceAccomplishment)
				resourceAccomplishmentDto.add(getResourceAccomplishmentDto(resource));
		} else {
			// resourceAccomplishmentDto.setMessage("Entry not Found");
		}
		return resourceAccomplishmentDto;
	}

	@Override
	public ResourceAccomplishmentDto checkEntryValidity(String name, String year) {
		List<ResourceAccomplishment> getResByname = new ArrayList<ResourceAccomplishment>();
		getResByname = resourceAccomplishmentRepository.findByResourceName(name);
		ResourceAccomplishmentDto resultDto = new ResourceAccomplishmentDto();
		if (getResByname != null) {
			if (getResByname.get(0).getCreatedDateTime().getYear() == Integer.parseInt(year)) {
				resultDto.setMessage("Already Record Exsits for this year: " + year);
				resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			} else {
				resultDto.setMessage("Record Doesn't Exsits for this year: " + year);
				resultDto.setResultStatus(HireProUsConstants.RETURN_STATUS_OK);
			}
		}
		return resultDto;

	}

}
