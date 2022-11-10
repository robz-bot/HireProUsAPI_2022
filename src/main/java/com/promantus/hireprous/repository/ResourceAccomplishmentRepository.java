package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.ResourceAccomplishment;

public interface ResourceAccomplishmentRepository extends MongoRepository<ResourceAccomplishment, String>{

	void deleteById(Long id);

	ResourceAccomplishment findById(Long id);

	List<ResourceAccomplishment> findByEmployeeId(String employeeId, Sort orderByUpdatedDateTimeDesc);

	List<ResourceAccomplishment> findByResourceName(String employeeId, Sort orderByUpdatedDateTimeDesc);

	List<ResourceAccomplishment> findByResourceName(String name);

	List<ResourceAccomplishment> findByBuId(long buId);

	List<ResourceAccomplishment> findByCreatedDateTime();

	List<ResourceAccomplishment> findByRefId(long refId);
	

}
