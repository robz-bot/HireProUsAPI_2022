/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import org.springframework.web.multipart.MultipartFile;

import com.promantus.hireprous.dto.ImageDto;

/**
 * @author Sihab.
 *
 */
public interface ImageService {

	/**
	 * @param imageName
	 * @param imageType
	 * @param imageFile
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	ImageDto uploadImage(String imageName, String imageType, MultipartFile imageFile, String lang) throws Exception;

	/**
	 * @param imageName
	 * @return
	 * @throws Exception
	 */
	ImageDto getImageByName(String imageName) throws Exception;

	/**
	 * @param imageId
	 * @throws Exception
	 */
	void deleteImageByName(String imageId) throws Exception;

	/**
	 * @param imageName
	 * @return
	 * @throws Exception
	 */
	String getImage(String imageName) throws Exception;

}
