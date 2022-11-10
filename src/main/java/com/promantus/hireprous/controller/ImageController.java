/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.ImageDto;
import com.promantus.hireprous.service.ImageService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * Controller class to handle User login related APIs.
 * 
 * @author Sihab.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ImageController extends CommonController {

	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	private ImageService imageService;

	/**
	 * @param imageName
	 * @param imageType
	 * @param image
	 * @param lang
	 * @return
	 */
	@PostMapping("/uploadImage/{imageName}/{imageType}")
	public ImageDto uploadImage(@PathVariable String imageName, @PathVariable String imageType,
			@RequestParam("image") MultipartFile image, @RequestHeader(name = "lang", required = false) String lang) {

		logger.info("Upload Image is started.");

		ImageDto resultDto = new ImageDto();
		try {
			resultDto = imageService.uploadImage(imageName, imageType, image, lang);
		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		logger.info("Upload Image is completed.");
		return resultDto;
	}

	/**
	 * @param imageName
	 * @param lang
	 * @return
	 */
	@GetMapping("/getImageByName/{imageName}")
	public ImageDto getImageByName(@PathVariable String imageName,
			@RequestHeader(name = "lang", required = false) String lang) {

		logger.info("Get Image is started.");

		ImageDto resultDto = new ImageDto();

		try {
			resultDto = imageService.getImageByName(imageName);
		} catch (final Exception e) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(e.getMessage());

			logger.error(HireProUsUtil.getErrorMessage(e));
			return resultDto;
		}

		logger.info("Get Image is completed.");
		return resultDto;
	}

	/**
	 * @param imageName
	 * @param lang
	 * @return
	 */
	@DeleteMapping("/deleteImageByName/{imageName}")
	public Boolean deleteImageByName(@PathVariable String imageName,
			@RequestHeader(name = "lang", required = false) String lang) {

		logger.info("Delete Image is started.");

		try {
			imageService.deleteImageByName(imageName);
		} catch (final Exception e) {
			logger.error(HireProUsUtil.getErrorMessage(e));
			return false;
		}

		logger.info("Delete Image is completed.");
		return true;
	}
}
