package com.helpdesk.ui.utils;

import java.util.Date;

public class Assigment {

	Integer requestFK;
	String administratorFK;
	String engineerFK;
	Date solveDate;
	String requestSolution;
	String status;
	int totalTimeSpend;

	public Assigment() {
		
	}
	
	public Assigment(Integer requestFK, String administratorFK,
			String engineerFK, Date solveDate, String requestSolution,
			String status, Integer timeSpend) {
		super();
		this.requestFK = requestFK;
		this.administratorFK = administratorFK;
		this.engineerFK = engineerFK;
		this.solveDate = solveDate;
		this.requestSolution = requestSolution;
		this.status = status;
		this.totalTimeSpend = timeSpend;
	}
	
	public void addTimeSpend(Integer timeSpend) {
		this.totalTimeSpend += timeSpend;
		
	}

	public Integer getRequestFK() {
		return requestFK;
	}

	public void setRequestFK(Integer requestFK) {
		this.requestFK = requestFK;
	}

	public String getAdministratorFK() {
		return administratorFK;
	}

	public void setAdministratorFK(String administratorFK) {
		this.administratorFK = administratorFK;
	}

	public String getEngineerFK() {
		return engineerFK;
	}

	public void setEngineerFK(String engineerFK) {
		this.engineerFK = engineerFK;
	}

	public Date getSolveDate() {
		return solveDate;
	}

	public void setSolveDate(Date solveDate) {
		this.solveDate = solveDate;
	}

	public String getRequestSolution() {
		return requestSolution;
	}

	public void setRequestSolution(String requestSolution) {
		this.requestSolution = requestSolution;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTimeSpend() {
		return totalTimeSpend;
	}

	public void setTimeSpend(Integer timeSpend) {
		this.totalTimeSpend = timeSpend;
	}

}
