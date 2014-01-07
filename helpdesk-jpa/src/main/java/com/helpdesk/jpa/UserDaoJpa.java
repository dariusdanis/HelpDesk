package com.helpdesk.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.UserDao;
import com.helpdesk.domain.entity.UserEntity;

@Repository
public class UserDaoJpa implements UserDao {

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
	public UserEntity merge(UserEntity userEntity) {
		return em.merge(userEntity);
	}

	@Override
	public List<UserEntity> findAllByRole(String role) {
		TypedQuery<UserEntity> query = em.createQuery(
				"SELECT u FROM UserEntity u WHERE u.roleEntity.role = :role", UserEntity.class);
	    query.setParameter("role", role);
		return query.getResultList();
	}

	@Override
	public List<UserEntity> findAll() {
		TypedQuery<UserEntity> query = em.createQuery(
				"SELECT u FROM UserEntity u", UserEntity.class);
		return query.getResultList();
	}

	@Override
	public UserEntity findById(int id) {
		return em.find(UserEntity.class, id);
	}
	
	@Override
	public UserEntity findByCompanyId(int companyFK) {
		TypedQuery<UserEntity> query = em.createQuery(
				"SELECT u FROM UserEntity u WHERE u.companyEntity.id = :companyFK order by active desc", UserEntity.class);
	    query.setParameter("companyFK", companyFK);
		return query.getResultList().get(0);
	}
	
	
}
