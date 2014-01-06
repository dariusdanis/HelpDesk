package com.helpdesk.domain.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.domain.dao.CompanyFacilityDao;
import com.helpdesk.domain.entity.CompanyFacilityEntity;

@Service
public class CompanyFacilityService {

	@Autowired
	private CompanyFacilityDao companyFacilityDao;
	
	@Transactional
	public CompanyFacilityEntity merge(CompanyFacilityEntity companyFacilityEntity) {
		return companyFacilityDao.merge(companyFacilityEntity);
	}
}
