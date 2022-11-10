/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.HireProUsDefaultMethods;
import com.promantus.hireprous.dto.EmailConfDto;
import com.promantus.hireprous.entity.EmailConf;
import com.promantus.hireprous.repository.EmailConfRepository;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.CustomerService;
import com.promantus.hireprous.service.EmailConfigurationService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class EmailConfigurationServiceImpl implements EmailConfigurationService {

	private static final Logger logger = LoggerFactory.getLogger(EmailConfigurationServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	CustomerService customerService;

	@Autowired
	EmailConfRepository emailRepository;

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public Boolean checkPurpose(String purpose, long buId) throws Exception {

		EmailConf email = emailRepository.getEmailConfByPurposeIgnoreCaseAndBuId(purpose, buId);

		if (email != null) {
			return true;
		}

		return false;
	}

	@Override
	public EmailConfDto addEmailConf(final EmailConfDto emailDto, final String lang) throws Exception {

		EmailConfDto resultDto = new EmailConfDto();
		if (this.checkPurpose(emailDto.getPurpose(), emailDto.getBuId())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists",
					new String[] { "EmailConf Purpose, Business Unit" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		EmailConf email = new EmailConf();
		email.setId(commonService.nextSequenceNumber());
		email.setPurpose(emailDto.getPurpose());

		email.setBuId(emailDto.getBuId());

		email.setTo(emailDto.getTo());
		email.setCc(emailDto.getCc());
		email.setBcc(emailDto.getBcc());

		email.setCreatedBy(emailDto.getCreatedBy());
		email.setUpdatedBy(emailDto.getUpdatedBy());
		email.setCreatedDateTime(LocalDateTime.now());
		email.setUpdatedDateTime(LocalDateTime.now());

		emailRepository.save(email);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public EmailConfDto updateEmailConf(final EmailConfDto emailDto, final String lang) throws Exception {

		EmailConfDto resultDto = new EmailConfDto();

		EmailConf email = emailRepository.findById(emailDto.getId());

		if (email == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "EmailConf Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (this.checkPurposeForUpdate(emailDto.getPurpose(), emailDto.getBuId(), emailDto.getId())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists",
					new String[] { "EmailConf Purpose, Business Unit" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		email.setPurpose(emailDto.getPurpose());

		email.setBuId(emailDto.getBuId());

		email.setTo(emailDto.getTo());
		email.setCc(emailDto.getCc());
		email.setBcc(emailDto.getBcc());

		email.setUpdatedBy(emailDto.getUpdatedBy());
		email.setUpdatedDateTime(LocalDateTime.now());

		emailRepository.save(email);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<EmailConfDto> getAllEmails() throws Exception {

		List<EmailConfDto> emailDtoList = new ArrayList<EmailConfDto>();

		List<EmailConf> emailsList = emailRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (EmailConf email : emailsList) {
			emailDtoList.add(this.getEmailDto(email));
		}

		return emailDtoList;
	}

	@Override
	public List<EmailConfDto> getAllEmailConfByBuId(final String buId, final String lang) throws Exception {

		List<EmailConf> emailsList = emailRepository.getEmailConfByBuId(Long.parseLong(buId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<EmailConfDto> emailDtoList = new ArrayList<EmailConfDto>();
		for (EmailConf email : emailsList) {
			emailDtoList.add(this.getEmailDto(email));
		}

		return emailDtoList;
	}

	@Override
	public EmailConfDto getEmailConfById(final String confId) throws Exception {

		EmailConf email = emailRepository.findById(Long.parseLong(confId));

		return email != null ? this.getEmailDto(email) : new EmailConfDto();
	}

	@Override
	public EmailConfDto getEmailConfByPurposeAndBuId(final String purpose, final Long buId) throws Exception {

		EmailConf emailConf = emailRepository.getEmailConfByPurposeIgnoreCaseAndBuId(purpose, buId);

		return emailConf != null ? this.getEmailDto(emailConf) : new EmailConfDto();
	}

	/**
	 * @param email
	 * @return
	 * @throws Exception
	 */
	private EmailConfDto getEmailDto(final EmailConf email) throws Exception {

		EmailConfDto emailDto = new EmailConfDto();
		emailDto.setId(email.getId());
		emailDto.setPurpose(email.getPurpose());

		emailDto.setBuId(email.getBuId());
		emailDto.setBuName(CacheUtil.getBusMap().get(email.getBuId()));

		emailDto.setTo(email.getTo());
		emailDto.setCc(email.getCc());
		emailDto.setBcc(email.getBcc());

		emailDto.setCreatedBy(email.getCreatedBy());
		emailDto.setCreatedByName(CacheUtil.getUsersMap().get(email.getCreatedBy()));
		emailDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(email.getCreatedDateTime()));

		emailDto.setUpdatedBy(email.getUpdatedBy());
		emailDto.setUpdatedByName(CacheUtil.getUsersMap().get(email.getUpdatedBy()));
		emailDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(email.getUpdatedDateTime()));

		return emailDto;
	}

	@Override
	public void deleteEmailConfById(final String emailId) throws Exception {

		emailRepository.deleteById(Long.parseLong(emailId));
	}

	@Override
	public List<EmailConfDto> searchEmailConf(final String keyword) throws Exception {

		List<EmailConfDto> emailDtoList = new ArrayList<EmailConfDto>();

		List<EmailConf> emailsList = emailRepository.findByPurposeRegex("(?i).*" + keyword + ".*",
				HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (EmailConf email : emailsList) {
			emailDtoList.add(this.getEmailDto(email));
		}

		return emailDtoList;
	}

	@Override
	public int getBUDependencyCount(Long buId) {

		return emailRepository.countByBuId(buId);
	}

	@Override
	public byte[] downloadEmailConfDetails(List<EmailConfDto> emailConfDtoList, String lang) throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/emailConf_details.xlsx").getFile();
		try (Workbook emailConfDetailsWB = new XSSFWorkbook(file)) {

			Sheet sheet = emailConfDetailsWB.getSheetAt(0);
			HireProUsDefaultMethods.cleanSheet(sheet);
			int rowNum = 2;
			for (EmailConfDto emailConfDto : emailConfDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(emailConfDto.getBuName());
				dataRow.createCell(2).setCellValue(emailConfDto.getPurpose());
				dataRow.createCell(3).setCellValue(emailConfDto.getTo());
				dataRow.createCell(4).setCellValue(emailConfDto.getCc());
				dataRow.createCell(5).setCellValue(emailConfDto.getBcc());
				dataRow.createCell(6).setCellValue(emailConfDto.getCreatedByName());
				dataRow.createCell(7).setCellValue(emailConfDto.getCreatedDateTime().toLocalDate().toString());
				dataRow.createCell(8).setCellValue(emailConfDto.getUpdatedByName());
				dataRow.createCell(9).setCellValue(emailConfDto.getUpdatedDateTime().toLocalDate().toString());
				dataRow.createCell(10).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			emailConfDetailsWB.write(outputStream);

			emailConfDetailsWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during EmailConf Details download file", ex);
			return null;
		}
	}

	@Override
	public List<EmailConfDto> searchEmailConfForDownload(EmailConfDto emailConfDto) throws Exception {

		List<EmailConfDto> emailConfDtoList = new ArrayList<EmailConfDto>();
		final List<Criteria> criteriaList = new ArrayList<>();

		if (emailConfDto.getBuId() != null) {
			criteriaList.add(Criteria.where("buId").is(emailConfDto.getBuId()));
		}

		if (emailConfDto.getPurpose() != null && !emailConfDto.getPurpose().isEmpty()) {
			criteriaList.add(Criteria.where("purpose").is(emailConfDto.getPurpose()));
		}

		if (emailConfDto.getTo() != null && !emailConfDto.getTo().isEmpty()) {
			criteriaList
					.add(new Criteria().orOperator(Criteria.where("to").regex("(?i).*" + emailConfDto.getTo() + ".*")));
		}

		if (emailConfDto.getCc() != null && !emailConfDto.getCc().isEmpty()) {
			criteriaList
					.add(new Criteria().orOperator(Criteria.where("cc").regex("(?i).*" + emailConfDto.getCc() + ".*")));
		}

		if (emailConfDto.getBcc() != null && !emailConfDto.getBcc().isEmpty()) {
			criteriaList.add(
					new Criteria().orOperator(Criteria.where("bcc").regex("(?i).*" + emailConfDto.getBcc() + ".*")));
		}

		if (emailConfDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(emailConfDto.getFromDateTime()));
		}
		if (emailConfDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(emailConfDto.getToDateTime()));
		}

		List<EmailConf> emailConfList = new ArrayList<EmailConf>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));

			emailConfList = mongoTemplate.find(searchQuery, EmailConf.class);
		}

		for (EmailConf emailConf : emailConfList) {
			emailConfDtoList.add(this.getEmailDto(emailConf));
		}

		Comparator<EmailConfDto> compareByUpdatedDateTime = Comparator.comparing(EmailConfDto::getUpdatedDateTime);
		emailConfDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return emailConfDtoList;
	}

	private Boolean checkPurposeForUpdate(String purpose, long buId, long id) throws Exception {

		EmailConf email = emailRepository.getEmailConfByPurposeIgnoreCaseAndBuId(purpose, buId);

		if (email != null && !(id == email.getId())) {
			return true;
		}

		return false;
	}

}
