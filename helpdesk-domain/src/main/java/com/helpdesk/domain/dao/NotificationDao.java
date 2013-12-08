package com.helpdesk.domain.dao;

import com.helpdesk.domain.entity.NotificationEntity;
import com.helpdesk.domain.entity.UserEntity;

public interface NotificationDao {
	
	Long getCount(UserEntity userEntity);
	
	NotificationEntity merge(NotificationEntity notificationEntity);
}
