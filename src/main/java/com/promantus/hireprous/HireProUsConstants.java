/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous;

/**
 * Constants class for HireProUs.
 * 
 * Commonly used constant values are declared here to access from all layers.
 * 
 * @author Sihab.
 * 
 */
public final class HireProUsConstants {

	/**
	 * Constructor.
	 * 
	 * @throws InstantiationException InstantiationException
	 */
	private HireProUsConstants() throws InstantiationException {
		throw new InstantiationException("Instances of this type are forbidden.");
	}

	/** INACTIVE - 0. */
	public static final int INACTIVE = 0;

	/** ACTIVE - 1. */
	public static final int ACTIVE = 1;

	/** RETURN_STATUS_OK - 0. */
	public static final int RETURN_STATUS_OK = 0;

	/** RETURN_STATUS_ERROR - 1. */
	public static final int RETURN_STATUS_ERROR = 1;

	/** PANEL_MEMBER_YES - 1. */
	public static final String PANEL_MEMBER_YES = "1";

	/** PANEL_MEMBER_NO - 0. */
	public static final String PANEL_MEMBER_NO = "0";

	// Prefix
	// ----------------------------------------------------------------------------------------------------------------

	/** USER_IMAGE_PREFIX - 'user_'. */
	public static final String USER_IMAGE_PREFIX = "user_";

	/** CANDIDATE_IMAGE_PREFIX - 'candidate_'. */
	public static final String CANDIDATE_IMAGE_PREFIX = "candidate_";

	/** CANDIDATE_RESUME_PREFIX - 'resume_'. */
	public static final String CANDIDATE_RESUME_PREFIX = "resume_";

	/** BASE64_IMAGE_PREFIX - 'data:image/[imgType];base64,'. */
	public static final String BASE64_IMAGE_PREFIX = "data:image/[imgType];base64,";

	/** BASE64_FILE_PREFIX - 'data:application/[fileType];base64,'. */
	public static final String BASE64_FILE_PREFIX = "data:application/[fileType];base64,";

	/** JOB_REQUEST_REFERENCE_PREFIX - 'REC'. */
	public static final String JOB_REQUEST_REFERENCE_PREFIX = "REC";

	// Job Request
	// ----------------------------------------------------------------------------------------------------------------

	/** JOB_REQUEST_REFERENCE_MAX_DIGIT_RUNNING_NUMBER - '6'. */
	public static final int JOB_REQUEST_REFERENCE_MAX_DIGIT_RUNNING_NUMBER = 6;

	/** JOB_REQUEST_REFERENCE_MAX_RUNNING_NUMBER - '999999'. */
	public static final int JOB_REQUEST_REFERENCE_MAX_RUNNING_NUMBER = 999999;

	// On board
	// ----------------------------------------------------------------------------------------------------------------

	/** ON_BOARD_MAX_DIGIT_RUNNING_NUMBER - '4'. */
	public static final int ON_BOARD_MAX_DIGIT_RUNNING_NUMBER = 4;

	/** ON_BOARD_MAX_RUNNING_NUMBER - '9999'. */
	public static final int ON_BOARD_MAX_RUNNING_NUMBER = 9999;

	// Role Name
	// ----------------------------------------------------------------------------------------------------------------

	/** ROLE_NAME_RECRUITMENT_MANAGER - 'Recruitment Manager'. */
	public static final String ROLE_NAME_RECRUITMENT_MANAGER = "Recruitment Manager";

	/** ROLE_NAME_RECRUITMENT_TEAM - 'Recruitment Team'. */
	public static final String ROLE_NAME_RECRUITMENT_TEAM = "Recruitment Team";

	/** ROLE_NAME_SALES_MANAGER - 'Sales Manager'. */
	public static final String ROLE_NAME_SALES_MANAGER = "Sales Manager";

	// Job Request Status
	// ----------------------------------------------------------------------------------------------------------------

	/** JOB_REQUEST_STATUS_YET_TO_START - 'Yet to Start'. */
	public static final String JOB_REQUEST_STATUS_YET_TO_START = "Yet to Start";

	/** JOB_REQUEST_STATUS_IN_PROGRESS - 'In Progress'. */
	public static final String JOB_REQUEST_STATUS_IN_PROGRESS = "In Progress";

	/** JOB_REQUEST_STATUS_HOLD - 'Hold'. */
	public static final String JOB_REQUEST_STATUS_HOLD = "Hold";

	/** JOB_REQUEST_STATUS_TERMINATED - 'Terminated'. */
	public static final String JOB_REQUEST_STATUS_TERMINATED = "Terminated";

