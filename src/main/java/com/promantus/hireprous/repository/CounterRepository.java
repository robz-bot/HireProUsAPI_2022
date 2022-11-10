/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Counter;

public interface CounterRepository extends MongoRepository<Counter, String> {

}
