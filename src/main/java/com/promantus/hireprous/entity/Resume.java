/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.entity;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class for the MongoDB collection - resumes.
 * 
 * @author Sihab.
 *
 */
@Document(collection = "resumes")
public class Resume {

	@Id
	private String id;
	private String resumeName;
	private String resumeType;
	private Binary resume;

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
	public Binary getResume() {
		return resume;
	}

	/**
	 * @param resume the resume to set
	 */
	public void setResume(Binary resume) {
		this.resume = resume;
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
}
