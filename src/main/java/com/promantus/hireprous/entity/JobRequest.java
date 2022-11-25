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
 * Entity class for the MongoDB collection - job_requests.
 * 
 * @author Sihab.
 *
 */
@Document(collection = "job_requests")
public class JobRequest {

	@Id
	private Long id;
	private String referenceNumber;
	private Integer year;
	private Integer runningNumber;
	private Integer nextCounter;
	private Long customerId;
	private Long buId;
	private Long roleId;
	private int noOfOpenings;
	private int closedOpenings;
	private String location;
	private String payRange;
	private String payFrequency;
	private String currency;
	private String employmentType;
	private String contractDuration;
	private String monthOrYear;
	private String minYearOfExp;

	private Long requesterId;

	private String placementFor;
	private LocalDate projectStartDate;
	private String remoteOption;
	private String jobDescription;
	private String mandatorySkills;
	private String optionalSkills;

	private String jobReqStatus;

	private Long recruiterId;

	private long createdBy;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private LocalDateTime updatedDateTime;

	private Long vendorId;
	
	private String vendorPriority;
	private String vendorActionStatus;

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
	 * @return the referenceNumber
	 */
	public String getReferenceNumber() {
		return referenceNumber;
	}

	/**
	 * @param referenceNumber the referenceNumber to set
	 */
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	/**
	 * @return the customerId
	 */
	public Long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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
	 * @return the closedOpenings
	 */
	public int getClosedOpenings() {
		return closedOpenings;
	}

	/**
	 * @param closedOpenings the closedOpenings to set
	 */
	public void setClosedOpenings(int closedOpenings) {
		this.closedOpenings = closedOpenings;
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
	 * @return the payRange
	 */
	public String getPayRange() {
		return payRange;
	}

	/**
	 * @param payRange the payRange to set
	 */
	public void setPayRange(String payRange) {
		this.payRange = payRange;
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
	 * @return the contractDuration
	 */
	public String getContractDuration() {
		return contractDuration;
	}

	/**
	 * @param contractDuration the contractDuration to set
	 */
	public void setContractDuration(String contractDuration) {
		this.contractDuration = contractDuration;
	}

	/**
	 * @return the requesterId
	 */
	public Long getRequesterId() {
		return requesterId;
	}

	/**
	 * @param requesterId the requesterId to set
	 */
	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
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
	 * @return the projectStartDate
	 */
	public LocalDate getProjectStartDate() {
		return projectStartDate;
	}

	/**
	 * @param projectStartDate the projectStartDate to set
	 */
	public void setProjectStartDate(LocalDate projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	/**
	 * @return the remoteOption
	 */
	public String getRemoteOption() {
		return remoteOption;
	}

	/**
	 * @param remoteOption the remoteOption to set
	 */
	public void setRemoteOption(String remoteOption) {
		this.remoteOption = remoteOption;
	}

	/**
	 * @return the jobDescription
	 */
	public String getJobDescription() {
		return jobDescription;
	}

	/**
	 * @param jobDescription the jobDescription to set
	 */
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	/**
	 * @return the mandatorySkills
	 */
	public String getMandatorySkills() {
		return mandatorySkills;
	}

	/**
	 * @param mandatorySkills the mandatorySkills to set
	 */
	public void setMandatorySkills(String mandatorySkills) {
		this.mandatorySkills = mandatorySkills;
	}

	/**
	 * @return the optionalSkills
	 */
	public String getOptionalSkills() {
		return optionalSkills;
	}

	/**
	 * @param optionalSkills the optionalSkills to set
	 */
	public void setOptionalSkills(String optionalSkills) {
		this.optionalSkills = optionalSkills;
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
	 * @return the jobReqStatus
	 */
	public String getJobReqStatus() {
		return jobReqStatus;
	}

	/**
	 * @param jobReqStatus the jobReqStatus to set
	 */
	public void setJobReqStatus(String jobReqStatus) {
		this.jobReqStatus = jobReqStatus;
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
	 * @return the nextCounter
	 */
	public Integer getNextCounter() {
		return nextCounter;
	}

	/**
	 * @param nextCounter the nextCounter to set
	 */
	public void setNextCounter(Integer nextCounter) {
		this.nextCounter = nextCounter;
	}

	/**
	 * @return the payFrequency
	 */
	public String getPayFrequency() {
		return payFrequency;
	}

	/**
	 * @param payFrequency the payFrequency to set
	 */
	public void setPayFrequency(String payFrequency) {
		this.payFrequency = payFrequency;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the monthOrYear
	 */
	public String getMonthOrYear() {
		return monthOrYear;
	}

	/**
	 * @param monthOrYear the monthOrYear to set
	 */
	public void setMonthOrYear(String monthOrYear) {
		this.monthOrYear = monthOrYear;
	}

	/**
	 * @return the vendorId
	 */
	public Long getVendorId() {
		return vendorId;
	}

	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * @return the vendorActionStatus
	 */
	public String getVendorActionStatus() {
		return vendorActionStatus;
	}

	/**
	 * @param vendorActionStatus the vendorActionStatus to set
	 */
	public void setVendorActionStatus(String vendorActionStatus) {
		this.vendorActionStatus = vendorActionStatus;
	}

	/**
	 * @return the vendorPriority
	 */
	public String getVendorPriority() {
		return vendorPriority;
	}

	/**
	 * @param vendorPriority the vendorPriority to set
	 */
	public void setVendorPriority(String vendorPriority) {
		this.vendorPriority = vendorPriority;
	}

	public String getMinYearOfExp() {
		return minYearOfExp;
	}

	public void setMinYearOfExp(String minYearOfExp) {
		this.minYearOfExp = minYearOfExp;
	}

	
	
}
