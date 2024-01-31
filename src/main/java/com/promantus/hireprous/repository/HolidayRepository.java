package com.promantus.hireprous.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.promantus.hireprous.entity.Holiday;

@Repository
public interface HolidayRepository extends MongoRepository<Holiday, Long> {

	Holiday findByHolidayName(String holidayName);

//	Holiday findById(Long id);

	Holiday findByYear(String year);

	Holiday findByMonth(String month);

	List<Holiday> findAllByYear(String year);



}
