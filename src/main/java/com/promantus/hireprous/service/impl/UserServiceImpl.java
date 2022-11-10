/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.dto.UserSearchDto;
import com.promantus.hireprous.entity.Role;
import com.promantus.hireprous.entity.User;
import com.promantus.hireprous.repository.UserRepository;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.ImageService;
import com.promantus.hireprous.service.InterviewScheduleService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.service.MailService;
import com.promantus.hireprous.service.RoleMenuMappingService;
import com.promantus.hireprous.service.RoleService;
import com.promantus.hireprous.service.UserService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	RoleService roleService;

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	InterviewScheduleService interviewScheduleService;

	@Autowired
	ImageService imageService;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MailService mailService;

	@Autowired
	RoleMenuMappingService roleMenuMappingService;

	@Override
	public Boolean checkUserName(String userName) {

		if (userRepository.findByEmailIgnoreCase(userName) != null) {
			return true;
		}

		return false;
	}

	@Override
	public UserDto addUser(final UserDto userDto, String lang) throws Exception {

		UserDto resultDto = new UserDto();
		if (userRepository.findByEmailIgnoreCase(userDto.getEmail()) != null) {

			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("already.exists", new String[] { "User Name" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		User user = new User();
		user.setId(commonService.nextSequenceNumber());
		user.setEmail(userDto.getEmail());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setContactNumber(userDto.getContactNumber());
		user.setManagerId(userDto.getManagerId());

		user.setPassword(HireProUsUtil.encrypt(HireProUsUtil.generateUUID(7)));

		user.setBusinessUnitId(userDto.getBusinessUnitId());
		user.setRoleId(userDto.getRoleId());

		user.setSex(userDto.getSex());
		user.setSkillSet(userDto.getSkillSet());
		user.setLocation(userDto.getLocation());
		user.setDesignation(userDto.getDesignation());

		user.setFirstLogin("1");
		user.setPanelMember(userDto.getPanelMember());
		user.setColorCode(userDto.getColorCode());

		user.setCreatedBy(userDto.getCreatedBy());
		user.setUpdatedBy(userDto.getUpdatedBy());
		user.setCreatedDateTime(LocalDateTime.now());
		user.setUpdatedDateTime(LocalDateTime.now());

		user.setActive("1");

		userRepository.save(user);

		// Cache
		CacheUtil.getUsersMap().put(user.getId(), user.getFirstName() + " " + user.getLastName());
		CacheUtil.getUsersEmailMap().put(user.getId(), user.getEmail());

		// Send Registered Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendRegisteredEmail(userDto, HireProUsUtil.decrypt(user.getPassword()));
				} catch (Exception e) {

					logger.error("Email for User Registration is not Sent, To - " + user.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setId(user.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public UserDto updateUser(final UserDto userDto, final String lang) throws Exception {

		UserDto resultDto = new UserDto();

		User user = userRepository.findById(userDto.getId());

		if (user == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "User Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		// De-activate check.
		if (userDto.getActive().equals("0")) {
			boolean hasDependency = false;
			if (jobRequestService.getUserDependencyCount(user.getId()) > 0) {
				hasDependency = true;
			}
			if (interviewScheduleService.getUserDependencyCount(user.getId()) > 0) {
				hasDependency = true;
			}

			if (hasDependency) {
				resultDto.setMessage(commonService.getMessage("cannot.inactive",
						new String[] { "User is assigned to some task. So" }, null));

				resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
				return resultDto;
			}
		}

		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setContactNumber(userDto.getContactNumber());
		user.setManagerId(userDto.getManagerId());

		user.setBusinessUnitId(userDto.getBusinessUnitId());
		user.setRoleId(userDto.getRoleId());

		user.setSex(userDto.getSex());
		user.setSkillSet(userDto.getSkillSet());
		user.setLocation(userDto.getLocation());
		user.setDesignation(userDto.getDesignation());
		user.setColorCode(userDto.getColorCode());

		user.setPanelMember(userDto.getPanelMember());

		user.setActive(userDto.getActive());

		user.setUpdatedBy(userDto.getUpdatedBy());
		user.setUpdatedDateTime(LocalDateTime.now());

		userRepository.save(user);

		// Cache
		CacheUtil.getUsersMap().remove(user.getId());
		CacheUtil.getUsersMap().put(user.getId(), user.getFirstName() + " " + user.getLastName());
		CacheUtil.getUsersEmailMap().remove(user.getId());
		CacheUtil.getUsersEmailMap().put(user.getId(), user.getEmail());

		// Send Profile Updated Mail.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mailService.sendProfileUpdateEmail(userDto, HireProUsUtil.decrypt(user.getPassword()));
				} catch (Exception e) {

					logger.error("Email for Update User is not Sent, To - " + userDto.getEmail());
					logger.error(HireProUsUtil.getErrorMessage(e));
				}
			}
		}).start();

		resultDto.setId(user.getId());
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);

		return resultDto;
	}

	@Override
	public String getUserNameById(final long userId) throws Exception {

		if (userId == 0) {
			return "Super Admin";
		}

		User user = userRepository.findById(userId);

		// Cache
		CacheUtil.getUsersMap().get(user.getId());

		return user != null ? user.getFirstName() + " " + user.getLastName() : "";
	}

	@Override
	public String getEmailIdById(final long userId) throws Exception {

		User user = userRepository.findById(userId);

		return user != null ? user.getEmail() : "";
	}

	@Override
	public UserDto getUserById(final String userId) throws Exception {

		User user = userRepository.findById(Long.parseLong(userId));

		return user != null ? this.getUserDto(user) : null;
	}

	@Override
	public List<UserDto> getUsersByIdList(final List<String> userIds) throws Exception {

		List<Long> userIdsInLong = userIds.stream().map(Long::valueOf).collect(Collectors.toList());
		List<User> usersList = userRepository.findAllByIdIn(userIdsInLong, HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (User user : usersList) {
			userDtoList.add(this.getUserDtoLimitedInfo(user));
		}

		return userDtoList;
	}

	@Override
	public List<UserDto> getAllUsers() throws Exception {

		List<User> usersList = userRepository.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (User user : usersList) {
			userDtoList.add(this.getUserDto(user));
		}

		return userDtoList;
	}

	@Override
	public List<UserDto> getAllUsersByBU(final String buId, final String lang) throws Exception {

		List<User> usersList = userRepository.getUserByBusinessUnitId(Long.parseLong(buId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (User user : usersList) {
			userDtoList.add(this.getUserDto(user));
		}

		return userDtoList;
	}

	@Override
	public List<UserDto> getInterviewersByBuId(final String buId, final String lang) throws Exception {

		List<User> usersList = userRepository.getUserByBusinessUnitIdAndPanelMember(Long.parseLong(buId),
				HireProUsConstants.PANEL_MEMBER_YES, HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (User user : usersList) {
			if (user.getActive().equals("1")) {
				userDtoList.add(this.getUserDtoLimitedInfo(user));
			}
		}

		return userDtoList;
	}

	@Override
	public List<UserDto> getAllUsersByRole(final String roleId, final String lang) throws Exception {

		List<User> usersList = userRepository.getUserByRoleId(Long.parseLong(roleId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (User user : usersList) {
			userDtoList.add(this.getUserDto(user));
		}

		return userDtoList;
	}

	@Override
	public List<UserDto> getRecruiters(final String lang) throws Exception {

		List<Long> roleIds = roleService.getRoleIdsByRoleNames(Arrays.asList(
				HireProUsConstants.ROLE_NAME_RECRUITMENT_MANAGER, HireProUsConstants.ROLE_NAME_RECRUITMENT_TEAM));

		if (roleIds == null || roleIds.size() <= 0) {
			return new ArrayList<UserDto>();
		}

		List<User> usersList = userRepository.getUserByRoleIdIn(roleIds, HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (User user : usersList) {
			if (user.getActive().equals("1")) {
				userDtoList.add(this.getUserDtoLimitedInfo(user));
			}
		}

		return userDtoList;
	}

	@Override
	public List<UserDto> searchUser(final String keyword) throws Exception {

		AggregationOperation project = Aggregation.project(User.class).andExpression("concat(firstName,' ', lastName)")
				.as("fullName");
		AggregationOperation match = Aggregation.match(Criteria.where("fullName").regex("(?i).*" + keyword + ".*"));
		Aggregation aggregation = Aggregation.newAggregation(project, match);
		List<User> usersList = mongoTemplate.aggregate(aggregation, User.class, User.class).getMappedResults();

		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (User user : usersList) {
			userDtoList.add(this.getUserDto(user));
		}

		return userDtoList;
	}

	@Override
	public List<UserDto> searchUser(final UserSearchDto userSearchDto, final String lang) throws Exception {

		final List<Criteria> criteriaList = new ArrayList<>();

		if (userSearchDto.getEmail() != null && !userSearchDto.getEmail().isEmpty()) {
			criteriaList.add(Criteria.where("email").regex("(?i).*" + userSearchDto.getEmail() + ".*"));
		}
		if (userSearchDto.getName() != null && !userSearchDto.getName().isEmpty()) {

			AggregationOperation project = Aggregation.project(User.class)
					.andExpression("concat(firstName,' ', lastName)").as("fullName");
			AggregationOperation match = Aggregation
					.match(Criteria.where("fullName").regex("(?i).*" + userSearchDto.getName() + ".*"));
			Aggregation aggregation = Aggregation.newAggregation(project, match);
			List<User> usersList = mongoTemplate.aggregate(aggregation, User.class, User.class).getMappedResults();

			List<Long> userIds = new ArrayList<Long>();
			for (User user : usersList) {
				userIds.add(user.getId());
			}
			if (userIds.size() > 0) {
				criteriaList.add(Criteria.where("id").in(userIds));
			} else {
				return new ArrayList<UserDto>();
			}
		}
		if (userSearchDto.getBuId() != null && !userSearchDto.getBuId().equals(0L)) {
			criteriaList.add(Criteria.where("businessUnitId").is(userSearchDto.getBuId()));
		}
		if (userSearchDto.getRoleId() != null && !userSearchDto.getRoleId().equals(0L)) {
			criteriaList.add(Criteria.where("roleId").is(userSearchDto.getRoleId()));
		}
		if (userSearchDto.getSex() != null && !userSearchDto.getSex().isEmpty()) {
			criteriaList.add(Criteria.where("sex").is(userSearchDto.getSex()));
		}
		if (userSearchDto.getPanelMember() != null && !userSearchDto.getPanelMember().isEmpty()) {
			criteriaList.add(Criteria.where("panelMember").is(userSearchDto.getPanelMember()));
		}
		if (userSearchDto.getLocation() != null && !userSearchDto.getLocation().isEmpty()) {
			criteriaList.add(Criteria.where("location").regex("(?i).*" + userSearchDto.getLocation() + ".*"));
		}

		List<User> usersList = new ArrayList<User>();
		if (!criteriaList.isEmpty()) {
			Query searchQuery = new Query();
			searchQuery
					.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
			usersList = mongoTemplate.find(searchQuery, User.class);
		}

		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (User user : usersList) {
			userDtoList.add(this.getUserDto(user));
		}

		Comparator<UserDto> compareByUpdatedDateTime = Comparator.comparing(UserDto::getUpdatedDateTime);
		userDtoList.stream().sorted(compareByUpdatedDateTime).collect(Collectors.toList());

		return userDtoList;
	}

	/**
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private UserDto getUserDto(final User user) throws Exception {

		UserDto userDto = new UserDto();

		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setFullName(user.getFirstName() + " " + user.getLastName());
		userDto.setContactNumber(user.getContactNumber());

		userDto.setSex(user.getSex());
		userDto.setSkillSet(user.getSkillSet());
		userDto.setLocation(user.getLocation());
		userDto.setDesignation(user.getDesignation());
		userDto.setPanelMember(user.getPanelMember());
		userDto.setColorCode(user.getColorCode());
		userDto.setManagerId(user.getManagerId());
		userDto.setManagerName(CacheUtil.getUsersMap().get(user.getManagerId()));
		userDto.setActive(user.getActive());

		userDto.setBusinessUnitId(user.getBusinessUnitId());
		userDto.setBusinessUnitName(CacheUtil.getBusMap().get(user.getBusinessUnitId()));

		userDto.setRoleId(user.getRoleId());
		userDto.setRoleName(CacheUtil.getRolesMap().get(user.getRoleId()));

		userDto.setImage(imageService.getImage(HireProUsConstants.USER_IMAGE_PREFIX + user.getId()));

		userDto.setMainMenus("");
		userDto.setSubMenus("");
		Role role = roleService.getRoleById(user.getRoleId());
		if (role != null) {
			userDto.setMainMenus(roleMenuMappingService.getMainMenusForLogin(role));
			userDto.setSubMenus(roleMenuMappingService.getSubMenusForLogin(role));
		}

		userDto.setCreatedBy(user.getCreatedBy());
		userDto.setCreatedByName(CacheUtil.getUsersMap().get(user.getCreatedBy()));
		userDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(user.getCreatedDateTime()));

		userDto.setUpdatedBy(user.getUpdatedBy());
		userDto.setUpdatedByName(CacheUtil.getUsersMap().get(user.getUpdatedBy()));
		userDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(user.getUpdatedDateTime()));

		return userDto;
	}

	/**
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private UserDto getUserDtoLimitedInfo(final User user) throws Exception {

		UserDto userDto = new UserDto();

		userDto.setId(user.getId());
		userDto.setFullName(user.getFirstName() + " " + user.getLastName());

		return userDto;
	}

	@Override
	public UserDto deleteUserById(final String userId) throws Exception {

		UserDto resultDto = new UserDto();

		User user = userRepository.findById(Long.parseLong(userId));

		if (user == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "User Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		boolean hasDependency = false;
		if (jobRequestService.getUserDependencyCount(user.getId()) > 0) {
			hasDependency = true;
		}
		if (interviewScheduleService.getUserDependencyCount(user.getId()) > 0) {
			hasDependency = true;
		}

		if (hasDependency) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("cannot.delete",
					new String[] { "User is assigned to some task. So" }, null));
			return resultDto;
		}

		imageService.deleteImageByName(HireProUsConstants.USER_IMAGE_PREFIX + userId);
		userRepository.deleteById(Long.parseLong(userId));

		CacheUtil.getUsersMap().remove(user.getId());
		CacheUtil.getUsersEmailMap().remove(user.getId());

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public String getPanelUsersStringByBusinessUnitId(final String buId) throws Exception {

		List<User> usersList = userRepository.getUserByBusinessUnitId(Long.parseLong(buId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		StringBuilder panelList = new StringBuilder("");
		for (User user : usersList) {
			if (user.getPanelMember().equals(HireProUsConstants.PANEL_MEMBER_YES) && user.getActive().equals("1")) {

				if (panelList.length() > 0) {
					panelList.append(",");
				}
				panelList.append(user.getFirstName() + " " + user.getLastName());
			}
		}

		return panelList.toString();
	}

	@Override
	public List<String> getPanelUsersIdsByBuId(final String buId) throws Exception {

		List<User> usersList = userRepository.getUserByBusinessUnitId(Long.parseLong(buId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<String> panelUserIds = new ArrayList<String>();
		for (User user : usersList) {
			if (user.getPanelMember().equals(HireProUsConstants.PANEL_MEMBER_YES)) {
				panelUserIds.add(user.getId() + "");
			}
		}

		return panelUserIds;
	}

	@Override
	public List<UserDto> getUsersByBusinessUnitId(final String buId) throws Exception {

		List<User> usersList = userRepository.getUserByBusinessUnitId(Long.parseLong(buId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<Long> roleIds = new ArrayList<Long>();
		if (usersList != null && usersList.size() > 0) {
			roleIds = roleService.getRoleIdsByRoleNames(Arrays.asList(HireProUsConstants.ROLE_NAME_SALES_MANAGER));

			if (roleIds == null || roleIds.size() <= 0) {
				return new ArrayList<UserDto>();
			}
		}

		List<UserDto> userDtoList = new ArrayList<UserDto>();

		for (User user : usersList) {
			// Skip the Sales Manager.
			if (!roleIds.contains(user.getRoleId())) {
				UserDto userDto = new UserDto();

				userDto.setId(user.getId());
				userDto.setFullName(user.getFirstName() + " " + user.getLastName());
				userDto.setPanelMember(user.getPanelMember());

				userDtoList.add(this.getUserDto(user));
			}
		}

		return userDtoList;
	}

	@Override
	public List<UserDto> getPanelUsersByBusinessUnitId(final String buId) throws Exception {

		List<User> usersList = userRepository.getUserByBusinessUnitId(Long.parseLong(buId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<UserDto> userDtoList = new ArrayList<UserDto>();

		for (User user : usersList) {
			if (user.getPanelMember().equals(HireProUsConstants.PANEL_MEMBER_YES)) {

				UserDto userDto = new UserDto();

				userDto.setId(user.getId());
				userDto.setFullName(user.getFirstName() + " " + user.getLastName());
				userDto.setPanelMember(user.getPanelMember());

				userDtoList.add(this.getUserDto(user));
			}
		}

		return userDtoList;
	}

	@Override
	public UserDto updatePanelUsers(final String buId, final List<String> userIds, final String lang) throws Exception {

		List<User> usersList = userRepository.getUserByBusinessUnitId(Long.parseLong(buId),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		UserDto resultDto = new UserDto();
		if (usersList == null || usersList.isEmpty()) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "BusinessUnit Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		for (User user : usersList) {
			if (userIds.contains(user.getId() + "")) {
				user.setPanelMember(HireProUsConstants.PANEL_MEMBER_YES);
			} else {
				// needed while un-selecting the member from panel.
				user.setPanelMember(HireProUsConstants.PANEL_MEMBER_NO);
			}

			user.setUpdatedDateTime(LocalDateTime.now());
		}

		userRepository.saveAll(usersList);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public int getBUDependencyCount(Long buId) {

		return userRepository.countByBusinessUnitId(buId);
	}

	@Override
	public List<Long> searchUsersIdsByName(String interviewerName) throws Exception {
		AggregationOperation project = Aggregation.project(User.class)
				.andExpression("concat(firstName,' ', lastName)").as("fullName");
		AggregationOperation match = Aggregation
				.match(Criteria.where("fullName").regex("(?i).*" + interviewerName + ".*"));
		Aggregation aggregation = Aggregation.newAggregation(project, match);
		List<User> usersList = mongoTemplate.aggregate(aggregation, User.class, User.class)
				.getMappedResults();

		List<Long> userIds = new ArrayList<Long>();
		for (User user : usersList) {
			userIds.add(user.getId());
		}
;
		return userIds;
		
	}
}
