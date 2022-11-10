/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Entity class for the MongoDB collection - job_requests.
 * 
 * @author Sihab.
 *
 */
public class InterviewScheduleSearchDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String jrNumber;
	private List<Long> interviewerIdList;
	private int round;
	private String candidateName;

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
	 * @return the interviewerIdList
	 */
	public List<Long> getInterviewerIdList() {
		return interviewerIdList;
	}

	/**
	 * @param interviewerIdList the interviewerIdList to set
	 */
	public void setInterviewerIdList(List<Long> interviewerIdList) {
		this.interviewerIdList = interviewerIdList;
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
}
