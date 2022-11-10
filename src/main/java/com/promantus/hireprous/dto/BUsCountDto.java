/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;

/**
 * Entity class for the MongoDB collection - businessUnits.
 * 
 * @author Sihab.
 *
 */
public class BUsCountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long buId;
	private String buName;
	private Long count;

	private Long open;
	private Long inprogress;

	private Long totalTagged;
	private Long uploaded;
	private Long shortlisted;
	private Long hold;
	private Long rejected;
	private Long scheduled;
	private Long selected;
	private Long onboarded;

	private int status;
	private String message;

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
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
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
	 * @return the open
	 */
	public Long getOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(Long open) {
		this.open = open;
	}

	/**
	 * @return the inprogress
	 */
	public Long getInprogress() {
		return inprogress;
	}

	/**
	 * @param inprogress the inprogress to set
	 */
	public void setInprogress(Long inprogress) {
		this.inprogress = inprogress;
	}

	/**
	 * @return the totalTagged
	 */
	public Long getTotalTagged() {
		return totalTagged;
	}

	/**
	 * @param totalTagged the totalTagged to set
	 */
	public void setTotalTagged(Long totalTagged) {
		this.totalTagged = totalTagged;
	}

	/**
	 * @return the uploaded
	 */
	public Long getUploaded() {
		return uploaded;
	}

	/**
	 * @param uploaded the uploaded to set
	 */
	public void setUploaded(Long uploaded) {
		this.uploaded = uploaded;
	}

	/**
	 * @return the shortlisted
	 */
	public Long getShortlisted() {
		return shortlisted;
	}

	/**
	 * @param shortlisted the shortlisted to set
	 */
	public void setShortlisted(Long shortlisted) {
		this.shortlisted = shortlisted;
	}

	/**
	 * @return the hold
	 */
	public Long getHold() {
		return hold;
	}

	/**
	 * @param hold the hold to set
	 */
	public void setHold(Long hold) {
		this.hold = hold;
	}

	/**
	 * @return the rejected
	 */
	public Long getRejected() {
		return rejected;
	}

	/**
	 * @param rejected the rejected to set
	 */
	public void setRejected(Long rejected) {
		this.rejected = rejected;
	}

	/**
	 * @return the scheduled
	 */
	public Long getScheduled() {
		return scheduled;
	}

	/**
	 * @param scheduled the scheduled to set
	 */
	public void setScheduled(Long scheduled) {
		this.scheduled = scheduled;
	}

	/**
	 * @return the selected
	 */
	public Long getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(Long selected) {
		this.selected = selected;
	}

	/**
	 * @return the onboarded
	 */
	public Long getOnboarded() {
		return onboarded;
	}

	/**
	 * @param onboarded the onboarded to set
	 */
	public void setOnboarded(Long onboarded) {
		this.onboarded = onboarded;
	}
}
