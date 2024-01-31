/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class for the MongoDB collection - projects.
 * 
 * @author Sangeetha.
 *
 */
@Document(collection = "time_sheet")

public class TimeSheet {

	@Id
	private Long id;
	private String date;
	private Long projectId;
	private Long userId;
	private Long managerId;
	private String task;
	private String startTime;
	private String endTime;
	private String calHrs;
	private String description;
	private boolean isSubmittedForApproval;
	private boolean billable;
	private String timesheetStatus;
	private String comments;
	
	private Long createdBy;
	private LocalDateTime createdDateTime;
	private Long updatedBy;
	private LocalDateTime updatedDateTime;
	
	private Long buId;
	private String approvedByManager;
	
	//added for dashboard charts @sumesh-05/04/23
	private Double hours;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCalHrs() {
		return calHrs;
	}
	public void setCalHrs(String calHrs) {
		this.calHrs = calHrs;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isSubmittedForApproval() {
		return isSubmittedForApproval;
	}
	public void setSubmittedForApproval(boolean isSubmittedForApproval) {
		this.isSubmittedForApproval = isSubmittedForApproval;
	}
	public String getTimesheetStatus() {
		return timesheetStatus;
	}
	public void setTimesheetStatus(String timesheetStatus) {
		this.timesheetStatus = timesheetStatus;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getApprovedByManager() {
		return approvedByManager;
	}
	public void setApprovedByManager(String approvedByManager) {
		this.approvedByManager = approvedByManager;
	}
	public Double getHours() {
		return hours;
	}
	public void setHours(Double hours) {
		this.hours = hours;
	}
	public boolean isBillable() {
		return billable;
	}
	public void setBillable(boolean billable) {
		this.billable = billable;
	}
	
		
}
