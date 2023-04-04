/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.entity.ResourceMgmt;
import com.promantus.hireprous.entity.Role;
import com.promantus.hireprous.entity.User;
import com.promantus.hireprous.repository.ResourceMgmtRepository;
import com.promantus.hireprous.repository.UserRepository;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.LoginService;
import com.promantus.hireprous.service.MailService;
import com.promantus.hireprous.service.RoleMenuMappingService;
import com.promantus.hireprous.service.RoleService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class LoginServiceImpl implements LoginService {

	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Value("${super.admin.password}")
	private String saPassword;

	@Autowired
	CommonService commonService;

	@Autowired
	MailService mailService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	RoleService roleService;

	@Autowired
	RoleMenuMappingService roleMenuMappingService;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ResourceMgmtRepository resourceMgmtRepository;

	@Override
	public UserDto login(final String userName, final String password, final String lang) throws Exception {

		UserDto resultDto = new UserDto();

		User user = userRepository.findByEmail(userName);

		if (user == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "User Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (!password.equals(HireProUsUtil.decrypt(user.getPassword()))) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Password" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (user.getActive().equals("0")) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("user.inactive", null, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		resultDto.setId(user.getId());
		resultDto.setEmail(user.getEmail());
		resultDto.setFirstName(user.getFirstName());
		resultDto.setLastName(user.getLastName());
		resultDto.setFullName(user.getFirstName() + " " + user.getLastName());
		resultDto.setDesignation(user.getDesignation());

		resultDto.setManagerId(user.getManagerId());
		resultDto.setManagerName(userRepository.findById(user.getManagerId()).getFirstName() + " "
				+ userRepository.findById(user.getManagerId()).getLastName());

		resultDto.setBusinessUnitId(user.getBusinessUnitId());
		resultDto.setBusinessUnitName(businessUnitService.getBusinessUnitNameById(user.getBusinessUnitId()));
		
		resultDto.setMessage("Logged In Success");;

		resultDto.setRoleId(user.getRoleId());
		resultDto.setRoleName(roleService.getRoleNameById(user.getRoleId()));

		resultDto.setMainMenus("");
		resultDto.setSubMenus("");
		Role role = roleService.getRoleById(user.getRoleId());
		if (role != null) {
			resultDto.setMainMenus(roleMenuMappingService.getMainMenusForLogin(role));
			resultDto.setSubMenus(roleMenuMappingService.getSubMenusForLogin(role));
		}
		
		ResourceMgmt resource = resourceMgmtRepository.findByEmailIgnoreCase(user.getEmail());
		if(resource != null) {
			resultDto.setUserType(resource.getEmploymentType());
			}

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public UserDto loginsa(final String userName, final String password, final String lang) throws Exception {

		UserDto resultDto = new UserDto();

		if (!password.equals(HireProUsUtil.decrypt(saPassword))) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Password" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		resultDto.setId(0);
		resultDto.setEmail("superadmin@mail.com");
		resultDto.setFirstName("Super");
		resultDto.setLastName("Admin");
		resultDto.setFullName("Super Admin");

		resultDto.setBusinessUnitId(0);
		resultDto.setRoleId(0);
		resultDto.setRoleName("Super Admin");
		resultDto.setMainMenus("All");
		resultDto.setSubMenus("All");

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public UserDto updateProfile(final UserDto userDto, final String lang) throws Exception {

		UserDto resultDto = new UserDto();

		User user = userRepository.findById(userDto.getId());

		if (user == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "User Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());

		user.setBusinessUnitId(userDto.getBusinessUnitId());
		user.setRoleId(userDto.getRoleId());

		user.setSex(userDto.getSex());
		user.setSkillSet(userDto.getSkillSet());
		user.setLocation(userDto.getLocation());
		user.setDesignation(userDto.getDesignation());

		user.setUpdatedBy(userDto.getUpdatedBy());
		user.setUpdatedDateTime(LocalDateTime.now());

		userRepository.save(user);

		// Send Profile Updated Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendProfileUpdateEmail(userDto, HireProUsUtil.decrypt(user.getPassword()));
				} catch (Exception e) {

					logger.error("Email for Update Profile is not Sent, To - " + userDto.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public UserDto changePassword(final UserDto userDto, String lang) throws Exception {

		UserDto resultDto = new UserDto();

		User user = userRepository.findById(userDto.getId());

		if (user == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "User Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (!HireProUsUtil.decrypt(user.getPassword()).equals(userDto.getPassword())) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Current Password" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}
		if (HireProUsUtil.decrypt(user.getPassword()).equalsIgnoreCase(userDto.getNewPassword())) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(
					commonService.getMessage("password.invalid", new String[] { userDto.getNewPassword() }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		user.setPassword(HireProUsUtil.encrypt(userDto.getNewPassword()));

		user.setUpdatedBy(userDto.getUpdatedBy());
		user.setUpdatedDateTime(LocalDateTime.now());

		userRepository.save(user);

		// Send Password Updated Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendPasswordUpdateEmail(user.getEmail(), user.getFirstName() + " " + user.getLastName(),
							HireProUsUtil.decrypt(user.getPassword()), null);
				} catch (Exception e) {

					logger.error("Email for Change Password is not Sent, To - " + user.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public UserDto checkEmailAndSendOtp(final String email, String lang) throws Exception {

		UserDto resultDto = new UserDto();

		User user = userRepository.findByEmailIgnoreCase(email);

		if (user == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Email" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		if (user.getActive().equals("0")) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("user.inactive", null, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		final String otp = HireProUsUtil.getOTP();

		// Set in Cache.
		CacheUtil.getResetOtpMap().put(email, otp);

		// Send SendOtp Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendOTPEmail(user.getEmail(), user.getFirstName() + " " + user.getLastName(), otp);
				} catch (Exception e) {

					logger.error("Email for Send OTP to reset password is not Sent, To - " + user.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public UserDto resetPassword(final UserDto userDto, String lang) throws Exception {

		UserDto resultDto = new UserDto();

		User user = userRepository.findByEmailIgnoreCase(userDto.getEmail());

		if (user == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "Email" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		user.setPassword(HireProUsUtil.encrypt(userDto.getNewPassword()));

		user.setUpdatedBy(userDto.getUpdatedBy());
		user.setUpdatedDateTime(LocalDateTime.now());

		userRepository.save(user);

		// Send Password Updated Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendPasswordUpdateEmail(user.getEmail(), user.getFirstName() + " " + user.getLastName(),
							HireProUsUtil.decrypt(user.getPassword()), null);
				} catch (Exception e) {

					logger.error("Email for reset Password is not Sent, To - " + user.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}
}
