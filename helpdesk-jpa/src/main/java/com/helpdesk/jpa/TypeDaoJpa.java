package com.helpdesk.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.TypeDao;
import com.helpdesk.domain.entity.TypeEntity;

@Repository
public class TypeDaoJpa implements TypeDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<TypeEntity> getAll() {
		 TypedQuery<TypeEntity> query = em.createQuery("SELECT type FROM TypeEntity type",
				 TypeEntity.class);
		return query.getResultList();
	}


}
