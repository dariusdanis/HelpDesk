package com.helpdesk.domain.entity;

import java.util.List;

import javax.persistence.Column;
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

	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String lhInc;
	
	@Column(nullable=false)
	private String lhReq;
	
	@OneToMany(targetEntity = CompanyFacilityEntity.class, fetch=FetchType.LAZY, mappedBy = "companyEntity")
	private List<CompanyFacilityEntity> companies;

	@OneToMany(targetEntity = RequestEntity.class, fetch=FetchType.LAZY, mappedBy = "facilityEntity")
	private List<RequestEntity> requests;
	
	public FacilityEntity() {}
	
	public FacilityEntity(String name, String lhInc, String lhReq) {
		super();
		this.name = name;
		this.lhInc= lhInc;
		this.lhReq = lhReq;
	}

	public FacilityEntity(int id) {
		this.id = id;
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
	
	public String getLhInc() {
		return lhInc;
	}

	public void setLhInc(String lhInc) {
		this.lhInc = lhInc;
	}

	public String getLhReq() {
		return lhReq;
	}

	public void setLhReq(String lhReq) {
		this.lhReq = lhReq;
	}

	public List<RequestEntity> getRequests() {
		return requests;
	}

	public void setRequests(List<RequestEntity> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
