package com.promantus.hireprous.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.promantus.hireprous.entity.TimeSheet;

public interface TimeSheetRepository extends MongoRepository<TimeSheet, String> {

	TimeSheet findById(Long id);

	TimeSheet deleteById(long parseLong);

	List<TimeSheet> findAllByUserIdAndIsSubmittedForApproval(long l, boolean isApproval);

	List<TimeSheet> findTimeSheetByManagerIdAndIsSubmittedForApproval(long managerId, boolean b);

	List<TimeSheet> findTimeSheetByUserId(long parseLong);

	List<TimeSheet> findAllByDate(String date);

	List<TimeSheet> findAllByDate(LocalDate today);

}
