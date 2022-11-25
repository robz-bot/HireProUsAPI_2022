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
public class JobRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String referenceNumber;
	private Integer year;
	private Integer runningNumber;
	private Long customerId;
	private Long buId;
	private Long roleId;
	private int noOfOpenings;
	private int closedOpenings;
	private int progress;
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
	private String recruiterName;

	private long createdBy;
	private String createdByName;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private String updatedByName;
	private LocalDateTime updatedDateTime;

	private String customerName;
	private String customerNameInDetail;
	private String buName;
	private String roleName;
	private String requesterName;

	private int status;
	private String message;

	private long daysTotal;
	private long daysSpent;
	private long daysLeft;

	private Long vendorId;
	private String vendorPriority;
	private String vendorActionStatus;

	// JobRequestReportCount Code Starts here
	private int totalJobRequest;
	private int openJobRequest;
	private int progressJobRequest;
	private int holdJobRequest;
	private int closedJobRequest;
	// JobRequestReportCount Code Ends here

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
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
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
	 * @return the requesterName
	 */
	public String getRequesterName() {
		return requesterName;
	}

	/**
	 * @param requesterName the requesterName to set
	 */
	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
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
	 * @return the totalJobRequest
	 */
	public int getTotalJobRequest() {
		return totalJobRequest;
	}

	/**
	 * @param totalJobRequest the totalJobRequest to set
	 */
	public void setTotalJobRequest(int totalJobRequest) {
		this.totalJobRequest = totalJobRequest;
	}

	/**
	 * @return the openJobRequest
	 */
	public int getOpenJobRequest() {
		return openJobRequest;
	}

	/**
	 * @param openJobRequest the openJobRequest to set
	 */
	public void setOpenJobRequest(int openJobRequest) {
		this.openJobRequest = openJobRequest;
	}

	/**
	 * @return the progressJobRequest
	 */
	public int getProgressJobRequest() {
		return progressJobRequest;
	}

	/**
	 * @param progressJobRequest the progressJobRequest to set
	 */
	public void setProgressJobRequest(int progressJobRequest) {
		this.progressJobRequest = progressJobRequest;
	}

	/**
	 * @return the holdJobRequest
	 */
	public int getHoldJobRequest() {
		return holdJobRequest;
	}

	/**
	 * @param holdJobRequest the holdJobRequest to set
	 */
	public void setHoldJobRequest(int holdJobRequest) {
		this.holdJobRequest = holdJobRequest;
	}

	/**
	 * @return the closedJobRequest
	 */
	public int getClosedJobRequest() {
		return closedJobRequest;
	}

	/**
	 * @param closedJobRequest the closedJobRequest to set
	 */
	public void setClosedJobRequest(int closedJobRequest) {
		this.closedJobRequest = closedJobRequest;
	}

	/**
	 * @return the daysSpent
	 */
	public long getDaysSpent() {
		return daysSpent;
	}

	/**
	 * @param daysSpent the daysSpent to set
	 */
	public void setDaysSpent(long daysSpent) {
		this.daysSpent = daysSpent;
	}

	/**
	 * @return the daysLeft
	 */
	public long getDaysLeft() {
		return daysLeft;
	}

	/**
	 * @param daysLeft the daysLeft to set
	 */
	public void setDaysLeft(long daysLeft) {
		this.daysLeft = daysLeft;
	}

	/**
	 * @return the daysTotal
	 */
	public long getDaysTotal() {
		return daysTotal;
	}

	/**
	 * @param daysTotal the daysTotal to set
	 */
	public void setDaysTotal(long daysTotal) {
		this.daysTotal = daysTotal;
	}

	/**
	 * @return the customerNameInDetail
	 */
	public String getCustomerNameInDetail() {
		return customerNameInDetail;
	}

	/**
	 * @param customerNameInDetail the customerNameInDetail to set
	 */
	public void setCustomerNameInDetail(String customerNameInDetail) {
		this.customerNameInDetail = customerNameInDetail;
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

	public String getMinYearOfExp() {
		return minYearOfExp;
	}

	public void setMinYearOfExp(String minYearOfExp) {
		this.minYearOfExp = minYearOfExp;
	}

	
	
}
