package com.helpdesk.domain.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CompanyFacilityEntity {

	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "companyFk")
	private CompanyEntity companyEntity;
	
    @ManyToOne
	@JoinColumn(name = "facilityFk")
	private FacilityEntity facilityEntity;
	
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date signedDate;
    
    public CompanyFacilityEntity() {
	}

	public CompanyFacilityEntity(CompanyEntity companyEntity,
			FacilityEntity facilityEntity, Date expiryDate) {
		super();
		this.companyEntity = companyEntity;
		this.facilityEntity = facilityEntity;
		this.expiryDate = expiryDate;
	}




	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CompanyEntity getCompanyEntity() {
		return companyEntity;
	}

	public void setCompanyEntity(CompanyEntity companyEntity) {
		this.companyEntity = companyEntity;
	}

	public FacilityEntity getFacilityEntity() {
		return facilityEntity;
	}

	public void setFacilityEntity(FacilityEntity facilityEntity) {
		this.facilityEntity = facilityEntity;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getSignedDate() {
		return signedDate;
	}

	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
	}
	
}
