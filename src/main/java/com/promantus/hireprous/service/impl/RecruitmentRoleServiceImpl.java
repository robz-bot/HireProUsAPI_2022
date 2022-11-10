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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.HireProUsDefaultMethods;
import com.promantus.hireprous.dto.RecruitmentRoleDto;
import com.promantus.hireprous.entity.RecruitmentRole;
import com.promantus.hireprous.repository.RecruitmentRoleRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.RecruitmentRoleService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class RecruitmentRoleServiceImpl implements RecruitmentRoleService {

	private static final Logger logger = LoggerFactory.getLogger(RecruitmentRoleServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	RecruitmentRoleRepository recruitmentRoleRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public Boolean checkRecRoleName(String recRoleName) throws Exception {

		if (recruitmentRoleRepository.getRecruitmentRoleByRecruitmentRoleNameIgnoreCase(recRoleName) != null) {
			return true;
		}

		return false;
	}

	@Override
	public RecruitmentRoleDto addRecruitmentRole(final RecruitmentRoleDto recruitmentRoleDto, final String lang)
			throws Exception {

		RecruitmentRoleDto resultDto = new RecruitmentRoleDto();
		if (this.checkRecRoleName(recruitmentRoleDto.getRecruitmentRoleName())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(
					commonService.getMessage("already.exists", new String[] { "Recruitment Role Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		RecruitmentRole recruitmentRole = new RecruitmentRole();
		recruitmentRole.setId(commonService.nextSequenceNumber());
		recruitmentRole.setRecruitmentRoleName(recruitmentRoleDto.getRecruitmentRoleName());
		recruitmentRole.setCreatedBy(recruitmentRoleDto.getCreatedBy());
		recruitmentRole.setUpdatedBy(recruitmentRoleDto.getUpdatedBy());
		recruitmentRole.setCreatedDateTime(LocalDateTime.now());
		recruitmentRole.setUpdatedDateTime(LocalDateTime.now());

		recruitmentRoleRepository.save(recruitmentRole);

		CacheUtil.getRecRolesMap().put(recruitmentRole.getId(), recruitmentRole.getRecruitmentRoleName());

		resultDto.setId(recruitmentRole.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public RecruitmentRoleDto updateRecruitmentRole(final RecruitmentRoleDto recruitmentRoleDto, final String lang)
			throws Exception {

		RecruitmentRoleDto resultDto = new RecruitmentRoleDto();
		RecruitmentRole recruitmentRole = recruitmentRoleRepository.findById(recruitmentRoleDto.getId());

		if (recruitmentRole == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Recruitment Role Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (this.checkRecRoleName(recruitmentRoleDto.getRecruitmentRoleName())) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(
					commonService.getMessage("already.exists", new String[] { "Recruitment Role Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		recruitmentRole.setRecruitmentRoleName(recruitmentRoleDto.getRecruitmentRoleName());
		recruitmentRole.setUpdatedBy(recruitmentRoleDto.getUpdatedBy());
		recruitmentRole.setUpdatedDateTime(LocalDateTime.now());

		recruitmentRoleRepository.save(recruitmentRole);

		CacheUtil.getRecRolesMap().remove(recruitmentRole.getId());
		CacheUtil.getRecRolesMap().put(recruitmentRole.getId(), recruitmentRole.getRecruitmentRoleName());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<RecruitmentRoleDto> getAllRecruitmentRoles() throws Exception {

		List<RecruitmentRoleDto> recruitmentRoleDtoList = new ArrayList<RecruitmentRoleDto>();

		List<RecruitmentRole> recruitmentRolesList = recruitmentRoleRepository
				.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (RecruitmentRole recruitmentRole : recruitmentRolesList) {
			recruitmentRoleDtoList.add(this.getRecruitmentRoleDto(recruitmentRole));
		}

		return recruitmentRoleDtoList;
	}

	@Override
	public String getRecruitmentRoleNameById(final long recruitmentRoleId) throws Exception {

		RecruitmentRole recruitmentRole = recruitmentRoleRepository.findById(recruitmentRoleId);

		return recruitmentRole != null ? recruitmentRole.getRecruitmentRoleName() : "";
	}

	@Override
	public List<RecruitmentRoleDto> getRecruitmentRolesByIds(final List<Long> recruitmentRoleIds) throws Exception {

		List<RecruitmentRoleDto> recruitmentRoleDtoList = new ArrayList<RecruitmentRoleDto>();

		List<RecruitmentRole> recruitmentRolesList = recruitmentRoleRepository.findByIdIn(recruitmentRoleIds,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		for (RecruitmentRole recruitmentRole : recruitmentRolesList) {
			recruitmentRoleDtoList.add(this.getRecruitmentRoleDto(recruitmentRole));
		}

		return recruitmentRoleDtoList;
	}

	@Override
	public String getRecruitmentRoleNameByIds(final List<Long> recruitmentRoleIds) throws Exception {

		List<RecruitmentRole> recRoleList = recruitmentRoleRepository.findByIdIn(recruitmentRoleIds,
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		StringBuilder recRoleListSb = new StringBuilder("");
		for (RecruitmentRole recRole : recRoleList) {
			if (recRoleListSb.length() > 0) {
				recRoleListSb.append(", ");
			}
			recRoleListSb.append(recRole.getRecruitmentRoleName());
		}

		return recRoleListSb.toString();
	}

	@Override
	public RecruitmentRoleDto getRecruitmentRoleById(final String recruitmentRoleId) throws Exception {

		RecruitmentRole recruitmentRole = recruitmentRoleRepository.findById(Long.parseLong(recruitmentRoleId));

		return recruitmentRole != null ? this.getRecruitmentRoleDto(recruitmentRole) : new RecruitmentRoleDto();
	}

	/**
	 * @param recruitmentRole
	 * @return
	 * @throws Exception
	 */
	private RecruitmentRoleDto getRecruitmentRoleDto(final RecruitmentRole recruitmentRole) throws Exception {

		RecruitmentRoleDto recruitmentRoleDto = new RecruitmentRoleDto();

		recruitmentRoleDto.setId(recruitmentRole.getId());
		recruitmentRoleDto.setRecruitmentRoleName(recruitmentRole.getRecruitmentRoleName());

		recruitmentRoleDto.setCreatedBy(recruitmentRole.getCreatedBy());
		recruitmentRoleDto.setCreatedByName(CacheUtil.getUsersMap().get(recruitmentRole.getCreatedBy()));
		recruitmentRoleDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(recruitmentRole.getCreatedDateTime()));

		recruitmentRoleDto.setUpdatedBy(recruitmentRole.getUpdatedBy());
		recruitmentRoleDto.setUpdatedByName(CacheUtil.getUsersMap().get(recruitmentRole.getUpdatedBy()));
		recruitmentRoleDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(recruitmentRole.getUpdatedDateTime()));

		return recruitmentRoleDto;
	}

	@Override
	public void deleteRecruitmentRoleById(final String recruitmentRoleId) throws Exception {

		recruitmentRoleRepository.deleteById(Long.parseLong(recruitmentRoleId));

		CacheUtil.getRecRolesMap().remove(Long.parseLong(recruitmentRoleId));
	}

	@Override
	public List<RecruitmentRoleDto> searchRecruitmentRole(final String keyword) throws Exception {

		List<RecruitmentRoleDto> recruitmentRoleDtoList = new ArrayList<RecruitmentRoleDto>();

		List<RecruitmentRole> recruitmentRolesList = recruitmentRoleRepository
				.findByRecruitmentRoleNameRegex("(?i).*" + keyword + ".*", HireProUsUtil.orderByUpdatedDateTimeDesc());
		for (RecruitmentRole recruitmentRole : recruitmentRolesList) {
			recruitmentRoleDtoList.add(this.getRecruitmentRoleDto(recruitmentRole));
		}

		return recruitmentRoleDtoList;
	}

	@Override
	public byte[] downloadRecruitmentRoleDetails(List<RecruitmentRoleDto> recruitmentRoleDtoList, String lang)
			throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/recruitmentRole_details.xlsx").getFile();
		try (Workbook recruitmentRoleWB = new XSSFWorkbook(file)) {

			Sheet sheet = recruitmentRoleWB.getSheetAt(0);
			HireProUsDefaultMethods.cleanSheet(sheet);
			int rowNum = 2;
			for (RecruitmentRoleDto recruitmentRoleDto : recruitmentRoleDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(recruitmentRoleDto.getRecruitmentRoleName());
				dataRow.createCell(2).setCellValue(recruitmentRoleDto.getCreatedByName());
				dataRow.createCell(3).setCellValue(recruitmentRoleDto.getCreatedDateTime().toLocalDate().toString());
				dataRow.createCell(4).setCellValue(recruitmentRoleDto.getUpdatedByName());
				dataRow.createCell(5).setCellValue(recruitmentRoleDto.getUpdatedDateTime().toLocalDate().toString());
				dataRow.createCell(6).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			recruitmentRoleWB.write(outputStream);

			recruitmentRoleWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during RecruitmentRole Details download file", ex);
			return null;
		}

	}

	@Override
	public List<RecruitmentRoleDto> searchRecruitmentRoleForDownload(RecruitmentRoleDto recruitmentRoleDto)
			throws Exception {

		List<RecruitmentRoleDto> recruitmentRoleDtoList = new ArrayList<RecruitmentRoleDto>();

		final List<Criteria> criteriaList = new ArrayList<>();

		if (recruitmentRoleDto.getRecruitmentRoleName() != null
				&& !recruitmentRoleDto.getRecruitmentRoleName().isEmpty()) {
			criteriaList.add(Criteria.where("recruitmentRoleName")
					.regex("(?i).*" + recruitmentRoleDto.getRecruitmentRoleName() + ".*"));
		}
		if (recruitmentRoleDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(recruitmentRoleDto.getFromDateTime()));
		}
		if (recruitmentRoleDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(recruitmentRoleDto.getToDateTime()));
		}

		List<RecruitmentRole> recruitmentRoleList = new ArrayList<RecruitmentRole>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			recruitmentRoleList = mongoTemplate.find(searchQuery, RecruitmentRole.class);
		}

		for (RecruitmentRole recruitmentRole : recruitmentRoleList) {
			recruitmentRoleDtoList.add(this.getRecruitmentRoleDto(recruitmentRole));
		}

		Comparator<RecruitmentRoleDto> compareByUpdatedDateTime = Comparator
				.comparing(RecruitmentRoleDto::getUpdatedDateTime);
		recruitmentRoleDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return recruitmentRoleDtoList;

	}
}
