package com.helpdesk.ui.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

	public static enum Status {NOT_ASSIGNED, WONT_SOLVE, SOLVED, ASSIGNED}
	public static enum Roles {ADMIN, CLIEN, ENGIN, DIREC}
	public static enum ReceiptMethod {EMAIL, PHONE, SELF_SERVICE}
	public static enum FilterOptions {CURRENT, HISTORY}
	
	public static List<String> receiptMethodsList = Arrays.asList(new String[] {ReceiptMethod.EMAIL.toString().toLowerCase(),
			ReceiptMethod.PHONE.toString().toLowerCase()});
	
	public static List<String> filterOptions = Arrays.asList(new String[] {FilterOptions.CURRENT.toString().toLowerCase(),
			FilterOptions.HISTORY.toString().toLowerCase()});
	
	public static final int MIN_LENGTH = 1;
	public static final int MAX_LENGTH = 40;
	public static final int MAX_LENGTH_TAREA = 9000;
	
	public static final String BAD_PHONE = "TODO: bad phone";
	public static final String BAD_EMAIL = "TODO: bad Email";
	public static final String TAKEN_EMAIL = "TODO: email is taken";
	public static final String REQUED = "TODO: field are requed!";
	public static final String NEW_REQUEST = "New Request!";
	public static final String BACK_TO_ADMIN = "TODO: Back To Admin!";
	public static final String NEW_ASSIGMENT = "TODO: New Assigment!";
	public static final String REQUEST_SOLVE = "TODO: Your Request Was Solved!";
	public static final String WOUNT_SOLVE = "TODO: Woun't solve!";
	
	public static final String HPCompany = "Help Desk";
	
	
	
	private Constants() {}
	
}
