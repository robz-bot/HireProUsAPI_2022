/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO class for the MongoDB collection - interview_schedule.
 * 
 * @author Sihab.
 *
 */
public class InterviewScheduleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String jrNumber;
	private Long candidateId;
	private String candidateName;
	private Long interviewerId;
	private String interviewerName;

	private LocalDateTime scheduleDateTime;
	private String timeZone;
	private String duration;
	private String scheduleRemarks;

	private String mode;
	private String venue;
	
	private int round;

	private String recStatus;
	private String resultRemarks;

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

	private String contactNumber;
	private String sex;
	private String experience;
	private String candidateType;

	private LocalDateTime fromDateTime;
	private LocalDateTime toDateTime;
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
	 * @return the interviewerId
	 */
	public Long getInterviewerId() {
		return interviewerId;
	}

	/**
	 * @param interviewerId the interviewerId to set
	 */
	public void setInterviewerId(Long interviewerId) {
		this.interviewerId = interviewerId;
	}

	/**
	 * @return the interviewerName
	 */
	public String getInterviewerName() {
		return interviewerName;
	}

	/**
	 * @param interviewerName the interviewerName to set
	 */
	public void setInterviewerName(String interviewerName) {
		this.interviewerName = interviewerName;
	}

	/**
	 * @return the scheduleDateTime
	 */
	public LocalDateTime getScheduleDateTime() {
		return scheduleDateTime;
	}

	/**
	 * @param scheduleDateTime the scheduleDateTime to set
	 */
	public void setScheduleDateTime(LocalDateTime scheduleDateTime) {
		this.scheduleDateTime = scheduleDateTime;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * @return the scheduleRemarks
	 */
	public String getScheduleRemarks() {
		return scheduleRemarks;
	}

	/**
	 * @param scheduleRemarks the scheduleRemarks to set
	 */
	public void setScheduleRemarks(String scheduleRemarks) {
		this.scheduleRemarks = scheduleRemarks;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the venue
	 */
	public String getVenue() {
		return venue;
	}

	/**
	 * @param venue the venue to set
	 */
	public void setVenue(String venue) {
		this.venue = venue;
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
	 * @return the resultRemarks
	 */
	public String getResultRemarks() {
		return resultRemarks;
	}

	/**
	 * @param resultRemarks the resultRemarks to set
	 */
	public void setResultRemarks(String resultRemarks) {
		this.resultRemarks = resultRemarks;
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
	 * @return the round
	 */
	public int getRound() {
		return round;
	}

	/**
	 * @param round the round to set
	 */
	public void setRound(int round) {
		this.round = round;
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
	 * @return the candidateType
	 */
	public String getCandidateType() {
		return candidateType;
	}

	/**
	 * @param candidateType the candidateType to set
	 */
	public void setCandidateType(String candidateType) {
		this.candidateType = candidateType;
	}

	public LocalDateTime getFromDateTime() {
		return fromDateTime;
	}

	public void setFromDateTime(LocalDateTime fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public LocalDateTime getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(LocalDateTime toDateTime) {
		this.toDateTime = toDateTime;
	}
}
