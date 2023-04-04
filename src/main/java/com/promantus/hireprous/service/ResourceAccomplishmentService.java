package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.ResourceAccomplishmentDto;

public interface ResourceAccomplishmentService {

	/**
		 * @param ResourceAccomplishmentDto
		 * @param lang
		 * @return
		 * @throws Exception
		 */
		ResourceAccomplishmentDto addResourceAccomplishment(final ResourceAccomplishmentDto resourceAccomplishmentDto, String lang)
				throws Exception;
		
		ResourceAccomplishmentDto updateResourceAccomplishment(final ResourceAccomplishmentDto resourceAccomplishmentDto, String lang)
				throws Exception;
		
		ResourceAccomplishmentDto deleteResourceAccomplishmentById(final ResourceAccomplishmentDto resourceAccomplishmentDto, String lang)
				throws Exception;
		
		List<ResourceAccomplishmentDto> getAllResourceAccomplishment()
				throws Exception;
		
		ResourceAccomplishmentDto getResourceAccomplishmentById(Long id)
				throws Exception;

		ResourceAccomplishmentDto deleteResourceAccomplishmentById(Long id) throws Exception;

		List<ResourceAccomplishmentDto> getResourceAccomplishmentByName(String name);

		ResourceAccomplishmentDto checkEntryValidity(String name,String year);

		List<ResourceAccomplishmentDto> getResourceAccomplishmentByBuId(Long buId) throws Exception;
		
		List<ResourceAccomplishmentDto> getResourceAccomplishmentByBuIdYear(Long buId,int year) throws Exception;

		
		
		

	
		

	}

