package com.helpdesk.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.UserDao;
import com.helpdesk.domain.entity.UserEntity;

@Repository
public class UserDaoJpa implements UserDao{

	@PersistenceContext
	private EntityManager em;

	@Override
	public UserEntity findByEmail(String email) {
		TypedQuery<UserEntity> query = em.createQuery(
				"SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class);
	    query.setParameter("email", email);
	    try {
	    	return query.getSingleResult();
	    } catch (NoResultException e) {
	    	return null;
	    }	
	}

	@Override
	public void merge(UserEntity userEntity) {
		em.merge(userEntity);
	}
	
	
}
