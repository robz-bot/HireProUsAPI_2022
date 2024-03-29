package com.promantus.hireprous.service.impl;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.HistoryDto;
import com.promantus.hireprous.dto.SearchDto;
import com.promantus.hireprous.dto.TimeSheetDto;
import com.promantus.hireprous.dto.UserDto;
import com.promantus.hireprous.entity.Project;
import com.promantus.hireprous.entity.TimeSheet;
import com.promantus.hireprous.entity.User;
import com.promantus.hireprous.repository.BusinessUnitRepository;
import com.promantus.hireprous.repository.ProjectRepository;
import com.promantus.hireprous.repository.TimeSheetRepository;
import com.promantus.hireprous.repository.UserRepository;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.TimeSheetService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

@Service
public class TimeSheetServiceImpl implements TimeSheetService {

	private static final Logger logger = LoggerFactory.getLogger(TimeSheetServiceImpl.class);

	@Autowired
	TimeSheetRepository timeSheetRepository;

	@Autowired
	CommonService commonService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	BusinessUnitRepository businessUnitRepository;

	@Override
	public TimeSheetDto addTimeSheet(TimeSheetDto timeSheetDto, String lang) throws Exception {
		TimeSheetDto resultDto = new TimeSheetDto();

		List<TimeSheet> timesheets = timeSheetRepository.findAllByUserIdAndDate(timeSheetDto.getUserId(),
				timeSheetDto.getDate());
		if (timesheets.size() >= 5) {
			resultDto.setMessage("Already 5 records added");
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			return resultDto;
		}

		TimeSheet timesheet = new TimeSheet();
		timesheet.setId(commonService.nextSequenceNumber());
		timesheet.setDate(timeSheetDto.getDate());
		timesheet.setProjectId(timeSheetDto.getProjectId());
		timesheet.setUserId(timeSheetDto.getUserId());
		timesheet.setManagerId(timeSheetDto.getManagerId());
		timesheet.setTask(timeSheetDto.getTask());
		timesheet.setStartTime(timeSheetDto.getStartTime());
		timesheet.setEndTime(timeSheetDto.getEndTime());
		timesheet.setCalHrs(timeSheetDto.getCalHrs());
		timesheet.setDescription(timeSheetDto.getDescription());
		timesheet.setSubmittedForApproval(false);
		timesheet.setTimesheetStatus(timeSheetDto.getTimesheetStatus());
		timesheet.setBillable(timeSheetDto.isBillable());

		timesheet.setCreatedBy(timeSheetDto.getCreatedBy());
		timesheet.setUpdatedBy(timeSheetDto.getUpdatedBy());
		timesheet.setCreatedDateTime(LocalDateTime.now());
		timesheet.setUpdatedDateTime(LocalDateTime.now());
		timesheet.setApprovedByManager("Pending");

		// Added hours field for dashboard @Sumesh-06-04-23
		int hours = Integer.parseInt(timeSheetDto.getCalHrs().split("h")[0]);
		int hoursMin = Integer.parseInt(timeSheetDto.getCalHrs().split("h")[1].split("min")[0].trim());

		Float val = Float.parseFloat(hours + "." + hoursMin);
		System.out.println("_____" + val);
		String formattedValue = String.format("%.2f", val);
		double roundedValue = (double) Math.round(Double.parseDouble(formattedValue) * 100) / 100;
		System.out.println(roundedValue);

		timesheet.setHours(roundedValue);

		timeSheetRepository.save(timesheet);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		resultDto.setMessage("Timesheet added");
		return resultDto;
	}

	@Override
	public TimeSheetDto updateTimeSheet(final TimeSheetDto timeSheetDto, final String lang) throws Exception {

		TimeSheetDto resultDto = new TimeSheetDto();

		TimeSheet timeSheet = timeSheetRepository.findById(timeSheetDto.getId());

		if (timeSheet == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "TimeSheet Id" }, lang));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		timeSheet.setDate(timeSheetDto.getDate());
		timeSheet.setProjectId(timeSheetDto.getProjectId());
		timeSheet.setUserId(timeSheetDto.getUserId());
