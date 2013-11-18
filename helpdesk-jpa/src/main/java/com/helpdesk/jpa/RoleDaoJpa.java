package com.helpdesk.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.RoleDao;
import com.helpdesk.domain.entity.RoleEntity;

@Repository
public class RoleDaoJpa implements RoleDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<RoleEntity> getAll() {
        TypedQuery<RoleEntity> query = em.createQuery("SELECT role FROM RoleEntity role",
        		RoleEntity.class);
		return query.getResultList();
	}
	
}
