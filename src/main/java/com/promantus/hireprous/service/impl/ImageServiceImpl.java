/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.util.Base64;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.ImageDto;
import com.promantus.hireprous.entity.Image;
import com.promantus.hireprous.repository.ImageRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.ImageService;

/**
 * @author Sihab.
 *
 */
@Service
public class ImageServiceImpl implements ImageService {

	private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	ImageRepository imageRepository;

	@Override
	public ImageDto uploadImage(final String imageName, final String imageType, final MultipartFile imageFile,
			final String lang) throws Exception {

		ImageDto resultDto = new ImageDto();

		Image image = imageRepository.getImageByImageName(imageName);

		if (image == null) {

			image = new Image();
			image.setImageName(imageName);
			image.setImageType(imageType);
			image.setImage(new Binary(BsonBinarySubType.BINARY, imageFile.getBytes()));

			imageRepository.insert(image);

			logger.info("Image Added.");

		} else {

			image.setImageType(imageType);
			image.setImage(new Binary(BsonBinarySubType.BINARY, imageFile.getBytes()));

			imageRepository.save(image);

			logger.info("Image Updated.");
		}

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public ImageDto getImageByName(String imageName) throws Exception {

		ImageDto imageDto = new ImageDto();

		Image image = imageRepository.getImageByImageName(imageName);
		if (image != null) {

			imageDto.setId(image.getId());
			imageDto.setImageName(image.getImageName());
			imageDto.setImageType(image.getImageType());

			imageDto.setImage(HireProUsConstants.BASE64_IMAGE_PREFIX.replace("[imgType]", image.getImageType())
					+ Base64.getEncoder().encodeToString(image.getImage().getData()));
		}

		return imageDto;
	}

	@Override
	public String getImage(String imageName) throws Exception {

		Image image = imageRepository.getImageByImageName(imageName);

		return image != null
				? HireProUsConstants.BASE64_IMAGE_PREFIX.replace("[imgType]", image.getImageType())
						+ Base64.getEncoder().encodeToString(image.getImage().getData())
				: "";
	}

	@Override
	public void deleteImageByName(final String imageName) throws Exception {

		imageRepository.deleteByImageName(imageName);
	}
}
