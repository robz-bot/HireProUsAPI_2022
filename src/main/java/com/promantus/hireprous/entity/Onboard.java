/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class for the MongoDB collection - onboard.
 * 
 * @author Sihab.
 *
 */
@Document(collection = "onboard")
public class Onboard {

	@Id
	private Long id;
	private String jrNumber;
	private Long candidateId;

	private Integer year;
	private Integer runningNumber;

	private String employeeId;
	private String employeeIdByHR;
	private String workOrderNumber;
	private LocalDate joiningDate;

	private Long projectId;

	private int docsVerified;
	private int joined;

	private String recStatus;

	private long createdBy;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private LocalDateTime updatedDateTime;

	private Long resourceId;
	private String email;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the jrNumber
	 */
	public String getJrNumber() {
		return jrNumber;
	}

	/**
	 * @param jrNumber the jrNumber to set
	 */
	public void setJrNumber(String jrNumber) {
		this.jrNumber = jrNumber;
	}

	/**
	 * @return the candidateId
	 */
	public Long getCandidateId() {
		return candidateId;
	}

	/**
	 * @param candidateId the candidateId to set
	 */
	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	/**
	 * @return the joiningDate
	 */
	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	/**
	 * @param joiningDate the joiningDate to set
	 */
	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	/**
	 * @return the docsVerified
	 */
	public int getDocsVerified() {
		return docsVerified;
	}

	/**
	 * @param docsVerified the docsVerified to set
	 */
	public void setDocsVerified(int docsVerified) {
		this.docsVerified = docsVerified;
	}

	/**
	 * @return the joined
	 */
	public int getJoined() {
		return joined;
	}

	/**
	 * @param joined the joined to set
	 */
	public void setJoined(int joined) {
		this.joined = joined;
	}

	/**
	 * @return the recStatus
	 */
	public String getRecStatus() {
		return recStatus;
	}

	/**
	 * @param recStatus the recStatus to set
	 */
	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}

	/**
	 * @return the createdBy
	 */
	public long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDateTime
	 */
	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	/**
	 * @return the updatedBy
	 */
	public long getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedDateTime
	 */
	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}

	/**
	 * @param updatedDateTime the updatedDateTime to set
	 */
	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the employeeIdByHR
	 */
	public String getEmployeeIdByHR() {
		return employeeIdByHR;
	}

	/**
	 * @param employeeIdByHR the employeeIdByHR to set
	 */
	public void setEmployeeIdByHR(String employeeIdByHR) {
		this.employeeIdByHR = employeeIdByHR;
	}

	/**
	 * @return the workOrderNumber
	 */
	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	/**
	 * @param workOrderNumber the workOrderNumber to set
	 */
	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	/**
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the runningNumber
	 */
	public Integer getRunningNumber() {
		return runningNumber;
	}

	/**
	 * @param runningNumber the runningNumber to set
	 */
	public void setRunningNumber(Integer runningNumber) {
		this.runningNumber = runningNumber;
	}

	/**
	 * @return the resourceId
	 */
	public Long getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
