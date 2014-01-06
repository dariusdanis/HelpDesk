package com.helpdesk.domain.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CompanyEntity {

	@Id
	@GeneratedValue
	private int id;
	private String comapanyName;
	private String companyAddress;
	
	@OneToMany(targetEntity = UserEntity.class, fetch=FetchType.LAZY, mappedBy = "companyEntity")
	private List<UserEntity> users;
	
	@OneToMany(targetEntity = CompanyFacilityEntity.class, fetch=FetchType.LAZY, mappedBy = "facilityEntity")
	private List<CompanyFacilityEntity> facilities;
	
	public CompanyEntity() {}
	
	public CompanyEntity(int id) {
		this.id = id;
	}
	
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

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}
	
	public List<CompanyFacilityEntity> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<CompanyFacilityEntity> facilities) {
		this.facilities = facilities;
	}

	@Override
	public String toString() {
		return comapanyName;
	}
	
}
