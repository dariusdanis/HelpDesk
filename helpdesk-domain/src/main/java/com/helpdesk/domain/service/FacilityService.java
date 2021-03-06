package com.helpdesk.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.domain.dao.FacilityDao;
import com.helpdesk.domain.entity.CompanyEntity;
import com.helpdesk.domain.entity.FacilityEntity;

@Service
public class FacilityService {

	@Autowired
	private FacilityDao facilityDao;

	@Transactional
	public List<FacilityEntity> getAllByCompany(CompanyEntity company) {
		return facilityDao.getAllByCompany(company);
	}
	
	@Transactional
	public FacilityEntity merge(FacilityEntity facilityEntity) {
		return facilityDao.merge(facilityEntity);
	}
	
	@Transactional
	public FacilityEntity getById(int id) {
		return facilityDao.getById(id);
	}
	
}
