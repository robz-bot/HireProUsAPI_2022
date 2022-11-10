/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.dto;

import java.io.Serializable;

import org.bson.types.Binary;

/**
 * Entity class for the MongoDB collection - resumes.
 * 
 * @author Sihab.
 *
 */
public class ResumeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String resumeName;
	private String resumeType;
	private String resume;
	
	private Binary resumeBinary;

	private int status;
	private String message;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the resumeName
	 */
	public String getResumeName() {
		return resumeName;
	}

	/**
	 * @param resumeName the resumeName to set
	 */
	public void setResumeName(String resumeName) {
		this.resumeName = resumeName;
	}

	/**
	 * @return the resume
	 */
	public String getResume() {
		return resume;
	}

	/**
	 * @param resume the resume to set
	 */
	public void setResume(String resume) {
		this.resume = resume;
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
	 * @return the resumeType
	 */
	public String getResumeType() {
		return resumeType;
	}

	/**
	 * @param resumeType the resumeType to set
	 */
	public void setResumeType(String resumeType) {
		this.resumeType = resumeType;
	}

	/**
	 * @return the resumeBinary
	 */
	public Binary getResumeBinary() {
		return resumeBinary;
	}

	/**
	 * @param resumeBinary the resumeBinary to set
	 */
	public void setResumeBinary(Binary resumeBinary) {
		this.resumeBinary = resumeBinary;
	}
}
