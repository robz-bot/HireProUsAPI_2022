package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.LeaveTracker;


public interface LeaveTrackerRepository extends MongoRepository<LeaveTracker, Long>{

	LeaveTracker findByLeaveType(String Leave);

	LeaveTracker findByMonth(String month);
	
	LeaveTracker findByYear(String year);

	List<LeaveTracker> findAllByYear(String year);
	
}
