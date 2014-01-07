package com.helpdesk.jpa;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.CompanyFacilityDao;
import com.helpdesk.domain.entity.CompanyFacilityEntity;

@Repository
public class CompanyFacilityDaoJpa implements CompanyFacilityDao {

	@PersistenceContext
	private EntityManager em;


	@Override
	public CompanyFacilityEntity merge(CompanyFacilityEntity companyFacilityEntity) {
		return em.merge(companyFacilityEntity);
	}
}
