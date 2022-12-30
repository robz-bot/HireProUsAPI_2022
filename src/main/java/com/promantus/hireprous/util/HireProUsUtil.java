/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import org.springframework.data.domain.Sort;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.TimeZoneDto;

/**
 * Utility class to handle common activities.
 * 
 * @author Sihab.
 *
 */
public final class HireProUsUtil {

	/** Characters allowed to create UUID. */
	private static final String UUID_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	/** Characters allowed to create UUID_OTP. */
	private static final String UUID_OTP = "0123456789";

	/** Length of each part of the UUID. */
	private static final int UUID_LEN = 6;

	/**
	 * Constructor Method.
	 * 
	 * @throws InstantiationException InstantiationException - if any exception
	 *                                occurs.
	 */
	private HireProUsUtil() throws InstantiationException {
		throw new InstantiationException("Instances of this type are forbidden.");
	}

	/**
	 * @return
	 */
	public static Sort orderByCreatedDateTimeAsc() {
		return Sort.by(Sort.Direction.ASC, "createdDateTime");
	}

	/**
	 * @return
	 */
	public static Sort orderByCreatedDateTimeDesc() {
		return Sort.by(Sort.Direction.DESC, "createdDateTime");
	}

	//Added for candidate History
	public static Sort orderByIdAsc() {
		return Sort.by(Sort.Direction.ASC, "_id");
	}

	/**
	 * @return
	 */
	public static Sort orderByUpdatedDateTimeDesc() {
		return Sort.by(Sort.Direction.DESC, "updatedDateTime");
	}

	/**
	 * @return
	 */
	public static Sort orderByJRNumberAsc() {
		return Sort.by(Sort.Direction.ASC, "referenceNumber");
	}

	/**
	 * @return
	 */
	public static Sort orderByNextCounterDescAndRunningNumberDesc() {
		return Sort.by(Sort.Direction.DESC, "nextCounter").and(Sort.by(Sort.Direction.DESC, "runningNumber"));
	}

	/**
	 * @return
	 */
	public static Sort orderByRunningNumberDesc() {
		return Sort.by(Sort.Direction.DESC, "runningNumber");
	}

	/**
	 * @return
	 */
	public static Sort orderByCodeNumberDesc() {
		return Sort.by(Sort.Direction.DESC, "codeNumber");
	}

	/**
	 * Generate UUID.
	 *
	 * @param uuidLength Integer - required UUID length
	 * @return String - return generated UUID
	 */
	public static String generateUUID(Integer uuidLength) {

		if (uuidLength == null || uuidLength == 0) {
			uuidLength = UUID_LEN;
		}
		Random random = new SecureRandom();
		StringBuilder uuid = new StringBuilder();

		for (int i = 0; i < uuidLength; i++) {
			uuid.append(UUID_STR.charAt(random.nextInt(UUID_STR.length())));
		}

		return uuid.toString();
	}

	/**
	 * Generate UUID.
	 *
	 * @param uuidLength Integer - required UUID length
	 * @return String - return generated UUID
	 */
	public static String getOTP() {

		Random random = new SecureRandom();
		StringBuilder uuid = new StringBuilder();

		for (int i = 0; i < UUID_LEN; i++) {
			uuid.append(UUID_OTP.charAt(random.nextInt(UUID_OTP.length())));
		}

		return uuid.toString();
	}

	/**
	 * To encrypt the given string using key.
	 * 
	 * @param stringToEncrypt String - given text to be encrypted
	 * @return String - encrypted string value
	 * @throws Exception if any error occurs.
	 */
	public static String encrypt(String stringToEncrypt) throws Exception {

		byte[] message = stringToEncrypt.getBytes(StandardCharsets.UTF_8);
		return Base64.getEncoder().encodeToString(message);
	}

	/**
	 * To decrypt the given string using key.
	 * 
	 * @param stringToDecrypt String - given text to be decrypted
	 * @return String - decrypted string value
	 * @throws Exception if any error occurs.
	 */
	public static String decrypt(String stringToDecrypt) throws Exception {

		byte[] decoded = Base64.getDecoder().decode(stringToDecrypt);
		return new String(decoded, StandardCharsets.UTF_8);
	}

	/**
	 * @param localDateTime
	 * @return
	 * @throws Exception
	 */
	public static String formatDateTime(LocalDateTime localDateTime) throws Exception {

		DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a");
		return FOMATTER.format(localDateTime);
	}

	/**
	 * @param localDateTime
	 * @return
	 */
	public static LocalDateTime getGMTDateTime(LocalDateTime localDateTime) {

		return LocalDateTime.ofInstant(localDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.of("GMT"));
	}

	/**
	 * @param localDateTime
	 * @return
	 */
	public static LocalDateTime getLocalDateTime(LocalDateTime localDateTime, String zoneId) {

		return LocalDateTime.ofInstant(localDateTime.atZone(ZoneId.of("GMT")).toInstant(), ZoneId.of(zoneId));
	}

	/**
	 * @param localDateTime
	 * @return
	 */
	public static LocalDateTime getISTLocalDateTime(LocalDateTime localDateTime) {

		return LocalDateTime.ofInstant(localDateTime.atZone(ZoneId.of("GMT")).toInstant(), ZoneId.of("Asia/Kolkata"));
	}

	/**
	 * Get error message from stack trace message.
	 * 
	 * @param e Exception - given exception object
	 * @return String - required error message
	 */
	public static synchronized String getErrorMessage(final Exception e) {

		final StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	/**
	 * @return
	 */
	public static List<TimeZoneDto> getTimeZoneList() {

		List<TimeZoneDto> timeZoneDtoList = new ArrayList<TimeZoneDto>();

		Set<String> zoneIds = ZoneId.getAvailableZoneIds();
		for (String zoneId : zoneIds) {

			ZoneId zone = ZoneId.of(zoneId);
			String longName = zone.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

			TimeZoneDto timeZoneDto = new TimeZoneDto();
			timeZoneDto.setId(zoneId);
			timeZoneDto.setName(zoneId + ", " + longName);
			timeZoneDtoList.add(timeZoneDto);
		}

		return timeZoneDtoList;
	}

	/**
	 * @param duration
	 * @return
	 */
	public static String getHourMinute(String duration) {

		String hour = "0";
		String minute = "0";

		if (duration.equals(HireProUsConstants.SCHEDULE_DURATION_30_MINS)) {
			minute = "30";
		} else if (duration.equals(HireProUsConstants.SCHEDULE_DURATION_45_MINS)) {
			minute = "45";
		} else if (duration.equals(HireProUsConstants.SCHEDULE_DURATION_1_HOURS)) {
			hour = "1";
		} else if (duration.equals(HireProUsConstants.SCHEDULE_DURATION_1_30_HOURS)) {
			hour = "1";
			minute = "30";
		} else if (duration.equals(HireProUsConstants.SCHEDULE_DURATION_2_HOURS)) {
			hour = "2";
		}

		return hour + "," + minute;
	}

	/**
	 * @param createdDateTime
	 * @param now
	 * @return
	 */
	public static long getDiffDays(LocalDateTime createdDateTime, LocalDateTime now) {

		return Duration.between(createdDateTime, now).toDays();
	}
}