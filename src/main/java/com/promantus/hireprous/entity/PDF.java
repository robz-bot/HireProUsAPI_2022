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
@Document(collection = "botminds_pdf")
public class PDF {

	@Id
	private String id;
	private String pdfName;
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
	public String getPdfName() {
		return pdfName;
	}

	/**
	 * @param resumeName the resumeName to set
	 */
	public void setPdfName(String resumeName) {
		this.pdfName = resumeName;
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
