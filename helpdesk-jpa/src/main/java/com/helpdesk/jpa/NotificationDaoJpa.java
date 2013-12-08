package com.helpdesk.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.NotificationDao;
import com.helpdesk.domain.entity.NotificationEntity;
import com.helpdesk.domain.entity.UserEntity;

@Repository
public class NotificationDaoJpa implements NotificationDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Long getCount(UserEntity userEntity) {
		TypedQuery<Long> query = em.createQuery("SELECT count(n) FROM NotificationEntity n " +
				"where n.userEntity.id = :id",
				Long.class);
		query.setParameter("id", userEntity.getId());
		return query.getResultList().get(0);
	}

	@Override
	public NotificationEntity merge(NotificationEntity notificationEntity) {
		return em.merge(notificationEntity);
	}


}
