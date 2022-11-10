/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.EmailConf;

/**
 * @author Sihab.
 *
 */
public interface EmailConfRepository extends MongoRepository<EmailConf, String> {

	/**
	 * @param id
	 * @return
	 */
	EmailConf findById(long id);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param purpose
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<EmailConf> findByPurposeRegex(String purpose, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param purpose
	 * @param buId
	 * @return
	 */
	EmailConf getEmailConfByPurposeAndBuId(String purpose, long buId);

	/**
	 * @param parseLong
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<EmailConf> getEmailConfByBuId(long parseLong, Sort orderByUpdatedDateTimeDesc);

	/**
	 * @param purpose
	 * @param buId
	 * @return
	 */
	EmailConf getEmailConfByPurposeIgnoreCaseAndBuId(String purpose, long buId);

	/**
	 * @param purpose
	 * @param buId
	 * @return
	 */
	EmailConf findByPurposeAndBuId(String purpose, Long buId);

	/**
	 * @param buId
	 * @return
	 */
	int countByBuId(Long buId);

}
