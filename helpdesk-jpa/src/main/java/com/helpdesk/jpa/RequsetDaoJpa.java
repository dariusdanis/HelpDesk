package com.helpdesk.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.RequsetDao;
import com.helpdesk.domain.entity.RequestEntity;

@Repository
public class RequsetDaoJpa implements RequsetDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public RequestEntity merge(RequestEntity requestEntity) {
		return em.merge(requestEntity);
	}

}