	/** JOB_REQUEST_STATUS_CLOSED - 'Closed'. */
	public static final String JOB_REQUEST_STATUS_CLOSED = "Closed";

	// Suggestion Status
	// ----------------------------------------------------------------------------------------------------------------

	/** SUGGESTION_STATUS_OPEN - 'Yet to Start'. */
	public static final String SUGGESTION_STATUS_OPEN = "Open";

	/** SUGGESTION_STATUS_IN_PROGRESS - 'In Progress'. */
	public static final String SUGGESTION_STATUS_IN_PROGRESS = "In Progress";

	/** SUGGESTION_STATUS_HOLD - 'Hold'. */
	public static final String SUGGESTION_STATUS_HOLD = "Hold";

	/** SUGGESTION_STATUS_CLOSED - 'Closed'. */
	public static final String SUGGESTION_STATUS_CLOSED = "Closed";

	/** SUGGESTION_MAX_DIGIT_CODE_NUMBER - '5'. */
	public static final int SUGGESTION_MAX_DIGIT_CODE_NUMBER = 5;

	// Employment Type
	// --------------------------------------------------------------------------

	/** EMPLOYMENT_TYPE_CONTRACT - 'Contract'. */
	public static final String EMPLOYMENT_TYPE_CONTRACT = "Contract";

	/** EMPLOYMENT_TYPE_FULLTIME - 'Full Time'. */
	public static final String EMPLOYMENT_TYPE_FULLTIME = "Full Time";

	// Placement For
	// --------------------------------------------------------------------------

	/** PLACEMENT_FOR_INTERNAL - 'Internal'. */
	public static final String PLACEMENT_FOR_INTERNAL = "Internal";

	/** PLACEMENT_FOR_CUSTOMER - 'Customer'. */
	public static final String PLACEMENT_FOR_CUSTOMER = "Customer";

	// Candidate Type
	// --------------------------------------------------------------------------

	/** CANDIDATE_TYPE_INTERNAL - 'Internal'. */
	public static final String CANDIDATE_TYPE_INTERNAL = "Internal";

	/** CANDIDATE_TYPE_EXTERNAL - 'External'. */
	public static final String CANDIDATE_TYPE_EXTERNAL = "External";

	// Resource Status
	// ----------------------------------------------------------------------------------------------------------------

	/** RESOURCE_STATUS_ACTIVE - 'Active'. */
	public static final String RESOURCE_STATUS_ACTIVE = "Active";

	/** RESOURCE_STATUS_INACTIVE - 'In-Active'. */
	public static final String RESOURCE_STATUS_INACTIVE = "In-Active";

	/** RESOURCE_STATUS_HOLD - 'Hold'. */
	public static final String RESOURCE_STATUS_HOLD = "Hold";

	/** RESOURCE_STATUS_RESIGNED - 'Resigned'. */
	public static final String RESOURCE_STATUS_RESIGNED = "Resigned";

	/** RESOURCE_STATUS_TERMINATED - 'Terminated'. */
	public static final String RESOURCE_STATUS_TERMINATED = "Terminated";

	// Interview Round
	// ----------------------------------------------------------------------------------------------------------------

	/** INTERVIEW_ROUND_INITIAL - '0'. */
	public static final int INTERVIEW_ROUND_INITIAL = 0;

	/** INTERVIEW_ROUND_INTERNAL1 - '1'. */
	public static final int INTERVIEW_ROUND_INTERNAL1 = 1;

	/** INTERVIEW_ROUND_INTERNAL2 - '2'. */
	public static final int INTERVIEW_ROUND_INTERNAL2 = 2;

	/** INTERVIEW_ROUND_CUSTOMER - '3'. */
	public static final int INTERVIEW_ROUND_CUSTOMER = 3;

	/** INTERVIEW_ROUND_HR - '4'. */
	public static final int INTERVIEW_ROUND_HR = 4;

	/** INTERVIEW_ROUND_BU - '5'. */
	public static final int INTERVIEW_ROUND_BU = 5;

	// Candidate Status
	// --------------------------------------------------------------------------

	/** REC_STATUS_UPLOADED - '00'. */
	public static final String REC_STATUS_UPLOADED = "00";

	/** REC_STATUS_SHORTLISTED_0 - '01'. */
	public static final String REC_STATUS_SHORTLISTED_0 = "01";

	/** REC_STATUS_HOLDED_0 - '02'. */
	public static final String REC_STATUS_HOLDED_0 = "02";

	/** REC_STATUS_REJECTED_0 - '03'. */
	public static final String REC_STATUS_REJECTED_0 = "03";

