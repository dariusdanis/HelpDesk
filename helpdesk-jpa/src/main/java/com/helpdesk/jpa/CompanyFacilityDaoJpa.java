package com.helpdesk.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.CompanyFacilityDao;
import com.helpdesk.domain.dao.UserDao;
import com.helpdesk.domain.entity.CompanyFacilityEntity;
import com.helpdesk.domain.entity.UserEntity;

@Repository
public class CompanyFacilityDaoJpa implements CompanyFacilityDao {

	@PersistenceContext
	private EntityManager em;


	@Override
	public CompanyFacilityEntity merge(CompanyFacilityEntity companyFacilityEntity) {
		return em.merge(companyFacilityEntity);
	}
}
