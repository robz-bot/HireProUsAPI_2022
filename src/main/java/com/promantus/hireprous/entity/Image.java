/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.entity;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class for the MongoDB collection - images.
 * 
 * @author Sihab.
 *
 */
@Document(collection = "images")
public class Image {

	@Id
	private String id;
	private String imageName;
	private String imageType;
	private Binary image;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the image
	 */
	public Binary getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Binary image) {
		this.image = image;
	}

	/**
	 * @return the imageType
	 */
	public String getImageType() {
		return imageType;
	}

	/**
	 * @param imageType the imageType to set
	 */
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
}
