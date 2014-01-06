package com.helpdesk.domain.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.domain.dao.NotificationDao;
import com.helpdesk.domain.entity.NotificationEntity;
import com.helpdesk.domain.entity.NotificationInfoEntity;
import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.UserEntity;

@Service
public class NotificationService {

	@Autowired
	private NotificationDao notificationDao;
	
	@Transactional
	public Long getCount(UserEntity userEntity) {
		return notificationDao.getCount(userEntity);
	}
	
	@Transactional
	public List<NotificationEntity> getNotificationByUser(UserEntity userEntity) {
		return notificationDao.getNotificationByUser(userEntity);
	}
	
	@Transactional
	public void merge(RequestEntity requestEntity, List<UserEntity> userEntity, String message) {
		NotificationEntity notificationEntity = new NotificationEntity();
		NotificationInfoEntity infoEntity = new NotificationInfoEntity(message, new Date());
		notificationEntity.setRequestEntity(requestEntity);
		notificationEntity.setInfoEntity(infoEntity);
		infoEntity.setNotificationEntity(notificationEntity);
		for (UserEntity u : userEntity) {
			notificationEntity.setUserEntity(u);
			notificationDao.merge(notificationEntity);
		}
	}
	@Transactional
	public void remove(NotificationEntity notificationEntity){
		notificationDao.remove(notificationEntity);
	}
	
}
