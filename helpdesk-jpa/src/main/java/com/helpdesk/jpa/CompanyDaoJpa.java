package com.helpdesk.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.CompanyDao;
import com.helpdesk.domain.entity.CompanyEntity;

@Repository
public class CompanyDaoJpa implements CompanyDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<CompanyEntity> getAll() {
		TypedQuery<CompanyEntity> query = em.createQuery("SELECT c FROM CompanyEntity c",
				CompanyEntity.class);
		return query.getResultList();
	}

}
