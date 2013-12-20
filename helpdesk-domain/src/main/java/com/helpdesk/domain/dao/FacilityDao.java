package com.helpdesk.domain.dao;

import java.util.List;

import com.helpdesk.domain.entity.CompanyEntity;
import com.helpdesk.domain.entity.FacilityEntity;

public interface FacilityDao {

	List<FacilityEntity> getAllByCompany(CompanyEntity company);
	
	List<FacilityEntity> getAll();
	
	FacilityEntity mege(FacilityEntity facilityEntity);
}
