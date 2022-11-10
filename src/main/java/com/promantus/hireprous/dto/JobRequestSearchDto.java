/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity class for the MongoDB collection - job_requests.
 * 
 * @author Sihab.
 *
 */
public class JobRequestSearchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jrNumber;
	private Long customerId;
	private Long buId;
	private Long roleId;
	private Long requesterId;
	private Long recruiterId;
	private String placementFor;
	private String jobReqStatus;
	private String employmentType;
	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
	private Long vendorId;
	private String vendorPriority;

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
	 * @return the fromDateTime
	 */
	public LocalDateTime getFromDateTime() {
		return fromDateTime;
	}

	/**
	 * @param fromDateTime the fromDateTime to set
	 */
	public void setFromDateTime(LocalDateTime fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	/**
	 * @return the toDateTime
	 */
	public LocalDateTime getToDateTime() {
		return toDateTime;
	}

	/**
	 * @param toDateTime the toDateTime to set
	 */
	public void setToDateTime(LocalDateTime toDateTime) {
		this.toDateTime = toDateTime;
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
}
