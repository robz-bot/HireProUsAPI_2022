/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.ResourceMgmt;

/**
 * @author Sihab.
 *
 */
public interface ResourceMgmtRepository extends MongoRepository<ResourceMgmt, String> {

	/**
	 * @param id
	 * @return
	 */
	ResourceMgmt findById(long id);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param string
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<ResourceMgmt> findByProjectAllocation(String string, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param string
	 * @param buId
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<ResourceMgmt> findByProjectAllocationAndBuId(String string, Long buId, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param string
	 * @param resourceStatusActive
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<ResourceMgmt> findByProjectAllocationAndResourceStatus(String string, String resourceStatusActive,
			Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param projectId
	 * @return
	 */
	int countByProjectId(Long projectId);

	List<ResourceMgmt> findByProjectAllocationAndBuId(String string, Long buId, String resourceStatusActive,
			Sort orderByUpdatedDateTimeDesc);

	List<ResourceMgmt> findByProjectAllocationAndBuIdAndResourceStatus(String string, Long buId,
			String resourceStatusActive, Sort orderByUpdatedDateTimeDesc);

	ResourceMgmt findByEmailIgnoreCase(String email);

}