	/** REC_STATUS_SCHEDULED_R1 - '04'. */
	public static final String REC_STATUS_SCHEDULED_R1 = "04";

	/** REC_STATUS_PASSED_R1 - '05'. */
	public static final String REC_STATUS_PASSED_R1 = "05";

	/** REC_STATUS_HOLDED_R1 - '06'. */
	public static final String REC_STATUS_HOLDED_R1 = "06";

	/** REC_STATUS_REJECTED_R1 - '07'. */
	public static final String REC_STATUS_REJECTED_R1 = "07";

	/** REC_STATUS_SCHEDULED_R2 - '08'. */
	public static final String REC_STATUS_SCHEDULED_R2 = "08";

	/** REC_STATUS_PASSED_R2 - '09'. */
	public static final String REC_STATUS_PASSED_R2 = "09";

	/** REC_STATUS_HOLDED_R2 - '10'. */
	public static final String REC_STATUS_HOLDED_R2 = "10";

	/** REC_STATUS_REJECTED_R2 - '11'. */
	public static final String REC_STATUS_REJECTED_R2 = "11";

	/** REC_STATUS_SCHEDULED_CR3 - '12'. */
	public static final String REC_STATUS_SCHEDULED_CR3 = "12";

	/** REC_STATUS_PASSED_CR3 - '13'. */
	public static final String REC_STATUS_PASSED_CR3 = "13";

	/** REC_STATUS_HOLDED_CR3 - '14'. */
	public static final String REC_STATUS_HOLDED_CR3 = "14";

	/** REC_STATUS_REJECTED_CR3 - '15'. */
	public static final String REC_STATUS_REJECTED_CR3 = "15";

	/** REC_STATUS_SCHEDULED_HR4 - '16'. */
	public static final String REC_STATUS_SCHEDULED_HR4 = "16";

	/** REC_STATUS_PASSED_HR4 - '17'. */
	public static final String REC_STATUS_PASSED_HR4 = "17";

	/** REC_STATUS_HOLDED_HR4 - '18'. */
	public static final String REC_STATUS_HOLDED_HR4 = "18";

	/** REC_STATUS_REJECTED_HR4 - '19'. */
	public static final String REC_STATUS_REJECTED_HR4 = "19";

	/** REC_STATUS_SCHEDULED_BU - '20'. */
	public static final String REC_STATUS_SCHEDULED_BU = "20";

	/** REC_STATUS_APPROVED_BU - '21'. */
	public static final String REC_STATUS_APPROVED_BU = "21";

	/** REC_STATUS_HOLDED_BU - '22'. */
	public static final String REC_STATUS_HOLDED_BU = "22";

	/** REC_STATUS_REJECTED_BU - '23'. */
	public static final String REC_STATUS_REJECTED_BU = "23";

	/** REC_STATUS_SELECTED - '24'. */
	public static final String REC_STATUS_SELECTED = "24";

	/** REC_STATUS_ONBOARDED - '25'. */
	public static final String REC_STATUS_ONBOARDED = "25";

	/** REC_STATUS_DROPPED - '26'. */
	public static final String REC_STATUS_DROPPED = "26";

	// Mail Purpose
	// --------------------------------------------------------------------------

	/** MAIL_PURPOSE_CONFIGURATION_CREATE - 'CONFIGURATION_CREATE'. */
	public static final String MAIL_PURPOSE_CONFIGURATION_CREATE = "CONFIGURATION_CREATE";

	/** MAIL_PURPOSE_CONFIGURATION_UPDATE - 'CONFIGURATION_UPDATE'. */
	public static final String MAIL_PURPOSE_CONFIGURATION_UPDATE = "CONFIGURATION_UPDATE";

	/** MAIL_PURPOSE_JOB_REQUEST_CREATE - 'JOB_REQUEST_CREATE'. */
	public static final String MAIL_PURPOSE_JOB_REQUEST_CREATE = "JOB_REQUEST_CREATE";

	/** MAIL_PURPOSE_JOB_REQUEST_UPDATE - 'JOB_REQUEST_UPDATE'. */
	public static final String MAIL_PURPOSE_JOB_REQUEST_UPDATE = "JOB_REQUEST_UPDATE";

	/** MAIL_PURPOSE_CANDIDATE_UPLOAD - 'CANDIDATE_UPLOAD'. */
	public static final String MAIL_PURPOSE_CANDIDATE_UPLOAD = "CANDIDATE_UPLOAD";

	/** MAIL_PURPOSE_CANDIDATE_UPDATED - 'CANDIDATE_UPDATED'. */
	public static final String MAIL_PURPOSE_CANDIDATE_UPDATED = "CANDIDATE_UPDATED";

