/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Entity class for the MongoDB collection - interview panel.
 * 
 * @author Sihab.
 *
 */
public class InterviewPanelDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long businessUnitId;
	private String businessUnitName;
	private String interviewPanelList;
	private List<String> panelIdList;

	private int status;
	private String message;

	/**
	 * @return the businessUnitId
	 */
	public long getBusinessUnitId() {
		return businessUnitId;
	}

	/**
	 * @param businessUnitId the businessUnitId to set
	 */
	public void setBusinessUnitId(long businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	/**
	 * @return the businessUnitName
	 */
	public String getBusinessUnitName() {
		return businessUnitName;
	}

	/**
	 * @param businessUnitName the businessUnitName to set
	 */
	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	/**
	 * @return
	 */
	public String getInterviewPanelList() {
		return interviewPanelList;
	}

	/**
	 * @param interviewPanelList
	 */
	public void setInterviewPanelList(String interviewPanelList) {
		this.interviewPanelList = interviewPanelList;
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
	 * @return the panelIdList
	 */
	public List<String> getPanelIdList() {
		return panelIdList;
	}

	/**
	 * @param panelIdList the panelIdList to set
	 */
	public void setPanelIdList(List<String> panelIdList) {
		this.panelIdList = panelIdList;
	}
}
