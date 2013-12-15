package com.helpdesk.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.RequestDao;
import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.UserEntity;

@Repository
public class RequestDaoJpa implements RequestDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public RequestEntity merge(RequestEntity requestEntity) {
		return em.merge(requestEntity);
	}

	@Override
	public List<RequestEntity> getAll() {
		 TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r",
				 RequestEntity.class);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByCreator(UserEntity userEntity) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				"r.creatorEntity.id = :id", RequestEntity.class);
		query.setParameter("id", userEntity.getId());
		return query.getResultList();
	}

	@Override
	public RequestEntity loadById(int id) {
		return em.find(RequestEntity.class, id);
	}

	@Override
	public List<RequestEntity> getAllUnassigned() {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				"r.engineerEntity = null", RequestEntity.class);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByEngineer(UserEntity engineerEntity) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				"r.engineerEntity.id = :id", RequestEntity.class);
		query.setParameter("id", engineerEntity.getId());
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByEngineerAndStatus(UserEntity engineerEntity, String status) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				"r.engineerEntity.id = :id and r.status = :status", RequestEntity.class);
		query.setParameter("id", engineerEntity.getId());
		query.setParameter("status", status);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllUnassignedAndStatus(String status) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				"r.engineerEntity = null and r.status = :status", RequestEntity.class);
		query.setParameter("status", status);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByEngineerAndNotStatus(UserEntity engineerEntity, String status) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				"r.engineerEntity.id = :id and r.status != :status", RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("id", engineerEntity.getId());
		return query.getResultList();
	}

}
