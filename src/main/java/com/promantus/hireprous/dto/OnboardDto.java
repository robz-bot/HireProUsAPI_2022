/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity class for the MongoDB collection - job_requests.
 * 
 * @author Sihab.
 *
 */
public class OnboardDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String jrNumber;
	private Long candidateId;
	private String candidateName;

	private String employeeId;
	private String employeeIdByHR;
	private String workOrderNumber;
	private LocalDate joiningDate;

	private Long projectId;
	private String projectName;

	private int docsVerified;
	private int joined;

	private String recStatus;

	private Long recruiterId;
	private String recruiterName;

	private long createdBy;
	private String createdByName;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private String updatedByName;
	private LocalDateTime updatedDateTime;

	private int status;
	private String message;

	private Long jrId;
	private String customerName;
	private Long buId;
	private String buName;
	private String roleName;
	private int noOfOpenings;
	private String location;
	private String employmentType;
	private String placementFor;

	private String email;
	private String contactNumber;
	private String sex;
	private String experience;

	private Long resourceId;

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
	 * @return the candidateName
	 */
	public String getCandidateName() {
		return candidateName;
	}

	/**
	 * @param candidateName the candidateName to set
	 */
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
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
	 * @return the recruiterId
	 */
	public Long getRecruiterId() {
		return recruiterId;
	}

	/**
	 * @param recruiterId the recruiterId to set
	 */
	public void setRecruiterId(Long recruiterId) {
		this.recruiterId = recruiterId;
	}

	/**
	 * @return the recruiterName
	 */
	public String getRecruiterName() {
		return recruiterName;
	}

	/**
	 * @param recruiterName the recruiterName to set
	 */
	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
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
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}

	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
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
	 * @return the updatedByName
	 */
	public String getUpdatedByName() {
		return updatedByName;
	}

	/**
	 * @param updatedByName the updatedByName to set
	 */
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the jrId
	 */
	public Long getJrId() {
		return jrId;
	}

	/**
	 * @param jrId the jrId to set
	 */
	public void setJrId(Long jrId) {
		this.jrId = jrId;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the buId
	 */
	public Long getBuId() {
		return buId;
	}

	/**
	 * @param buId the buId to set
	 */
	public void setBuId(Long buId) {
		this.buId = buId;
	}

	/**
	 * @return the buName
	 */
	public String getBuName() {
		return buName;
	}

	/**
	 * @param buName the buName to set
	 */
	public void setBuName(String buName) {
		this.buName = buName;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the noOfOpenings
	 */
	public int getNoOfOpenings() {
		return noOfOpenings;
	}

	/**
	 * @param noOfOpenings the noOfOpenings to set
	 */
	public void setNoOfOpenings(int noOfOpenings) {
		this.noOfOpenings = noOfOpenings;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the employmentType
	 */
	public String getEmploymentType() {
		return employmentType;
	}

	/**
	 * @param employmentType the employmentType to set
	 */
	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	/**
	 * @return the placementFor
	 */
	public String getPlacementFor() {
		return placementFor;
	}

	/**
	 * @param placementFor the placementFor to set
	 */
	public void setPlacementFor(String placementFor) {
		this.placementFor = placementFor;
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

	/**
	 * @return the contactNumber
	 */
	public String getContactNumber() {
		return contactNumber;
	}

	/**
	 * @param contactNumber the contactNumber to set
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the experience
	 */
	public String getExperience() {
		return experience;
	}

	/**
	 * @param experience the experience to set
	 */
	public void setExperience(String experience) {
		this.experience = experience;
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
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
}
