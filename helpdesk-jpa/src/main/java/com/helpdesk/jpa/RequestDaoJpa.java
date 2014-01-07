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

	private final static int MAX_REZULT = 10;

	@PersistenceContext
	private EntityManager em;

	@Override
	public RequestEntity merge(RequestEntity requestEntity) {
		return em.merge(requestEntity);
	}

	@Override
	public List<RequestEntity> getAll(int from) {
		TypedQuery<RequestEntity> query = em.createQuery(
				"SELECT r FROM RequestEntity r", RequestEntity.class);
		query.setMaxResults(MAX_REZULT);
		query.setFirstResult(from);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByCreatOrBelongsTo(UserEntity userEntity) {
		TypedQuery<RequestEntity> query = em
				.createQuery(
						"SELECT r FROM RequestEntity r where "
								+ "r.creatorEntity.id = :id or r.requestBelongsTo.id = :id",
						RequestEntity.class);
		query.setParameter("id", userEntity.getId());
		return query.getResultList();
	}

	@Override
	public RequestEntity loadById(int id) {
		return em.find(RequestEntity.class, id);
	}

	@Override
	public List<RequestEntity> getAllUnassigned() {
		TypedQuery<RequestEntity> query = em.createQuery(
				"SELECT r FROM RequestEntity r where "
						+ "r.engineerEntity = null", RequestEntity.class);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByEngineer(UserEntity engineerEntity) {
		TypedQuery<RequestEntity> query = em.createQuery(
				"SELECT r FROM RequestEntity r where "
						+ "r.engineerEntity.id = :id", RequestEntity.class);
		query.setParameter("id", engineerEntity.getId());
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByEngineerAndStatus(
			UserEntity engineerEntity, String status, int from) {
		TypedQuery<RequestEntity> query = em.createQuery(
				"SELECT r FROM RequestEntity r where "
						+ "r.engineerEntity.id = :id and r.status = :status",
				RequestEntity.class);
		query.setParameter("id", engineerEntity.getId());
		query.setParameter("status", status);
		query.setMaxResults(MAX_REZULT);
		query.setFirstResult(from);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByStatus(String status, int from) {
		TypedQuery<RequestEntity> query = em.createQuery(
				"SELECT r FROM RequestEntity r where " + "r.status = :status",
				RequestEntity.class);
		query.setParameter("status", status);
		query.setMaxResults(MAX_REZULT);
		query.setFirstResult(from);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByEngineerAndNotStatus(
			UserEntity engineerEntity, String status, int from) {
		TypedQuery<RequestEntity> query = em.createQuery(
				"SELECT r FROM RequestEntity r where "
						+ "r.engineerEntity.id = :id and r.status != :status",
				RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("id", engineerEntity.getId());
		query.setMaxResults(MAX_REZULT);
		query.setFirstResult(from);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getTopThree() {
		List<Integer> list = new ArrayList<Integer>();
		Query q = em
				.createQuery("SELECT r.facilityEntity.id, SUM(r.timeSpend) AS timeSped FROM RequestEntity r "
						+ "GROUP BY r.facilityEntity.id ORDER BY timeSped DESC");
		q.setMaxResults(3);
		List<Object[]> employees = (List<Object[]>) q.getResultList();
		for (Object[] employee : employees) {
			if (employee[1] != null) {
				list.add((Integer) employee[0]); // ID
				list.add(Integer.parseInt((String) employee[1])); // COUNT
			}
		}
		return list;
	}

	@Override
	public List<RequestEntity> getAllByAdminAndNotStatus(UserEntity user,
			String status, int from) {
		TypedQuery<RequestEntity> query = em
				.createQuery(
						"SELECT r FROM RequestEntity r where "
								+ " r.administratorEntity.id = :id AND r.status != :status",
						RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("id", user.getId());
		query.setMaxResults(MAX_REZULT);
		query.setFirstResult(from);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByBelongsTosAndNotStatu(
			UserEntity belongsTo, String status, int from, String status2) {
		TypedQuery<RequestEntity> query = em
				.createQuery(
						"SELECT r FROM RequestEntity r where "
								+ " r.requestBelongsTo.id = :id AND r.status != :status AND r.status != :status2",
						RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("status2", status2);
		query.setParameter("id", belongsTo.getId());
		query.setMaxResults(MAX_REZULT);
		query.setFirstResult(from);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByBelongsToAndStatus(UserEntity belongsTo,
			String status, String status2, int from) {
		TypedQuery<RequestEntity> query = em
				.createQuery(
						"SELECT r FROM RequestEntity r where "
								+ " r.requestBelongsTo.id = :id AND (r.status = :status OR r.status = :status2)",
						RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("status2", status2);
		query.setParameter("id", belongsTo.getId());
		query.setMaxResults(MAX_REZULT);
		query.setFirstResult(from);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllByStatusOrAssignetToUser(String status,
			UserEntity user, int from) {
		TypedQuery<RequestEntity> query = em
				.createQuery(
						"SELECT r FROM RequestEntity r where "
								+ "(r.engineerEntity.id = :id AND r.status != 'SOLVED')  OR "
								+ "(r.status = :status)", RequestEntity.class);
		query.setParameter("status", status);
		query.setParameter("id", user.getId());
		query.setMaxResults(MAX_REZULT);
		query.setFirstResult(from);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getDirectorHistory(UserEntity director,
			String solved, String assigned, int from) {
		TypedQuery<RequestEntity> query = em
				.createQuery(
						"SELECT r FROM RequestEntity r where "
								+ "(r.engineerEntity.id = :id OR r.administratorEntity.id = :id) AND "
								+ "(r.status = :status1 OR r.status = :status2)",
						RequestEntity.class);
		query.setParameter("status1", solved);
		query.setParameter("status2", assigned);
		query.setParameter("id", director.getId());
		query.setMaxResults(MAX_REZULT);
		query.setFirstResult(from);
		return query.getResultList();
	}

	@Override
	public List<RequestEntity> getAllOverDoRequest(Date startDate, Date endDate) {
		TypedQuery<RequestEntity> query = em.createQuery(
				"SELECT r FROM RequestEntity r WHERE "
						+ "r.requestDate BETWEEN :startDate AND :endDate)",
				RequestEntity.class);
		query.setParameter("startDate", startDate, TemporalType.TIMESTAMP);
		query.setParameter("endDate", endDate, TemporalType.TIMESTAMP);
		return query.getResultList();
	}

	@Override
	public Long getAllByStatusCount(String status) {
		TypedQuery<Long> query = em.createQuery(
				"SELECT count(r) FROM RequestEntity r where "
						+ "r.status = :status", Long.class);
		query.setParameter("status", status);
		return query.getSingleResult();
	}

	@Override
	public Long getAllByAdminAndNotStatusCount(UserEntity user, String status) {
		TypedQuery<Long> query = em
				.createQuery(
						"SELECT count(r) FROM RequestEntity r where "
								+ " r.administratorEntity.id = :id AND r.status != :status",
						Long.class);
		query.setParameter("status", status);
		query.setParameter("id", user.getId());
		return query.getSingleResult();
	}

	@Override
	public Long getAllCount() {
		TypedQuery<Long> query = em.createQuery(
				"SELECT count(r) FROM RequestEntity r", Long.class);
		return query.getSingleResult();
	}

	@Override
	public Long getAllByBelongsTosAndNotStatuCount(UserEntity belongsTo,
			String status, String status2) {
		TypedQuery<Long> query = em
				.createQuery(
						"SELECT count(r) FROM RequestEntity r where "
								+ " r.requestBelongsTo.id = :id AND r.status != :status AND r.status != :status2",
						Long.class);
		query.setParameter("status", status);
		query.setParameter("status2", status2);
		query.setParameter("id", belongsTo.getId());
		return query.getSingleResult();
	}

	@Override
	public Long getAllByBelongsToAndStatusCount(UserEntity belongsTo,
			String status, String status2) {
		TypedQuery<Long> query = em
				.createQuery(
						"SELECT count(r) FROM RequestEntity r where "
								+ " r.requestBelongsTo.id = :id AND (r.status = :status OR r.status = :status2)",
						Long.class);
		query.setParameter("status", status);
		query.setParameter("status2", status2);
		query.setParameter("id", belongsTo.getId());
		return query.getSingleResult();
	}

	@Override
	public Long getAllByEngineerAndNotStatusCount(UserEntity engineerEntity,
			String status) {
		TypedQuery<Long> query = em.createQuery(
				"SELECT count(r) FROM RequestEntity r where "
						+ "r.engineerEntity.id = :id and r.status != :status",
				Long.class);
		query.setParameter("status", status);
		query.setParameter("id", engineerEntity.getId());
		return query.getSingleResult();
	}

	@Override
	public Long getAllByEngineerAndStatusCount(UserEntity engineerEntity,
			String status) {
		TypedQuery<Long> query = em.createQuery(
				"SELECT count(r) FROM RequestEntity r where "
						+ "r.engineerEntity.id = :id and r.status = :status",
				Long.class);
		query.setParameter("id", engineerEntity.getId());
		query.setParameter("status", status);
		return query.getSingleResult();
	}

	@Override
	public Long getAllByStatusOrAssignetToUserCount(String status,
			UserEntity user) {
		TypedQuery<Long> query = em
				.createQuery(
						"SELECT count(r) FROM RequestEntity r where "
								+ " (r.engineerEntity.id = :id AND r.status != 'SOLVED') OR "
								+ "(r.status = :status)", Long.class);
		query.setParameter("status", status);
		query.setParameter("id", user.getId());
		return query.getSingleResult();
	}

	@Override
	public Long getDirectorHistoryCount(UserEntity director, String solved,
			String assigned) {
		TypedQuery<Long> query = em
				.createQuery(
						"SELECT count(r) FROM RequestEntity r where "
								+ "(r.engineerEntity.id = :id OR r.administratorEntity.id = :id) AND "
								+ "(r.status = :status1 OR r.status = :status2)",
						Long.class);
		query.setParameter("status1", solved);
		query.setParameter("status2", assigned);
		query.setParameter("id", director.getId());
		return query.getSingleResult();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Long getTodaySolvedCount(Date startDate) {
		Date endDate = new Date();

		startDate.setHours(0);
		startDate.setMinutes(0);
		startDate.setSeconds(0);
		endDate.setTime(startDate.getTime() + 1 * 24 * 60 * 60 * 1000);

		TypedQuery<Long> query = em.createQuery(
				"SELECT count(r) FROM RequestEntity r WHERE "
						+ "r.solveDate BETWEEN :startDate AND :endDate) AND"
						+ "(r.status = 'SOLVED')", Long.class);
		query.setParameter("startDate", startDate, TemporalType.TIMESTAMP);
		query.setParameter("endDate", endDate, TemporalType.TIMESTAMP);
		return query.getSingleResult();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Long getTodayNewReq(Date startDate) {
		Date endDate = new Date();

		startDate.setHours(0);
		startDate.setMinutes(0);
		startDate.setSeconds(0);
		endDate.setTime(startDate.getTime() + 1 * 24 * 60 * 60 * 1000);

		TypedQuery<Long> query = em
				.createQuery(
						"SELECT count(r) FROM RequestEntity r WHERE "
								+ "(r.requestDate BETWEEN :startDate AND :endDate) AND "
								+ "(r.typeEntity.type = 'REQ')", Long.class);
		query.setParameter("startDate", startDate, TemporalType.TIMESTAMP);
		query.setParameter("endDate", endDate, TemporalType.TIMESTAMP);

		return query.getSingleResult();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Long getTodayNewInfoReq(Date startDate) {
		Date endDate = new Date();

		startDate.setHours(0);
		startDate.setMinutes(0);
		startDate.setSeconds(0);
		endDate.setTime(startDate.getTime() + 1 * 24 * 60 * 60 * 1000);

		TypedQuery<Long> query = em
				.createQuery(
						"SELECT count(r) FROM RequestEntity r WHERE "
								+ "(r.requestDate BETWEEN :startDate AND :endDate) AND "
								+ "(r.typeEntity.type = 'INFO')", Long.class);
		query.setParameter("startDate", startDate, TemporalType.TIMESTAMP);
		query.setParameter("endDate", endDate, TemporalType.TIMESTAMP);

		return query.getSingleResult();
	}

}
