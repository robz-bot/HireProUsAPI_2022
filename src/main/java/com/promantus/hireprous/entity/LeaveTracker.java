package com.promantus.hireprous.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "leaveTracker")
public class LeaveTracker implements Serializable{
	
private static final long serialVersionUID = 1L;	 

		@Id
		private long id;
		private long userId;
		private String leaveDate;
		private String fromDate;
		private String toDate;
		private String leaveType;
		private String reason;
		private long approvedBy;
		private String month;
		private String year;
		private String createdBy;
		private LocalDateTime createdOn;
		private String updatedBy;
		private LocalDateTime updatedOn;
		
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		public String getFromDate() {
			return fromDate;
		}
		public void setFromDate(String fromDate) {
			this.fromDate = fromDate;
		}
		public String getToDate() {
			return toDate;
		}
		public void setToDate(String toDate) {
			this.toDate = toDate;
		}
		public String getLeaveType() {
			return leaveType;
		}
		public void setLeaveType(String leaveType) {
			this.leaveType = leaveType;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public long getApprovedBy() {
			return approvedBy;
		}
		public void setApprovedBy(long approvedBy) {
			this.approvedBy = approvedBy;
		}
		public String getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}
		public LocalDateTime getCreatedOn() {
			return createdOn;
		}
		public void setCreatedOn(LocalDateTime createdOn) {
			this.createdOn = createdOn;
		}
		public String getUpdatedBy() {
			return updatedBy;
		}
		public void setUpdatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
		}
		public LocalDateTime getUpdatedOn() {
			return updatedOn;
		}
		public void setUpdatedOn(LocalDateTime updatedOn) {
			this.updatedOn = updatedOn;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		public String getMonth() {
			return month;
		}
		public void setMonth(String month) {
			this.month = month;
		}
		public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
		public String getLeaveDate() {
			return leaveDate;
		}
		public void setLeaveDate(String leaveDate) {
			this.leaveDate = leaveDate;
		}
		
	
}
