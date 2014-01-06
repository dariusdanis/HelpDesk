package com.helpdesk.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
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
	public List<RequestEntity> getAllByCreatOrBelongsTo(UserEntity userEntity) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				"r.creatorEntity.id = :id or r.requestBelongsTo.id = :id", RequestEntity.class);
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
	public List<RequestEntity> getAllByStatus(String status) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				"r.status = :status", RequestEntity.class);
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

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getTopThree() {
		List<Integer> list = new ArrayList<Integer>();
		Query q = em.createQuery("SELECT r.facilityEntity.id, SUM(r.timeSpend) AS timeSped FROM RequestEntity r " +
				"GROUP BY r.facilityEntity.id ORDER BY timeSped DESC");
		q.setMaxResults(3);
		List<Object[]> employees = (List<Object[]>) q.getResultList();
		for (Object[] employee : employees) {
			if (employee[1] != null) {
				list.add((Integer) employee[0]); //ID
				list.add(Integer.parseInt((String) employee[1])); //COUNT
			}
		}
		return list;
	}

	@Override
	public List<RequestEntity> getAllByAdminAndNotStatus(UserEntity user, String status) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				" r.administratorEntity.id = :id AND r.status != :status", RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("id", user.getId());
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByBelongsTosAndNotStatu(UserEntity belongsTo, String status) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				" r.requestBelongsTo.id = :id AND r.status != :status", RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("id", belongsTo.getId());
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByBelongsToAndStatus(UserEntity belongsTo, String status) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				" r.requestBelongsTo.id = :id AND r.status = :status", RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("id", belongsTo.getId());
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByStatusOrAssignetToUser(String status, UserEntity user) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				" r.engineerEntity.id = :id OR r.status = :status", RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("id", user.getId());
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getDirectorHistory(UserEntity director,
			String solved, String assigned) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r where " +
				"(r.engineerEntity.id = :id OR r.administratorEntity.id = :id) AND " +
				"(r.status = :status1 OR r.status = :status2)", RequestEntity.class);
		query.setParameter("status1", solved);
		query.setParameter("status2", assigned);
		query.setParameter("id", director.getId());
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllOverDoRequest(Date startDate, Date endDate) {
		TypedQuery<RequestEntity> query = em.createQuery("SELECT r FROM RequestEntity r WHERE " +
				"r.requestDate BETWEEN :startDate AND :endDate)" 
				, RequestEntity.class);
		query.setParameter("startDate", startDate, TemporalType.TIMESTAMP);
		query.setParameter("endDate", endDate, TemporalType.TIMESTAMP);
		return query.getResultList();
	}
	
}
