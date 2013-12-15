package com.helpdesk.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.helpdesk.domain.dao.NotificationDao;
import com.helpdesk.domain.entity.NotificationEntity;
import com.helpdesk.domain.entity.NotificationInfoEntity;
import com.helpdesk.domain.entity.UserEntity;

@Repository
public class NotificationDaoJpa implements NotificationDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Long getCount(UserEntity userEntity) {
		TypedQuery<Long> query = em.createQuery("SELECT count(n) FROM NotificationEntity n " +
				"where n.userEntity.id = :id", Long.class);
		query.setParameter("id", userEntity.getId());
		return query.getResultList().get(0);
	}

	@Override
	public NotificationEntity merge(NotificationEntity notificationEntity) {
		return em.merge(notificationEntity);
	}

	@Override
	public NotificationInfoEntity merge(NotificationInfoEntity notificationInfoEntity) {
		return em.merge(notificationInfoEntity);
	}

	@Override
	public List<NotificationEntity> getNotificationByUser(UserEntity userEntity) {
		TypedQuery<NotificationEntity> query = em.createQuery("SELECT n FROM NotificationEntity n " +
				"where n.userEntity.id = :id", NotificationEntity.class);
		query.setParameter("id", userEntity.getId());
		return query.getResultList();
	}

	@Override
	public void remove(NotificationEntity notificationEntity) {
		em.remove(em.merge(notificationEntity));
	}


}
