package com.helpdesk.domain.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class FacilityEntity {

	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	
	@OneToMany(targetEntity = CompanyFacilityEntity.class, fetch=FetchType.LAZY, mappedBy = "companyEntity")
	private List<CompanyFacilityEntity> companies;

	@OneToMany(targetEntity = RequestEntity.class, fetch=FetchType.LAZY, mappedBy = "facilityEntity")
	private List<RequestEntity> requests;
	
	public FacilityEntity() {}
	
	public FacilityEntity(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<CompanyFacilityEntity> getCompanies() {
		return companies;
	}

	public void setCompanies(List<CompanyFacilityEntity> companies) {
		this.companies = companies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
