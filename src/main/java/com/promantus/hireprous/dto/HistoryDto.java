package com.promantus.hireprous.dto;

import com.promantus.hireprous.entity.TimeSheet;

public class HistoryDto {

	private String _id;

	private java.util.List<TimeSheet> records;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public java.util.List<TimeSheet> getRecords() {
		return records;
	}

	public void setRecords(java.util.List<TimeSheet> records) {
		this.records = records;
	}

}
