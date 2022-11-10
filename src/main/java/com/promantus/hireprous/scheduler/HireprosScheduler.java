/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Class to handle scheduler activities.
 * 
 * @author Sihab.
 *
 */
@Configuration
@EnableScheduling
public class HireprosScheduler {

	private static final Logger logger = LoggerFactory.getLogger(HireprosScheduler.class);

	/**
	 * This method will run every half an hour.
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void scheduleCronHalfHourfor() {

		logger.info("From HirePro Scheduler.");
	}
}
