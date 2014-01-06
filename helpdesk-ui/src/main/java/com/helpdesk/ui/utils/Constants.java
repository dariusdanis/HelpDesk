package com.helpdesk.ui.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

	public static enum Status {NOT_ASSIGNED, WONT_SOLVE, SOLVED, ASSIGNED}
	public static enum Roles {ADMIN, CLIEN, ENGIN, DIREC}
	public static enum ReceiptMethod {EMAIL, PHONE, SELF_SERVICE}
	public static enum FilterOptions {CURRENT, HISTORY, ALL}
	
	public static List<String> receiptMethodsList = Arrays.asList(new String[] {ReceiptMethod.EMAIL.toString().toLowerCase(),
			ReceiptMethod.PHONE.toString().toLowerCase()});
		
	public static List<String> filterOptionsAdminAndDirect = Arrays.asList(new String[] {FilterOptions.CURRENT.toString().toLowerCase(),
			FilterOptions.HISTORY.toString().toLowerCase(), FilterOptions.ALL.toString().toLowerCase()});
	
	public static List<String> filterOptionsClient = Arrays.asList(new String[] {FilterOptions.CURRENT.toString().toLowerCase(),
			FilterOptions.HISTORY.toString().toLowerCase()});
	public static List<String> filterOptionsEngin = Arrays.asList(new String[] {FilterOptions.CURRENT.toString().toLowerCase(),
			FilterOptions.HISTORY.toString().toLowerCase()});
	
	public static List<String> ACTIONS_ENG = Arrays.asList(new String[] {"Back To Administration!", "Solved!", "Woun't Solve!"});
	public static List<String> ACTIONS_ADM = Arrays.asList(new String[] {"Solved!", "Woun't Solve!"});
	
	public static final int MIN_LENGTH = 1;
	public static final int MAX_LENGTH = 40;
	public static final int MAX_LENGTH_TAREA = 9000;
	
	public static final String BAD_PHONE = "Wrong phone number";
	public static final String BAD_EMAIL = "Wrong email adress";
	public static final String TAKEN_EMAIL = "Email is taken";
	public static final String REQUIRED = "Field is required!";
	public static final String NEW_REQUEST = "New Request!";
	public static final String BACK_TO_ADMIN = "Back To Admin!";
	public static final String NEW_ASSIGMENT = "New Assigment!";
	public static final String REQUEST_SOLVE = "Your request was solved!";
	public static final String WOUNT_SOLVE = "Request can't be solved!";
	public static final String BAD_DATE = "Bad Date Format!";
	public static final String BAD_DATE_PERIOD = "Bad Period!";
	
	public static final String HPCompany = "Help Desk";
	
	
	private Constants() {}
	
	public static String FOCurrent() {
		return FilterOptions.CURRENT.toString().toLowerCase();
	}
	
	public static String FOHistory() {
		return FilterOptions.HISTORY.toString().toLowerCase();
	}

	public static String FOAll() {
		return FilterOptions.ALL.toString().toLowerCase();
	}
}
