package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.AccomplishmentRating;

public interface AccomplishmentRatingRepositroy extends MongoRepository<AccomplishmentRating, String>{

	List<AccomplishmentRating> findByResourceNameOrEmployeeId(String resourceName, String employeeId,
			Sort orderByUpdatedDateTimeDesc);

	AccomplishmentRating findById(Long id);

	List<AccomplishmentRating> findByResourceName(String name);

	void deleteById(Long id);

	List<AccomplishmentRating> findByBuId();

	List<AccomplishmentRating> findByBuId(Long businessUnitId);

	List<AccomplishmentRating> findByRoleIdIn(List<String> roleIds);

}
