package com.promantus.hireprous.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "evaluate_resume")
public class EvaluateResume {
	
	@Id
	private Long id;
	private String jdNum;
	private String jdYOE;
	private String jdSkillset;
	private String jdDesig;
	private String candiYOE;
	private String candiSkillset;
	private String candiDesig;
	private String candiName;
	private Long candiId;
	
	private long createdBy;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private LocalDateTime updatedDateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	public String getJdNum() {
		return jdNum;
	}
	public void setJdNum(String jdNum) {
		this.jdNum = jdNum;
	}
	public String getJdYOE() {
		return jdYOE;
	}
	public void setJdYOE(String jdYOE) {
		this.jdYOE = jdYOE;
	}
	public String getJdSkillset() {
		return jdSkillset;
	}
	public void setJdSkillset(String jdSkillset) {
		this.jdSkillset = jdSkillset;
	}
	public String getJdDesig() {
		return jdDesig;
	}
	public void setJdDesig(String jdDesig) {
		this.jdDesig = jdDesig;
	}
	public String getCandiYOE() {
		return candiYOE;
	}
	public void setCandiYOE(String candiYOE) {
		this.candiYOE = candiYOE;
	}
	public String getCandiSkillset() {
		return candiSkillset;
	}
	public void setCandiSkillset(String candiSkillset) {
		this.candiSkillset = candiSkillset;
	}
	public String getCandiDesig() {
		return candiDesig;
	}
	public void setCandiDesig(String candiDesig) {
		this.candiDesig = candiDesig;
	}
	public String getCandiName() {
		return candiName;
	}
	public void setCandiName(String candiName) {
		this.candiName = candiName;
	}
	public Long getCandiId() {
		return candiId;
	}
	public void setCandiId(Long candiId) {
		this.candiId = candiId;
	}
}
