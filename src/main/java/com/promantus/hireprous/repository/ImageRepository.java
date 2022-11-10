/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Image;

/**
 * @author Sihab.
 *
 */
public interface ImageRepository extends MongoRepository<Image, String> {

	/**
	 * @param imageName
	 * @return
	 */
	Image getImageByImageName(String imageName);

	/**
	 * @param imageName
	 */
	void deleteByImageName(String imageName);

}