//		timeSheet.setManagerId(timeSheetDto.getUserId());

		timeSheet.setTask(timeSheetDto.getTask());
		timeSheet.setDescription(timeSheetDto.getDescription());

		timeSheet.setStartTime(timeSheetDto.getStartTime());
		timeSheet.setEndTime(timeSheetDto.getEndTime());
		timeSheet.setCalHrs(timeSheetDto.getCalHrs());

		timeSheet.setSubmittedForApproval(timeSheetDto.isSubmittedForApproval());
		timeSheet.setTimesheetStatus(timeSheetDto.getTimesheetStatus());
		timeSheet.setSubmittedForApproval(false);
		timeSheet.setBillable(timeSheetDto.isBillable());

		timeSheet.setUpdatedBy(timeSheetDto.getUpdatedBy());
		timeSheet.setUpdatedDateTime(LocalDateTime.now());
		timeSheet.setApprovedByManager("Pending");

		timeSheetRepository.save(timeSheet);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		return resultDto;
	}

	@Override
	public List<TimeSheetDto> getAllTimeSheets() throws Exception {

		List<TimeSheetDto> timeSheetsDtoList = new ArrayList<TimeSheetDto>();

		List<TimeSheet> timeSheetsList = timeSheetRepository.findAll();
		for (TimeSheet timeSheet : timeSheetsList) {
			timeSheetsDtoList.add(this.getTimeSheetDto(timeSheet));
		}
		return timeSheetsDtoList;
	}

	private TimeSheetDto getTimeSheetDto(final TimeSheet timeSheet) throws Exception {

		TimeSheetDto timeSheetDto = new TimeSheetDto();
		timeSheetDto.setId(timeSheet.getId());
		timeSheetDto.setDate(timeSheet.getDate());
		timeSheetDto.setManagerId(timeSheet.getManagerId());
//		timeSheetDto.setProjectId(timeSheet.getProjectId());
		timeSheetDto.setProjectId(timeSheet.getProjectId());
		timeSheetDto.setProjectName(CacheUtil.getProjectsMap().get(timeSheet.getProjectId()));
		timeSheetDto.setUserId(timeSheet.getUserId());
		timeSheetDto.setUserName(CacheUtil.getUsersMap().get(timeSheet.getUserId()));
		timeSheetDto.setTask(timeSheet.getTask());
		timeSheetDto.setDescription(timeSheet.getDescription());
		timeSheetDto.setStartTime(timeSheet.getStartTime());
		timeSheetDto.setEndTime(timeSheet.getEndTime());
		timeSheetDto.setCalHrs(timeSheet.getCalHrs());
		timeSheetDto.setSubmittedForApproval(timeSheet.isSubmittedForApproval());
		timeSheetDto.setTimesheetStatus(timeSheet.getTimesheetStatus());
		timeSheetDto.setCreatedBy(timeSheet.getCreatedBy());
		timeSheetDto.setCreatedByName(CacheUtil.getUsersMap().get(timeSheet.getCreatedBy()));
		timeSheetDto.setCreatedDateTime(HireProUsUtil.getGMTDateTime(timeSheet.getCreatedDateTime()));
		timeSheetDto.setUpdatedBy(timeSheet.getUpdatedBy());
		timeSheetDto.setUpdatedByName(CacheUtil.getUsersMap().get(timeSheet.getUpdatedBy()));
		timeSheetDto.setUpdatedDateTime(HireProUsUtil.getGMTDateTime(timeSheet.getUpdatedDateTime()));
		timeSheetDto.setApprovedByManager(timeSheet.getApprovedByManager());
		timeSheetDto.setHours(timeSheet.getHours());
		timeSheetDto.setBillable(timeSheet.isBillable());
		timeSheetDto.setComments(timeSheet.getComments());

//		timeSheetDto.setDesignation(CacheUtil.getUsersMap().get(timeSheet.getUserId()));	
		User user = userRepository.findById(timeSheet.getUserId());
		timeSheetDto.setDesignation(user.getDesignation());

		return timeSheetDto;

	}

	@Override
	public TimeSheetDto submitTimeSheet(List<TimeSheetDto> timeSheetDtoList, String lang) throws Exception {

		TimeSheetDto resultDto = new TimeSheetDto();

		for (TimeSheetDto timeSheetDto : timeSheetDtoList) {

			TimeSheet timeSheet = timeSheetRepository.findById(timeSheetDto.getId());

			timeSheet.setSubmittedForApproval(true);
			timeSheet.setTimesheetStatus("Pending");

			timeSheet.setUpdatedBy(timeSheetDto.getUpdatedBy());
			timeSheet.setUpdatedDateTime(LocalDateTime.now());

			timeSheetRepository.save(timeSheet);
		}

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);
		resultDto.setMessage("TimeSheet Submitted Sucessfully");
		return resultDto;
	}

	@Override
	public TimeSheetDto getTimeSheetById(String timeSheetId) throws Exception {

		TimeSheet timeSheet = timeSheetRepository.findById(Long.parseLong(timeSheetId));

		return timeSheet != null ? this.getTimeSheetDto(timeSheet) : new TimeSheetDto();
	}

	@Override
	public TimeSheetDto deleteTimeSheetById(String timeSheetId) throws Exception {

		TimeSheetDto resultDto = new TimeSheetDto();

		TimeSheet timeSheet = timeSheetRepository.findById(Long.parseLong(timeSheetId));

		if (timeSheet == null) {
			resultDto.setStatus(HireProUsConstants.RETURN_STATUS_ERROR);
			resultDto.setMessage(commonService.getMessage("invalid", new String[] { "TimeSheet Id" }, null));

			logger.info(resultDto.getMessage());
			return resultDto;
		}

		timeSheetRepository.deleteById(Long.parseLong(timeSheetId));

		CacheUtil.getProjectsMap().remove(Long.parseLong(timeSheetId));
		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);

		return resultDto;
	}

	@Override
	public List<TimeSheetDto> getTimeSheetByUserIdAndApproval(String userId, boolean isApproval) throws Exception {

		List<TimeSheetDto> timeSheetsDtoList = new ArrayList<TimeSheetDto>();

		List<TimeSheet> list = timeSheetRepository.findAllByUserIdAndIsSubmittedForApproval(Long.parseLong(userId),
				isApproval);
		for (TimeSheet timeSheet : list) {
			timeSheetsDtoList.add(this.getTimeSheetDto(timeSheet));
		}
		return timeSheetsDtoList;
	}

	@Override
	public List<String> getAllEmployeeName() throws Exception {

		List<TimeSheet> employeeList = timeSheetRepository.findAll();

//		List<String> resultList = new ArrayList<>();
		Map<String, Long> resultList = new HashMap<>();

		for (TimeSheet timeSheet : employeeList) {
			resultList.put(
					this.getTimeSheetDto(timeSheet).getUserName() + " - "
							+ this.getTimeSheetDto(timeSheet).getDesignation(),
					this.getTimeSheetDto(timeSheet).getUserId());
		}

		List<Map.Entry<String, Long>> entryList = new ArrayList<>(resultList.entrySet());

		List<String> valueList = entryList.stream().map(entry -> entry.getKey() + ": " + entry.getValue())
				.collect(Collectors.toList());
		return valueList;
	}

	@Override
	public List<TimeSheetDto> getAllTimeSheetByManagerId(long managerId) throws Exception {

		List<TimeSheetDto> timeSheetDtoList = new ArrayList<>();

		List<TimeSheet> list = timeSheetRepository.findTimeSheetByManagerIdAndIsSubmittedForApproval(managerId, true);
//		List<TimeSheet> list = timeSheetRepository.findTimeSheetByManagerIdAndApprovedByManager(managerId, HireProUsConstants.PENDING);
		for (TimeSheet timeSheet : list) {
			timeSheetDtoList.add(this.getTimeSheetDto(timeSheet));
		}
		return timeSheetDtoList;
	}

	@Override
	public List<TimeSheetDto> searchTimeSheetByUserId(String userId) throws Exception {

		List<TimeSheetDto> timeSheetDtoList = new ArrayList<>();

		List<TimeSheet> timeSheetList = timeSheetRepository.findTimeSheetByUserId(Long.parseLong(userId));

		for (TimeSheet timeSheet : timeSheetList) {
			timeSheetDtoList.add(this.getTimeSheetDto(timeSheet));
		}
		return timeSheetDtoList;
	}

	@Override
	public TimeSheetDto approveTimeSheet(TimeSheetDto timeSheetDto, String lang) throws Exception {
		TimeSheetDto resultDto = new TimeSheetDto();

		TimeSheet timeSheet = timeSheetRepository.findById(timeSheetDto.getId());
//	    timeSheet.setTimesheetStatus(timeSheetDto.getTimesheetStatus());
		if (timeSheetDto.getApprovedByManager().equals(HireProUsConstants.APPROVED)) {

			timeSheet.setComments("Approved");
			resultDto.setMessage("TimeSheet Approved");
			timeSheet.setApprovedByManager(HireProUsConstants.APPROVED);
			timeSheet.setTimesheetStatus(HireProUsConstants.APPROVED);
			timeSheet.setUpdatedDateTime(LocalDateTime.now());
		} else if (timeSheetDto.getApprovedByManager().equals(HireProUsConstants.REJECT)) {
			timeSheet.setUpdatedDateTime(LocalDateTime.now());
			timeSheet.setComments(timeSheetDto.getComments());
			resultDto.setMessage("TimeSheet Declined");
			timeSheet.setApprovedByManager(HireProUsConstants.REJECT);
			timeSheet.setTimesheetStatus(HireProUsConstants.REJECT);
		}

		timeSheet.setUpdatedBy(timeSheetDto.getUpdatedBy());
		timeSheet.setUpdatedDateTime(LocalDateTime.now());
		timeSheetRepository.save(timeSheet);

		resultDto.setStatus(HireProUsConstants.RETURN_STATUS_OK);

		return resultDto;
	}

	@Override
	public List<TimeSheetDto> searchByDate(SearchDto searchDto, String lang) throws Exception {

		List<TimeSheetDto> timeSheetDtoList = new ArrayList<>();

		if (searchDto.getType().equals(HireProUsConstants.THISWEEK)) {
			// Get the current date
			LocalDate currentDate = LocalDate.now();

			// Calculate the start and end dates for the current week
			LocalDate startDate = currentDate.with(DayOfWeek.MONDAY);
//			LocalDate endDate = currentDate.with(DayOfWeek.SUNDAY);

			// Create an array to hold the dates for the current week
			int numDays = 7;
			LocalDate[] weekDates = new LocalDate[numDays];

			// Populate the array with the dates for the current week
			for (int i = 0; i < numDays; i++) {
				weekDates[i] = startDate.plusDays(i);
			}

			// Format the dates as strings in the format "yyyy-M-d"
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
			String[] weekDateStrings = new String[numDays];

			for (int i = 0; i < numDays; i++) {
				weekDateStrings[i] = weekDates[i].format(formatter);
			}
			List<TimeSheet> timeSheetList = new ArrayList<>();
			for (String date : weekDateStrings) {
				timeSheetList.addAll(timeSheetRepository.findAllByDate(date));
			}
			for (TimeSheet timesheet : timeSheetList) {
				timeSheetDtoList.add(this.getTimeSheetDto(timesheet));
			}
		} else if (searchDto.getType().equals(HireProUsConstants.TODAY)) {

			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
			String today = currentDate.format(formatter);

			List<TimeSheet> timeSheetList = timeSheetRepository.findAllByDate(today);

			for (TimeSheet timesheet : timeSheetList) {

				timeSheetDtoList.add(this.getTimeSheetDto(timesheet));
			}
		} else if (searchDto.getType().equals(HireProUsConstants.YESTERDAY)) {

			LocalDate yesterdayDate = LocalDate.now().minusDays(1);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
			String yesterday = yesterdayDate.format(formatter);

			List<TimeSheet> timeSheetList = timeSheetRepository.findAllByDate(yesterday);
			for (TimeSheet timeSheet : timeSheetList) {

				timeSheetDtoList.add(this.getTimeSheetDto(timeSheet));
			}
		} else if (searchDto.getType().equals(HireProUsConstants.LASTWEEK)) {

//			LocalDate currentDate = LocalDate.now();
//			LocalDate lastWeekStartDate = currentDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
//			LocalDate lastWeekEndDate = lastWeekStartDate.plusDays(6);
//			int numDays = 7;
//			LocalDate[] lastWeekDates = new LocalDate[numDays];
//			for (int i = 0; i < numDays; i++) {
//			    lastWeekDates[i] = lastWeekStartDate.plusDays(i);
//			}
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
//			String[] lastWeekDateStrings = new String[numDays];
//			for (int i = 0; i < numDays; i++) {
//			    lastWeekDateStrings[i] = lastWeekDates[i].format(formatter);
//			}
			LocalDate currentDate = LocalDate.now();
			LocalDate lastWeekStartDate = currentDate.minusWeeks(1).with(DayOfWeek.MONDAY);
			LocalDate lastWeekEndDate = currentDate.minusWeeks(1).with(DayOfWeek.SUNDAY);

			int numDays = 7;
			LocalDate[] lastWeekDates = new LocalDate[numDays];
			for (int i = 0; i < numDays; i++) {
				lastWeekDates[i] = lastWeekStartDate.plusDays(i);
			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
			String[] lastWeekDateStrings = new String[numDays];
			for (int i = 0; i < numDays; i++) {
				lastWeekDateStrings[i] = lastWeekDates[i].format(formatter);
			}

			List<TimeSheet> timeSheetList = new ArrayList<>();
			for (String date : lastWeekDateStrings) {
				timeSheetList.addAll(timeSheetRepository.findAllByDate(date));
			}
			for (TimeSheet timesheet : timeSheetList) {
				timeSheetDtoList.add(this.getTimeSheetDto(timesheet));
			}
		}
		return timeSheetDtoList;
	}

	@Override
	public Map<String, Double> getBillable(long userId) {
		List<TimeSheet> timeSheets = timeSheetRepository.findAll();
		Map<String, Double> billable = new HashMap<>();
		Map<String, Double> nonBillable = new HashMap<>();

		int month = LocalDateTime.now().getMonthValue();
		int year = LocalDateTime.now().getYear();

		for (TimeSheet timeSheet : timeSheets) {
			if (timeSheet.getUserId().equals(userId) && timeSheet.isBillable()
					&& timeSheet.getUpdatedDateTime().getMonthValue() == month
					&& timeSheet.getUpdatedDateTime().getYear() == year)
				billable.put("Billable", 0D);
		}

		for (String key : billable.keySet()) {
			Double totalHours = 0D;
			for (TimeSheet timeSheet : timeSheets) {
				if (timeSheet.getUserId().equals(userId) && timeSheet.isBillable()
						&& timeSheet.getUpdatedDateTime().getMonthValue() == month
						&& timeSheet.getUpdatedDateTime().getYear() == year) {

					Double hours = timeSheet.getHours();
					totalHours += hours;
					DecimalFormat df = new DecimalFormat("#.00");
					Double formattedValue = Double.parseDouble(df.format(totalHours));

					if (billable.containsKey(key)) {
						billable.put(key, formattedValue);
					}
				}
			}
		}

		for (TimeSheet timeSheet : timeSheets) {
			if (timeSheet.getUserId().equals(userId) && !timeSheet.isBillable()
					&& timeSheet.getUpdatedDateTime().getMonthValue() == month
					&& timeSheet.getUpdatedDateTime().getYear() == year)
				nonBillable.put("NonBillable", 0D);
		}

		for (String key : nonBillable.keySet()) {
			Double totalHours = 0D;
			for (TimeSheet timeSheet : timeSheets) {
				if (timeSheet.getUserId().equals(userId) && !timeSheet.isBillable()
						&& timeSheet.getUpdatedDateTime().getMonthValue() == month
						&& timeSheet.getUpdatedDateTime().getYear() == year) {

					Double hours = timeSheet.getHours();
					totalHours += hours;
					DecimalFormat df = new DecimalFormat("#.00");
					Double formattedValue = Double.parseDouble(df.format(totalHours));

					if (nonBillable.containsKey(key)) {
						billable.put(key, formattedValue);
					}
				}
			}
		}

		return billable;
	}

	@Override
	public Map<String, Object> getTaskAndHours(Long userId) throws Exception {

		List<TimeSheet> timeSheets = timeSheetRepository.findAll();

		Map<String, Double> taskAndHours = new HashMap<>();
		Map<String, Double> projectAndHours = new HashMap<>();

		int month = LocalDateTime.now().getMonthValue();
		int year = LocalDateTime.now().getYear();

		for (TimeSheet timeSheet : timeSheets) {
			if (timeSheet.getUserId().equals(userId) && timeSheet.getUpdatedDateTime().getMonthValue() == month
					&& timeSheet.getUpdatedDateTime().getYear() == year)
				taskAndHours.put(timeSheet.getTask(), 0D);
		}

		for (TimeSheet timeSheet : timeSheets) {
			if (timeSheet.getUserId().equals(userId) && timeSheet.getUpdatedDateTime().getMonthValue() == month
					&& timeSheet.getUpdatedDateTime().getYear() == year) {
				Project project = projectRepository.findById(timeSheet.getProjectId());
				projectAndHours.put(project.getProjectName(), 0D);
			}
		}

		for (String key : taskAndHours.keySet()) {
			Double totalHours = 0D;
			for (TimeSheet timeSheet : timeSheets) {
				if (key.equals(timeSheet.getTask()) && timeSheet.getUserId().equals(userId)
						&& timeSheet.getUpdatedDateTime().getMonthValue() == month
						&& timeSheet.getUpdatedDateTime().getYear() == year) {

					Double hours = timeSheet.getHours();
					totalHours += hours;
					DecimalFormat df = new DecimalFormat("#.00");
					Double formattedValue = Double.parseDouble(df.format(totalHours));

					if (taskAndHours.containsKey(key)) {
						taskAndHours.put(key, formattedValue);
					}
				}
			}
		}

		for (String key : projectAndHours.keySet()) {
			Double totalHours = 0D;
			for (TimeSheet timeSheet : timeSheets) {
				Project project = projectRepository.findById(timeSheet.getProjectId());
				if (key.equals(project.getProjectName()) && timeSheet.getUserId().equals(userId)
						&& timeSheet.getUpdatedDateTime().getMonthValue() == month
						&& timeSheet.getUpdatedDateTime().getYear() == year) {

					Double hours = timeSheet.getHours();
					totalHours += hours;
					DecimalFormat df = new DecimalFormat("#.00");
					Double formattedValue = Double.parseDouble(df.format(totalHours));

					if (projectAndHours.containsKey(key)) {
						projectAndHours.put(key, formattedValue);
					}
				}
			}
		}

		List<String> tasks = new ArrayList<>(taskAndHours.keySet());
		List<Double> thours = new ArrayList<>(taskAndHours.values());

		List<String> projects = new ArrayList<>(projectAndHours.keySet());
		List<Double> phours = new ArrayList<>(projectAndHours.values());

		Map<String, Object> response = new HashMap<>();

		response.put("tasks", tasks);
		response.put("thours", thours);
		response.put("projects", projects);
		response.put("phours", phours);

		return response;
	}

	@Override
	public List<String> getAllDateOfBirth() throws Exception {

//		int date = LocalDateTime.now().getDayOfMonth();
//		int month = LocalDateTime.now().getMonthValue();
		String date = String.format("%02d", LocalDateTime.now().getDayOfMonth());
		String month = String.format("%02d", LocalDateTime.now().getMonthValue());

		String dateAndMonth = month + "-" + date;

		List<String> dobNames = new ArrayList<>();

		List<User> users = userRepository.findAll();
		for (User user : users) {
			String userDob = user.getDateOfBirth().split("-")[1] + "-" + user.getDateOfBirth().split("-")[2];
			System.out.println(userDob);
			if (userDob.equals(dateAndMonth)) {
				String buName = businessUnitRepository.findById(user.getBusinessUnitId()).getBusinessUnitName();
				dobNames.add(user.getFirstName() + " " + user.getLastName() + " - " + buName);
			}

		}

		return dobNames;

	}

	@Override
	public List<String> getAllDateOfJoining() throws Exception {

		String date = String.format("%02d", LocalDateTime.now().getDayOfMonth());
		String month = String.format("%02d", LocalDateTime.now().getMonthValue());

		String dateAndMonth = month + "-" + date;

		List<String> dojNames = new ArrayList<>();

		List<User> users = userRepository.findAll();
		for (User user : users) {
			String userDoj = user.getDateOfJoining().split("-")[1] + "-" + user.getDateOfJoining().split("-")[2];
			if (userDoj.equals(dateAndMonth)) {
				String buName = businessUnitRepository.findById(user.getBusinessUnitId()).getBusinessUnitName();
				dojNames.add(user.getFirstName() + user.getLastName() + "-" + buName);
			}
		}
		return dojNames;
	}

	@Override
	public Map<String, List<TimeSheetDto>> getApprovedHistory() {

		List<TimeSheet> documents = timeSheetRepository.findAll();
		Map<String, List<TimeSheet>> groupedDocuments = documents.stream()
				.sorted(Comparator.comparing(TimeSheet::getUpdatedDateTime).thenComparing(TimeSheet::getUpdatedDateTime))
				.collect(Collectors.groupingBy(TimeSheet::getDate));
		Map<String, List<TimeSheetDto>> res = new HashMap<>();

		for (Map.Entry<String, List<TimeSheet>> entry : groupedDocuments.entrySet()) {
			List<TimeSheet> value = entry.getValue();
			String key = entry.getKey();
			List<TimeSheetDto> timeSheetDtoList = new ArrayList<>();
			for (TimeSheet timeSheet : value) {
				try {
					if (key.equals(timeSheet.getDate()) && timeSheet.getApprovedByManager().equals("Approved"))
						timeSheetDtoList.add(this.getTimeSheetDto(timeSheet));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			res.put(key, timeSheetDtoList);
		}

		return res;
	}

	@Override
	public Map<String, List<TimeSheetDto>> getRejectedHistory() {
		List<TimeSheet> documents = timeSheetRepository.findAll();
		Map<String, List<TimeSheet>> groupedDocuments = documents.stream()
				.sorted(Comparator.comparing(TimeSheet::getUpdatedDateTime).thenComparing(TimeSheet::getUpdatedDateTime))
				.collect(Collectors.groupingBy(TimeSheet::getDate));
		Map<String, List<TimeSheetDto>> res = new HashMap<>();

		for (Map.Entry<String, List<TimeSheet>> entry : groupedDocuments.entrySet()) {
			List<TimeSheet> value = entry.getValue();
			String key = entry.getKey();
			List<TimeSheetDto> timeSheetDtoList = new ArrayList<>();
			for (TimeSheet timeSheet : value) {
				try {
					if (key.equals(timeSheet.getDate()) && timeSheet.getApprovedByManager().equals("Rejected"))
						timeSheetDtoList.add(this.getTimeSheetDto(timeSheet));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			res.put(key, timeSheetDtoList);

		}

		return res;
	}

}
