package com.helpdesk.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserEntity {

	@Id
	@GeneratedValue
	private int id;
	private String email;
	private Long telNr;
	private String password;
	private String role;
	
	public UserEntity() {}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getTelNr() {
		return telNr;
	}
	
	public void setTelNr(Long telNr) {
		this.telNr = telNr;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