	/** MAIL_PURPOSE_RESUME_SHORTLIST - 'RESUME_SHORTLIST'. */
	public static final String MAIL_PURPOSE_RESUME_SHORTLIST = "RESUME_SHORTLIST";

	/** MAIL_PURPOSE_INTERNAL_ROUND1_SCHEDULED - 'INTERNAL_ROUND1_SCHEDULED'. */
	public static final String MAIL_PURPOSE_INTERNAL_ROUND1_SCHEDULED = "INTERNAL_ROUND1_SCHEDULED";

	/** MAIL_PURPOSE_INTERNAL_ROUND1_RESULT - 'INTERNAL_ROUND1_RESULT'. */
	public static final String MAIL_PURPOSE_INTERNAL_ROUND1_RESULT = "INTERNAL_ROUND1_RESULT";

	/** MAIL_PURPOSE_INTERNAL_ROUND2_SCHEDULED - 'INTERNAL_ROUND2_SCHEDULED'. */
	public static final String MAIL_PURPOSE_INTERNAL_ROUND2_SCHEDULED = "INTERNAL_ROUND2_SCHEDULED";

	/** MAIL_PURPOSE_INTERNAL_ROUND2_RESULT - 'INTERNAL_ROUND2_RESULT'. */
	public static final String MAIL_PURPOSE_INTERNAL_ROUND2_RESULT = "INTERNAL_ROUND2_RESULT";

	/** MAIL_PURPOSE_CUSTOMER_ROUND_SCHEDULED - 'CUSTOMER_ROUND_SCHEDULED'. */
	public static final String MAIL_PURPOSE_CUSTOMER_ROUND_SCHEDULED = "CUSTOMER_ROUND_SCHEDULED";

	/** MAIL_PURPOSE_CUSTOMER_ROUND_RESULT - 'CUSTOMER_ROUND_RESULT'. */
	public static final String MAIL_PURPOSE_CUSTOMER_ROUND_RESULT = "CUSTOMER_ROUND_RESULT";

	/** MAIL_PURPOSE_HR_ROUND_SCHEDULED - 'HR_ROUND_SCHEDULED'. */
	public static final String MAIL_PURPOSE_HR_ROUND_SCHEDULED = "HR_ROUND_SCHEDULED";

	/** MAIL_PURPOSE_HR_ROUND_RESULT - 'HR_ROUND_RESULT'. */
	public static final String MAIL_PURPOSE_HR_ROUND_RESULT = "HR_ROUND_RESULT";

	/** MAIL_PURPOSE_FOR_BU_APPROVAL - 'FOR_BU_APPROVAL'. */
	public static final String MAIL_PURPOSE_FOR_BU_APPROVAL = "FOR_BU_APPROVAL";

	/** MAIL_PURPOSE_SELECTED - 'SELECTED'. */
	public static final String MAIL_PURPOSE_SELECTED = "SELECTED";

	/** MAIL_PURPOSE_ON_BOARDED - 'ON_BOARDED'. */
	public static final String MAIL_PURPOSE_ON_BOARDED = "ON_BOARDED";

	/** MAIL_PURPOSE_WEEKLY_REPORT - 'WEEKLY_REPORT'. */
	public static final String MAIL_PURPOSE_WEEKLY_REPORT = "WEEKLY_REPORT";

	/** MAIL_PURPOSE_MONTHLY_REPORT - 'MONTHLY_REPORT'. */
	public static final String MAIL_PURPOSE_MONTHLY_REPORT = "MONTHLY_REPORT";

	/** MAIL_PURPOSE_YEARLY_REPORT - 'YEARLY_REPORT'. */
	public static final String MAIL_PURPOSE_YEARLY_REPORT = "YEARLY_REPORT";


	// Schedule Duration
	// --------------------------------------------------------------------------

	/** SCHEDULE_DURATION_30_MINS - '30 mins'. */
	public static final String SCHEDULE_DURATION_30_MINS = "30 mins";

	/** SCHEDULE_DURATION_45_MINS - '45 mins'. */
	public static final String SCHEDULE_DURATION_45_MINS = "45 mins";

	/** SCHEDULE_DURATION_1_HOURS - '1 hour'. */
	public static final String SCHEDULE_DURATION_1_HOURS = "1 hour";

	/** SCHEDULE_DURATION_1_30_HOURS - '1h 30m'. */
	public static final String SCHEDULE_DURATION_1_30_HOURS = "1h 30m";

	/** SCHEDULE_DURATION_2_HOURS - '2 hours'. */
	public static final String SCHEDULE_DURATION_2_HOURS = "2 hours";

}