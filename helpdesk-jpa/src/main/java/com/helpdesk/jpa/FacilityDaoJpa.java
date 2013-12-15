package com.helpdesk.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.FacilityDao;
import com.helpdesk.domain.entity.CompanyEntity;
import com.helpdesk.domain.entity.CompanyFacilityEntity;
import com.helpdesk.domain.entity.FacilityEntity;

@Repository
public class FacilityDaoJpa implements FacilityDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<FacilityEntity> getAllByCompany(CompanyEntity company) {
		TypedQuery<CompanyFacilityEntity> query = em.createQuery("SELECT f FROM CompanyFacilityEntity f " +
				"where f.companyEntity.id = :id", CompanyFacilityEntity.class);
		query.setParameter("id", company.getId());
		List<CompanyFacilityEntity> cfList = query.getResultList();
		List<FacilityEntity> fList = new ArrayList<FacilityEntity>(); 
		for (CompanyFacilityEntity cCompanyFacilityEntity : cfList) {
			fList.add(cCompanyFacilityEntity.getFacilityEntity());
		}
		return fList;
	}

	@Override
	public List<FacilityEntity> getAll() {
		 TypedQuery<FacilityEntity> query = em.createQuery("SELECT f FROM FacilityEntity f",
				 FacilityEntity.class);
		return query.getResultList();
	}

}
