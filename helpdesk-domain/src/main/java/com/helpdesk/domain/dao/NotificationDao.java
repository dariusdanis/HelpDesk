package com.helpdesk.domain.dao;

import java.util.List;

import com.helpdesk.domain.entity.NotificationEntity;
import com.helpdesk.domain.entity.NotificationInfoEntity;
import com.helpdesk.domain.entity.UserEntity;

public interface NotificationDao {
	
	Long getCount(UserEntity userEntity);
	
	NotificationEntity merge(NotificationEntity notificationEntity);
	
	NotificationInfoEntity merge(NotificationInfoEntity notificationInfoEntity);
	
	List<NotificationEntity> getNotificationByUser(UserEntity userEntity);
	
	void remove(NotificationEntity notificationEntity);
	
}
