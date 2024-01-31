package com.promantus.hireprous.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "holiday")
public class Holiday implements Serializable{
	
private static final long serialVersionUID = 1L;	

@Id
private long id;
private String holidayDate;
private String holidayName;
private String Branch;
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
public String getHolidayDate() {
	return holidayDate;
}
public void setHolidayDate(String holidayDate) {
	this.holidayDate = holidayDate;
}
public String getHolidayName() {
	return holidayName;
}
public void setHolidayName(String holidayName) {
	this.holidayName = holidayName;
}
public String getBranch() {
	return Branch;
}
public void setBranch(String branch) {
	Branch = branch;
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



}
