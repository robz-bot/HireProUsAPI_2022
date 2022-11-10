/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;

/**
 * Dto class for the MongoDB collection - Candidates Count .
 * 
 * @author Sihab.
 *
 */
public class CandidatesCountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long totalTagged;
	private Long uploaded;
	private Long shortlisted;
	private Long hold;
	private Long rejected;
	private Long scheduled;
	private Long selected;
	private Long onboarded;
	private Long dropped;

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

	/**
	 * @return the dropped
	 */
	public Long getDropped() {
		return dropped;
	}

	/**
	 * @param dropped the dropped to set
	 */
	public void setDropped(Long dropped) {
		this.dropped = dropped;
	}
}
