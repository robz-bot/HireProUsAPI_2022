package com.promantus.hireprous.util;

import java.util.*;

/**
 * Utility class to handle Cache related activities.
 * 
 * @author Sihab.
 * 
 */
public final class CacheUtil {

	private static Map<String, String> resetOtpMap = Collections.synchronizedMap(new HashMap<String, String>());

	private static Map<Long, String> usersMap = Collections.synchronizedMap(new HashMap<Long, String>());
	private static Map<Long, String> usersEmailMap = Collections.synchronizedMap(new HashMap<Long, String>());

	private static Map<Long, String> busMap = Collections.synchronizedMap(new HashMap<Long, String>());

	private static Map<Long, String> rolesMap = Collections.synchronizedMap(new HashMap<Long, String>());

	private static Map<Long, String> customersMap = Collections.synchronizedMap(new HashMap<Long, String>());

	private static Map<Long, String> projectsMap = Collections.synchronizedMap(new HashMap<Long, String>());
	private static Map<Long, String> projectsByBUMap = Collections.synchronizedMap(new HashMap<Long, String>());

	private static Map<Long, String> recRolesMap = Collections.synchronizedMap(new HashMap<Long, String>());
	private static Map<Long, String> recRolesByCustomerMap = Collections.synchronizedMap(new HashMap<Long, String>());

	private static Map<Long, String> candidatesMap = Collections.synchronizedMap(new HashMap<Long, String>());

	private static Map<Long, String> vendorsMap = Collections.synchronizedMap(new HashMap<Long, String>());
	private static Map<Long, String> vendorsEmailMap = Collections.synchronizedMap(new HashMap<Long, String>());
	private static Map<Long, String> vendorsCCEmailMap = Collections.synchronizedMap(new HashMap<Long, String>());

	/**
	 * Constructor Method.
	 * 
	 * @throws InstantiationException InstantiationException - if any exception
	 *                                occurs.
	 */
	private CacheUtil() throws InstantiationException {
		throw new InstantiationException("Instances of this type are forbidden.");
	}

	/**
	 * @return the resetOtpMap
	 */
	public static Map<String, String> getResetOtpMap() {
		return resetOtpMap;
	}

	/**
	 * @param resetOtpMap the resetOtpMap to set
	 */
	public static void setResetOtpMap(Map<String, String> resetOtpMap) {
		CacheUtil.resetOtpMap = resetOtpMap;
	}

	/**
	 * @return the usersMap
	 */
	public static Map<Long, String> getUsersMap() {
		return usersMap;
	}

	/**
	 * @param usersMap the usersMap to set
	 */
	public static void setUsersMap(Map<Long, String> usersMap) {
		CacheUtil.usersMap = usersMap;
	}

	/**
	 * @return the usersEmailMap
	 */
	public static Map<Long, String> getUsersEmailMap() {
		return usersEmailMap;
	}

	/**
	 * @param usersEmailMap the usersEmailMap to set
	 */
	public static void setUsersEmailMap(Map<Long, String> usersEmailMap) {
		CacheUtil.usersEmailMap = usersEmailMap;
	}

	/**
	 * @return the busMap
	 */
	public static Map<Long, String> getBusMap() {
		return busMap;
	}

	/**
	 * @param busMap the busMap to set
	 */
	public static void setBusMap(Map<Long, String> busMap) {
		CacheUtil.busMap = busMap;
	}

	/**
	 * @return the customersMap
	 */
	public static Map<Long, String> getCustomersMap() {
		return customersMap;
	}

	/**
	 * @param customersMap the customersMap to set
	 */
	public static void setCustomersMap(Map<Long, String> customersMap) {
		CacheUtil.customersMap = customersMap;
	}

	/**
	 * @return the projectsMap
	 */
	public static Map<Long, String> getProjectsMap() {
		return projectsMap;
	}

	/**
	 * @param projectsMap the projectsMap to set
	 */
	public static void setProjectsMap(Map<Long, String> projectsMap) {
		CacheUtil.projectsMap = projectsMap;
	}

	/**
	 * @return the projectsByBUMap
	 */
	public static Map<Long, String> getProjectsByBUMap() {
		return projectsByBUMap;
	}

	/**
	 * @param projectsByBUMap the projectsByBUMap to set
	 */
	public static void setProjectsByBUMap(Map<Long, String> projectsByBUMap) {
		CacheUtil.projectsByBUMap = projectsByBUMap;
	}

	/**
	 * @return the recRolesMap
	 */
	public static Map<Long, String> getRecRolesMap() {
		return recRolesMap;
	}

	/**
	 * @param recRolesMap the recRolesMap to set
	 */
	public static void setRecRolesMap(Map<Long, String> recRolesMap) {
		CacheUtil.recRolesMap = recRolesMap;
	}

	/**
	 * @return the recRolesByCustomerMap
	 */
	public static Map<Long, String> getRecRolesByCustomerMap() {
		return recRolesByCustomerMap;
	}

	/**
	 * @param recRolesByCustomerMap the recRolesByCustomerMap to set
	 */
	public static void setRecRolesByCustomerMap(Map<Long, String> recRolesByCustomerMap) {
		CacheUtil.recRolesByCustomerMap = recRolesByCustomerMap;
	}

	/**
	 * @return the candidatesMap
	 */
	public static Map<Long, String> getCandidatesMap() {
		return candidatesMap;
	}

	/**
	 * @param candidatesMap the candidatesMap to set
	 */
	public static void setCandidatesMap(Map<Long, String> candidatesMap) {
		CacheUtil.candidatesMap = candidatesMap;
	}

	/**
	 * @return the vendorsMap
	 */
	public static Map<Long, String> getVendorsMap() {
		return vendorsMap;
	}

	/**
	 * @param vendorsMap the vendorsMap to set
	 */
	public static void setVendorsMap(Map<Long, String> vendorsMap) {
		CacheUtil.vendorsMap = vendorsMap;
	}

	/**
	 * @return the rolesMap
	 */
	public static Map<Long, String> getRolesMap() {
		return rolesMap;
	}

	/**
	 * @param rolesMap the rolesMap to set
	 */
	public static void setRolesMap(Map<Long, String> rolesMap) {
		CacheUtil.rolesMap = rolesMap;
	}

	/**
	 * @return the vendorsEmailMap
	 */
	public static Map<Long, String> getVendorsEmailMap() {
		return vendorsEmailMap;
	}

	/**
	 * @param vendorsEmailMap the vendorsEmailMap to set
	 */
	public static void setVendorsEmailMap(Map<Long, String> vendorsEmailMap) {
		CacheUtil.vendorsEmailMap = vendorsEmailMap;
	}

	/**
	 * @return the vendorsCCEmailMap
	 */
	public static Map<Long, String> getVendorsCCEmailMap() {
		return vendorsCCEmailMap;
	}

	/**
	 * @param vendorsCCEmailMap the vendorsCCEmailMap to set
	 */
	public static void setVendorsCCEmailMap(Map<Long, String> vendorsCCEmailMap) {
		CacheUtil.vendorsCCEmailMap = vendorsCCEmailMap;
	}
}