/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.entity;

import java.time.LocalDateTime;
//import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class for the MongoDB collection - interview_schedule.
 * 
 * @author Sihab.
 *
 */
@Document(collection = "interview_schedule")
public class InterviewSchedule {

	@Id
	private Long id;
	private String jrNumber;
	private Long candidateId;
	private Long interviewerId;

	private LocalDateTime scheduleDateTime;
	private String timeZone;
	private String duration;
	private String scheduleRemarks;

	private String mode;
	private String venue;

	private int round;
	private int completed;

	private String recStatus;
	private String resultRemarks;

	private long createdBy;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private LocalDateTime updatedDateTime;

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
	 * @return the completed
	 */
	public int getCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(int completed) {
		this.completed = completed;
	}
}
