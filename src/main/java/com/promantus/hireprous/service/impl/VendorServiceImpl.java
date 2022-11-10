/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.HireProUsDefaultMethods;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.VendorDto;
import com.promantus.hireprous.entity.JobRequest;
import com.promantus.hireprous.entity.Vendor;
import com.promantus.hireprous.repository.JobRequestRepository;
import com.promantus.hireprous.repository.VendorRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.service.MailService;
import com.promantus.hireprous.service.VendorService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class VendorServiceImpl implements VendorService {

	private static final Logger logger = LoggerFactory.getLogger(VendorServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	MailService mailService;

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	VendorRepository vendorRepository;

	@Autowired
	JobRequestRepository jobRequestRepository;

	@Override
	public VendorDto addVendor(final VendorDto vendorDto, final String lang) throws Exception {

		VendorDto resultDto = new VendorDto();

		String errorMessage = this.checkDuplicate(vendorDto, true);
		if (errorMessage != null && !errorMessage.isEmpty()) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { errorMessage }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		Vendor vendor = new Vendor();
		vendor.setId(commonService.nextSequenceNumber());

		this.setVendorId(vendor);

		vendor.setVendorName(vendorDto.getVendorName());
		vendor.setPassword(HireProUsUtil.encrypt(HireProUsUtil.generateUUID(7)));

		vendor.setEmail(vendorDto.getEmail());
		vendor.setContactNumber(vendorDto.getContactNumber());
		vendor.setLocation(vendorDto.getLocation());

		vendor.setAddress(vendorDto.getAddress());
		vendor.setCcEmailIds(vendorDto.getCcEmailIds());
		vendor.setActive("1");

		vendor.setCreatedBy(vendorDto.getCreatedBy());
		vendor.setUpdatedBy(vendorDto.getUpdatedBy());
		vendor.setCreatedDateTime(LocalDateTime.now());
		vendor.setUpdatedDateTime(LocalDateTime.now());
		vendor.setVendorPriority(vendorDto.getVendorPriority());

		vendorRepository.save(vendor);

		CacheUtil.getVendorsMap().put(vendor.getId(), vendor.getVendorName());
		CacheUtil.getVendorsEmailMap().put(vendor.getId(), vendor.getEmail());
		CacheUtil.getVendorsCCEmailMap().put(vendor.getId(), vendor.getCcEmailIds());

		vendorDto.setVendorId(vendor.getVendorId());

		// Send Vendor Created Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendVendorCreatedEmail(vendorDto, HireProUsUtil.decrypt(vendor.getPassword()));
				} catch (Exception e) {

					logger.error("Email for Vendor Created is not Sent, To - " + vendor.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setId(vendor.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	/**
	 * @return
	 */
	private void setVendorId(final Vendor vendor) {

		int year = Calendar.getInstance().get(Calendar.YEAR);
		String runningNumber = String.format("%0" + HireProUsConstants.ON_BOARD_MAX_DIGIT_RUNNING_NUMBER + "d", 1);

		Vendor vendorCheck = vendorRepository.findFirstByYear(year, HireProUsUtil.orderByRunningNumberDesc());

		if (vendorCheck != null) {
			runningNumber = String.format("%0" + HireProUsConstants.ON_BOARD_MAX_DIGIT_RUNNING_NUMBER + "d",
					vendorCheck.getRunningNumber() + 1);
		}

		vendor.setYear(year);
		vendor.setRunningNumber(Integer.parseInt(runningNumber));
		vendor.setVendorId("V" + year + runningNumber);
	}

	@Override
	public VendorDto updateVendor(final VendorDto vendorDto, final String lang) throws Exception {

		VendorDto resultDto = new VendorDto();

		Vendor vendor = vendorRepository.findById(vendorDto.getId());

		if (vendor == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Vendor Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		String errorMessage = this.checkDuplicate(vendorDto, false);
		if (errorMessage != null && !errorMessage.isEmpty()) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { errorMessage }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		vendor.setVendorName(vendorDto.getVendorName());
		vendor.setEmail(vendorDto.getEmail());
		vendor.setContactNumber(vendorDto.getContactNumber());
		vendor.setLocation(vendorDto.getLocation());

		vendor.setAddress(vendorDto.getAddress());
		vendor.setCcEmailIds(vendorDto.getCcEmailIds());
		vendor.setActive(vendorDto.getActive());

		vendor.setUpdatedBy(vendorDto.getUpdatedBy());
		vendor.setUpdatedDateTime(LocalDateTime.now());
		vendor.setVendorPriority(vendorDto.getVendorPriority());

		vendorRepository.save(vendor);

		vendorDto.setVendorId(vendor.getVendorId());

		// Cache.
		CacheUtil.getVendorsMap().remove(vendor.getId());
		CacheUtil.getVendorsMap().put(vendor.getId(), vendor.getVendorName());

		CacheUtil.getVendorsEmailMap().remove(vendor.getId());
		CacheUtil.getVendorsEmailMap().put(vendor.getId(), vendor.getEmail());

		CacheUtil.getVendorsCCEmailMap().remove(vendor.getId());
		CacheUtil.getVendorsCCEmailMap().put(vendor.getId(), vendor.getCcEmailIds());

		// Send Vendor Updated Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendVendorUpdatedEmail(vendorDto, HireProUsUtil.decrypt(vendor.getPassword()));
				} catch (Exception e) {

					logger.error("Email for Vendor Updated is not Sent, To - " + vendor.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setId(vendor.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	/**
	 * @param vendorDto
	 * @param isAdd
	 * @return
	 * @throws Exception
	 */
	private String checkDuplicate(final VendorDto vendorDto, boolean isAdd) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();
		criteriaList.add(new Criteria().orOperator(
				Criteria.where("vendorName").regex("(?i).*" + vendorDto.getVendorName() + ".*"),
				Criteria.where("contactNumber").is(vendorDto.getContactNumber()),
				Criteria.where("email").regex("(?i).*" + vendorDto.getEmail() + ".*")));

		Query searchQuery = new Query();
		searchQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
		List<Vendor> vendorListCheck = mongoTemplate.find(searchQuery, Vendor.class);

		String errorMessage = "";
		if ((isAdd && vendorListCheck != null && vendorListCheck.size() > 0) || (vendorListCheck != null
				&& vendorListCheck.size() > 0 && !vendorDto.getId().equals(vendorListCheck.get(0).getId()))) {

			if (vendorDto.getVendorName().equals(vendorListCheck.get(0).getVendorName())) {
				errorMessage = "Vendor Name";
			} else if (vendorDto.getContactNumber().equals(vendorListCheck.get(0).getContactNumber())) {
				errorMessage = "Contact Number";
			} else if (vendorDto.getEmail().equals(vendorListCheck.get(0).getEmail())) {
				errorMessage = "Email";
			}
		}

		return errorMessage;
	}

	@Override
	public List<VendorDto> getAllVendors() throws Exception {

		List<Vendor> vendorsList = vendorRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<VendorDto> vendorDtoList = new ArrayList<VendorDto>();
		for (Vendor vendor : vendorsList) {
			vendorDtoList.add(this.getVendorDto(vendor));
		}

		return vendorDtoList;
	}

	@Override
	public List<VendorDto> getActiveVendors() throws Exception {

		List<Vendor> vendorsList = vendorRepository.findByActive("1", HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<VendorDto> vendorDtoList = new ArrayList<VendorDto>();
		for (Vendor vendor : vendorsList) {
			vendorDtoList.add(this.getVendorDto(vendor));
		}

		return vendorDtoList;
	}

	@Override
	public VendorDto getVendorById(final String vendorId) throws Exception {

		Vendor vendor = vendorRepository.findById(Long.parseLong(vendorId));

		return vendor != null ? this.getVendorDto(vendor) : new VendorDto();
	}

	@Override
	public List<String> getAllVendorIds() throws Exception {

		List<Vendor> vendorsList = vendorRepository.findAll(Sort.by(Sort.Direction.ASC, "vendorId"));

		List<String> employeeIds = new ArrayList<String>();
		for (Vendor vendor : vendorsList) {
			if (vendor.getVendorId() != null && !vendor.getVendorId().isEmpty()) {
				employeeIds.add(vendor.getVendorId());
			}
		}

		return employeeIds;
	}

	@Override
	public List<String> getAllVendorNames() throws Exception {

		List<Vendor> vendorsList = vendorRepository.findAll(Sort.by(Sort.Direction.ASC, "vendorName"));

		List<String> vendorNames = new ArrayList<String>();
		for (Vendor vendor : vendorsList) {
			if (vendor.getVendorName() != null && !vendor.getVendorName().isEmpty()) {
				vendorNames.add(vendor.getVendorName());
			}
		}

		return vendorNames;
	}

	/**
	 * @param vendor
	 * @return
	 * @throws Exception
	 */
	private VendorDto getVendorDto(final Vendor vendor) throws Exception {

		VendorDto vendorDto = new VendorDto();

		vendorDto.setId(vendor.getId());
		vendorDto.setVendorId(vendor.getVendorId());
		vendorDto.setVendorName(vendor.getVendorName());
		vendorDto.setEmail(vendor.getEmail());
		vendorDto.setContactNumber(vendor.getContactNumber());
		vendorDto.setLocation(vendor.getLocation());
		vendorDto.setVendorPriority(vendor.getVendorPriority());

		vendorDto.setAddress(vendor.getAddress());
		vendorDto.setCcEmailIds(vendor.getCcEmailIds());
		vendorDto.setActive(vendor.getActive());

		vendorDto.setCreatedBy(vendor.getCreatedBy());
		vendorDto.setCreatedByName(CacheUtil.getVendorsMap().get(vendor.getCreatedBy()));
		vendorDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(vendor.getCreatedDateTime()));

		vendorDto.setUpdatedBy(vendor.getUpdatedBy());
		vendorDto.setUpdatedByName(CacheUtil.getVendorsMap().get(vendor.getUpdatedBy()));
		vendorDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(vendor.getUpdatedDateTime()));

		return vendorDto;
	}

	@Override
	public VendorDto deleteVendorById(final String vendorId) throws Exception {

		VendorDto resultDto = new VendorDto();

		Vendor vendor = vendorRepository.findById(Long.parseLong(vendorId));

		if (vendor == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Vendor Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		boolean hasDependency = false;
		if (jobRequestService.getVendorDependencyCount(vendor.getId()) > 0) {
			hasDependency = true;
		}

		if (hasDependency) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("cannot.delete",
					new String[] { "Vendor is assigned to some task. So" }, null));
			return resultDto;
		}

		vendorRepository.deleteById(Long.parseLong(vendorId));

		CacheUtil.getVendorsMap().remove(Long.parseLong(vendorId));
		CacheUtil.getVendorsEmailMap().remove(Long.parseLong(vendorId));
		CacheUtil.getVendorsCCEmailMap().remove(Long.parseLong(vendorId));

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<VendorDto> searchVendor(final VendorDto vendorDto) throws Exception {
		List<VendorDto> vendorDtoList = new ArrayList<VendorDto>();

		final List<Criteria> criteriaList = new ArrayList<>();
		if (vendorDto.getVendorId() != null && !vendorDto.getVendorId().isEmpty()) {
			criteriaList.add(Criteria.where("vendorId").regex("(?i).*" + vendorDto.getVendorId() + ".*"));
		}
		if (vendorDto.getVendorName() != null && !vendorDto.getVendorName().isEmpty()) {
			criteriaList.add(Criteria.where("vendorName").regex("(?i).*" + vendorDto.getVendorName() + ".*"));
		}
		if (vendorDto.getLocation() != null && !vendorDto.getLocation().isEmpty()) {
			criteriaList.add(Criteria.where("location").regex("(?i).*" + vendorDto.getLocation() + ".*"));
		}
		if (vendorDto.getEmail() != null && !vendorDto.getEmail().isEmpty()) {
			criteriaList.add(Criteria.where("email").regex("(?i).*" + vendorDto.getEmail() + ".*"));
		}
		if (vendorDto.getFromDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").gte(vendorDto.getFromDateTime()));
		}
		if (vendorDto.getToDateTime() != null) {
			criteriaList.add(Criteria.where("updatedDateTime").lte(vendorDto.getToDateTime()));
		}
		if (vendorDto.getVendorPriority() != null && !vendorDto.getVendorPriority().isEmpty()) {
			criteriaList.add(Criteria.where("vendorPriority").is(vendorDto.getVendorPriority()));
		}

		List<Vendor> vendorsList = new ArrayList<Vendor>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			vendorsList = mongoTemplate.find(searchQuery, Vendor.class);
		}

		for (Vendor vendor : vendorsList) {
			vendorDtoList.add(this.getVendorDto(vendor));
		}

		Comparator<VendorDto> compareByUpdatedDateTime = Comparator.comparing(VendorDto::getUpdatedDateTime);
		vendorDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return vendorDtoList;
	}

	@Override
	public byte[] downloadVendorDetails(final List<VendorDto> vendorDtoList, final String lang) throws Exception {

		File file = resourceLoader.getResource("classpath:excel-templates/vendor_details.xlsx").getFile();
		try (Workbook resourcesMgmtWB = new XSSFWorkbook(file)) {

			Sheet sheet = resourcesMgmtWB.getSheetAt(0);
			HireProUsDefaultMethods.cleanSheet(sheet);
			int rowNum = 2;
			for (VendorDto vendorDto : vendorDtoList) {

				Row dataRow = sheet.createRow(rowNum);

				Cell slNo = dataRow.createCell(0);
				slNo.setCellValue(rowNum - 1);

				dataRow.createCell(1).setCellValue(vendorDto.getVendorId());
				dataRow.createCell(2).setCellValue(vendorDto.getVendorName());
				dataRow.createCell(3).setCellValue(vendorDto.getLocation());
				dataRow.createCell(4).setCellValue(vendorDto.getEmail());
				dataRow.createCell(5).setCellValue(vendorDto.getContactNumber());
				dataRow.createCell(6).setCellValue(vendorDto.getAddress());
				dataRow.createCell(7).setCellValue(vendorDto.getCcEmailIds());
				dataRow.createCell(8).setCellValue(vendorDto.getActive().equals("1") ? "Active" : "In-Active");
				dataRow.createCell(9).setCellValue("");

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			resourcesMgmtWB.write(outputStream);

			resourcesMgmtWB.close();

			return outputStream.toByteArray();

		} catch (Exception ex) {
			logger.error("Error during Vendor download file", ex);
			return null;
		}
	}

	@Override
	public VendorDto loginVendor(final String vendorId, final String password, final String lang) throws Exception {

		VendorDto resultDto = new VendorDto();

		Vendor vendor = vendorRepository.findByVendorId(vendorId);

		if (vendor == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Vendor Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (!password.equals(HireProUsUtil.decrypt(vendor.getPassword()))) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Password" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (vendor.getActive().equals("0")) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("vendor.inactive", null, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		resultDto.setId(vendor.getId());
		resultDto.setVendorId(vendor.getVendorId());
		resultDto.setVendorName(vendor.getVendorName());
		resultDto.setEmail(vendor.getEmail());
		resultDto.setContactNumber(vendor.getContactNumber());
		resultDto.setIsVendor("1");

		List<JobRequestDto> jobRequestDtoList = jobRequestService.getAllJobRequests();

		for (JobRequestDto jobRequestDto : jobRequestDtoList) {

			Long days = Duration.between(jobRequestDto.getCreatedDateTime(), LocalDateTime.now()).toDays();
			if (days > 15 && jobRequestDto.getVendorPriority().equals("Primary")
					&& (jobRequestDto.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START)
							|| jobRequestDto.getJobReqStatus()
									.equals(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS))) {

				JobRequest jobRequest = jobRequestRepository.findById(jobRequestDto.getId());
				jobRequest.setVendorPriority("All");
				jobRequestRepository.save(jobRequest);
			}
		}

		resultDto.setVendorPriority(vendor.getVendorPriority());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		resultDto.setMessage("Vendor Logged In Success");
		return resultDto;
	}

	@Override
	public VendorDto changePasswordVendor(final VendorDto vendorDto, String lang) throws Exception {

		VendorDto resultDto = new VendorDto();

		Vendor vendor = vendorRepository.findById(vendorDto.getId());

		if (vendor == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Vendor Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (!HireProUsUtil.decrypt(vendor.getPassword()).equals(vendorDto.getPassword())) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Current Password" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (HireProUsUtil.decrypt(vendor.getPassword()).equalsIgnoreCase(vendorDto.getNewPassword())) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(
					commonService.getMessage("password.invalid", new String[] { vendorDto.getNewPassword() }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		vendor.setPassword(HireProUsUtil.encrypt(vendorDto.getNewPassword()));

		vendor.setUpdatedBy(vendorDto.getUpdatedBy());
		vendor.setUpdatedDateTime(LocalDateTime.now());

		vendorRepository.save(vendor);

		// Send Password Updated Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendPasswordUpdateEmail(vendor.getEmail(), vendor.getVendorName(),
							HireProUsUtil.decrypt(vendor.getPassword()), vendor.getVendorId());
				} catch (Exception e) {

					logger.error("Email for Change Password Vendor is not Sent, To - " + vendor.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public VendorDto checkVendorIdAndSendOtpVendor(final String vendorId, String lang) throws Exception {

		VendorDto resultDto = new VendorDto();

		Vendor vendor = vendorRepository.findByVendorId(vendorId);

		if (vendor == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Vendor Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (vendor.getActive().equals("0")) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("vendor.inactive", null, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		final String otp = HireProUsUtil.getOTP();

		// Set in Cache.
		CacheUtil.getResetOtpMap().put(vendorId, otp);

		// Send SendOtp Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendOTPEmail(vendor.getEmail(), vendor.getVendorName(), otp);
				} catch (Exception e) {

					logger.error("Email for Send OTP to reset password Vendor is not Sent, To - " + vendor.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public VendorDto resetPasswordVendor(final VendorDto vendorDto, String lang) throws Exception {

		VendorDto resultDto = new VendorDto();

		Vendor vendor = vendorRepository.findByVendorId(vendorDto.getVendorId());

		if (vendor == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Vendor Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		vendor.setPassword(HireProUsUtil.encrypt(vendorDto.getNewPassword()));

		vendor.setUpdatedBy(vendorDto.getUpdatedBy());
		vendor.setUpdatedDateTime(LocalDateTime.now());

		vendorRepository.save(vendor);

		// Send Password Updated Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendPasswordUpdateEmail(vendor.getEmail(), vendor.getVendorName(),
							HireProUsUtil.decrypt(vendor.getPassword()), vendor.getVendorId());
				} catch (Exception e) {

					logger.error("Email for reset Password Vendor is not Sent, To - " + vendor.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}
}