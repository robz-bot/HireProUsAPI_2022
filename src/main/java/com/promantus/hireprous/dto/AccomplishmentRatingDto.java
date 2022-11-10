package com.promantus.hireprous.dto;

import java.time.LocalDateTime;

public class AccomplishmentRatingDto {


	private Long id;
	private Long buId;
	private String buName;
	private String resourceName;
	private String employeeId;
	private String achievements;
	private String status ;
	private String rating;
	private String ratingByname;
	private String ratingbyId;
	private LocalDateTime reviewedOn;
	private long refId;
		
	private long createdBy;
	private String createdByName;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private String updatedByName;
	private LocalDateTime updatedDateTime;
	
	private Long roleId;
	private String roleName;
	private int year; 
	
	private String comments;
	

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	private int resultStatus;
	private String message;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getAchievements() {
		return achievements;
	}
	public void setAchievements(String achievements) {
		this.achievements = achievements;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getRatingByname() {
		return ratingByname;
	}
	public void setRatingByname(String ratingByname) {
		this.ratingByname = ratingByname;
	}
	public String getRatingbyId() {
		return ratingbyId;
	}
	public void setRatingbyId(String ratingbyId) {
		this.ratingbyId = ratingbyId;
	}
	public long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedByName() {
		return createdByName;
	}
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
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
	public String getUpdatedByName() {
		return updatedByName;
	}
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	public int getResultStatus() {
		return resultStatus;
	}
	public void setResultStatus(int resultStatus) {
		this.resultStatus = resultStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getBuName() {
		return buName;
	}
	public void setBuName(String buName) {
		this.buName = buName;
	}
	public void setMessage(String string, String[] strings, String lang) {
		// TODO Auto-generated method stub
		
	}
	public LocalDateTime getReviewedOn() {
		return reviewedOn;
	}
	public void setReviewedOn(LocalDateTime reviewedOn) {
		this.reviewedOn = reviewedOn;
	}
	public long getRefId() {
		return refId;
	}
	public void setRefId(long refId) {
		this.refId = refId;
	}
}
