package com.helpdesk.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CompanyEntity {

	@Id
	@GeneratedValue
	private int id;
	private String comapanyName;
	private String companyAddress;
	private UserEntity user;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getComapanyName() {
		return comapanyName;
	}
	
	public void setComapanyName(String comapanyName) {
		this.comapanyName = comapanyName;
	}
	
	public String getCompanyAddress() {
		return companyAddress;
	}
	
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	
	public UserEntity getUser() {
		return user;
	}
	
	public void setUser(UserEntity user) {
		this.user = user;
	}
}
