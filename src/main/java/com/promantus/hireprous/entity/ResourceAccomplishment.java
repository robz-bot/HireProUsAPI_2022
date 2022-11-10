package com.promantus.hireprous.entity;

import java.time.LocalDateTime;
//import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "resource_entry")
public class ResourceAccomplishment {
	@Id
	private Long id;
	private Long buId;

	private String resourceName;
	private String employeeId;
	private String achievements;
	private String status;
	private String rating;
	private String ratingByname;
	private String ratingbyId;
	private LocalDateTime reviewedOn;
	private long refId;
    private String comments;
	private long createdBy;
	private LocalDateTime createdDateTime;
	private long updatedBy;
	private LocalDateTime updatedDateTime;
	private Long roleId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

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

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
